/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.cdi.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.ResolutionException;
import javax.inject.Inject;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * <p>
 * TODO: Insert short summary for AnnotatedFieldProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
abstract class AnnotatedFieldProcessor {

	static final Set<Class<? extends Annotation>> CAMEL = ImmutableSet.of(
			EndpointInject.class, Produce.class);

	static final Set<Class<? extends Annotation>> CDI_CONFLICTS = ImmutableSet
			.of(Inject.class, Produces.class);

	private static final Predicate<Field> IS_CAMEL_INJECT_ANNOTATED = new IsCamelInjectAnnotated();

	private static final Predicate<Field> HAS_CONFLICTING_CDI_ANNOTATIONS = Predicates
			.and(IS_CAMEL_INJECT_ANNOTATED,
					new HasPotentiallyConflictingCdiAnnotations());

	/**
	 * @param <X>
	 * @param type
	 * @throws ResolutionException
	 */
	static <X> void ensureNoConflictingAnnotationsPresentOn(final Class<X> type)
			throws ResolutionException {
		final Set<Field> fieldsHavingConflictingAnnotations = Sets.filter(
				ImmutableSet.copyOf(allFieldsAndSuperclassFieldsIn(type)),
				HAS_CONFLICTING_CDI_ANNOTATIONS);
		if (!fieldsHavingConflictingAnnotations.isEmpty()) {
			final String error = buildErrorMessageFrom(fieldsHavingConflictingAnnotations);

			throw new ResolutionException(error);
		}
	}

	/**
	 * @param <X>
	 * @param type
	 * @return
	 */
	static <X> boolean hasCamelInjectAnnotatedFields(final Class<X> type) {
		return !camelInjectAnnotatedFieldsIn(type).isEmpty();
	}

	/**
	 * @param <X>
	 * @param type
	 * @return
	 */
	static <X> Set<Field> camelInjectAnnotatedFieldsIn(final Class<X> type) {
		return Sets.filter(ImmutableSet
				.copyOf(allFieldsAndSuperclassFieldsIn(type)),
				IS_CAMEL_INJECT_ANNOTATED);
	}

	private static <X> Field[] allFieldsAndSuperclassFieldsIn(
			final Class<X> type) throws SecurityException {
		final List<Field> answer = new ArrayList<Field>();
		Class<? super X> currentType = type;
		while ((currentType != null) && (currentType != Object.class)) {
			answer.addAll(Arrays.asList(currentType.getDeclaredFields()));
			currentType = currentType.getSuperclass();
		}

		return answer.toArray(new Field[answer.size()]);
	}

	private static String buildErrorMessageFrom(
			final Set<Field> fieldsHavingConflictingAnnotations) {
		final StringBuilder error = new StringBuilder("The field(s) [");
		for (final Field fieldHavingConflictingAnnotations : fieldsHavingConflictingAnnotations) {
			error.append(fieldHavingConflictingAnnotations).append(", ");
		}
		error.delete(error.lastIndexOf(","), error.lastIndexOf(",") + 2)
				.append("]");

		error.append(" is/are annotated with one of [");
		for (final Class<? extends Annotation> camelAnnotation : CAMEL) {
			error.append(camelAnnotation.getName()).append(", ");
		}
		error.delete(error.lastIndexOf(","), error.lastIndexOf(",") + 2)
				.append("]");

		error.append(" as well as with one of [");
		for (final Class<? extends Annotation> cdiAnnotation : CDI_CONFLICTS) {
			error.append(cdiAnnotation.getName()).append(", ");
		}
		error.delete(error.lastIndexOf(","), error.lastIndexOf(",") + 2)
				.append("].");

		error
				.append(" This, however, is illegal. No more than one of these two annotations must be present on any given field.");

		return error.toString();
	}

	/**
	 * <p>
	 * A {@link Predicate <code>Predicate</code>} that tests for the presence of
	 * one of the annotations
	 * <ul>
	 * <li>
	 * {@link EndpointInject <code>EndpointInject</code>},</li>
	 * <li>
	 * {@link Produce <code>Produce</code>}</li>
	 * </ul>
	 * on a field.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	private static class IsCamelInjectAnnotated implements Predicate<Field> {

		IsCamelInjectAnnotated() {
		}

		/**
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final Field input) {
			for (final Class<? extends Annotation> anno : CAMEL) {
				if (input.isAnnotationPresent(anno)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * <p>
	 * A {@link Predicate <code>Predicate</code>} that tests for the presence of
	 * one of the <tt>CDI</tt> annotations
	 * <ul>
	 * <li>
	 * {@link Inject <code>Inject</code>},</li>
	 * <li>
	 * {@link Produces <code>Produces</code>}</li>
	 * </ul>
	 * on a field.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	private static class HasPotentiallyConflictingCdiAnnotations implements
			Predicate<Field> {

		HasPotentiallyConflictingCdiAnnotations() {
		}

		/**
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final Field input) {
			for (final Class<? extends Annotation> anno : CDI_CONFLICTS) {
				if (input.isAnnotationPresent(anno)) {
					return true;
				}
			}
			return false;
		}
	}
}
