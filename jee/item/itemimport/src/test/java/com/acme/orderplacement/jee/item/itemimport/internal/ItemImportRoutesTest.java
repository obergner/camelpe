/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;

/**
 * <p>
 * TODO: Insert short summary for ItemImportRoutesTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemImportRoutesTest extends CamelTestSupport {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@EndpointInject(uri = MockEndpoints.ITEMIMPORT_SERVICE_MOCK_EP)
	protected MockEndpoint resultEndpoint;

	@EndpointInject(uri = MockEndpoints.EXCEPTION_HANDLER_MOCK_EP)
	protected MockEndpoint exceptionEndpoint;

	@Produce(uri = ItemImportCoreRoutes.INCOMING_XML_MESSAGES)
	protected ProducerTemplate template;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Override
	protected RouteBuilder[] createRouteBuilders() {
		return new RouteBuilder[] { new ItemImportCoreRoutes(),
				new TestRoutes() };
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	public void testThatIntegrationLayerProperlyPropagatesItemCreatedEvents()
			throws Exception {
		final String validItemCreatedEventMsg = readFile("ValidItemCreatedEventMsg.xml");

		this.resultEndpoint.expectedMessageCount(1);

		this.template.sendBodyAndHeader(validItemCreatedEventMsg, "foo", "bar");

		this.resultEndpoint.assertIsSatisfied();
	}

	public void testThatIntegrationLayerProperlyPropagatesExceptions()
			throws Exception {
		final String invalidItemCreatedEventMsg = readFile("InvalidItemCreatedEventMsg.xml");

		this.exceptionEndpoint.expectedMessageCount(1);

		this.template.sendBodyAndHeader(invalidItemCreatedEventMsg, "foo",
				"bar");

		this.exceptionEndpoint.assertIsSatisfied();
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

	static final class TestRoutes extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from(ItemImportCoreRoutes.TRANSFORMED_JAVA_OBJECT_MESSAGES).to(
					MockEndpoints.ITEMIMPORT_SERVICE_MOCK_EP);
			from(ItemImportCoreRoutes.FAULT_MESSAGES).to(
					MockEndpoints.EXCEPTION_HANDLER_MOCK_EP);
		}
	}
}
