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

import org.apache.camel.IsSingleton;
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

		final T beanInstance;
		final InjectionTarget<T> injectionTarget = createInjectionTargetFor(type);
		final Bean<T> bean = lookupBeanInBeanManager(type);
		if (bean == null) {
			/*
			 * No matching bean found in BeanManager. Create new instance via
			 * reflection. This instance will NOT be registered with the
			 * BeanManager.
			 */
			this.log
					.debug(
							"No bean matching type = [{}] is currently registered with "
									+ "the BeanManager. A new bean instance will be created using reflection. "
									+ "This new bean instance will NOT be registered with the BeanManager.",
							type.getName());
			beanInstance = createBeanInstanceViaReflection(type);
			injectDependenciesIntoBeanInstance(beanInstance, injectionTarget);
		} else {
			this.log
					.debug(
							"Found bean [{}] matching type = [{}] in BeanManager. "
									+ "The requested instance will be created from this bean.",
							type.getName());
			beanInstance = createBeanInstanceViaBeanManager(type, bean);
			injectDependenciesIntoBeanInstance(beanInstance, bean,
					injectionTarget);
		}
		invokePostConstrucAnnotatedMethodsOnBeanInstance(beanInstance,
				injectionTarget);

		this.log.trace("Successfully created new configured instance [{}].",
				beanInstance);

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
		this.log.trace("Creating new configured instance of type = [{}], "
				+ "optionally using instance [{}] ...", type.getName(),
				instance);
		if (isSingleton(instance)) {
			this.log.debug(
					"The supplied type [{}] is annotated to be a singleton: "
							+ "returning the supplied instance [{}].", type
							.getName(), instance);

			return type.cast(instance);
		}
		this.log.debug(
				"The supplied type [{}] is not known to be a singleton: "
						+ "will create a new instance.", type.getName());

		return newInstance(type);
	}

	private <T> Bean<T> lookupBeanInBeanManager(final Class<T> type)
			throws IllegalArgumentException {
		final Set<Bean<?>> beans = this.beanManager.getBeans(type);
		if (beans.isEmpty()) {

			return null;
		}
		// TODO: What is the appropriate behavior if more than one matching
		// bean is found?
		if (beans.size() > 1) {
			this.log.warn(
					"Found more than one [{}] beans having type = [{}] in BeanManager: "
							+ "will use arbitrarily chosen bean.", Integer
							.valueOf(beans.size()), type.getName());
		}

		return (Bean<T>) beans.iterator().next();
	}

	private <T> T createBeanInstanceViaReflection(final Class<T> type) {
		try {
			this.log.trace(
					"Creating bean instance of type = [{}] via reflection ...",
					type.getName());
			final T beanInstance = type.newInstance();
			this.log.trace("New bean instance [{}] successfully created.",
					beanInstance, type.getName());

			return beanInstance;
		} catch (final Exception e) {

			throw new RuntimeException(
					"Failed to create new bean instance of type ["
							+ type.getName() + "] via reflection: "
							+ e.getMessage(), e);
		}
	}

	private <T> T createBeanInstanceViaBeanManager(final Class<T> type,
			final Bean<T> bean) {
		final T beanInstance = type.cast(this.beanManager.getReference(bean,
				type, this.beanManager.createCreationalContext(bean)));
		this.log.trace("Obtained bean instance [{}] from BeanManager.",
				beanInstance, type.getName());

		return beanInstance;
	}

	private <T> InjectionTarget<T> createInjectionTargetFor(final Class<T> type) {
		final AnnotatedType<T> annotatedType = this.beanManager
				.createAnnotatedType(type);
		final InjectionTarget<T> injectionTarget = this.beanManager
				.createInjectionTarget(annotatedType);

		return injectionTarget;
	}

	private <T> void injectDependenciesIntoBeanInstance(final T beanInstance,
			final InjectionTarget<T> injectionTarget) {
		injectionTarget.inject(beanInstance, this.beanManager
				.<T> createCreationalContext(null));
		this.log.trace("Injected dependencies into bean instance [{}].",
				beanInstance, beanInstance.getClass().getName());
	}

	private <T> void injectDependenciesIntoBeanInstance(final T beanInstance,
			final Bean<T> bean, final InjectionTarget<T> injectionTarget) {
		injectionTarget.inject(beanInstance, this.beanManager
				.createCreationalContext(bean));
		this.log.trace("Injected dependencies into bean instance [{}].",
				beanInstance, beanInstance.getClass().getName());
	}

	private <T> void invokePostConstrucAnnotatedMethodsOnBeanInstance(
			final T beanInstance, final InjectionTarget<T> injectionTarget) {
		injectionTarget.postConstruct(beanInstance);
		this.log
				.trace(
						"Invoked @PostConstruct annotated methods on bean instance [{}].",
						beanInstance, beanInstance.getClass().getName());
	}

	private boolean isSingleton(final Object instance) {
		return instance.getClass().isAnnotationPresent(Singleton.class)
				|| instance.getClass().isAnnotationPresent(
						ApplicationScoped.class)
				|| ((instance instanceof IsSingleton) && ((IsSingleton) instance)
						.isSingleton());
	}
}
