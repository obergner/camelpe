/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport;

/**
 * <p>
 * TODO: Insert short summary for Channels
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ItemImportChannels {

	public static final String INCOMING_RAW = "direct:itemimport.incoming.raw";

	public static final String TRANSFER_JAXB = "direct:itemimport.transfer.jaxb";

	public static final String EXCEPTIONS = "direct:itemimport.exceptions";

	private ItemImportChannels() {
		// Intentionally left blank
	}

}
