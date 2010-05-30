/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import org.apache.camel.builder.RouteBuilder;

import com.acme.orderplacement.framework.service.exception.IllegalServiceUsageException;

/**
 * <p>
 * TODO: Insert short summary for ItemImportBoundaryRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportBoundaryRoutes extends RouteBuilder {

	public static final String FAULT_MESSAGES_PROPAGATION = "activemq:queue:jms/queue/com/acme/ItemCreatedEventsErrorQueue";

	public static final String ITEM_REGISTRATION_SERVICE = "bean:integration.inbound.item.ItemStorageServiceDelegate?method=registerItem";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		errorHandler(deadLetterChannel(ItemImportCoreRoutes.FAULT_MESSAGES));

		onException(IllegalServiceUsageException.class).handled(true).to(
				ItemImportCoreRoutes.FAULT_MESSAGES);

		from(ItemImportCoreRoutes.TRANSFORMED_JAVA_OBJECT_MESSAGES).to(
				ITEM_REGISTRATION_SERVICE);

		from(ItemImportCoreRoutes.FAULT_MESSAGES)
				.to(FAULT_MESSAGES_PROPAGATION);
	}
}
