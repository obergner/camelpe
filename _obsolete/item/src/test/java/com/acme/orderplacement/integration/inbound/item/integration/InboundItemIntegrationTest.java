/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.message.StringMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * An integration test for verifying that the <tt>Integration Layer</tt> is
 * correctly wired.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ContextConfiguration(locations = { "classpath:/META-INF/spring/integration.inbound.item.test.integrationLayer.scontext" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InboundItemIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@Resource(name = "integration.inbound.item.xml.ItemCreatedEventsChannel")
	private MessageChannel messageChannel;

	@Resource(name = ItemDtoReceiver.COMPONENT_NAME)
	private ItemDtoReceiver testServiceActivator;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		this.testServiceActivator.getReceivedItemDtos().clear();
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public final void integrationLayerShouldProperlyPropagateAndTransformItemCreatedEvents()
			throws IOException {
		final String validTestDoc = readFile("ValidItemCreatedEventMsg.xml");
		this.messageChannel.send(new StringMessage(validTestDoc));

		Assert.assertEquals(
				"Integration layer did not propagate ItemCreatedEvent", 1,
				this.testServiceActivator.getReceivedItemDtos().size());
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
