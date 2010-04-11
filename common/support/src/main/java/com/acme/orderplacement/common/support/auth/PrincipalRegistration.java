/**
 * 
 */
package com.acme.orderplacement.common.support.auth;

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

	/**
	 * @param principal
	 */
	void registerCurrentPrincipal(final Principal principal);

	void unregisterCurrentPrincipal();
}
