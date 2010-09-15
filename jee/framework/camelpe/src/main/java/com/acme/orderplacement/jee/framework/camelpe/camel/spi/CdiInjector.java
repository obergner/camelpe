/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Singleton;

import org.apache.camel.IsSingleton;
import org.apache.camel.NoSuchBeanException;
import org.apache.camel.spi.Injector;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiInjector
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiInjector implements Injector {

	private final BeanManager beanManager;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @param beanManager
	 * @throws IllegalArgumentException
	 */
	public CdiInjector(final BeanManager beanManager)
			throws IllegalArgumentException {
		Validate.notNull(beanManager, "beanManager");
		this.beanManager = beanManager;
	}

	/**
	 * @see org.apache.camel.spi.Injector#newInstance(java.lang.Class)
	 */
	@Override
	public <T> T newInstance(final Class<T> type)
			throws IllegalArgumentException, NoSuchBeanException {
		Validate.notNull(type, "type");
		getLog().trace("Creating new configured instance of type = [{}] ...",
				type.getName());

		final Bean<T> bean = lookupBeanInBeanManager(type);
		getLog()
				.debug(
						"Found bean [{}] matching type = [{}] in BeanManager. "
								+ "The requested instance will be created from this bean.",
						type.getName());
		final T beanInstance = createBeanInstanceViaBeanManager(type, bean);

		getLog().trace("Successfully created new configured instance [{}].",
				beanInstance);

		return beanInstance;
	}

	/**
	 * @see org.apache.camel.spi.Injector#newInstance(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public <T> T newInstance(final Class<T> type, final Object instance)
			throws IllegalArgumentException, NoSuchBeanException {
		Validate.notNull(type, "type");
		Validate.notNull(instance, "instance");
		Validate.isTrue(type.isAssignableFrom(instance.getClass()),
				"The supplied instance [" + instance
						+ "] is not an instance of type [" + type.getName()
						+ "]");
		getLog().trace(
				"Creating new configured instance of type = [{}], "
						+ "optionally using instance [{}] ...", type.getName(),
				instance);
		if (isSingleton(instance)) {
			getLog().debug(
					"The supplied type [{}] is annotated to be a singleton: "
							+ "returning the supplied instance [{}].",
					type.getName(), instance);

			return type.cast(instance);
		}
		getLog().debug(
				"The supplied type [{}] is not known to be a singleton: "
						+ "will create a new instance.", type.getName());

		return newInstance(type);
	}

	private <T> Bean<T> lookupBeanInBeanManager(final Class<T> type)
			throws NoSuchBeanException {
		final Set<Bean<?>> beans = getBeanManager().getBeans(type);
		if (beans.isEmpty()) {
			throw new NoSuchBeanException(type.getName());
		}
		// TODO: What is the appropriate behavior if more than one matching
		// bean is found?
		if (beans.size() > 1) {
			getLog().warn(
					"Found more than one [{}] beans having type = [{}] in BeanManager: "
							+ "will use arbitrarily chosen bean.",
					Integer.valueOf(beans.size()), type.getName());
		}

		return (Bean<T>) beans.iterator().next();
	}

	private <T> T createBeanInstanceViaBeanManager(final Class<T> type,
			final Bean<T> bean) {
		final T beanInstance = type.cast(getBeanManager().getReference(bean,
				type, getBeanManager().createCreationalContext(bean)));
		getLog().trace("Obtained bean instance [{}] from BeanManager.",
				beanInstance, type.getName());

		return beanInstance;
	}

	private boolean isSingleton(final Object instance) {
		return instance.getClass().isAnnotationPresent(Singleton.class)
				|| instance.getClass().isAnnotationPresent(
						ApplicationScoped.class)
				|| ((instance instanceof IsSingleton) && ((IsSingleton) instance)
						.isSingleton());
	}

	/**
	 * @return the log
	 */
	private Logger getLog() {
		return this.log;
	}

	/**
	 * @return the beanManager
	 */
	private BeanManager getBeanManager() {
		return this.beanManager;
	}
}
