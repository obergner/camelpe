/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.internal.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.acme.orderplacement.integration.inbound.itemimport.ItemImportChannels;

/**
 * <p>
 * TODO: Insert short summary for ItemImportRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportRoutes extends RouteBuilder {

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		onException(Exception.class).handled(true).to(
				ItemImportChannels.EXCEPTIONS);

		from(ItemImportChannels.INCOMING_RAW)
				.unmarshal(
						new JaxbDataFormat(
								"com.external.schema.ns.events.itemcreated._1:com.external.schema.ns.models.item._1"))
				.to(ItemImportChannels.TRANSFER_JAXB);
	}

}
