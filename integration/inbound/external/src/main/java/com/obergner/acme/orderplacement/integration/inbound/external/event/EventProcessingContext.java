/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.util.Date;

import javax.enterprise.context.RequestScoped;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RequestScoped
public interface EventProcessingContext {

	/**
	 * @return the eventType
	 */
	String getEventType();

	/**
	 * @return the eventId
	 */
	String getEventId();

	/**
	 * @return the creationTimestamp
	 */
	Date getCreationTimestamp();

	/**
	 * @return the eventSourceSystem
	 */
	String getEventSourceSystem();

	/**
	 * @return the propagationId
	 */
	String getPropagationId();

	/**
	 * @return the inflowId
	 */
	String getInflowId();

	/**
	 * @return the inflowTimestamp
	 */
	Date getInflowTimestamp();

	/**
	 * @return the processingId
	 */
	String getProcessingId();

	/**
	 * @return the sequenceNumber
	 */
	int getSequenceNumber();

	/**
	 * @return the initiationTimestamp
	 */
	Date getInitiationTimestamp();

	/**
	 * @return the completionTimestamp
	 */
	Date getCompletionTimestamp();

	/**
	 * @return the processingState
	 */
	ProcessingState getProcessingState();

	/**
	 * @return the error
	 */
	Exception getError();

	/**
	 * @param error
	 *            the error to set
	 */
	void fail(final Exception error);

	void succeed();

}