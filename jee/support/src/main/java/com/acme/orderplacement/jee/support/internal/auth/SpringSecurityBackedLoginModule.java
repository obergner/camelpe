/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.auth;

import java.util.Collection;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * TODO: Insert short summary for SpringSecurityBackedLoginModule
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SpringSecurityBackedLoginModule implements LoginModule {

	private static final String DEFAULT_USERNAME = "guest";

	private static final String DEFAULT_PASSWORD = "guest";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Authentication authentication;

	private Subject subject;

	private boolean ignoreMissingAuthentication = false;

	/**
	 * Abort the authentication process by forgetting the Spring Security
	 * <code>Authentication</code>.
	 * 
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @exception LoginException
	 *                if the abort fails
	 */
	public boolean abort() throws LoginException {
		this.log
				.debug(
						"Aborting JAAS login request for Spring Security authenticated user [{}] ...",
						this.authentication);
		if (this.authentication == null) {
			this.log
					.warn("No authentication has been registered by Spring Security prior to attempting JAAS login");

			return false;
		}

		this.authentication = null;
		this.log
				.info(
						"JAAS login request for Spring Security authenticated user [{}] has been aborted",
						this.authentication);

		return true;
	}

	/**
	 * Authenticate the <code>Subject</code> (phase two) by adding the Spring
	 * Security <code>Authentication</code> to the <code>Subject</code>'s
	 * principals.
	 * 
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * 
	 * @exception LoginException
	 *                if the commit fails
	 */
	public boolean commit() throws LoginException {
		this.log
				.info(
						"Committing JAAS login for Spring Security authenticated user [{}] ...",
						this.authentication);
		if (this.authentication == null) {
			this.log
					.warn("No authentication has been registered by Spring Security prior to committing JAAS login");

			return false;
		}

		this.subject.getPrincipals().add(this.authentication);
		this.log
				.info(
						"JAAS login for Spring Security authenticated user [{}] has been committed",
						this.authentication);

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

		if (options != null) {
			this.ignoreMissingAuthentication = "true".equals(options
					.get("ignoreMissingAuthentication"));
		}
		this.log
				.info(
						"Initialized Spring Security backed JAAS login module for Subject [{}] using options [{}] and shared state [{}]",
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
		this.authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		this.log
				.info(
						"Attempting to perform a JAAS login for user [{}] previously authenticated by Spring Security ...",
						this.authentication);

		if (this.authentication == null) {
			this.log
					.warn(
							"No authentication found in Spring Security context. Assuming default user having username = [{}]",
							DEFAULT_USERNAME);
			this.authentication = new UsernamePasswordAuthenticationToken(
					DEFAULT_USERNAME, DEFAULT_PASSWORD,
					(Collection<GrantedAuthority>) null);
			SecurityContextHolder.getContext().setAuthentication(
					this.authentication);
			// final String msg =
			// "Login cannot complete, authentication not found in security context";
			//
			// if (this.ignoreMissingAuthentication) {
			// this.log.warn(msg);
			//
			// return false;
			// }
			// throw new LoginException(msg);
		}

		this.log
				.info(
						"Successfully logged in user [{}] (previously authenticated by Spring Security) via JAAS",
						this.authentication);

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
		this.log
				.info(
						"Attempting to log out user [{}] previously authenticated by Spring Security from JAAS",
						this.authentication);
		if (this.authentication == null) {
			this.log
					.warn("No authentication found in Spring security context. Aborting attempt to log out from JAAS.");

			return false;
		}

		this.subject.getPrincipals().remove(this.authentication);
		this.authentication = null;
		this.log
				.info(
						"User [{}] (previously authenticated by Spring Security) has been logged out from JAAS",
						this.authentication);

		return true;
	}

}
