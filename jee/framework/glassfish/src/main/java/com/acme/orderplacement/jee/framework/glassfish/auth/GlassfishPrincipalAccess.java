/**
 * 
 */
package com.acme.orderplacement.jee.framework.glassfish.auth;

import java.security.Principal;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess;
import com.sun.enterprise.security.SecurityContext;

/**
 * <p>
 * A <a href="http://dev.java.net.glassfish">Glassfish</a> specific
 * implementation of {@link PrincipalAccess <code>PrincipalAccess</code>} that
 * delegates to Glassfish's {@link com.sun.enterprise.security.SecurityContext
 * <code>com.sun.enterprise.security.SecurityContext</code>} when obtaining the
 * current <code>Principal</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class GlassfishPrincipalAccess implements PrincipalAccess {

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		return SecurityContext.getCurrent().getCallerPrincipal();
	}

}
