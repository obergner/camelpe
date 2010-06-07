/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;

import org.apache.camel.CamelContext;
import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for CamelContextBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CamelContextBean implements Bean<CamelContext> {

	private final CamelContext instance;

	CamelContextBean(final CamelContext instance)
			throws IllegalArgumentException {
		Validate.notNull(instance, "instance");
		this.instance = instance;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getBeanClass()
	 */
	@Override
	public Class<?> getBeanClass() {
		return CamelContext.class;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getInjectionPoints()
	 */
	@Override
	public Set<InjectionPoint> getInjectionPoints() {
		return Collections.emptySet();
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getName()
	 */
	@Override
	public String getName() {
		return "camelContext";
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getQualifiers()
	 */
	@Override
	public Set<Annotation> getQualifiers() {
		final Set<Annotation> qualifiers = new HashSet<Annotation>();
		qualifiers.add(new AnnotationLiteral<Default>() {
		});
		qualifiers.add(new AnnotationLiteral<Any>() {
		});

		return qualifiers;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getScope()
	 */
	@Override
	public Class<? extends Annotation> getScope() {
		return ApplicationScoped.class;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getStereotypes()
	 */
	@Override
	public Set<Class<? extends Annotation>> getStereotypes() {
		return Collections.emptySet();
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#getTypes()
	 */
	@Override
	public Set<Type> getTypes() {
		final Set<Type> types = new HashSet<Type>();
		types.add(CamelContext.class);
		types.add(Object.class);

		return types;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#isAlternative()
	 */
	@Override
	public boolean isAlternative() {
		return false;
	}

	/**
	 * @see javax.enterprise.inject.spi.Bean#isNullable()
	 */
	@Override
	public boolean isNullable() {
		return false;
	}

	/**
	 * @see javax.enterprise.context.spi.Contextual#create(javax.enterprise.context.spi.CreationalContext)
	 */
	@Override
	public CamelContext create(
			final CreationalContext<CamelContext> creationalContext) {
		return this.instance;
	}

	/**
	 * @see javax.enterprise.context.spi.Contextual#destroy(java.lang.Object,
	 *      javax.enterprise.context.spi.CreationalContext)
	 */
	@Override
	public void destroy(final CamelContext instance,
			final CreationalContext<CamelContext> creationalContext) {
		// Noop
	}

}
