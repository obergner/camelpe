/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.auth;

/**
 * <p>
 * A simple security service for authenticating a user via username and
 * password.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface AuthenticationService {

	public interface Factory {

		AuthenticationService getAuthenticationService();
	}

	Factory FACTORY = new AuthenticationServiceFactory();

	void login(final String username, final String password)
			throws javax.security.auth.login.FailedLoginException;
}
