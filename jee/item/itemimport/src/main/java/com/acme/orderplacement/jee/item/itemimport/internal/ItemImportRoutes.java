/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import javax.inject.Inject;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.milyn.smooks.camel.dataformat.SmooksDataFormat;

import com.acme.orderplacement.jee.framework.camel.event.EventProcessingContextProvider;
import com.acme.orderplacement.jee.framework.camel.jmslog.IncomingMessageExchangeLogging;
import com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLogging;
import com.acme.orderplacement.jee.item.itemimport.config.IncomingItemCreatedEventsEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.ItemRegistrationServiceEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.OutgoingItemImportFailedMessagesEndpoint;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.obergner.acme.orderplacement.integration.inbound.external.ExternalNamespaces;

/**
 * <p>
 * TODO: Insert short summary for ItemImportBoundaryRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemImportRoutes extends RouteBuilder {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final String SCHEMA_VERSION_1_X_XPATH_TEST = "count(//ice1:ItemCreatedEvent) = 1";

	private static final String SCHEMA_VERSION_2_X_XPATH_TEST = "starts-with(//ice2:CreatedItem/@v:version,'2.')";

	private static final String FAULT_MESSAGES_URI = "direct:itemimport.core.faults";

	private static final String INCOMING_XML_MESSAGES_URI = "direct:itemimport.core.incoming";

	private static final String TRANSFORMED_JAVA_OBJECT_MESSAGES_URI = "direct:itemimport.core.transformed";

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	@Inject
	@IncomingItemCreatedEventsEndpoint
	private String incomingItemCreatedEventsEndpoint;

	@Inject
	@OutgoingItemImportFailedMessagesEndpoint
	private String outgoingItemImportFailedMessagesEndpoint;

	@Inject
	@ItemRegistrationServiceEndpoint
	private String itemRegistrationServiceEndpoint;

	@Inject
	@IncomingMessageExchangeLogging
	private Processor incomingMessageLogger;

	@Inject
	@MessageExchangeCompletionLogging
	private Processor completionLogger;

	@Inject
	private EventProcessingContextProvider eventProcessingContextProvider;

	// -------------------------------------------------------------------------
	// org.apache.camel.builder.RouteBuilder
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		final Namespaces ns = namespaces();

		errorHandler(deadLetterChannel(FAULT_MESSAGES_URI));

		onException(Exception.class).handled(true).process(
				this.completionLogger).to(FAULT_MESSAGES_URI);

		interceptFrom(this.incomingItemCreatedEventsEndpoint).process(
				this.eventProcessingContextProvider).process(
				this.incomingMessageLogger);

		from(this.incomingItemCreatedEventsEndpoint).to(
				INCOMING_XML_MESSAGES_URI);

		from(INCOMING_XML_MESSAGES_URI).choice().when().xpath(
				SCHEMA_VERSION_2_X_XPATH_TEST, String.class, ns).unmarshal(
				new SmooksDataFormat(
						BindingResources.ITEMCREATEDEVENT_2_X_BINDING))
				.convertBodyTo(ItemDto.class).to(
						TRANSFORMED_JAVA_OBJECT_MESSAGES_URI).when().xpath(
						SCHEMA_VERSION_1_X_XPATH_TEST, ns).unmarshal(
						new SmooksDataFormat(
								BindingResources.ITEMCREATEDEVENT_1_X_BINDING))
				.convertBodyTo(ItemDto.class).to(
						TRANSFORMED_JAVA_OBJECT_MESSAGES_URI).otherwise().to(
						FAULT_MESSAGES_URI);

		from(TRANSFORMED_JAVA_OBJECT_MESSAGES_URI).to(
				this.itemRegistrationServiceEndpoint).process(
				this.completionLogger);

		from(FAULT_MESSAGES_URI).to(
				this.outgoingItemImportFailedMessagesEndpoint);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private Namespaces namespaces() {
		return new Namespaces("env", ExternalNamespaces.NS_EVENT_ENVELOPE_1_0)
				.add("ice1", ExternalNamespaces.NS_ITEM_CREATED_EVENT_1_0).add(
						"ice2", ExternalNamespaces.NS_ITEM_CREATED_EVENT_2_0)
				.add("v", ExternalNamespaces.NS_SCHEMA_VERSIONING);
	}
}
