/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany.
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
import javax.enterprise.context.ContextException;
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

import net.camelpe.extension.camel.spi.CdiInjector;
import net.camelpe.extension.camel.spi.CdiRegistry;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.Registry;

/**
 * <p>
 * TODO: Insert short summary for CdiCamelContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class CdiCamelContext extends DefaultCamelContext {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    @Inject
    private BeanManager beanManager;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    CdiCamelContext() {
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * @see org.apache.camel.impl.DefaultCamelContext#createInjector()
     */
    @Override
    protected Injector createInjector() {
        return new CdiInjector(this.beanManager);
    }

    /**
     * @see org.apache.camel.impl.DefaultCamelContext#createRegistry()
     */
    @Override
    protected Registry createRegistry() {
        return new CdiRegistry(this.beanManager);
    }

    /**
     * @see org.apache.camel.impl.DefaultCamelContext#createTypeConverter()
     */
    @Override
    protected TypeConverter createTypeConverter() {
        final DefaultTypeConverter answer = new DefaultTypeConverter(
                getPackageScanClassResolver(), getInjector(),
                getDefaultFactoryFinder());
        setTypeConverterRegistry(answer);
        return answer;
    }

    // -------------------------------------------------------------------------
    // equals(), hashCode(), toString()
    // -------------------------------------------------------------------------

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CDI CamelContext [name = " + getName() + " | beanManager = "
                + this.beanManager.getClass().getName() + "]";
    }

    // -------------------------------------------------------------------------
    // This class wrapped in a CDI bean
    // -------------------------------------------------------------------------

    static class CdiBean implements Bean<CdiCamelContext> {

        private final InjectionTarget<CdiCamelContext> injectionTarget;

        /**
         * @param beanManager
         */
        CdiBean(final BeanManager beanManager) {
            final AnnotatedType<CdiCamelContext> annotatedType = beanManager
                    .createAnnotatedType(CdiCamelContext.class);
            this.injectionTarget = beanManager
                    .createInjectionTarget(annotatedType);
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getBeanClass()
         */
        @Override
        public Class<?> getBeanClass() {
            return CdiCamelContext.class;
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
            return "cdiCamelContext";
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
            types.add(CdiCamelContext.class);
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
        public CdiCamelContext create(
                final CreationalContext<CdiCamelContext> creationalContext) {
            final CdiCamelContext instance = this.injectionTarget
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
        public void destroy(final CdiCamelContext instance,
                final CreationalContext<CdiCamelContext> creationalContext) {
            try {
                this.injectionTarget.preDestroy(instance);
                instance.stop();
                this.injectionTarget.dispose(instance);
                creationalContext.release();
            } catch (final Exception e) {
                throw new ContextException("Failed to destroy CamelContext ["
                        + instance + "]: " + e.getMessage(), e);
            }
        }
    }
}
