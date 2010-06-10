/**
 * 
 */
package com.acme.orderplacement.jee.framework.glassfish.auth;

import java.security.Principal;

import com.acme.orderplacement.jee.framework.common.auth.PrincipalAccess;
import com.sun.enterprise.security.SecurityContext;

/**
 * <p>
 * TODO: Insert short summary for GlassfishPrincipalAccess
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class GlassfishPrincipalAccess implements PrincipalAccess {

	/**
	 * @see com.acme.orderplacement.jee.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		return SecurityContext.getCurrent().getCallerPrincipal();
	}

}
