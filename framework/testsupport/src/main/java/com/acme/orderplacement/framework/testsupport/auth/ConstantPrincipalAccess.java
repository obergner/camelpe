/**
 * 
 */
package com.acme.orderplacement.framework.testsupport.auth;

import java.security.Principal;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess;

/**
 * <p>
 * An implementation of {@link PrincipalAccess <code>PrincipalAccess</code>}
 * which, for testing purposes, returns a fake {@link Principal
 * <code>Principal</code>} having a constant username.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConstantPrincipalAccess implements PrincipalAccess {

	private static final String USERNAME = "TESTER";

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		return new Principal() {
			@Override
			public String getName() {
				return USERNAME;
			}
		};
	}
}
