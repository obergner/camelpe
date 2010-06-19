/**
 * 
 */
package com.acme.orderplacement.jee.item.wsapi;

import javax.xml.namespace.QName;

/**
 * <p>
 * TODO: Insert short summary for ItemstorageQNames
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class ItemstorageQNames {

	public static final QName AUTHENTICATION_HEADER = new QName(
			ItemstorageNamespaces.NS_AUTHENTICATION_HEADER_1_0,
			"AuthenticationContext");
}
