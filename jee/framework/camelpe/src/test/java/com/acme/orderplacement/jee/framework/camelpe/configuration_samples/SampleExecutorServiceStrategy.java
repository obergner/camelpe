/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.configuration_samples;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.ThreadPoolProfile;

import com.acme.orderplacement.jee.framework.camelpe.CamelContextModifying;

/**
 * <p>
 * TODO: Insert short summary for SampleExecutorServiceStrategy
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SampleExecutorServiceStrategy implements ExecutorServiceStrategy {

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#getDefaultThreadPoolProfile()
	 */
	@Override
	public ThreadPoolProfile getDefaultThreadPoolProfile() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#getThreadName(java.lang.String)
	 */
	@Override
	public String getThreadName(final String name) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#getThreadNamePattern()
	 */
	@Override
	public String getThreadNamePattern() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#getThreadPoolProfile(java.lang.String)
	 */
	@Override
	public ThreadPoolProfile getThreadPoolProfile(final String id) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#lookup(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ExecutorService lookup(final Object source, final String name,
			final String executorServiceRef) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#lookupScheduled(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ScheduledExecutorService lookupScheduled(final Object source,
			final String name, final String executorServiceRef) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newCachedThreadPool(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public ExecutorService newCachedThreadPool(final Object source,
			final String name) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newDefaultThreadPool(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public ExecutorService newDefaultThreadPool(final Object source,
			final String name) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newFixedThreadPool(java.lang.Object,
	 *      java.lang.String, int)
	 */
	@Override
	public ExecutorService newFixedThreadPool(final Object source,
			final String name, final int poolSize) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newScheduledThreadPool(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public ScheduledExecutorService newScheduledThreadPool(final Object source,
			final String name) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newScheduledThreadPool(java.lang.Object,
	 *      java.lang.String, int)
	 */
	@Override
	public ScheduledExecutorService newScheduledThreadPool(final Object source,
			final String name, final int poolSize) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newSingleThreadExecutor(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public ExecutorService newSingleThreadExecutor(final Object source,
			final String name) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newThreadPool(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ExecutorService newThreadPool(final Object source,
			final String name, final String threadPoolProfileId) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newThreadPool(java.lang.Object,
	 *      java.lang.String, int, int)
	 */
	@Override
	public ExecutorService newThreadPool(final Object source,
			final String name, final int corePoolSize, final int maxPoolSize) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#newThreadPool(java.lang.Object,
	 *      java.lang.String, int, int, long, java.util.concurrent.TimeUnit,
	 *      int, java.util.concurrent.RejectedExecutionHandler, boolean)
	 */
	@Override
	public ExecutorService newThreadPool(final Object source,
			final String name, final int corePoolSize, final int maxPoolSize,
			final long keepAliveTime, final TimeUnit timeUnit,
			final int maxQueueSize,
			final RejectedExecutionHandler rejectedExecutionHandler,
			final boolean daemon) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#registerThreadPoolProfile(org.apache.camel.spi.ThreadPoolProfile)
	 */
	@Override
	public void registerThreadPoolProfile(final ThreadPoolProfile profile) {
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#setDefaultThreadPoolProfile(org.apache.camel.spi.ThreadPoolProfile)
	 */
	@Override
	public void setDefaultThreadPoolProfile(
			final ThreadPoolProfile defaultThreadPoolProfile) {
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#setThreadNamePattern(java.lang.String)
	 */
	@Override
	public void setThreadNamePattern(final String pattern)
			throws IllegalArgumentException {
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#shutdown(java.util.concurrent.ExecutorService)
	 */
	@Override
	public void shutdown(final ExecutorService executorService) {
	}

	/**
	 * @see org.apache.camel.spi.ExecutorServiceStrategy#shutdownNow(java.util.concurrent.ExecutorService)
	 */
	@Override
	public List<Runnable> shutdownNow(final ExecutorService executorService) {
		return null;
	}

	/**
	 * @see org.apache.camel.ShutdownableService#shutdown()
	 */
	@Override
	public void shutdown() throws Exception {
	}

	/**
	 * @see org.apache.camel.Service#start()
	 */
	@Override
	public void start() throws Exception {
	}

	/**
	 * @see org.apache.camel.Service#stop()
	 */
	@Override
	public void stop() throws Exception {
	}
}
