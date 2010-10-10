/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import javax.inject.Inject;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.milyn.smooks.camel.dataformat.SmooksDataFormat;

import com.acme.orderplacement.jee.framework.camel.event.EventProcessingContextProvider;
import com.acme.orderplacement.jee.framework.camel.jmslog.IncomingMessageExchangeLogging;
import com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLogging;
import com.acme.orderplacement.jee.item.itemimport.config.IncomingItemCreatedEventsEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.ItemRegistrationServiceEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.OutgoingItemImportFailedMessagesEndpoint;
import com.acme.orderplacement.service.item.dto.ItemDto;

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

	private static final String FAULT_MESSAGES = "direct:itemimport.core.faults";

	private static final String INCOMING_XML_MESSAGES = "direct:itemimport.core.incoming";

	private static final String TRANSFORMED_JAVA_OBJECT_MESSAGES = "direct:itemimport.core.transformed";

	private static final String BINDING_DEFINITION_RESOURCE = "META-INF/binding/itemcreatedevent-xml-to-java.xml";

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
		errorHandler(deadLetterChannel(FAULT_MESSAGES));

		onException(Exception.class).handled(true).process(
				this.completionLogger).to(FAULT_MESSAGES);

		interceptFrom(this.incomingItemCreatedEventsEndpoint).process(
				this.incomingMessageLogger).process(
				this.eventProcessingContextProvider);

		from(this.incomingItemCreatedEventsEndpoint).to(INCOMING_XML_MESSAGES);

		from(INCOMING_XML_MESSAGES).unmarshal(
				new SmooksDataFormat(BINDING_DEFINITION_RESOURCE))
				.convertBodyTo(ItemDto.class).to(
						TRANSFORMED_JAVA_OBJECT_MESSAGES);

		from(TRANSFORMED_JAVA_OBJECT_MESSAGES).to(
				this.itemRegistrationServiceEndpoint).process(
				this.completionLogger);

		from(FAULT_MESSAGES).to(this.outgoingItemImportFailedMessagesEndpoint);
	}
}
