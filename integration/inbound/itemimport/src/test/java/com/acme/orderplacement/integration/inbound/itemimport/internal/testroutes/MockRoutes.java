/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.internal.testroutes;

import org.apache.camel.builder.RouteBuilder;

import com.acme.orderplacement.integration.inbound.itemimport.ItemImportChannels;
import com.acme.orderplacement.integration.inbound.itemimport.internal.routes.ItemImportRoutes;

/**
 * <p>
 * TODO: Insert short summary for TestRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MockRoutes {

	public static final String ITEMIMPORT_SERVICE_MOCK_EP = "mock:itemimportService";

	public static final String EXCEPTION_HANDLER_MOCK_EP = "mock:exceptionHandler";

	public static final MockRoutes INSTANCE = new MockRoutes();

	public RouteBuilder[] createRouteBuilders() {
		return new RouteBuilder[] { new ItemImportRoutes(), new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from(ItemImportChannels.TRANSFER_JAXB).to(
						ITEMIMPORT_SERVICE_MOCK_EP);
				from(ItemImportChannels.EXCEPTIONS).to(
						EXCEPTION_HANDLER_MOCK_EP);
			}
		} };
	}

	/**
	 * 
	 */
	private MockRoutes() {
		super();
	}

}
