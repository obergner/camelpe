/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.auth;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.acme.orderplacement.jee.framework.common.auth.AuthenticationService;

/**
 * <p>
 * An implementation of {@link AuthenticationService
 * <code>AuthenticationService</code>} that delegates to <tt>Glassfish</tt>'s
 * {@link ProgrammaticLogin <code>ProgrammaticLogin</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JaasAuthenticationService implements AuthenticationService {

	/**
	 * @see com.acme.orderplacement.jee.framework.common.auth.AuthenticationService#login(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void login(final String username, final String password)
			throws FailedLoginException {
		try {
			final LoginContext loginContext = new LoginContext(
					"orderplacement",
					new ConstantUsernamePasswordCallbackHandler(username,
							password));
			loginContext.login();
		} catch (final LoginException e) {
			throw new FailedLoginException(
					"Failed to authenticate user identified by username = ["
							+ username + "] and password = [HIDDEN]: "
							+ e.getMessage());
		}
	}

}
