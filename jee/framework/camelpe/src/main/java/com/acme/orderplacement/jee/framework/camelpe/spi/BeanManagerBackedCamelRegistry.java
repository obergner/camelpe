/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.camel.spi.Registry;
import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for BeanManagerBackedCamelRegistry
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanManagerBackedCamelRegistry implements Registry {

	private final BeanManager delegate;

	/**
	 * @param delegate
	 * @throws IllegalArgumentException
	 */
	public BeanManagerBackedCamelRegistry(final BeanManager delegate)
			throws IllegalArgumentException {
		Validate.notNull(delegate, "delegate");
		this.delegate = delegate;
	}

	/**
	 * @see org.apache.camel.spi.Registry#lookup(java.lang.String)
	 */
	@Override
	public Object lookup(final String name) {
		final Set<Bean<?>> beans = this.delegate.getBeans(name);
		if (beans.isEmpty()) {
			return null;
		}
		if (beans.size() > 1) {
			throw new IllegalStateException(
					"Expected to find exactly one bean having name [" + name
							+ "]. Got [" + beans.size() + "]");
		}
		final Bean<?> bean = beans.iterator().next();
		final CreationalContext<?> creationalContext = this.delegate
				.createCreationalContext(null);

		return this.delegate.getReference(bean, bean.getBeanClass(),
				creationalContext);
	}

	/**
	 * @see org.apache.camel.spi.Registry#lookup(java.lang.String,
	 *      java.lang.Class)
	 */
	@Override
	public <T> T lookup(final String name, final Class<T> type) {

		return (T) lookup(name);
	}

	/**
	 * @see org.apache.camel.spi.Registry#lookupByType(java.lang.Class)
	 */
	@Override
	public <T> Map<String, T> lookupByType(final Class<T> type) {
		final Set<Bean<?>> beans = this.delegate.getBeans(type);
		if (beans.isEmpty()) {
			return Collections.emptyMap();
		}
		final Map<String, T> beansByName = new HashMap<String, T>(beans.size());
		final CreationalContext<?> creationalContext = this.delegate
				.createCreationalContext(null);
		for (final Bean<?> bean : beans) {
			beansByName.put(bean.getName(), (T) this.delegate.getReference(
					bean, type, creationalContext));
		}

		return beansByName;
	}
}
