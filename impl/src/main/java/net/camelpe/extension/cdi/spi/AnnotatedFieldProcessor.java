/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.camelpe.extension.cdi.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.ResolutionException;
import javax.inject.Inject;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;

/**
 * <p>
 * TODO: Insert short summary for AnnotatedFieldProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
abstract class AnnotatedFieldProcessor {

	static final Set<Class<? extends Annotation>> CAMEL;

	static final Set<Class<? extends Annotation>> CDI_CONFLICTS;

	static {
		final Set<Class<? extends Annotation>> camelTmp = new HashSet<Class<? extends Annotation>>(
		        2);
		camelTmp.add(EndpointInject.class);
		camelTmp.add(Produce.class);
		CAMEL = Collections.unmodifiableSet(camelTmp);

		final Set<Class<? extends Annotation>> cdiConflictsTmp = new HashSet<Class<? extends Annotation>>(
		        2);
		cdiConflictsTmp.add(Inject.class);
		cdiConflictsTmp.add(Produces.class);
		CDI_CONFLICTS = Collections.unmodifiableSet(cdiConflictsTmp);
	}

	/**
	 * @param <X>
	 * @param type
	 * @throws ResolutionException
	 */
	static <X> void ensureNoConflictingAnnotationsPresentOn(final Class<X> type)
	        throws ResolutionException {
		final Set<Field> fieldsHavingConflictingAnnotations = new HashSet<Field>();
		for (final Field fieldToInspect : allFieldsAndSuperclassFieldsIn(type)) {
			if (isAnnotatedWithOneOf(fieldToInspect, CAMEL)
			        && isAnnotatedWithOneOf(fieldToInspect, CDI_CONFLICTS)) {
				fieldsHavingConflictingAnnotations.add(fieldToInspect);
			}
		}
		if (!fieldsHavingConflictingAnnotations.isEmpty()) {
			final String error = buildErrorMessageFrom(fieldsHavingConflictingAnnotations);

			throw new ResolutionException(error);
		}
	}

	private static boolean isAnnotatedWithOneOf(final Field fieldToInspect,
	        final Set<Class<? extends Annotation>> annotations) {
		for (final Class<? extends Annotation> annotationType : annotations) {
			if (fieldToInspect.isAnnotationPresent(annotationType)) {
				return true;
			}
		}

		return false;
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
		final Set<Field> camelInjectAnnotatedFields = new HashSet<Field>();
		for (final Field fieldToInspect : allFieldsAndSuperclassFieldsIn(type)) {
			if (isAnnotatedWithOneOf(fieldToInspect, CAMEL)) {
				camelInjectAnnotatedFields.add(fieldToInspect);
			}
		}

		return Collections.unmodifiableSet(camelInjectAnnotatedFields);
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

		error.append(" This, however, is illegal. No more than one of these two annotations must be present on any given field.");

		return error.toString();
	}
}
