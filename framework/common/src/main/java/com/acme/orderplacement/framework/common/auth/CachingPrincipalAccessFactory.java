/**
 * 
 */
package com.acme.orderplacement.framework.common.auth;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess.Factory;

/**
 * <p>
 * TODO: Insert short summary for CachingPrincipalAccessFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class CachingPrincipalAccessFactory implements Factory {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private PrincipalAccess cachedInstance;

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess.Factory#getPrincipalAccess()
	 */
	@Override
	public PrincipalAccess getPrincipalAccess() {
		if (this.cachedInstance == null) {
			synchronized (this) {
				if (this.cachedInstance == null) {
					this.cachedInstance = loadPrincipalAccess();
				}
			}
		}
		return this.cachedInstance;
	}

	/**
	 * @return
	 * @throws IllegalStateException
	 */
	private synchronized PrincipalAccess loadPrincipalAccess()
			throws IllegalStateException {
		getLog().debug("Attempting to load an implementation of [{}] ...",
				PrincipalAccess.class.getName());

		final ServiceLoader<PrincipalAccess> principalAccessLoader = ServiceLoader
				.load(PrincipalAccess.class);
		final Set<PrincipalAccess> availablePrincipalAccessImplementations = new HashSet<PrincipalAccess>();
		for (final PrincipalAccess principalAccessImpl : principalAccessLoader) {
			availablePrincipalAccessImplementations.add(principalAccessImpl);
		}
		if (availablePrincipalAccessImplementations.isEmpty()) {
			throw new IllegalStateException(
					"No implementation of ["
							+ PrincipalAccess.class.getName()
							+ "] found on classpath. Please make sure that a file [META-INF/services/"
							+ PrincipalAccess.class.getName()
							+ "] containing the fully qualified name of a class implementing ["
							+ PrincipalAccess.class.getName()
							+ "] can be found in the current ThreadContextClassLoader.");
		}
		if (availablePrincipalAccessImplementations.size() > 1) {
			throw new IllegalStateException(
					"More than one ["
							+ availablePrincipalAccessImplementations.size()
							+ "] implementation of ["
							+ PrincipalAccess.class.getName()
							+ "] found on classpath. Please make sure that only one file [META-INF/services/"
							+ PrincipalAccess.class.getName()
							+ "] containing the fully qualified name of a class implementing ["
							+ PrincipalAccess.class.getName()
							+ "] can be found in the current ThreadContextClassLoader.");
		}
		final PrincipalAccess uniqueImplementation = availablePrincipalAccessImplementations
				.iterator().next();
		getLog().debug("Unique implementation [{}] of [{}] found.",
				uniqueImplementation, PrincipalAccess.class.getName());

		return uniqueImplementation;
	}

	/**
	 * @return the log
	 */
	private Logger getLog() {
		return this.log;
	}

}
