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

package net.camelpe.extension;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder;
import net.camelpe.extension.camel.typeconverter.TypeConverterHolder;

import org.apache.camel.CamelContext;
import org.apache.camel.Converter;
import org.apache.camel.FallbackConverter;
import org.jboss.seam.solder.bean.BeanBuilder;
import org.jboss.seam.solder.literal.AnyLiteral;
import org.jboss.seam.solder.literal.DefaultLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for TypeConverterDiscovery
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class TypeConverterDiscovery {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	@SuppressWarnings("serial")
	private static final Annotation CONVERTER_QUALIFIER = new AnnotationLiteral<Converter>() {
	};

	private static final Annotation FALLBACK_CONVERTER_QUALIFIER = new FallbackConverter() {
		@Override
		public boolean canPromote() {
			return false;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return FallbackConverter.class;
		}
	};

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private BeanManager beanManager;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	void registerIn(final CamelContext camelContext) {
		this.log.debug(
		        "About to register discovered TypeConverters in CamelContext [{}] ...",
		        camelContext);
		final CdiTypeConverterBuilder typeConverterBuilder = new CdiTypeConverterBuilder(
		        this.beanManager, camelContext.getTypeConverterRegistry());
		int registeredTypeConvertersCount = 0;
		for (final Class<?> typeConverterClass : annotatedTypeConverterClasses()) {
			final Set<TypeConverterHolder> typeConverterHolders = typeConverterBuilder
			        .buildTypeConvertersFrom(typeConverterClass);
			for (final TypeConverterHolder typeConverterHolder : typeConverterHolders) {
				typeConverterHolder.registerIn(camelContext);
				registeredTypeConvertersCount++;
			}
		}
		this.log.debug(
		        "Registered [{}] discovered TypeConverter(s) in CamelContext [{}]",
		        registeredTypeConvertersCount, camelContext);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private Set<Class<?>> annotatedTypeConverterClasses() {
		final Set<Class<?>> answer = new HashSet<Class<?>>();
		final Set<Bean<?>> converterBeans = this.beanManager.getBeans(
		        Object.class, CONVERTER_QUALIFIER);
		for (final Bean<?> converterBean : converterBeans) {
			answer.add(converterBean.getBeanClass());
		}
		this.log.trace("Discovered [{}] TypeConverter Bean(s) in BeanManager",
		        converterBeans.size());

		final Set<Bean<?>> fallbackConverterBeans = this.beanManager.getBeans(
		        Object.class, FALLBACK_CONVERTER_QUALIFIER);
		for (final Bean<?> fallbackConverterBean : fallbackConverterBeans) {
			answer.add(fallbackConverterBean.getBeanClass());
		}
		this.log.trace(
		        "Discovered [{}] FallbackTypeConverter Bean(s) in BeanManager",
		        fallbackConverterBeans.size());

		return Collections.unmodifiableSet(answer);
	}

	// -------------------------------------------------------------------------
	// This class wrapped in a CDI bean
	// -------------------------------------------------------------------------

	static Bean<TypeConverterDiscovery> cdiBean(final BeanManager beanManager) {
		final AnnotatedType<TypeConverterDiscovery> annotatedType = beanManager
		        .createAnnotatedType(TypeConverterDiscovery.class);
		final InjectionTarget<TypeConverterDiscovery> injectionTarget = beanManager
		        .createInjectionTarget(annotatedType);

		return new BeanBuilder<TypeConverterDiscovery>(beanManager)
		        .name("typeConverterDiscovery").readFromType(annotatedType)
		        .scope(ApplicationScoped.class)
		        .addQualifiers(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE)
		        .addTypes(TypeConverterDiscovery.class, Object.class)
		        .alternative(false).nullable(false)
		        .injectionPoints(injectionTarget.getInjectionPoints()).create();
	}
}
