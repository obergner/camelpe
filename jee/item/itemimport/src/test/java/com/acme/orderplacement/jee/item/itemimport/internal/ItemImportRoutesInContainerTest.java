/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.jee.framework.camel.event.EventProcessingContextProvider;
import com.acme.orderplacement.jee.framework.camel.jmslog.IncomingMessageExchangeLogging;
import com.acme.orderplacement.jee.item.itemimport.config.IncomingItemCreatedEventsEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.ItemRegistrationServiceEndpoint;
import com.acme.orderplacement.jee.item.itemimport.config.OutgoingItemImportFailedMessagesEndpoint;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder;

/**
 * <p>
 * TODO: Insert short summary for ItemImportRoutesInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class ItemImportRoutesInContainerTest extends CamelTestSupport {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@EndpointInject(uri = AlternativeItemImportRoutesConfiguration.MOCK_ITEM_REGISTRATION_SERVICE_EP)
	protected MockEndpoint resultEndpoint;

	@EndpointInject(uri = AlternativeItemImportRoutesConfiguration.MOCK_ITEM_IMPORT_FAILED_EP)
	protected MockEndpoint exceptionEndpoint;

	@Produce(uri = AlternativeItemImportRoutesConfiguration.DIRECT_ITEM_CREATED_EVENTS_EP)
	protected ProducerTemplate template;

	@Inject
	private ItemImportRoutes itemImportRoutes;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap.create(JavaArchive.class,
				"test.jar").addPackages(false,
				EventProcessingContextHolder.class.getPackage(),
				EventProcessingContextProvider.class.getPackage(),
				IncomingItemCreatedEventsEndpoint.class.getPackage(),
				ItemImportRoutes.class.getPackage()).addClasses(
				IncomingMessageExchangeLogging.class,
				OutgoingItemImportFailedMessagesEndpoint.class,
				ItemRegistrationServiceEndpoint.class).addManifestResource(
				new File("src/test/resources/META-INF/beans.xml"),
				ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.itemimport.internal.ItemImportRoutes#configure()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatItemImportRoutesSuccessfullyRouteValidMessage()
			throws Exception {
		this.context.addRoutes(this.itemImportRoutes);

		final String validItemCreatedEventMsg = readFile("ValidItemCreatedEventMsg.xml");

		this.resultEndpoint.expectedMessageCount(1);

		this.template.sendBodyAndHeader(validItemCreatedEventMsg, "foo", "bar");

		this.resultEndpoint.assertIsSatisfied();
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.itemimport.internal.ItemImportRoutes#configure()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatItemImportRoutesPropagateFailedImportsToDeadLetterChannel()
			throws Exception {
		this.context.addRoutes(this.itemImportRoutes);

		final String invalidItemCreatedEventMsg = readFile("InvalidItemCreatedEventMsg.xml");

		this.exceptionEndpoint.expectedMessageCount(1);

		this.template.sendBodyAndHeader(invalidItemCreatedEventMsg, "foo",
				"bar");

		this.exceptionEndpoint.assertIsSatisfied();
	}

	@Test
	public void assertThatItemImportRoutesConvertXmlMessageToItemDto()
			throws Exception {
		this.context.addRoutes(this.itemImportRoutes);

		final String validItemCreatedEventMsg = readFile("ValidItemCreatedEventMsg.xml");

		this.resultEndpoint.expectedBodyReceived().body(ItemDto.class);
		this.resultEndpoint.message(0).body().isNotNull();
		this.resultEndpoint.message(0).body().in(new ContainsExpectedItemDto());

		this.template.sendBodyAndHeader(validItemCreatedEventMsg, "foo", "bar");

		this.resultEndpoint.assertIsSatisfied();
	}

	/**
	 * <p>
	 * TODO: Insert short summary for ContainsExpectedItemDto
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	final class ContainsExpectedItemDto implements Predicate {

		private static final String EXPECTED_ITEM_NUMBER = "ITM-23-567884";

		private static final String EXPECTED_ITEM_NAME = "Item Created Event";

		@Override
		public boolean matches(final Exchange exchange) {
			final ItemDto itemDto = exchange.getIn().getBody(ItemDto.class);

			return itemDto.getItemNumber().equals(EXPECTED_ITEM_NUMBER)
					&& itemDto.getName().equals(EXPECTED_ITEM_NAME);
		}
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
