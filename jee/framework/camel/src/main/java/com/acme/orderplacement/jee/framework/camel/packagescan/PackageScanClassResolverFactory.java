/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.packagescan;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.camel.impl.DefaultPackageScanClassResolver;
import org.apache.camel.spi.PackageScanClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for PackageScanClassResolverFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PackageScanClassResolverFactory {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final PackageScanClassResolverFactory INSTANCE = new PackageScanClassResolverFactory();

	private final Logger log = LoggerFactory.getLogger(getClass());

	private PackageScanClassResolver cachedInstance;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private PackageScanClassResolverFactory() {
		// Noop
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public PackageScanClassResolver getPackageScanClassResolver() {
		if (this.cachedInstance == null) {
			synchronized (this) {
				if (this.cachedInstance == null) {
					this.cachedInstance = loadPackageScanClassResolver();
				}
			}
		}
		return this.cachedInstance;
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	/**
	 * @return
	 * @throws IllegalStateException
	 */
	private synchronized PackageScanClassResolver loadPackageScanClassResolver()
			throws IllegalStateException {
		getLog().debug("Attempting to load an implementation of [{}] ...",
				PackageScanClassResolver.class.getName());

		final ServiceLoader<PackageScanClassResolver> authenticationServiceLoader = ServiceLoader
				.load(PackageScanClassResolver.class);
		final Set<PackageScanClassResolver> availablePackageScanClassResolverImplementations = new HashSet<PackageScanClassResolver>();
		for (final PackageScanClassResolver authenticationServiceImpl : authenticationServiceLoader) {
			availablePackageScanClassResolverImplementations
					.add(authenticationServiceImpl);
		}
		if (availablePackageScanClassResolverImplementations.size() > 1) {
			throw new IllegalStateException(
					"More than one ["
							+ availablePackageScanClassResolverImplementations
									.size()
							+ "] implementation of ["
							+ PackageScanClassResolver.class.getName()
							+ "] found on classpath. Please make sure that only one file [META-INF/services/"
							+ PackageScanClassResolver.class.getName()
							+ "] containing the fully qualified name of a class implementing ["
							+ PackageScanClassResolver.class.getName()
							+ "] can be found in the current ThreadContextClassLoader.");
		}
		final PackageScanClassResolver uniqueImplementation;
		if (availablePackageScanClassResolverImplementations.isEmpty()) {
			uniqueImplementation = new DefaultPackageScanClassResolver();
			getLog()
					.info(
							"No implementation of [{}] found on classpath. Using default implementation [{}].",
							PackageScanClassResolver.class.getName(),
							DefaultPackageScanClassResolver.class.getName());
		} else {
			uniqueImplementation = availablePackageScanClassResolverImplementations
					.iterator().next();
			getLog().info("Unique implementation [{}] of [{}] found.",
					uniqueImplementation,
					PackageScanClassResolver.class.getName());
		}

		return uniqueImplementation;
	}

	/**
	 * @return the log
	 */
	private Logger getLog() {
		return this.log;
	}
}
