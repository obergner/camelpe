/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import org.apache.camel.builder.RouteBuilder;

import com.acme.orderplacement.jee.framework.camel.jmslog.IncomingMessageExchangeLoggingProcessor;
import com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor;

/**
 * <p>
 * TODO: Insert short summary for ItemImportBoundaryRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportBoundaryRoutes extends RouteBuilder {

	public static final String ITEM_CREATED_EVENTS = "hornetq:topic:ItemCreatedEventsTopic";

	public static final String ITEM_IMPORT_FAILED = "hornetq:queue:ItemImportFailuresQueue";

	public static final String ITEM_REGISTRATION_SERVICE = "ejb:orderplacement.jee.ear-1.0-SNAPSHOT/ItemStorageServiceBean/local?method=registerItem";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		errorHandler(deadLetterChannel(ItemImportCoreRoutes.FAULT_MESSAGES));

		onException(Exception.class).handled(true).process(
				new MessageExchangeCompletionLoggingProcessor(false)).to(
				ItemImportCoreRoutes.FAULT_MESSAGES);

		from(ITEM_CREATED_EVENTS).process(
				new IncomingMessageExchangeLoggingProcessor()).to(
				ItemImportCoreRoutes.INCOMING_XML_MESSAGES);

		from(ItemImportCoreRoutes.TRANSFORMED_JAVA_OBJECT_MESSAGES).to(
				ITEM_REGISTRATION_SERVICE).onCompletion().onCompleteOnly()
				.process(new MessageExchangeCompletionLoggingProcessor(true));

		from(ItemImportCoreRoutes.FAULT_MESSAGES).to(ITEM_IMPORT_FAILED);
	}
}
