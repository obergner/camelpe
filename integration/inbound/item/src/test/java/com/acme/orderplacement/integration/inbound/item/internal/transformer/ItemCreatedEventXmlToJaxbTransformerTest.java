/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item.internal.transformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;

import com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent;

/**
 * <p>
 * Test {@link ItemCreatedEventXmlToJaxbTransformer}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemCreatedEventXmlToJaxbTransformerTest {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final String EXPECTED_ITEM_NUMBER = "ITM-23-567884";

	private final ItemCreatedEventXmlToJaxbTransformer classUnderTest = new ItemCreatedEventXmlToJaxbTransformer();

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.item.internal.transformer.ItemCreatedEventXmlToJaxbTransformer#transformFromXml(java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void transformFromXmlShouldNotReturnNullForValidInput()
			throws IOException, JAXBException {
		final String validTestDoc = readFile("ValidItemCreatedEventMsg.xml");
		final ItemCreatedEvent converted = this.classUnderTest
				.transformFromXml(validTestDoc);

		Assert.assertNotNull("transformFromXml(validTestDoc) returned <null>",
				converted);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.item.internal.transformer.ItemCreatedEventXmlToJaxbTransformer#transformFromXml(java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void transformFromXmlShouldNotReturnValidJaxbObjectForValidInput()
			throws IOException, JAXBException {
		final String validTestDoc = readFile("ValidItemCreatedEventMsg.xml");
		final ItemCreatedEvent converted = this.classUnderTest
				.transformFromXml(validTestDoc);

		Assert.assertNotNull("transformFromXml(validTestDoc) returned "
				+ "ItemCreatedEvent without createdItem property", converted
				.getCreatedItem());
		Assert.assertEquals("transformFromXml(validTestDoc) returned "
				+ "ItemCreatedEvent with wrong itemNumber",
				EXPECTED_ITEM_NUMBER, converted.getCreatedItem()
						.getItemNumber());
		Assert.assertNotNull("transformFromXml(validTestDoc) returned "
				+ "ItemCreatedEvent without ItemSpecs", converted
				.getCreatedItem().getItemSpecs());
		Assert.assertNotNull("transformFromXml(validTestDoc) returned "
				+ "ItemCreatedEvent without ItemSpecs", converted
				.getCreatedItem().getItemSpecs().getItemSpec());
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
