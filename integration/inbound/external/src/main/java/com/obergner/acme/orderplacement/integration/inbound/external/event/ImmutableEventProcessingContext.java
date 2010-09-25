/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.acme.orderplacement.framework.common.qualifier.Internal;

/**
 * <p>
 * TODO: Insert short summary for ImmutableEventProcessingContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Internal
public class ImmutableEventProcessingContext implements Serializable,
		EventProcessingContext {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	public static final Builder BUILDER = new Builder();

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final String eventType;

	private final String eventId;

	private final Date creationTimestamp;

	private final String eventSourceSystem;

	private final String propagationId;

	private final String inflowId;

	private final Date inflowTimestamp;

	private final String processingId;

	private final int sequenceNumber;

	private final Date initiationTimestamp;

	private final Date completionTimestamp;

	private ProcessingState processingState;

	private Exception error;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected ImmutableEventProcessingContext(final String eventType,
			final String eventId, final Date creationTimestamp,
			final String eventSourceSystem, final String propagationId,
			final String inflowId, final Date inflowTimestamp,
			final String processingId, final int sequenceNumber,
			final Date initiationTimestamp, final Date completionTimestamp) {
		this.eventType = eventType;
		this.eventId = eventId;
		this.creationTimestamp = creationTimestamp;
		this.eventSourceSystem = eventSourceSystem;
		this.propagationId = propagationId;
		this.inflowId = inflowId;
		this.inflowTimestamp = inflowTimestamp;
		this.processingId = processingId;
		this.sequenceNumber = sequenceNumber;
		this.initiationTimestamp = initiationTimestamp;
		this.completionTimestamp = completionTimestamp;
		this.processingState = ProcessingState.IN_PROGRESS;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventType()
	 */
	public final String getEventType() {
		return this.eventType;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventId()
	 */
	public final String getEventId() {
		return this.eventId;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getCreationTimestamp()
	 */
	public final Date getCreationTimestamp() {
		return this.creationTimestamp;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getEventSourceSystem()
	 */
	public final String getEventSourceSystem() {
		return this.eventSourceSystem;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getPropagationId()
	 */
	public final String getPropagationId() {
		return this.propagationId;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInflowId()
	 */
	public final String getInflowId() {
		return this.inflowId;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInflowTimestamp()
	 */
	public final Date getInflowTimestamp() {
		return this.inflowTimestamp;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getProcessingId()
	 */
	public final String getProcessingId() {
		return this.processingId;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getSequenceNumber()
	 */
	public final int getSequenceNumber() {
		return this.sequenceNumber;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getInitiationTimestamp()
	 */
	public final Date getInitiationTimestamp() {
		return this.initiationTimestamp;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getCompletionTimestamp()
	 */
	public final Date getCompletionTimestamp() {
		return this.completionTimestamp;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getProcessingState()
	 */
	public final ProcessingState getProcessingState() {
		return this.processingState;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#getError()
	 */
	public final Exception getError() {
		return this.error;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#fail(java.lang.Exception)
	 */
	public final void fail(final Exception error) {
		Validate.notNull(error, "error");
		this.error = error;
		this.processingState = ProcessingState.FAILED;
	}

	/**
	 * @see com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext#succeed()
	 */
	public final void succeed() {
		this.processingState = ProcessingState.SUCCESSFUL;
	}

	// -------------------------------------------------------------------------
	// Builder
	// -------------------------------------------------------------------------

	public static class Builder {

		Builder() {
			// Noop
		}

		public EventProcessingContext buildFrom(
				final Map<String, String> headers)
				throws IllegalArgumentException {
			Validate.notNull(headers, "headers");

			String eventType = null;
			String eventId = null;
			Date creationTimestamp = null;
			String eventSourceSystem = null;
			String propagationId = null;
			String inflowId = null;
			Date inflowTimestamp = null;
			String processingId = null;
			int sequenceNumber = -1;
			Date initiationTimestamp = null;
			Date completionTimestamp = null;

			final EventHeaders eventHeaders = EventHeaderSpec
					.newEventHeadersFrom(headers);
			for (final EventHeader anEventHeader : eventHeaders) {
				if (anEventHeader.isSpecifiedBy(EventHeaderSpec.EVENT_TYPE)) {
					eventType = String.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.EVENT_ID)) {
					eventId = String.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.CREATION_TIMESTAMP)) {
					creationTimestamp = Date.class.cast(anEventHeader
							.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.EVENT_SOURCE_SYSTEM)) {
					eventSourceSystem = String.class.cast(anEventHeader
							.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.PROPAGATION_ID)) {
					propagationId = String.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.INFLOW_ID)) {
					inflowId = String.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.INFLOW_TIMESTAMP)) {
					inflowTimestamp = Date.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.PROCESSING_ID)) {
					processingId = String.class.cast(anEventHeader.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.SEQUENCE_NUMBER)) {
					sequenceNumber = anEventHeader.getValue() != null ? Integer.class
							.cast(anEventHeader.getValue()).intValue()
							: -1;
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.INITIATION_TIMESTAMP)) {
					initiationTimestamp = Date.class.cast(anEventHeader
							.getValue());
				} else if (anEventHeader
						.isSpecifiedBy(EventHeaderSpec.COMPLETION_TIMESTAMP)) {
					completionTimestamp = Date.class.cast(anEventHeader
							.getValue());
				}
			}

			return new ImmutableEventProcessingContext(eventType, eventId,
					creationTimestamp, eventSourceSystem, propagationId,
					inflowId, inflowTimestamp, processingId, sequenceNumber,
					initiationTimestamp, completionTimestamp);
		}
	}
}
