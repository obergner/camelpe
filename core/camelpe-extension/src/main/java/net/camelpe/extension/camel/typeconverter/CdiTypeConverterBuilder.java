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

package net.camelpe.extension.camel.typeconverter;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import net.camelpe.extension.camel.spi.CdiInjector;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.converter.StaticMethodFallbackTypeConverter;
import org.apache.camel.impl.converter.StaticMethodTypeConverter;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiTypeConverterBuilder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiTypeConverterBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BeanManager beanManager;

    private final TypeConverterRegistry typeConverterRegistry;

    public CdiTypeConverterBuilder(final BeanManager beanManager,
            final TypeConverterRegistry typeConverterRegistry)
            throws IllegalArgumentException {
        Validate.notNull(beanManager, "beanManager");
        Validate.notNull(typeConverterRegistry, "typeConverterRegistry");
        this.beanManager = beanManager;
        this.typeConverterRegistry = typeConverterRegistry;
    }

    public Set<TypeConverterHolder> buildTypeConvertersFrom(final Class<?> type) {
        this.log.debug("Building TypeConverters from class [{}] ...",
                type.getName());
        final Set<TypeConverterHolder> answer = new HashSet<TypeConverterHolder>();
        buildTypeConvertersFromClassHierarchy(type, answer);
        this.log.debug("Built [{}] TypeConverter(s) from class [{}]",
                answer.size(), type.getName());

        return Collections.unmodifiableSet(answer);
    }

    private void buildTypeConvertersFromClassHierarchy(final Class<?> type,
            final Set<TypeConverterHolder> alreadyBuiltTypeConverters) {
        try {
            final Method[] methods = type.getDeclaredMethods();
            for (final Method method : methods) {
                // this may be prone to ClassLoader or packaging problems when
                // the same class is defined
                // in two different jars (as is the case sometimes with specs).
                if (ObjectHelper.hasAnnotation(method, Converter.class, true)) {
                    alreadyBuiltTypeConverters
                            .add(buildTypeConverterFromConverterAnnotatedClass(
                                    type, method));
                } else if (ObjectHelper.hasAnnotation(method,
                        FallbackConverter.class, true)) {
                    alreadyBuiltTypeConverters
                            .add(buildFallbackTypeConverterFromFallbackConverterAnnotatedClass(
                                    type, method));
                }
            }

            final Class<?> superclass = type.getSuperclass();
            if ((superclass != null) && !superclass.equals(Object.class)) {
                buildTypeConvertersFromClassHierarchy(superclass,
                        alreadyBuiltTypeConverters);
            }
        } catch (final NoClassDefFoundError e) {
            throw new RuntimeException("Failed to load superclass of ["
                    + type.getName() + "]: " + e.getMessage(), e);
        }
    }

    private TypeConverterHolder buildTypeConverterFromConverterAnnotatedClass(
            final Class<?> type, final Method method)
            throws IllegalArgumentException {
        ensureConverterMethodIsValid(type, method);
        this.log.trace("Building TypeConverter from method [{}] ...", method);
        final TypeConverter typeConverter;
        final Class<?> fromType = method.getParameterTypes()[0];
        final Class<?> toType = method.getReturnType();
        if (isStatic(method.getModifiers())) {
            typeConverter = new StaticMethodTypeConverter(method);
        } else {
            typeConverter = new CdiInstanceMethodTypeConverter(new CdiInjector(
                    this.beanManager), method, this.typeConverterRegistry);
        }
        this.log.trace("Built TypeConverter [{}]", typeConverter, method);

        return TypeConverterHolder.newNonFallbackTypeConverterHolder(fromType,
                toType, typeConverter);
    }

    private void ensureConverterMethodIsValid(final Class<?> type,
            final Method method) throws IllegalArgumentException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final boolean hasCorrectParameters = (parameterTypes != null)
                && ((parameterTypes.length == 1) || ((parameterTypes.length == 2) && Exchange.class
                        .isAssignableFrom(parameterTypes[1])));
        if (!hasCorrectParameters) {
            throw new IllegalArgumentException(
                    "Illegal converter method ["
                            + method
                            + "] on type ["
                            + type.getName()
                            + "]: a converter method must have exactly one parameter, or it must have exactly "
                            + "two parameters where the second parameter is of type ["
                            + Exchange.class.getName() + "].");
        }

        final int modifiers = method.getModifiers();
        if (isAbstract(modifiers) || !isPublic(modifiers)) {
            throw new IllegalArgumentException(
                    "Illegal converter method ["
                            + method
                            + "] on type ["
                            + type.getName()
                            + "]: a converter method must not be abstract, and it must be public.");
        }

        final Class<?> toType = method.getReturnType();
        if (toType.equals(Void.class)) {
            throw new IllegalArgumentException("Illegal converter method ["
                    + method + "] on type [" + type.getName()
                    + "]: a converter method must not return void.");
        }
    }

    private TypeConverterHolder buildFallbackTypeConverterFromFallbackConverterAnnotatedClass(
            final Class<?> type, final Method method)
            throws IllegalArgumentException {
        ensureFallbackConverterMethodIsValid(type, method);
        this.log.trace("Building Fallback TypeConverter from method [{}] ...",
                method);
        final TypeConverter typeConverter;
        final boolean canPromote = method
                .isAnnotationPresent(FallbackConverter.class) ? method
                .getAnnotation(FallbackConverter.class).canPromote() : false;
        if (isStatic(method.getModifiers())) {
            typeConverter = new StaticMethodFallbackTypeConverter(method,
                    this.typeConverterRegistry);
        } else {
            typeConverter = new CdiInstanceMethodFallbackTypeConverter(
                    new CdiInjector(this.beanManager), method,
                    this.typeConverterRegistry);
        }
        this.log.trace("Built Fallback TypeConverter [{}]", typeConverter,
                method);

        return TypeConverterHolder.newFallbackTypeConverterHolder(
                typeConverter, canPromote);
    }

    private void ensureFallbackConverterMethodIsValid(final Class<?> type,
            final Method method) throws IllegalArgumentException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final boolean hasCorrectParameters = (parameterTypes != null)
                && ((parameterTypes.length == 3) || (((parameterTypes.length == 4) && Exchange.class
                        .isAssignableFrom(parameterTypes[1])) && (TypeConverterRegistry.class
                        .isAssignableFrom(parameterTypes[parameterTypes.length - 1]))));
        if (!hasCorrectParameters) {
            throw new IllegalArgumentException(
                    "Illegal fallback converter method ["
                            + method
                            + "] on type ["
                            + type.getName()
                            + "]: a fallback converter method must have exactly three parameters, or it must have "
                            + "exactly four parameters. In both cases, the second parameter must be of type ["
                            + Exchange.class.getName()
                            + "] and the last parameter must be of type ["
                            + TypeConverterRegistry.class.getName() + "].");
        }

        final int modifiers = method.getModifiers();
        if (isAbstract(modifiers) || !isPublic(modifiers)) {
            throw new IllegalArgumentException(
                    "Illegal fallback converter method ["
                            + method
                            + "] on type ["
                            + type.getName()
                            + "]: a fallback converter method must not be abstract, and it must be public.");
        }

        final Class<?> toType = method.getReturnType();
        if (toType.equals(Void.class)) {
            throw new IllegalArgumentException(
                    "Illegal fallback converter method ["
                            + method
                            + "] on type ["
                            + type.getName()
                            + "]: a fallback converter method must not return void.");
        }
    }
}
