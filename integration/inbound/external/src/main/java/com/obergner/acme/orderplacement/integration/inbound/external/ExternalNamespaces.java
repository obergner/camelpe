/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external;

/**
 * <p>
 * TODO: Insert short summary for ExternalNamespaces
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ExternalNamespaces {

	public static final String NS_EVENT_ENVELOPE_1_0 = "http://www.external.com/schema/ns/event/event-envelope/1.0/";

	public static final String NS_SCHEMA_VERSIONING = "http://www.external.com/schema/ns/schema-versioning/";

	public static final String NS_ITEM_MODEL_1_0 = "http://www.external.com/schema/ns/models/item/1.0/";

	public static final String NS_ITEM_MODE_2_0 = "http://www.external.com/schema/ns/models/item/2.0/";

	public static final String NS_ITEM_CREATED_EVENT_1_0 = "http://www.external.com/schema/ns/events/itemcreated/1.0/";

	public static final String NS_ITEM_CREATED_EVENT_2_0 = "http://www.external.com/schema/ns/events/itemcreated/2.0/";

	private ExternalNamespaces() {
		// Noop
	}
}
