/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * An integration test for verifying that the <tt>Integration Layer</tt> is
 * correctly wired.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemImportRoutesIntegrationTest extends CamelSpringTestSupport {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String ITEMIMPORT_SERVICE_MOCK_EP = "mock:itemimportService";

	private static final String EXCEPTION_HANDLER_MOCK_EP = "mock:exceptionHandler";

	@EndpointInject(uri = ITEMIMPORT_SERVICE_MOCK_EP)
	protected MockEndpoint resultEndpoint;

	@EndpointInject(uri = EXCEPTION_HANDLER_MOCK_EP)
	protected MockEndpoint exceptionEndpoint;

	@Produce(uri = ItemImportChannels.INCOMING_RAW)
	protected ProducerTemplate template;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.test.junit4.CamelSpringTestSupport#createApplicationContext()
	 */
	@Override
	protected AbstractXmlApplicationContext createApplicationContext() {

		return new ClassPathXmlApplicationContext(
				new String[] { "classpath:META-INF/spring/integration.inbound.itemimport.test.integrationLayer.scontext" },
				getRouteExcludingApplicationContext());
	}

	/**
	 * @see org.apache.camel.test.junit4.CamelSpringTestSupport#excludeRoute()
	 */
	@Override
	protected Class<? extends RouteBuilder> excludeRoute() {

		return ItemImportRoutesTest.TestRoutes.class;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void testThatIntegrationLayerProperlyPropagatesItemCreatedEvents()
			throws Exception {
		final String validItemCreatedEventMsg = readFile("ValidItemCreatedEventMsg.xml");

		this.resultEndpoint.expectedMessageCount(1);

		this.template.sendBodyAndHeader(validItemCreatedEventMsg, "foo", "bar");

		this.resultEndpoint.assertIsSatisfied();
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private String readFile(final String relativePath) throws IOException {
		BufferedReader reader = null;
		try {
			final StringBuffer fileData = new StringBuffer(1000);
			final InputStream is = getClass().getResourceAsStream(relativePath);
			reader = new BufferedReader(new InputStreamReader(is));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				final String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			return fileData.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

}
