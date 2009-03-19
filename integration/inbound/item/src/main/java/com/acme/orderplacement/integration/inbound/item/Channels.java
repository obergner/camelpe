/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item;

/**
 * <p>
 * Defines the well-known names of our
 * {@link org.springframework.integration.core.MessageChannel
 * <code>MessageChannel</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class Channels {

	public static final String ITEM_CREATED_EVENTS_XML = "integration.inbound.item.xml.ItemCreatedEventsChannel";

	public static final String ITEM_CREATED_EVENTS_JAXB = "integration.inbound.item.jaxb.ItemCreatedEventsChannel";

	public static final String ITEM_CREATED_EVENTS_TARGET = "integration.inbound.item.target.ItemCreatedEventsChannel";

	/**
	 * 
	 */
	private Channels() {
		// Empty
	}

}
