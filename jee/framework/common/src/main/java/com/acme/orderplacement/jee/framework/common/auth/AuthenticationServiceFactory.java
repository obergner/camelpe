/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.auth;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for AuthenticationServiceFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class AuthenticationServiceFactory implements AuthenticationService.Factory {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private AuthenticationService cachedInstance;

	/**
	 * @see com.acme.orderplacement.jee.framework.common.auth.AuthenticationService.Factory#getAuthenticationService()
	 */
	@Override
	public AuthenticationService getAuthenticationService() {
		if (this.cachedInstance == null) {
			synchronized (this) {
				if (this.cachedInstance == null) {
					this.cachedInstance = loadAuthenticationService();
				}
			}
		}
		return this.cachedInstance;
	}

	/**
	 * @return
	 * @throws IllegalStateException
	 */
	private synchronized AuthenticationService loadAuthenticationService()
			throws IllegalStateException {
		getLog().debug("Attempting to load an implementation of [{}] ...",
				AuthenticationService.class.getName());

		final ServiceLoader<AuthenticationService> authenticationServiceLoader = ServiceLoader
				.load(AuthenticationService.class);
		final Set<AuthenticationService> availableAuthenticationServiceImplementations = new HashSet<AuthenticationService>();
		for (final AuthenticationService authenticationServiceImpl : authenticationServiceLoader) {
			availableAuthenticationServiceImplementations
					.add(authenticationServiceImpl);
		}
		if (availableAuthenticationServiceImplementations.isEmpty()) {
			throw new IllegalStateException(
					"No implementation of ["
							+ AuthenticationService.class.getName()
							+ "] found on classpath. Please make sure that a file [META-INF/services/"
							+ AuthenticationService.class.getName()
							+ "] containing the fully qualified name of a class implementing ["
							+ AuthenticationService.class.getName()
							+ "] can be found in the current ThreadContextClassLoader.");
		}
		if (availableAuthenticationServiceImplementations.size() > 1) {
			throw new IllegalStateException(
					"More than one ["
							+ availableAuthenticationServiceImplementations
									.size()
							+ "] implementation of ["
							+ AuthenticationService.class.getName()
							+ "] found on classpath. Please make sure that only one file [META-INF/services/"
							+ AuthenticationService.class.getName()
							+ "] containing the fully qualified name of a class implementing ["
							+ AuthenticationService.class.getName()
							+ "] can be found in the current ThreadContextClassLoader.");
		}
		final AuthenticationService uniqueImplementation = availableAuthenticationServiceImplementations
				.iterator().next();
		getLog().debug("Unique implementation [{}] of [{}] found.",
				uniqueImplementation, AuthenticationService.class.getName());

		return uniqueImplementation;
	}

	/**
	 * @return the log
	 */
	private Logger getLog() {
		return this.log;
	}
}
