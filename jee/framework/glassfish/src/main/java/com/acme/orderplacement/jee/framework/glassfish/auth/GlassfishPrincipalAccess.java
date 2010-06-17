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

	private static final String DEFAULT_PRINICPAL_NAME = "SECURITY_DISABLED";

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		final Principal callerPrincipal = SecurityContext.getCurrent()
				.getCallerPrincipal();

		return callerPrincipal != null ? callerPrincipal : new Principal() {
			@Override
			public String getName() {
				return DEFAULT_PRINICPAL_NAME;
			}
		};
	}

}
