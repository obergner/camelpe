/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item.internal.transformer;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

import com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent;

/**
 * <p>
 * Convert an XML document containing an <tt>Item Created Event</tt> into an
 * equivalent Java object using <tt>JAXB</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MessageEndpoint(ItemCreatedEventXmlToJaxbTransformer.COMPONENT_NAME)
public class ItemCreatedEventXmlToJaxbTransformer {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "integration.inbound.item.ItemCreatedEventXmlToJaxbTransformer";

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param itemCreatedEventAsXml
	 * @return
	 * @throws JAXBException
	 */
	@Transformer(inputChannel = "integration.inbound.item.xml.ItemCreatedEventsChannel", outputChannel = "integration.inbound.item.jaxb.ItemCreatedEventsChannel")
	public ItemCreatedEvent transformFromXml(final String itemCreatedEventAsXml)
			throws JAXBException {
		getLog().debug(
				"Attempting to convert XML formatted ItemCreatedEvent [{}] "
						+ "into equivalent JAXB object representation ...",
				itemCreatedEventAsXml);

		final Unmarshaller unmarshaller = createUnmarshaller(itemCreatedEventAsXml);

		final JAXBElement<ItemCreatedEvent> converted = (JAXBElement<ItemCreatedEvent>) unmarshaller
				.unmarshal(new StringReader(itemCreatedEventAsXml));
		getLog()
				.debug(
						"XML formatted ItemCreatedEvent successfully converted into JAXB representation: [{}]",
						converted);

		return converted.getValue();
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	protected final Logger getLog() {

		return this.log;
	}

	/**
	 * @param itemCreatedEventAsXml
	 * @return
	 * @throws JAXBException
	 */
	private Unmarshaller createUnmarshaller(final String itemCreatedEventAsXml)
			throws JAXBException {
		final JAXBContext unmarshallingContext = JAXBContext
				.newInstance("com.external.schema.ns.events.itemcreated._1:com.external.schema.ns.models.item._1");
		final Unmarshaller unmarshaller = unmarshallingContext
				.createUnmarshaller();
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			public boolean handleEvent(final ValidationEvent event) {
				getLog()
						.warn(
								"Failed to convert XML formatted ItemCreatedEvent [{}] into equivalent JAXB representation. "
										+ "Illegal content encountered at line [{}], column [{}]: [{}]",
								new Object[] {
										itemCreatedEventAsXml,
										Integer.valueOf(event.getLocator()
												.getLineNumber()),
										Integer.valueOf(event.getLocator()
												.getColumnNumber()),
										event.getMessage() });

				return false;
			}
		});

		return unmarshaller;
	}
}
