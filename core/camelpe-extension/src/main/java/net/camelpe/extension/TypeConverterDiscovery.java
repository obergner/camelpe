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
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import net.camelpe.extension.camel.typeconverter.CdiTypeConverterBuilder;
import net.camelpe.extension.camel.typeconverter.TypeConverterHolder;

import org.apache.camel.CamelContext;
import org.apache.camel.Converter;
import org.apache.camel.FallbackConverter;
import org.apache.commons.lang.Validate;
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

    static class CdiBean implements Bean<TypeConverterDiscovery> {

        private final InjectionTarget<TypeConverterDiscovery> injectionTarget;

        CdiBean(final BeanManager beanManager) throws IllegalArgumentException {
            Validate.notNull(beanManager, "beanManager");
            final AnnotatedType<TypeConverterDiscovery> at = beanManager
                    .createAnnotatedType(TypeConverterDiscovery.class);
            this.injectionTarget = beanManager.createInjectionTarget(at);
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getBeanClass()
         */
        @Override
        public Class<?> getBeanClass() {
            return TypeConverterDiscovery.class;
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getInjectionPoints()
         */
        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return this.injectionTarget.getInjectionPoints();
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getName()
         */
        @Override
        public String getName() {
            return "typeConverterDiscovery";
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getQualifiers()
         */
        @SuppressWarnings("serial")
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
            types.add(TypeConverterDiscovery.class);
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
        public TypeConverterDiscovery create(
                final CreationalContext<TypeConverterDiscovery> creationalContext) {
            final TypeConverterDiscovery instance = this.injectionTarget
                    .produce(creationalContext);
            this.injectionTarget.inject(instance, creationalContext);
            this.injectionTarget.postConstruct(instance);

            return instance;
        }

        /**
         * @see javax.enterprise.context.spi.Contextual#destroy(java.lang.Object,
         *      javax.enterprise.context.spi.CreationalContext)
         */
        @Override
        public void destroy(
                final TypeConverterDiscovery instance,
                final CreationalContext<TypeConverterDiscovery> creationalContext) {
            this.injectionTarget.preDestroy(instance);
            this.injectionTarget.dispose(instance);
            creationalContext.release();
        }
    }
}
