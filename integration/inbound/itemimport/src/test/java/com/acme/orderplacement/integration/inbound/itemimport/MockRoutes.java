/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport;

import org.apache.camel.builder.RouteBuilder;

/**
 * <p>
 * TODO: Insert short summary for TestRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MockRoutes extends RouteBuilder {

	public static final String ITEMIMPORT_SERVICE_MOCK_EP = "mock:itemimportService";

	public static final String EXCEPTION_HANDLER_MOCK_EP = "mock:exceptionHandler";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		from(ItemImportChannels.TRANSFER_JAXB).to(ITEMIMPORT_SERVICE_MOCK_EP);
		from(ItemImportChannels.EXCEPTIONS).to(EXCEPTION_HANDLER_MOCK_EP);
	}
}
