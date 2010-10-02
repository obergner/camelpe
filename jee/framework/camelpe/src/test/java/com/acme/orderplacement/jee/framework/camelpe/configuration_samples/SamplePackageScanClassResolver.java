/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.configuration_samples;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.PackageScanFilter;

import com.acme.orderplacement.jee.framework.camelpe.CamelContextModifying;

/**
 * <p>
 * TODO: Insert short summary for SamplePackageScanClassResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SamplePackageScanClassResolver implements PackageScanClassResolver {

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#addClassLoader(java.lang.ClassLoader)
	 */
	@Override
	public void addClassLoader(final ClassLoader classLoader) {
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#addFilter(org.apache.camel.spi.PackageScanFilter)
	 */
	@Override
	public void addFilter(final PackageScanFilter filter) {
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#findAnnotated(java.lang.Class,
	 *      java.lang.String[])
	 */
	@Override
	public Set<Class<?>> findAnnotated(
			final Class<? extends Annotation> annotation,
			final String... packageNames) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#findAnnotated(java.util.Set,
	 *      java.lang.String[])
	 */
	@Override
	public Set<Class<?>> findAnnotated(
			final Set<Class<? extends Annotation>> annotations,
			final String... packageNames) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#findByFilter(org.apache.camel.spi.PackageScanFilter,
	 *      java.lang.String[])
	 */
	@Override
	public Set<Class<?>> findByFilter(final PackageScanFilter filter,
			final String... packageNames) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#findImplementations(java.lang.Class,
	 *      java.lang.String[])
	 */
	@Override
	public Set<Class<?>> findImplementations(final Class<?> parent,
			final String... packageNames) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#getClassLoaders()
	 */
	@Override
	public Set<ClassLoader> getClassLoaders() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#removeFilter(org.apache.camel.spi.PackageScanFilter)
	 */
	@Override
	public void removeFilter(final PackageScanFilter filter) {
	}

	/**
	 * @see org.apache.camel.spi.PackageScanClassResolver#setClassLoaders(java.util.Set)
	 */
	@Override
	public void setClassLoaders(final Set<ClassLoader> classLoaders) {
	}
}
