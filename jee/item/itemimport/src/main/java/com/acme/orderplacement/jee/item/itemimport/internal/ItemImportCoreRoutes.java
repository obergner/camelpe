/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * <p>
 * TODO: Insert short summary for ItemImportCoreRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportCoreRoutes extends RouteBuilder {

	public static final String FAULT_MESSAGES = "direct:itemimport.exceptions";

	public static final String INCOMING_XML_MESSAGES = "direct:itemimport.incoming.raw";

	public static final String TRANSFORMED_JAVA_OBJECT_MESSAGES = "direct:itemimport.transfer.jaxb";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		onException(Exception.class).handled(true).to(FAULT_MESSAGES);

		from(INCOMING_XML_MESSAGES)
				.unmarshal(
						new JaxbDataFormat(
								"com.external.schema.ns.events.itemcreated._1:com.external.schema.ns.models.item._1"))
				.to(TRANSFORMED_JAVA_OBJECT_MESSAGES);
	}

}
