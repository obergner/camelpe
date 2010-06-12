/**
 * 
 */
package com.acme.orderplacement.framework.common.auth;

import java.security.Principal;

/**
 * <p>
 * An interface to be implemented by components that provide access to the
 * {@link java.security.Principal <code>Principal</code>} associated with the
 * current request/invocation.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface PrincipalAccess {

	public interface Factory {

		PrincipalAccess getPrincipalAccess();
	}

	Factory FACTORY = new CachingPrincipalAccessFactory();

	/**
	 * @return The current <code>Principal</code> or <code>null</code>
	 */
	Principal currentPrincipal();
}
