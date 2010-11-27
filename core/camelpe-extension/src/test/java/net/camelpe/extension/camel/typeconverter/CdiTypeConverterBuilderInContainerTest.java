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

package net.camelpe.extension.camel.typeconverter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * TODO: Insert short summary for CdiTypeConverterBuilderInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class CdiTypeConverterBuilderInContainerTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@Inject
	private BeanManager beanManager;

	private final CamelContext camelContext = new DefaultCamelContext();

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap
		        .create(JavaArchive.class, "test.jar")
		        .addPackages(
		                false,
		                InstanceMethodTypeConverterHavingNoInjectionPoints.class
		                        .getPackage())
		        .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
		                ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsBasicInstanceMethodTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(InstanceMethodTypeConverterHavingNoInjectionPoints.class);

		assertNotNull(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverter.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverter.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final Object objectToConvert = new Object();
		final String convertedObject = builtTypeConverter.convertTo(
		        String.class, objectToConvert);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverter.class) did not properly build TypeConverter",
		        objectToConvert.toString(), convertedObject);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsInjectionAwareInstanceMethodTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(InstanceMethodTypeConverterHavingOneInjectionPoint.class);

		assertNotNull(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverterHavingOneInjectionPoint.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverterHavingOneInjectionPoint.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final String stringToConvert = "CONVERT_ME";
		final byte[] convertedString = builtTypeConverter.convertTo(
		        byte[].class, stringToConvert);
		assertArrayEquals(
		        "buildTypeConvertersFrom(InstanceMethodTypeConverterHavingOneInjectionPoint.class) did not properly build TypeConverter",
		        stringToConvert.getBytes(), convertedString);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsBasicInstanceMethodFallbackTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingNoInjectionPoints.class);

		assertNotNull(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingNoInjectionPoints.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingNoInjectionPoints.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final Object objectToConvert = new Object();
		final String convertedObject = builtTypeConverter.convertTo(
		        String.class, objectToConvert);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingNoInjectionPoints.class) did not properly build TypeConverter",
		        objectToConvert.toString(), convertedObject);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsInjectionAwareInstanceMethodFallbackTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingOneInjectionPoint.class);

		assertNotNull(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingOneInjectionPoint.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingOneInjectionPoint.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final String stringToConvert = "CONVERT_ME";
		final byte[] convertedString = builtTypeConverter.convertTo(
		        byte[].class, stringToConvert);
		assertArrayEquals(
		        "buildTypeConvertersFrom(InstanceMethodFallbackTypeConverterHavingOneInjectionPoint.class) did not properly build TypeConverter",
		        stringToConvert.getBytes(), convertedString);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsStaticMethodTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(StaticMethodTypeConverter.class);

		assertNotNull(
		        "buildTypeConvertersFrom(StaticMethodTypeConverter.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(StaticMethodTypeConverter.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final Object objectToConvert = new Object();
		final String convertedObject = builtTypeConverter.convertTo(
		        String.class, objectToConvert);
		assertEquals(
		        "buildTypeConvertersFrom(StaticMethodTypeConverter.class) did not properly build TypeConverter",
		        objectToConvert.toString(), convertedObject);
	}

	/**
	 * Test method for
	 * {@link net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder#buildTypeConvertersFrom(java.lang.Class)}
	 * .
	 */
	@Test
	public final void assertThatBuildTypeConvertersFromProperlyBuildsStaticMethodFallbackTypeConverter() {
		final CdiTypeConverterBuilder classUnderTest = new CdiTypeConverterBuilder(
		        this.beanManager, this.camelContext.getTypeConverterRegistry());

		final Set<TypeConverterHolder> builtTypeConverters = classUnderTest
		        .buildTypeConvertersFrom(StaticMethodFallbackTypeConverter.class);

		assertNotNull(
		        "buildTypeConvertersFrom(StaticMethodFallbackTypeConverter.class) returned <null>",
		        builtTypeConverters);
		assertEquals(
		        "buildTypeConvertersFrom(StaticMethodFallbackTypeConverter.class) returned wrong number of TypeConverters",
		        1, builtTypeConverters.size());
		final TypeConverter builtTypeConverter = builtTypeConverters.iterator()
		        .next().getTypeConverter();
		final Object objectToConvert = new Object();
		final String convertedObject = builtTypeConverter.convertTo(
		        String.class, objectToConvert);
		assertEquals(
		        "buildTypeConvertersFrom(StaticMethodFallbackTypeConverter.class) did not properly build TypeConverter",
		        objectToConvert.toString(), convertedObject);
	}
}
