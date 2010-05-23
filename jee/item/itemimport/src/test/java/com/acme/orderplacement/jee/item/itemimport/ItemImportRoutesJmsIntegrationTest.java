/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport;

import static junit.framework.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.framework.persistence.config.PlatformIntegrationConfig;
import com.acme.orderplacement.framework.testsupport.annotation.TestUser;
import com.acme.orderplacement.framework.testsupport.annotation.spring.PrincipalRegistrationTestExecutionListener;
import com.acme.orderplacement.jee.item.itemimport.ItemImportBoundaryRoutes;
import com.acme.orderplacement.jee.item.itemimport.ItemImportCoreRoutes;
import com.acme.orderplacement.jee.item.persistence.ItemDao;

/**
 * <p>
 * An integration test for verifying that the <tt>Integration Layer</tt> is
 * correctly wired.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = { "classpath:META-INF/spring/integration.inbound.itemimport.testEnvironment.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		PrincipalRegistrationTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME, defaultRollback = true)
@Transactional
public class ItemImportRoutesJmsIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * It's the number of the item contained in our
	 * ValidItemCreatedEventMsg.xml.
	 */
	private static final String ITEM_NUMBER_TO_REGISTER = "ITM-23-567884";

	@Resource(name = ItemDao.REPOSITORY_NAME)
	private ItemDao itemDao;

	@Resource(name = "integration.inbound.item.CamelContext")
	private CamelContext camelContext;

	@EndpointInject(uri = MockEndpoints.EXCEPTION_HANDLER_MOCK_EP)
	protected MockEndpoint exceptionEndpoint;

	@Produce(uri = ItemImportCoreRoutes.INCOMING_XML_MESSAGES)
	protected ProducerTemplate template;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Before
	public void startCamelContext() throws Exception {
		this.camelContext.start();
	}

	@After
	public void shutdownCamelContext() throws Exception {
		this.camelContext.stop();
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void testThatIntegrationLayerProperlyPropagatesItemCreatedEvents()
			throws Exception {
		final String validItemCreatedEventMsg = readFile("ValidItemCreatedEventMsg.xml");
		this.template.sendBodyAndHeader(validItemCreatedEventMsg, "foo", "bar");

		final Item registeredItem = this.itemDao
				.findByItemNumber(ITEM_NUMBER_TO_REGISTER);
		assertNotNull("The item [" + validItemCreatedEventMsg
				+ "] has NOT been persisted", registeredItem);
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

	/**
	 * <p>
	 * TODO: Insert short summary for JmsTestRoutes
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	static final class JmsTestRoutes extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from(ItemImportBoundaryRoutes.FAULT_MESSAGES_PROPAGATION).to(
					MockEndpoints.EXCEPTION_HANDLER_MOCK_EP);
		}
	}
}
