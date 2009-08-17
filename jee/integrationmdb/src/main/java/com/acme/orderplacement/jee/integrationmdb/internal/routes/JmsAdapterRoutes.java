/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.routes;

import org.apache.camel.builder.RouteBuilder;

import com.acme.orderplacement.integration.inbound.itemimport.ItemImportChannels;

/**
 * <p>
 * TODO: Insert short summary for JmsAdapterRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class JmsAdapterRoutes extends RouteBuilder {

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		from(ItemImportChannels.EXCEPTIONS)
				.to(
						"activemq:queue:jms/queue/com/acme/ItemCreatedEventsErrorQueue");
	}
}
