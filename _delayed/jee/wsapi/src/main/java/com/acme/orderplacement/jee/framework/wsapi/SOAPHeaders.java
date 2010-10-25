/**
 * 
 */
package com.acme.orderplacement.jee.framework.wsapi;

import javax.xml.namespace.QName;

/**
 * <p>
 * TODO: Insert short summary for SOAPHeaders
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class SOAPHeaders {

	public abstract static class AuthenticationContext {

		public static final String NS_AUTHENTICATION_CONTEXT_1_0 = "http://www.acme.com/schema/ns/itemstorageservice/headers/authentication/1.0/";

		public static final QName QN_AUTHENTICATION_HEADER = new QName(
				NS_AUTHENTICATION_CONTEXT_1_0, "AuthenticationContext");

		public static final QName QN_USERNAME = new QName(
				NS_AUTHENTICATION_CONTEXT_1_0, "username");

		public static final QName QN_PASSWORD = new QName(
				NS_AUTHENTICATION_CONTEXT_1_0, "password");
	}
}
