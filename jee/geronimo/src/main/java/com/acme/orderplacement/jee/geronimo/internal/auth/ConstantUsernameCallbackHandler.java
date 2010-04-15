/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.internal.auth;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;

import org.apache.geronimo.security.credentialstore.SingleCallbackHandler;

/**
 * <p>
 * TODO: Insert short summary for ConstantUsernameCallbackHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConstantUsernameCallbackHandler implements SingleCallbackHandler {

	private static final long serialVersionUID = 1L;

	private static final String USERNAME = "admin";

	/**
	 * @see org.apache.geronimo.security.credentialstore.SingleCallbackHandler#getCallbackType()
	 */
	public String getCallbackType() {
		return getClass().getName();
	}

	/**
	 * @see org.apache.geronimo.security.credentialstore.SingleCallbackHandler#handle(javax.security.auth.callback.Callback)
	 */
	public void handle(final Callback callback) {
		if (callback instanceof NameCallback) {
			((NameCallback) callback).setName(USERNAME);
		}
	}
}
