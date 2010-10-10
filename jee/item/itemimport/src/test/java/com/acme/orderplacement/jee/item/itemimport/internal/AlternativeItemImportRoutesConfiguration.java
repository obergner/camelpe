/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.acme.orderplacement.jee.framework.camel.jmslog.IncomingMessageExchangeLogging;
import com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLogging;
import com.acme.orderplacement.jee.item.itemimport.config.IncomingItemCreatedEventsEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.ItemRegistrationServiceEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.OutgoingItemImportFailedMessagesEndpoint;

/**
 * <p>
 * TODO: Insert short summary for AlternativeItemImportRoutesConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
@Alternative
class AlternativeItemImportRoutesConfiguration {

	public static final String DIRECT_ITEM_CREATED_EVENTS_EP = "direct:incomingItemCreatedEvents";

	public static final String MOCK_ITEM_IMPORT_FAILED_EP = "mock:outgoingItemImportFailedMessages";

	public static final String MOCK_ITEM_REGISTRATION_SERVICE_EP = "mock:itemRegistrationService";

	@Produces
	@IncomingItemCreatedEventsEndpoint
	public String incomingItemCreatedEventsEndpoint() {
		return DIRECT_ITEM_CREATED_EVENTS_EP;
	}

	@Produces
	@OutgoingItemImportFailedMessagesEndpoint
	public String outgoingItemImportFailedMessagesEndpoint() {
		return MOCK_ITEM_IMPORT_FAILED_EP;
	}

	@Produces
	@ItemRegistrationServiceEndpoint
	public String itemRegistrationServiceEndpoint() {
		return MOCK_ITEM_REGISTRATION_SERVICE_EP;
	}

	@Produces
	@IncomingMessageExchangeLogging
	public Processor incomingMessageExchangeLoggingProcessor() {
		return new Processor() {
			@Override
			public void process(final Exchange exchange) throws Exception {
			}
		};
	}

	@Produces
	@MessageExchangeCompletionLogging
	public Processor messageExchangeCompletionLoggingProcessor() {
		return new Processor() {
			@Override
			public void process(final Exchange exchange) throws Exception {
			}
		};
	}
}
