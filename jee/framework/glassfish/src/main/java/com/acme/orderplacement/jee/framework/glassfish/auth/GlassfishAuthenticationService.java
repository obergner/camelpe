/**
 * 
 */
package com.acme.orderplacement.jee.framework.glassfish.auth;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang.BooleanUtils;

import com.acme.orderplacement.jee.framework.common.auth.AuthenticationService;
import com.sun.appserv.security.ProgrammaticLogin;

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
public class GlassfishAuthenticationService implements AuthenticationService {

	private final ProgrammaticLogin programmaticLogin = new ProgrammaticLogin();

	/**
	 * @see com.acme.orderplacement.jee.framework.common.auth.AuthenticationService#login(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void login(final String username, final String password)
			throws FailedLoginException {
		final Boolean loggedIn = this.programmaticLogin.login(username,
				password);
		if (BooleanUtils.isNotTrue(loggedIn)) {
			throw new FailedLoginException(
					"Failed to authenticate user identified by username = ["
							+ username + "] and password = [HIDDEN]");
		}
	}

}
