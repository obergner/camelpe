/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.commons.lang.Validate;

import com.acme.orderplacement.framework.common.qualifier.Public;

/**
 * <p>
 * A holder for {@link ImmutableEventProcessingContext
 * <code>ImmutableEventProcessingContext</code>} instances. Instances of this
 * class are supposed to act as request scoped CDI beans, allowing arbitrary
 * code involved in processing a request to at all times obtain a reference to
 * the current <code>ImmutableEventProcessingContext</code>. Instead of making
 * <code>ImmutableEventProcessingContext</code> itself a request scoped bean,
 * the additional indirection of a holder instance was introduced to allow
 * <code>ImmutableEventProcessingContext</code> to remain immutable.
 * </p>
 * <p>
 * Note that instances of this class double as a CDI producer for request scoped
 * <code>ImmutableEventProcessingContext</code> instances.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RequestScoped
public class EventProcessingContextHolder implements Serializable,
		EventProcessingContext {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private EventProcessingContext eventProcessingContext;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	@Produces
	@Public
	@RequestScoped
	public EventProcessingContext eventProcessingContext()
			throws IllegalStateException {
		if (this.eventProcessingContext == null) {
			throw new IllegalStateException(
					"Premature attempt to obtain an EventProcessingContext: no EventProcessingContext has been set");
		}

		return this;
	}

	public void initialize(final EventProcessingContext eventProcessingContext)
			throws IllegalArgumentException, IllegalStateException {
		Validate.notNull(eventProcessingContext, "eventProcessingContext");
		if (this.eventProcessingContext != null) {
			throw new IllegalStateException(
					"Illegal attempt to initialize an already initialized EventProcessingContextHolder: EventProcessingContext has already been set");
		}

		this.eventProcessingContext = eventProcessingContext;
	}

	// -------------------------------------------------------------------------
	// com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext
	// -------------------------------------------------------------------------

	/**
	 * @param error
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#fail(java.lang.Exception)
	 */
	public void fail(final Exception error) {
		delegate().fail(error);
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getCompletionTimestamp()
	 */
	public Date getCompletionTimestamp() {
		return delegate().getCompletionTimestamp();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getCreationTimestamp()
	 */
	public Date getCreationTimestamp() {
		return delegate().getCreationTimestamp();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getError()
	 */
	public Exception getError() {
		return delegate().getError();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventId()
	 */
	public String getEventId() {
		return delegate().getEventId();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventSourceSystem()
	 */
	public String getEventSourceSystem() {
		return delegate().getEventSourceSystem();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventType()
	 */
	public String getEventType() {
		return delegate().getEventType();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInflowId()
	 */
	public String getInflowId() {
		return delegate().getInflowId();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInflowTimestamp()
	 */
	public Date getInflowTimestamp() {
		return delegate().getInflowTimestamp();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInitiationTimestamp()
	 */
	public Date getInitiationTimestamp() {
		return delegate().getInitiationTimestamp();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getProcessingId()
	 */
	public String getProcessingId() {
		return delegate().getProcessingId();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getProcessingState()
	 */
	public ProcessingState getProcessingState() {
		return delegate().getProcessingState();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getPropagationId()
	 */
	public String getPropagationId() {
		return delegate().getPropagationId();
	}

	/**
	 * @return
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getSequenceNumber()
	 */
	public int getSequenceNumber() {
		return delegate().getSequenceNumber();
	}

	/**
	 * 
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#succeed()
	 */
	public void succeed() {
		delegate().succeed();
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private EventProcessingContext delegate() throws IllegalStateException {
		if (this.eventProcessingContext == null) {
			throw new IllegalStateException(
					"Premature attempt to obtain an EventProcessingContext: no EventProcessingContext has been set");
		}

		return this.eventProcessingContext;
	}
}
