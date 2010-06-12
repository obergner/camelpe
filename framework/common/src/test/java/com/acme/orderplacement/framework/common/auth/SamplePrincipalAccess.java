/**
 * 
 */
package com.acme.orderplacement.framework.common.auth;

import java.security.Principal;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess;

/**
 * <p>
 * TODO: Insert short summary for SamplePrincipalAccess
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SamplePrincipalAccess implements PrincipalAccess {

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		return null;
	}

}
