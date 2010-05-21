/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jndi;

/**
 * <p>
 * TODO: Insert short summary for JndiNames
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class JndiNames {

	public static final String PLATFORM_ADAPTER_APPLICATION_CONTEXT = "java:spring/com/acme/orderplacement/app/SharedApplicationContext";

	public static final String ITEM_MODULE_APPLICATION_CONTEXT = "java:spring/com/acme/orderplacement/item/ejb/ItemModuleApplicationContext";

	private JndiNames() {
		// Empty
	}
}
