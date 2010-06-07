/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * <p>
 * TODO: Insert short summary for SampleRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SampleRoutes extends RouteBuilder {

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		from("direct:testOut").to("mock:sampleTarget");
	}
}
