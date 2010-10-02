/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camelpe_samples;

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

	public static final String SAMPLE_SOURCE_EP = "direct:sampleSource";

	public static final String SAMPLE_TARGET_EP = "mock:sampleTarget";

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		from(SAMPLE_SOURCE_EP).to(SAMPLE_TARGET_EP);
	}
}
