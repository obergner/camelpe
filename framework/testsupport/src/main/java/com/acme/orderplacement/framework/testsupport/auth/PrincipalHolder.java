/**
 * 
 */
package com.acme.orderplacement.framework.testsupport.auth;

import java.security.Principal;

import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO: Insert short summary for PrincipalHolder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalHolder.COMPONENT_NAME)
public class PrincipalHolder {

	public static final String COMPONENT_NAME = "test.support.auth.PrincipalHolder";

	private final ThreadLocal<Principal> currentPrincipal = new ThreadLocal<Principal>();

	/**
	 * @return
	 */
	public Principal currentPrincipal() {
		return this.currentPrincipal.get();
	}

	/**
	 * @param principal
	 */
	public void registerCurrentPrincipal(final Principal principal) {
		this.currentPrincipal.set(principal);
	}

	/**
	 * 
	 */
	public void unregisterCurrentPrincipal() {
		this.currentPrincipal.remove();
	}
}
