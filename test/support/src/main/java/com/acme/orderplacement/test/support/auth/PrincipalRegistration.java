/**
 * 
 */
package com.acme.orderplacement.test.support.auth;

import java.security.Principal;

/**
 * <p>
 * TODO: Insert short summary for PrincipalRegistration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface PrincipalRegistration {

	String COMPONENT_NAME = "test.support.auth.PrincipalRegistration";

	/**
	 * @param principal
	 */
	void registerCurrentPrincipal(final Principal principal);

	void unregisterCurrentPrincipal();
}
