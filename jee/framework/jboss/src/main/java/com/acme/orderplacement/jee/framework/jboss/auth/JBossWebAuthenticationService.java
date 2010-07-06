/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.auth;

import javax.security.auth.login.FailedLoginException;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import com.acme.orderplacement.jee.framework.common.auth.AuthenticationService;

/**
 * <p>
 * An implementation of {@link AuthenticationService
 * <code>AuthenticationService</code>} that delegates to <tt>JBoss</tt>'
 * {@link org.jboss.web.tomcat.security.login.WebAuthentication
 * <code>WebAuthentication</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JBossWebAuthenticationService implements AuthenticationService {

	/**
	 * @see com.acme.orderplacement.jee.framework.common.auth.AuthenticationService#login(java.lang.String,java.lang.String)
	 */
	@Override
	public void login(final String username, final String password)
			throws FailedLoginException {
		final WebAuthentication webAuthenticationService = new WebAuthentication();
		final boolean loginSucceeded = webAuthenticationService.login(username,
				password);
		if (!loginSucceeded) {
			throw new FailedLoginException(
					"Failed to authenticate user identified by username = ["
							+ username + "] and password = [HIDDEN]");
		}
	}
}
