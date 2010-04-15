/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.internal.auth;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.geronimo.security.realm.providers.GeronimoGroupPrincipal;
import org.apache.geronimo.security.realm.providers.GeronimoUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;

/**
 * <p>
 * TODO: Insert short summary for ConstantTestGeronimoLoginModule
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConstantTestGeronimoLoginModule implements LoginModule {

	private static final String DEFAULT_USERNAME = "admin";

	// private static final String DEFAULT_PASSWORD = "admin";

	private static final String DEFAULT_ROLE_NAME = ApplicationUserRole.ROLE_ADMIN;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Subject subject;

	/**
	 * Abort the authentication process.
	 * 
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @exception LoginException
	 *                if the abort fails
	 */
	public boolean abort() throws LoginException {
		this.log.debug("Aborting JAAS login request ...");

		return true;
	}

	/**
	 * Unconditionally authenticate the <code>Subject</code> (phase two).
	 * 
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @exception LoginException
	 *                if the commit fails
	 */
	public boolean commit() throws LoginException {
		this.log.info("Committing JAAS login ...");

		this.subject.getPrincipals().add(
				new GeronimoUserPrincipal(DEFAULT_USERNAME));
		this.subject.getPrincipals().add(
				new GeronimoGroupPrincipal(DEFAULT_ROLE_NAME));
		this.log.info("JAAS login has been committed");

		return true;
	}

	/**
	 * Initialize this <code>LoginModule</code>. Ignores the callback handler,
	 * since the code establishing the <code>LoginContext</code> likely won't
	 * provide one that understands Spring Security. Also ignores the
	 * <code>sharedState</code> and <code>options</code> parameters, since none
	 * are recognized.
	 * 
	 * @param subject
	 *            the <code>Subject</code> to be authenticated.
	 * @param callbackHandler
	 *            is ignored
	 * @param sharedState
	 *            is ignored
	 * @param options
	 *            are ignored
	 */
	public void initialize(final Subject subject,
			final CallbackHandler callbackHandler, final Map sharedState,
			final Map options) {
		this.subject = subject;

		this.log
				.info(
						"Initialized Test JAAS login module for Subject [{}] using options [{}] and shared state [{}]",
						new Object[] { subject, options, sharedState });
	}

	/**
	 * Authenticate the <code>Subject</code> (phase one) by extracting the
	 * Spring Security <code>Authentication</code> from the current
	 * <code>SecurityContext</code>.
	 * 
	 * @return true if the authentication succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @throws LoginException
	 *             if the authentication fails
	 */
	public boolean login() throws LoginException {
		this.log
				.info(
						"Successfully logged in user [{}] (statically defined test user) via JAAS",
						DEFAULT_USERNAME);

		return true;
	}

	/**
	 * Log out the <code>Subject</code>.
	 * 
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @exception LoginException
	 *                if the logout fails
	 */
	public boolean logout() throws LoginException {
		this.subject.getPrincipals().remove(null);
		this.log
				.info(
						"User [{}] (statically defined test user) has been logged out from JAAS",
						DEFAULT_USERNAME);

		return true;
	}

}
