/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * <p>
 * TODO: Insert short summary for AlternativeItemImportRoutesConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class ItemImportRoutesConfiguration {

	private static final String ITEM_CREATED_EVENTS_EP = "hornetq:topic:ItemCreatedEventsTopic";

	private static final String ITEM_IMPORT_FAILED_EP = "hornetq:queue:ItemImportFailuresQueue";

	private static final String ITEM_REGISTRATION_SERVICE_EP = "ejb:orderplacement.jee.ear-1.0-SNAPSHOT/ItemStorageServiceBean/local?method=registerItem";

	@Produces
	@IncomingItemCreatedEventsEndpoint
	public String incomingItemCreatedEventsEndpoint() {
		return ITEM_CREATED_EVENTS_EP;
	}

	@Produces
	@OutgoingItemImportFailedMessagesEndpoint
	public String outgoingItemImportFailedMessagesEndpoint() {
		return ITEM_IMPORT_FAILED_EP;
	}

	@Produces
	@ItemRegistrationServiceEndpoint
	public String itemRegistrationServiceEndpoint() {
		return ITEM_REGISTRATION_SERVICE_EP;
	}
}
