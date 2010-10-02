package com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor.weld;

import java.util.UUID;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.api.Lifecycle;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.api.BeanStore;
import org.jboss.weld.context.beanstore.HashMapBeanStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Encapsulates a request context <code>id</code>, its {@link BeanStore
 * <code>BeanStore</code>} and its {@link Lifecycle <code>Lifecycle</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
final class WeldRequestContext {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final ThreadLocal<WeldRequestContext> PER_THREAD_WELD_REQUEST_CONTEXT = new ThreadLocal<WeldRequestContext>() {
		@Override
		protected WeldRequestContext initialValue() {
			return WeldRequestContext.newRequestContext();
		}
	};

	/**
	 * <p>
	 * Associates a Weld RequestContext with the calling thread.
	 * </p>
	 * <p>
	 * <strong>IMPORTANT</strong>: MUST ONLY BE CALLED FROM THE THREAD THAT
	 * NEEDS A REQUEST CONTEXT.
	 * </p>
	 */
	static final void begin() {
		PER_THREAD_WELD_REQUEST_CONTEXT.get().beginInternal();
	}

	/**
	 * <p>
	 * Removes the Weld RequestContext from the calling thread.
	 * </p>
	 * <p>
	 * <strong>IMPORTANT</strong>: MUST ONLY BE CALLED FROM THE THREAD WHOSE
	 * REQUEST CONTEXT NEEDS TO BE DESTROYED.
	 * </p>
	 */
	static final void end() {
		PER_THREAD_WELD_REQUEST_CONTEXT.get().endInternal();
		PER_THREAD_WELD_REQUEST_CONTEXT.remove();
	}

	private static WeldRequestContext newRequestContext() {
		final String id = "ThreadRequestContext :: [ThreadID = "
				+ Thread.currentThread().getId() + " | ThreadName = "
				+ Thread.currentThread().getName() + " | UUID = "
				+ UUID.randomUUID() + "]";
		final BeanStore beanStore = new HashMapBeanStore();
		final Lifecycle lifecycle = Container.instance().services().get(
				ContextLifecycle.class);

		return new WeldRequestContext(id, beanStore, lifecycle);
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String id;

	private final BeanStore beanStore;

	private final Lifecycle lifecycle;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private WeldRequestContext(final String id, final BeanStore beanStore,
			final Lifecycle lifecycle) {
		this.id = id;
		this.beanStore = beanStore;
		this.lifecycle = lifecycle;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	private void beginInternal() {
		this.lifecycle.beginRequest(this.id, this.beanStore);
		this.log
				.trace(
						"New Weld RequestContext [ID = {}] started on Thread [ID = {} | Name = {}]",
						new Object[] { this.id, Thread.currentThread().getId(),
								Thread.currentThread().getName() });
	}

	private void endInternal() {
		this.lifecycle.endRequest(this.id, this.beanStore);
		this.log
				.trace(
						"Weld RequestContext [ID = {}] terminated on Thread [ID = {} | Name = {}]",
						new Object[] { this.id, Thread.currentThread().getId(),
								Thread.currentThread().getName() });
	}
}