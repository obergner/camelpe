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

package net.camelpe.extension.cdi.spi;

import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.ResolutionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import net.camelpe.extension.util.BeanReference;

import org.apache.camel.CamelContext;
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
     * @param pit
     * @param beanManager
     * @throws IllegalArgumentException
     * @throws ResolutionException
     */
    public static <X> void wrap(final ProcessInjectionTarget<X> pit,
            final BeanManager beanManager) throws IllegalArgumentException,
            ResolutionException {
        Validate.notNull(pit, "pit");
        Validate.notNull(beanManager, "beanManager");
        AnnotatedFieldProcessor.ensureNoConflictingAnnotationsPresentOn(pit
                .getAnnotatedType().getJavaClass());

        final InjectionTarget<X> wrapper = injectionTargetFor(
                pit.getAnnotatedType(), pit.getInjectionTarget(), beanManager);
        pit.setInjectionTarget(wrapper);
    }

    /**
     * For testing purposes only.
     */
    static <X> InjectionTarget<X> injectionTargetFor(
            final AnnotatedType<X> annotatedType,
            final InjectionTarget<X> originalInjectionTarget,
            final BeanManager beanManager) throws IllegalArgumentException,
            ResolutionException {
        Validate.notNull(annotatedType, "annotatedType");
        Validate.notNull(originalInjectionTarget, "originalInjectionTarget");
        Validate.notNull(beanManager, "beanManager");
        AnnotatedFieldProcessor
                .ensureNoConflictingAnnotationsPresentOn(annotatedType
                        .getJavaClass());

        return AnnotatedFieldProcessor
                .hasCamelInjectAnnotatedFields(annotatedType.getJavaClass()) ? new CamelInjectionTargetWrapper<X>(
                originalInjectionTarget, beanManager) : originalInjectionTarget;
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final InjectionTarget<T> wrapped;

    private final BeanReference<CamelContext> camelContextRef;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @param wrapped
     * @param camelContext
     */
    private CamelInjectionTargetWrapper(final InjectionTarget<T> wrapped,
            final BeanManager beanManager) throws IllegalArgumentException {
        Validate.notNull(wrapped, "wrapped");
        Validate.notNull(beanManager, "beanManager");
        this.wrapped = wrapped;
        this.camelContextRef = new BeanReference<CamelContext>(
                CamelContext.class, beanManager);
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
        getWrapped().inject(instance, ctx);
        injectEndpointsInto(instance);
    }

    /**
     * @see javax.enterprise.inject.spi.InjectionTarget#postConstruct(java.lang.Object)
     */
    @Override
    public void postConstruct(final T instance) {
        getWrapped().postConstruct(instance);
    }

    /**
     * @see javax.enterprise.inject.spi.InjectionTarget#preDestroy(java.lang.Object)
     */
    @Override
    public void preDestroy(final T instance) {
        getWrapped().preDestroy(instance);
    }

    /**
     * @see javax.enterprise.inject.spi.Producer#dispose(java.lang.Object)
     */
    @Override
    public void dispose(final T instance) {
        getWrapped().dispose(instance);
    }

    /**
     * @see javax.enterprise.inject.spi.Producer#getInjectionPoints()
     */
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return getWrapped().getInjectionPoints();
    }

    /**
     * @see javax.enterprise.inject.spi.Producer#produce(javax.enterprise.context.spi.CreationalContext)
     */
    @Override
    public T produce(final CreationalContext<T> ctx) {
        return getWrapped().produce(ctx);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private void injectEndpointsInto(final T instance)
            throws ResolutionException {
        final Set<Field> camelInjectAnnotatedFields = AnnotatedFieldProcessor
                .camelInjectAnnotatedFieldsIn(instance.getClass());
        for (final Field camelInjectAnnotatedField : camelInjectAnnotatedFields) {
            injectEndpointInto(instance, camelInjectAnnotatedField);
        }
    }

    private void injectEndpointInto(final T instance,
            final Field camelInjectAnnotatedField) throws ResolutionException {
        final Object valueToInject;
        if (camelInjectAnnotatedField
                .isAnnotationPresent(org.apache.camel.EndpointInject.class)) {
            final org.apache.camel.EndpointInject endpointInjectAnnotation = camelInjectAnnotatedField
                    .getAnnotation(org.apache.camel.EndpointInject.class);
            valueToInject = getCamelPostProcessorHelper().getInjectionValue(
                    camelInjectAnnotatedField.getType(),
                    endpointInjectAnnotation.uri(),
                    endpointInjectAnnotation.ref(),
                    camelInjectAnnotatedField.getName(), instance,
                    instance.getClass().getName());
        } else if (camelInjectAnnotatedField
                .isAnnotationPresent(org.apache.camel.Produce.class)) {
            final org.apache.camel.Produce endpointInjectAnnotation = camelInjectAnnotatedField
                    .getAnnotation(org.apache.camel.Produce.class);
            valueToInject = getCamelPostProcessorHelper().getInjectionValue(
                    camelInjectAnnotatedField.getType(),
                    endpointInjectAnnotation.uri(),
                    endpointInjectAnnotation.ref(),
                    camelInjectAnnotatedField.getName(), instance,
                    instance.getClass().getName());
        } else {
            throw new IllegalStateException("Neither ["
                    + org.apache.camel.EndpointInject.class.getName()
                    + "] nor [" + org.apache.camel.Produce.class
                    + "] are present on field ["
                    + camelInjectAnnotatedField.toString() + "]");
        }
        setField(instance, camelInjectAnnotatedField, valueToInject);
    }

    private void setField(final T instance, final Field fieldToSet,
            final Object valueToInject) throws ResolutionException {
        try {
            fieldToSet.setAccessible(true);
            fieldToSet.set(instance, valueToInject);
        } catch (final Exception e) {
            throw new ResolutionException("Failed to inject [" + valueToInject
                    + "] into field [" + fieldToSet + "] on instance ["
                    + instance + "]: " + e.getMessage(), e);
        }
    }

    /**
     * @return the wrapped
     */
    private InjectionTarget<T> getWrapped() {
        return this.wrapped;
    }

    /**
     * @return the camelPostProcessorHelper
     */
    private CamelPostProcessorHelper getCamelPostProcessorHelper() {
        return new CamelPostProcessorHelper(this.camelContextRef.get());
    }
}
