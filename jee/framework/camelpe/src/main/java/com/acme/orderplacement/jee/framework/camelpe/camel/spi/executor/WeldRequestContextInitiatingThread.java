/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.api.Lifecycle;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.api.BeanStore;
import org.jboss.weld.context.beanstore.HashMapBeanStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * A {@link Thread <code>Thread</code>} subclass that starts a
 * {@link org.jboss.weld.context.RequestContext
 * <code>Weld RequestContext</code>} before running each task and stops it after
 * said task is finished.
 * </p>
 * <p>
 * <strong>IMPORTANT</strong> This implementation is Weld specific and thus ties
 * us to using an environment in which Weld is the CDI container.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingThread extends Thread {

	public WeldRequestContextInitiatingThread() {
		super();
	}

	public WeldRequestContextInitiatingThread(final Runnable target,
			final String name) {
		super(target, name);
	}

	public WeldRequestContextInitiatingThread(final Runnable target) {
		super(target);
	}

	public WeldRequestContextInitiatingThread(final String name) {
		super(name);
	}

	public WeldRequestContextInitiatingThread(final ThreadGroup group,
			final Runnable target, final String name, final long stackSize) {
		super(group, target, name, stackSize);
	}

	public WeldRequestContextInitiatingThread(final ThreadGroup group,
			final Runnable target, final String name) {
		super(group, target, name);
	}

	public WeldRequestContextInitiatingThread(final ThreadGroup group,
			final Runnable target) {
		super(group, target);
	}

	public WeldRequestContextInitiatingThread(final ThreadGroup group,
			final String name) {
		super(group, name);
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		final WeldRequestContext weldRequestContext = WeldRequestContext
				.newRequestContext();
		try {
			weldRequestContext.begin();
			super.run();
		} finally {
			weldRequestContext.end();
		}
	}

	/**
	 * <p>
	 * Encapsulates a request context <code>id</code>, its {@link BeanStore
	 * <code>BeanStore</code>} and its {@link Lifecycle <code>Lifecycle</code>}.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	private static final class WeldRequestContext {

		private final Logger log = LoggerFactory.getLogger(getClass());

		static WeldRequestContext newRequestContext() {
			final String id = "ThreadRequestContext-"
					+ Thread.currentThread().getName() + "-"
					+ Thread.currentThread().getId();
			final BeanStore beanStore = new HashMapBeanStore();
			final Lifecycle lifecycle = Container.instance().services().get(
					ContextLifecycle.class);

			return new WeldRequestContext(id, beanStore, lifecycle);
		}

		private final String id;

		private final BeanStore beanStore;

		private final Lifecycle lifecycle;

		private WeldRequestContext(final String id, final BeanStore beanStore,
				final Lifecycle lifecycle) {
			this.id = id;
			this.beanStore = beanStore;
			this.lifecycle = lifecycle;
		}

		void begin() {
			this.lifecycle.beginRequest(this.id, this.beanStore);
			this.log
					.trace("New Weld RequestContext [ID = {}] started", this.id);
		}

		void end() {
			this.lifecycle.endRequest(this.id, this.beanStore);
			this.log.trace("Weld RequestContext [ID = {}] terminated", this.id);
		}
	}
}
