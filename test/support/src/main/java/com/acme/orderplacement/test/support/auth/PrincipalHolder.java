/**
 * 
 */
package com.acme.orderplacement.test.support.auth;

import java.security.Principal;

import org.springframework.stereotype.Component;

import com.acme.orderplacement.common.support.auth.PrincipalAccess;
import com.acme.orderplacement.common.support.auth.PrincipalRegistration;

/**
 * <p>
 * TODO: Insert short summary for PrincipalHolder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalHolder.COMPONENT_NAME)
public class PrincipalHolder implements PrincipalAccess, PrincipalRegistration {

	public static final String COMPONENT_NAME = "persistence.testsupport.PrincipalHolder";

	private final ThreadLocal<Principal> currentPrincipal = new ThreadLocal<Principal>();

	/**
	 * @see com.acme.orderplacement.common.support.auth.PrincipalAccess#currentPrincipal()
	 */
	public Principal currentPrincipal() {
		return this.currentPrincipal.get();
	}

	/**
	 * @see com.acme.orderplacement.common.support.auth.PrincipalRegistration#registerCurrentPrincipal(java.security.Principal)
	 */
	public void registerCurrentPrincipal(final Principal principal) {
		this.currentPrincipal.set(principal);
	}

	/**
	 * @see com.acme.orderplacement.common.support.auth.PrincipalRegistration#unregisterCurrentPrincipal()
	 */
	public void unregisterCurrentPrincipal() {
		this.currentPrincipal.remove();
	}
}
