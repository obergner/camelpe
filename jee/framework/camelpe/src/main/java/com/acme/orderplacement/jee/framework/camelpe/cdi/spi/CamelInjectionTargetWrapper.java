/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.cdi.spi;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.ResolutionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.impl.CamelPostProcessorHelper;
import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for CamelInjectionTargetWrapper
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CamelInjectionTargetWrapper<T> implements InjectionTarget<T> {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	/**
	 * @param <X>
	 * @param annotatedType
	 * @param originalInjectionTarget
	 * @param camelContext
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ResolutionException
	 */
	public static <X> InjectionTarget<X> injectionTargetFor(
			final AnnotatedType<X> annotatedType,
			final InjectionTarget<X> originalInjectionTarget,
			final CamelContext camelContext) throws IllegalArgumentException,
			ResolutionException {
		Validate.notNull(annotatedType, "annotatedType");
		Validate.notNull(originalInjectionTarget, "originalInjectionTarget");
		Validate.notNull(camelContext, "camelContext");

		return needsInjectionTargetWrapper(annotatedType.getJavaClass()) ? new CamelInjectionTargetWrapper<X>(
				originalInjectionTarget, camelContext)
				: originalInjectionTarget;
	}

	private static <X> boolean needsInjectionTargetWrapper(final Class<X> type)
			throws ResolutionException {
		return !endpointInjectAnnotatedFieldsOf(type).isEmpty();
	}

	private static <X> Set<Field> endpointInjectAnnotatedFieldsOf(
			final Class<X> type) throws ResolutionException {
		final Field[] allFields = type.getFields();
		final Set<Field> answer = new HashSet<Field>(allFields.length);
		for (final Field aField : allFields) {
			if (aField
					.isAnnotationPresent(org.apache.camel.EndpointInject.class)) {
				ensureNoConflictingAnnotationsPresentOn(aField);

				answer.add(aField);
			}
		}

		return answer;
	}

	private static void ensureNoConflictingAnnotationsPresentOn(
			final Field endpointInjectAnnotatedField)
			throws ResolutionException {
		if (endpointInjectAnnotatedField
				.isAnnotationPresent(javax.inject.Inject.class)) {

			throw new ResolutionException(
					"The field ["
							+ endpointInjectAnnotatedField
							+ "] is annotated with ["
							+ org.apache.camel.EndpointInject.class.getName()
							+ "] as well as with ["
							+ javax.inject.Inject.class.getName()
							+ "]. This, however, is illegal. No more than one of these two annotations must be present on any given field.");
		}
		if (endpointInjectAnnotatedField
				.isAnnotationPresent(javax.enterprise.inject.Produces.class)) {

			throw new ResolutionException(
					"The field ["
							+ endpointInjectAnnotatedField
							+ "] is annotated with ["
							+ org.apache.camel.EndpointInject.class.getName()
							+ "] as well as with ["
							+ javax.enterprise.inject.Produces.class.getName()
							+ "]. This, however, is illegal. No more than one of these two annotations must be present on any given field.");
		}
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final InjectionTarget<T> wrapped;

	private final CamelPostProcessorHelper camelPostProcessorHelper;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param wrapped
	 * @param camelContext
	 */
	private CamelInjectionTargetWrapper(final InjectionTarget<T> wrapped,
			final CamelContext camelContext) throws IllegalArgumentException {
		Validate.notNull(wrapped, "wrapped");
		Validate.notNull(camelContext, "camelContext");
		this.wrapped = wrapped;
		this.camelPostProcessorHelper = new CamelPostProcessorHelper(
				camelContext);
	}

	// -------------------------------------------------------------------------
	// javax.enterprise.inject.spi.InjectionTarget
	// -------------------------------------------------------------------------

	/**
	 * @see javax.enterprise.inject.spi.InjectionTarget#inject(java.lang.Object,
	 *      javax.enterprise.context.spi.CreationalContext)
	 */
	@Override
	public void inject(final T instance, final CreationalContext<T> ctx) {
		this.wrapped.inject(instance, ctx);
		injectEndpointsInto(instance);
	}

	/**
	 * @see javax.enterprise.inject.spi.InjectionTarget#postConstruct(java.lang.Object)
	 */
	@Override
	public void postConstruct(final T instance) {
		this.wrapped.postConstruct(instance);
	}

	/**
	 * @see javax.enterprise.inject.spi.InjectionTarget#preDestroy(java.lang.Object)
	 */
	@Override
	public void preDestroy(final T instance) {
		this.wrapped.preDestroy(instance);
	}

	/**
	 * @see javax.enterprise.inject.spi.Producer#dispose(java.lang.Object)
	 */
	@Override
	public void dispose(final T instance) {
		this.wrapped.dispose(instance);
	}

	/**
	 * @see javax.enterprise.inject.spi.Producer#getInjectionPoints()
	 */
	@Override
	public Set<InjectionPoint> getInjectionPoints() {
		return this.wrapped.getInjectionPoints();
	}

	/**
	 * @see javax.enterprise.inject.spi.Producer#produce(javax.enterprise.context.spi.CreationalContext)
	 */
	@Override
	public T produce(final CreationalContext<T> ctx) {
		return this.wrapped.produce(ctx);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private void injectEndpointsInto(final T instance)
			throws ResolutionException {
		final Set<Field> endpointInjectAnnotatedFields = endpointInjectAnnotatedFieldsOf(instance
				.getClass());
		for (final Field endpointInjectAnnotatedField : endpointInjectAnnotatedFields) {
			injectEndpointInto(instance, endpointInjectAnnotatedField);
		}
	}

	private void injectEndpointInto(final T instance,
			final Field endpointInjectAnnotatedField)
			throws ResolutionException {
		final EndpointInject endpointInjectAnnotation = endpointInjectAnnotatedField
				.getAnnotation(org.apache.camel.EndpointInject.class);
		final Object valueToInject = this.camelPostProcessorHelper
				.getInjectionValue(endpointInjectAnnotatedField.getType(),
						endpointInjectAnnotation.uri(),
						endpointInjectAnnotation.ref(),
						endpointInjectAnnotatedField.getName());
		setField(instance, endpointInjectAnnotatedField, valueToInject);
	}

	private void setField(final T instance, final Field fieldToSet,
			final Object valueToInject) throws ResolutionException {
		try {
			fieldToSet.set(instance, valueToInject);
		} catch (final Exception e) {
			throw new ResolutionException("Failed to inject [" + valueToInject
					+ "] into field [" + fieldToSet + "] on instance ["
					+ instance + "]: " + e.getMessage(), e);
		}
	}
}
