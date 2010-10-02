/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor.weld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.impl.ThreadPoolProfileSupport;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.LifecycleStrategy;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <a href="http://seamframework.org/Weld">Weld</a>, the CDI reference
 * implementation by JBoss, initializes a <code>RequestScope</code> only for
 * threads that
 * <ol>
 * <li>originate from the web container (servlet requests)</li>,
 * <li>pass through a custom session bean interceptor</li>, or
 * <li>process a CDI event</li>.
 * </ol>
 * This does <strong>not</strong> include threads managed by Apache Camel, i.e.
 * by the Spring message listener container, to be more precise. It follows that
 * we cannot use request scope beans in threads processing incoming JMS
 * messages. To remedy this situation, we introduce a special Apache Camel
 * {@link ExecutorServiceStrategy <code>ExecutorServiceStrategy</code>}. This
 * custom strategy returns {@link java.util.concurrent.ThreadPoolExecutor
 * <code>java.util.concurrent.ThreadPoolExecutor</code>} subclasses
 * {@link WeldRequestContextInitiatingThreadPoolExecutor
 * <code>WeldRequestContextInitiatingThreadPoolExecutor</code>} and
 * {@link WeldRequestContextInitiatingScheduledThreadPoolExecutor
 * <code>WeldRequestContextInitiatingScheduledThreadPoolExecutor</code>} that
 * will initiate a <code>RequestScope</code> for each task execution.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiExecutorServiceStrategy extends ServiceSupport implements
		ExecutorServiceStrategy {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final List<ExecutorService> executorServices = new ArrayList<ExecutorService>();

	private final CamelContext camelContext;

	private String threadNamePattern = "CDI Camel Thread ${counter} - ${name}";

	private String defaultThreadPoolProfileId;

	private final Map<String, ThreadPoolProfile> threadPoolProfiles = new HashMap<String, ThreadPoolProfile>();

	public CdiExecutorServiceStrategy(final CamelContext camelContext) {
		this.camelContext = camelContext;

		// create and register the default profile
		this.defaultThreadPoolProfileId = "defaultThreadPoolProfile";
		final ThreadPoolProfile defaultProfile = new ThreadPoolProfileSupport(
				this.defaultThreadPoolProfileId);
		// the default profile has the following values
		defaultProfile.setDefaultProfile(true);
		defaultProfile.setPoolSize(10);
		defaultProfile.setMaxPoolSize(20);
		defaultProfile.setKeepAliveTime(60L);
		defaultProfile.setTimeUnit(TimeUnit.SECONDS);
		defaultProfile.setMaxQueueSize(1000);
		defaultProfile.setRejectedPolicy(ThreadPoolRejectedPolicy.CallerRuns);
		registerThreadPoolProfile(defaultProfile);
	}

	public void registerThreadPoolProfile(final ThreadPoolProfile profile) {
		this.threadPoolProfiles.put(profile.getId(), profile);
	}

	public ThreadPoolProfile getThreadPoolProfile(final String id) {
		return this.threadPoolProfiles.get(id);
	}

	public ThreadPoolProfile getDefaultThreadPoolProfile() {
		return getThreadPoolProfile(this.defaultThreadPoolProfileId);
	}

	public void setDefaultThreadPoolProfile(
			final ThreadPoolProfile defaultThreadPoolProfile) {
		final ThreadPoolProfile oldProfile = this.threadPoolProfiles
				.remove(this.defaultThreadPoolProfileId);
		if (oldProfile != null) {
			// the old is no longer default
			oldProfile.setDefaultProfile(false);

			// fallback and use old default values for new default profile if
			// absent (convention over configuration)
			if (defaultThreadPoolProfile.getKeepAliveTime() == null) {
				defaultThreadPoolProfile.setKeepAliveTime(oldProfile
						.getKeepAliveTime());
			}
			if (defaultThreadPoolProfile.getMaxPoolSize() == null) {
				defaultThreadPoolProfile.setMaxPoolSize(oldProfile
						.getMaxPoolSize());
			}
			if (defaultThreadPoolProfile.getRejectedPolicy() == null) {
				defaultThreadPoolProfile.setRejectedPolicy(oldProfile
						.getRejectedPolicy());
			}
			if (defaultThreadPoolProfile.getMaxQueueSize() == null) {
				defaultThreadPoolProfile.setMaxQueueSize(oldProfile
						.getMaxQueueSize());
			}
			if (defaultThreadPoolProfile.getPoolSize() == null) {
				defaultThreadPoolProfile.setPoolSize(oldProfile.getPoolSize());
			}
			if (defaultThreadPoolProfile.getTimeUnit() == null) {
				defaultThreadPoolProfile.setTimeUnit(oldProfile.getTimeUnit());
			}
		}

		// validate that all options has been given as its mandatory for a
		// default thread pool profile
		// as it is used as fallback for other profiles if they do not have that
		// particular value
		ObjectHelper.notEmpty(defaultThreadPoolProfile.getId(), "id",
				defaultThreadPoolProfile);
		ObjectHelper.notNull(defaultThreadPoolProfile.getKeepAliveTime(),
				"keepAliveTime", defaultThreadPoolProfile);
		ObjectHelper.notNull(defaultThreadPoolProfile.getMaxPoolSize(),
				"maxPoolSize", defaultThreadPoolProfile);
		ObjectHelper.notNull(defaultThreadPoolProfile.getMaxQueueSize(),
				"maxQueueSize", defaultThreadPoolProfile);
		ObjectHelper.notNull(defaultThreadPoolProfile.getPoolSize(),
				"poolSize", defaultThreadPoolProfile);
		ObjectHelper.notNull(defaultThreadPoolProfile.getTimeUnit(),
				"timeUnit", defaultThreadPoolProfile);

		this.log.info("Using custom DefaultThreadPoolProfile [{}]",
				defaultThreadPoolProfile);

		// and replace with the new default profile
		this.defaultThreadPoolProfileId = defaultThreadPoolProfile.getId();
		// and mark the new profile as default
		defaultThreadPoolProfile.setDefaultProfile(true);
		registerThreadPoolProfile(defaultThreadPoolProfile);
	}

	public String getThreadName(final String name) {
		return CdiExecutorServiceStrategyUtil.getThreadName(
				this.threadNamePattern, name);
	}

	public String getThreadNamePattern() {
		return this.threadNamePattern;
	}

	public void setThreadNamePattern(final String threadNamePattern) {
		this.threadNamePattern = threadNamePattern;
	}

	public ExecutorService lookup(final Object source, final String name,
			final String executorServiceRef) {
		ExecutorService answer = this.camelContext.getRegistry().lookup(
				executorServiceRef, ExecutorService.class);
		if (answer != null) {
			this.log
					.debug(
							"Looked up ExecutorService with ref [{}] and found it in Registry: [{}]",
							executorServiceRef, answer);
		} else {
			// try to see if we got a thread pool profile with that id
			answer = newThreadPool(source, name, executorServiceRef);
			if (answer != null) {
				this.log
						.debug(
								"Looked up ExecutorService with ref [{}] and found a matching ThreadPoolProfile to create the ExecutorService: [{}]",
								executorServiceRef, answer);
			}
		}

		return answer;
	}

	public ScheduledExecutorService lookupScheduled(final Object source,
			final String name, final String executorServiceRef) {
		ScheduledExecutorService answer = this.camelContext.getRegistry()
				.lookup(executorServiceRef, ScheduledExecutorService.class);
		if (answer != null) {
			this.log
					.debug(
							"Looked up ScheduledExecutorService with ref [{}] and found it in Registry: [{}]",
							executorServiceRef, answer);
		} else {
			final ThreadPoolProfile profile = getThreadPoolProfile(name);
			if (profile != null) {
				final int poolSize = profile.getPoolSize();
				answer = newScheduledThreadPool(source, name, poolSize);
				if (answer != null) {
					this.log
							.debug(
									"Looked up ScheduledExecutorService with ref [{}]  and found a matching ThreadPoolProfile "
											+ "to create the ScheduledExecutorService: [{}]",
									executorServiceRef, answer);
				}
			}
		}

		return answer;
	}

	public ExecutorService newDefaultThreadPool(final Object source,
			final String name) {
		final ThreadPoolProfile profile = getDefaultThreadPoolProfile();
		ObjectHelper.notNull(profile, "DefaultThreadPoolProfile");

		return newThreadPool(source, name, profile.getPoolSize(), profile
				.getMaxPoolSize(), profile.getKeepAliveTime(), profile
				.getTimeUnit(), profile.getMaxQueueSize(), profile
				.getRejectedExecutionHandler(), false);
	}

	public ExecutorService newThreadPool(final Object source,
			final String name, final String threadPoolProfileId) {
		final ThreadPoolProfile defaultProfile = getDefaultThreadPoolProfile();
		final ThreadPoolProfile profile = getThreadPoolProfile(threadPoolProfileId);
		if (profile == null) {
			// no profile with that id
			return null;
		}
		// fallback to use values from default profile if not specified
		final Integer poolSize = profile.getPoolSize() != null ? profile
				.getPoolSize() : defaultProfile.getPoolSize();
		final Integer maxPoolSize = profile.getMaxPoolSize() != null ? profile
				.getMaxPoolSize() : defaultProfile.getMaxPoolSize();
		final Long keepAliveTime = profile.getKeepAliveTime() != null ? profile
				.getKeepAliveTime() : defaultProfile.getKeepAliveTime();
		final TimeUnit timeUnit = profile.getTimeUnit() != null ? profile
				.getTimeUnit() : defaultProfile.getTimeUnit();
		final Integer maxQueueSize = profile.getMaxQueueSize() != null ? profile
				.getMaxQueueSize()
				: defaultProfile.getMaxQueueSize();
		final RejectedExecutionHandler handler = profile
				.getRejectedExecutionHandler() != null ? profile
				.getRejectedExecutionHandler() : defaultProfile
				.getRejectedExecutionHandler();
		// create the pool
		return newThreadPool(source, name, poolSize, maxPoolSize,
				keepAliveTime, timeUnit, maxQueueSize, handler, false);
	}

	public ExecutorService newCachedThreadPool(final Object source,
			final String name) {
		final ExecutorService answer = CdiExecutorServiceStrategyUtil
				.newCachedThreadPool(this.threadNamePattern, name, true);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new cached thread pool for source [{}] with name [{}] -> [{}]",
						new Object[] { source, name, answer });

		return answer;
	}

	public ScheduledExecutorService newScheduledThreadPool(final Object source,
			final String name) {
		final int poolSize = getDefaultThreadPoolProfile().getPoolSize();
		return newScheduledThreadPool(source, name, poolSize);
	}

	public ScheduledExecutorService newScheduledThreadPool(final Object source,
			final String name, final int poolSize) {
		final ScheduledExecutorService answer = CdiExecutorServiceStrategyUtil
				.newScheduledThreadPool(poolSize, this.threadNamePattern, name,
						true);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new scheduled thread pool for source [{}] with name [{}] and pool size [{}] -> [{}]",
						new Object[] { source, name, poolSize, answer });

		return answer;
	}

	public ExecutorService newFixedThreadPool(final Object source,
			final String name, final int poolSize) {
		final ExecutorService answer = CdiExecutorServiceStrategyUtil
				.newFixedThreadPool(poolSize, this.threadNamePattern, name,
						true);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new fixed thread pool for source [{}] with name [{}] and pool size [{}] -> [{}]",
						new Object[] { source, name, poolSize, answer });

		return answer;
	}

	public ExecutorService newSingleThreadExecutor(final Object source,
			final String name) {
		final ExecutorService answer = CdiExecutorServiceStrategyUtil
				.newSingleThreadExecutor(this.threadNamePattern, name, true);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new single thread pool for source [{}] with name [{}] -> [{}]",
						new Object[] { source, name, answer });

		return answer;
	}

	public ExecutorService newThreadPool(final Object source,
			final String name, final int corePoolSize, final int maxPoolSize) {
		final ExecutorService answer = CdiExecutorServiceStrategyUtil
				.newThreadPool(this.threadNamePattern, name, corePoolSize,
						maxPoolSize);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new fixed thread pool for source [{}] with name [{}], pool size [{}] and max pool size [{}] -> [{}]",
						new Object[] { source, name, corePoolSize, maxPoolSize,
								answer });

		return answer;
	}

	public ExecutorService newThreadPool(final Object source,
			final String name, final int corePoolSize, final int maxPoolSize,
			final long keepAliveTime, final TimeUnit timeUnit,
			final int maxQueueSize,
			final RejectedExecutionHandler rejectedExecutionHandler,
			final boolean daemon) {

		// the thread name must not be null
		ObjectHelper.notNull(name, "ThreadName");

		final ExecutorService answer = CdiExecutorServiceStrategyUtil
				.newThreadPool(this.threadNamePattern, name, corePoolSize,
						maxPoolSize, keepAliveTime, timeUnit, maxQueueSize,
						rejectedExecutionHandler, daemon);
		onThreadPoolCreated(answer);

		this.log
				.debug(
						"Created new fixed thread pool for source [{}] with name [{}], pool size [{}], max pool size [{}], "
								+ "keep alive time [{} {}], max queue size [{}], rejected execution handler [{}] and daemon [{}]  -> [{}]",
						new Object[] { source, name, corePoolSize, maxPoolSize,
								keepAliveTime, timeUnit, maxQueueSize,
								rejectedExecutionHandler, daemon, answer });

		return answer;
	}

	public void shutdown(final ExecutorService executorService) {
		ObjectHelper.notNull(executorService, "executorService");

		if (executorService.isShutdown()) {
			return;
		}

		this.log.debug("Shutdown of ExecutorService [{}] requested",
				executorService);

		executorService.shutdown();

		this.log.debug("Shutdown of ExecutorService [{}] completed",
				executorService);
	}

	public List<Runnable> shutdownNow(final ExecutorService executorService) {
		ObjectHelper.notNull(executorService, "executorService");

		if (executorService.isShutdown()) {
			return null;
		}

		this.log.debug("ShutdownNow of ExecutorService [{}] requested",
				executorService);

		final List<Runnable> answer = executorService.shutdownNow();

		this.log.debug("ShutdownNow of ExecutorService [{}] complete",
				executorService);

		return answer;
	}

	private void onThreadPoolCreated(final ExecutorService executorService) {
		// add to internal list of thread pools
		this.executorServices.add(executorService);

		// let lifecycle strategy be notified as well which can let it be
		// managed in JMX as well
		if (executorService instanceof ThreadPoolExecutor) {
			final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
			for (final LifecycleStrategy lifecycle : this.camelContext
					.getLifecycleStrategies()) {
				lifecycle.onThreadPoolAdd(this.camelContext, threadPool);
			}
		}

		// now call strategy to allow custom logic
		onNewExecutorService(executorService);
	}

	/**
	 * Strategy callback when a new {@link java.util.concurrent.ExecutorService}
	 * have been created.
	 * 
	 * @param executorService
	 *            the created {@link java.util.concurrent.ExecutorService}
	 */
	protected void onNewExecutorService(final ExecutorService executorService) {
		// noop
	}

	@Override
	protected void doStart() throws Exception {
		// noop
	}

	@Override
	protected void doStop() throws Exception {
		// noop
	}

	@Override
	protected void doShutdown() throws Exception {
		// shutdown all executor services
		for (final ExecutorService executorService : this.executorServices) {
			// only log if something goes wrong as we want to shutdown them all
			try {
				shutdownNow(executorService);
			} catch (final Throwable e) {
				this.log.warn(
						"Error occurred during shutdown of ExecutorService: "
								+ executorService
								+ ". This exception will be ignored.", e);
			}
		}
		this.executorServices.clear();

		// do not clear the default profile as we could potential be restarted
		final Iterator<ThreadPoolProfile> it = this.threadPoolProfiles.values()
				.iterator();
		while (it.hasNext()) {
			final ThreadPoolProfile profile = it.next();
			if (!profile.isDefaultProfile().booleanValue()) {
				it.remove();
			}
		}
	}
}
