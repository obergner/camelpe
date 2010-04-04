/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.routes;

import org.apache.camel.builder.RouteBuilder;

import com.acme.orderplacement.integration.inbound.itemimport.ItemImportChannels;
import com.acme.orderplacement.service.support.exception.IllegalServiceUsageException;

/**
 * <p>
 * TODO: Insert short summary for JmsAdapterRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportAdapterRoutes extends RouteBuilder {

	public static final String ITEM_CREATED_EVENTS_ERROR_QUEUE = "activemq:queue:jms/queue/com/acme/ItemCreatedEventsErrorQueue";

	public static final String ITEM_STORAGE_SERVICE_EP = "bean:integration.inbound.item.ItemStorageServiceDelegate?method=registerItem";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		errorHandler(deadLetterChannel(ItemImportChannels.EXCEPTIONS));

		onException(IllegalServiceUsageException.class).handled(true).to(
				ItemImportChannels.EXCEPTIONS);

		from(ItemImportChannels.TRANSFER_JAXB).to(ITEM_STORAGE_SERVICE_EP);

		from(ItemImportChannels.EXCEPTIONS).to(ITEM_CREATED_EVENTS_ERROR_QUEUE);
	}
}
