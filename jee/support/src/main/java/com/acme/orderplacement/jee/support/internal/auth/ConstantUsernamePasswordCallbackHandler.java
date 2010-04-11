/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.auth;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * <p>
 * TODO: Insert short summary for ConstantUsernamePasswordCallbackHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConstantUsernamePasswordCallbackHandler implements CallbackHandler {

	private static final String PASSWORD = "admin";

	private static final String USERNAME = "admin";

	/**
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	public void handle(final Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (final Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				((NameCallback) callback).setName(USERNAME);
			} else if (callback instanceof PasswordCallback) {
				((PasswordCallback) callback).setPassword(PASSWORD
						.toCharArray());
			}
		}
	}
}
