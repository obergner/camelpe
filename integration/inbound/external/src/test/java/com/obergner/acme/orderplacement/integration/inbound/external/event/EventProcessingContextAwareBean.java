/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.acme.orderplacement.framework.common.qualifier.Public;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContextAwareBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RequestScoped
public class EventProcessingContextAwareBean {

	@Inject
	@Public
	private EventProcessingContext eventProcessingContext;

	public void failProcessing(final Exception error) {
		this.eventProcessingContext.fail(error);
	}
}
