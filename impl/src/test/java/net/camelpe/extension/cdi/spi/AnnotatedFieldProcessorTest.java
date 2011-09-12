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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.inject.ResolutionException;

import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAndInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingNoEndpointInjectAnnotatedField;

import org.junit.Test;

/**
 * <p>
 * Test {@link AnnotatedFieldProcessor}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AnnotatedFieldProcessorTest {

	/**
	 * Test method for
	 * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#ensureNoConflictingAnnotationsPresentOn(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatEnsureNoConflictingAnnotationsPresentOnCorrectlyRecognizesNoConflict() {
		AnnotatedFieldProcessor
		        .ensureNoConflictingAnnotationsPresentOn(BeanHavingEndpointInjectAnnotatedField.class);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#ensureNoConflictingAnnotationsPresentOn(java.lang.Class)}
	 * .
	 */
	@Test(expected = ResolutionException.class)
	public final void assertThatEnsureNoConflictingAnnotationsPresentOnCorrectlyRecognizesAConflict() {
		AnnotatedFieldProcessor
		        .ensureNoConflictingAnnotationsPresentOn(BeanHavingEndpointInjectAndInjectAnnotatedField.class);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#hasCamelInjectAnnotatedFields(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatHasCamelInjectAnnotatedFieldsRecognizesThatNoCamelInjectAnnotationIsPresentOnAnyField() {
		final boolean answer = AnnotatedFieldProcessor
		        .hasCamelInjectAnnotatedFields(BeanHavingNoEndpointInjectAnnotatedField.class);

		assertFalse("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
		        + BeanHavingNoEndpointInjectAnnotatedField.class.getName()
		        + ") should have recognized that no field is annotated with "
		        + "@EndpointInject on the supplied class, yet it didn't",
		        answer);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#hasCamelInjectAnnotatedFields(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatHasCamelInjectAnnotatedFieldsRecognizesEndpointInjectAnnotationOnField() {
		final boolean answer = AnnotatedFieldProcessor
		        .hasCamelInjectAnnotatedFields(BeanHavingEndpointInjectAnnotatedField.class);

		assertTrue("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
		        + BeanHavingEndpointInjectAnnotatedField.class.getName()
		        + ") should have recognized a field annotated with "
		        + "@EndpointInject on the supplied class, yet it didn't",
		        answer);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#camelInjectAnnotatedFieldsIn(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatCamelInjectAnnotatedFieldsInReturnsEnpointInjectAnnotatedField() {
		final Set<Field> camelInjectAnnotatedFields = AnnotatedFieldProcessor
		        .camelInjectAnnotatedFieldsIn(BeanHavingEndpointInjectAnnotatedField.class);

		assertEquals("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
		        + BeanHavingEndpointInjectAnnotatedField.class.getName()
		        + ") should have returned exactly one field annotated with "
		        + "@EndpointInject on the supplied class, yet it didn't", 1,
		        camelInjectAnnotatedFields.size());
	}

}
