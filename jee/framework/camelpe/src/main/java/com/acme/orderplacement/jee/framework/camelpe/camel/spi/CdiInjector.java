/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Singleton;

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
	public <T> T newInstance(final Class<T> type) {
		Validate.notNull(type, "type");
		this.log.trace("Creating new configured instance of type = [{}] ...",
				type.getName());

		final Bean<T> bean = lookupBeanInBeanManager(type);

		final T beanInstance = createBeanInstance(type, bean);

		final InjectionTarget<T> injectionTarget = createInjectionTargetFor(type);
		injectDependenciesIntoBeanInstance(beanInstance, bean, injectionTarget);

		invokePostConstrucAnnotatedMethodsOnBeanInstance(beanInstance,
				injectionTarget);

		return beanInstance;
	}

	/**
	 * @see org.apache.camel.spi.Injector#newInstance(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public <T> T newInstance(final Class<T> type, final Object instance) {
		Validate.notNull(type, "type");
		Validate.notNull(instance, "instance");
		Validate.isTrue(type.isAssignableFrom(instance.getClass()),
				"The supplied instance [" + instance
						+ "] is not an instance of type [" + type.getName()
						+ "]");
		this.log
				.trace(
						"Creating new configured instance of type = [{}], optionally using instance [{}] ...",
						type.getName(), instance);

		if (isSingleton(type)) {
			this.log
					.debug(
							"The supplied type [{}] is annotated to be a singleton: returning the supplied instance [{}]",
							type.getName(), instance);

			return type.cast(instance);
		}

		return newInstance(type);
	}

	/**
	 * @param <T>
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 */
	private <T> Bean<T> lookupBeanInBeanManager(final Class<T> type)
			throws IllegalArgumentException {
		final Set<Bean<?>> beans = this.beanManager.getBeans(type);
		if (beans.isEmpty()) {
			throw new IllegalArgumentException("No beans having type = ["
					+ type.getName() + "] could be found in CDI registry");
		}
		// TODO: What is the appropriate behavior if more than one matching
		// bean is found?
		if (beans.size() > 1) {
			this.log
					.warn(
							"Found more than one [{}] beans having type = [{}] in CDI registry: will use arbitrarily chosen bean",
							Integer.valueOf(beans.size()), type.getName());
		}

		return (Bean<T>) beans.iterator().next();
	}

	/**
	 * @param <T>
	 * @param type
	 * @param bean
	 * @return
	 */
	private <T> T createBeanInstance(final Class<T> type, final Bean<T> bean) {
		final T beanInstance = type.cast(this.beanManager.getReference(bean,
				type, this.beanManager.createCreationalContext(bean)));
		this.log.trace(
				"Obtained bean instance [{}] of type = [{}] from CDI registry",
				beanInstance, type.getName());

		return beanInstance;
	}

	/**
	 * @param <T>
	 * @param type
	 * @return
	 */
	private <T> InjectionTarget<T> createInjectionTargetFor(final Class<T> type) {
		final AnnotatedType<T> annotatedType = this.beanManager
				.createAnnotatedType(type);
		final InjectionTarget<T> injectionTarget = this.beanManager
				.createInjectionTarget(annotatedType);

		return injectionTarget;
	}

	/**
	 * @param <T>
	 * @param beanInstance
	 * @param bean
	 * @param injectionTarget
	 */
	private <T> void injectDependenciesIntoBeanInstance(final T beanInstance,
			final Bean<T> bean, final InjectionTarget<T> injectionTarget) {
		injectionTarget.inject(beanInstance, this.beanManager
				.createCreationalContext(bean));
		this.log.trace(
				"Injected dependencies into bean instance [{}] of type = [{}]",
				beanInstance, beanInstance.getClass().getName());
	}

	/**
	 * @param <T>
	 * @param beanInstance
	 * @param injectionTarget
	 */
	private <T> void invokePostConstrucAnnotatedMethodsOnBeanInstance(
			final T beanInstance, final InjectionTarget<T> injectionTarget) {
		injectionTarget.postConstruct(beanInstance);
		this.log
				.trace(
						"Invoked @PostConstruct annotated methods on bean instance [{}] of type = [{}]",
						beanInstance, beanInstance.getClass().getName());
	}

	/**
	 * @param <T>
	 * @param type
	 * @return
	 */
	private <T> boolean isSingleton(final Class<T> type) {
		return type.isAnnotationPresent(Singleton.class)
				|| type.isAnnotationPresent(ApplicationScoped.class);
	}
}
