/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.auth;

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

	private final String username;

	private final char[] password;

	/**
	 * @param username
	 * @param password
	 */
	public ConstantUsernamePasswordCallbackHandler(final String username,
			final String password) {
		this.username = username;
		this.password = password != null ? password.toCharArray() : null;
	}

	/**
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(final Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (final Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				((NameCallback) callback).setName(this.username);
			} else if (callback instanceof PasswordCallback) {
				((PasswordCallback) callback).setPassword(this.password);
			} else {
				throw new UnsupportedCallbackException(callback,
						"Callback class not supported");
			}
		}
<<<<<<< HEAD:jee/framework/jboss/src/main/java/com/acme/orderplacement/jee/framework/jboss/auth/ConstantUsernamePasswordCallbackHandler.java

	}

=======
	}
>>>>>>> 2783e200e540ea02246b9c601bccf4afe8e4fee0:jee/framework/jboss/src/main/java/com/acme/orderplacement/jee/framework/jboss/auth/ConstantUsernamePasswordCallbackHandler.java
}
