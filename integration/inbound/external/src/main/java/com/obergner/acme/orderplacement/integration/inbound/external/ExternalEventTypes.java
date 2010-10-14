/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external;

/**
 * <p>
 * TODO: Insert short summary for ExternalEventTypes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ExternalEventTypes {

	private static final String EVENT_TYPE_URN_SCHEME = "urn:event-type:";

	public static final String UNKNOWN = EVENT_TYPE_URN_SCHEME + "<UNKNOWN>";

	public static final String ITEM_CREATED = EVENT_TYPE_URN_SCHEME
			+ "external.item.Created";

	private ExternalEventTypes() {
		// Noop
	}

}
