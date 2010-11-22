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

package net.camelpe.extension.configuration_samples;

import java.lang.annotation.Annotation;
import java.util.Set;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.PackageScanFilter;

/**
 * <p>
 * TODO: Insert short summary for SamplePackageScanClassResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
public class SamplePackageScanClassResolver implements PackageScanClassResolver {

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#addClassLoader(java.lang.ClassLoader)
     */
    @Override
    public void addClassLoader(final ClassLoader classLoader) {
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#addFilter(org.apache.camel.spi.PackageScanFilter)
     */
    @Override
    public void addFilter(final PackageScanFilter filter) {
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#findAnnotated(java.lang.Class,
     *      java.lang.String[])
     */
    @Override
    public Set<Class<?>> findAnnotated(
            final Class<? extends Annotation> annotation,
            final String... packageNames) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#findAnnotated(java.util.Set,
     *      java.lang.String[])
     */
    @Override
    public Set<Class<?>> findAnnotated(
            final Set<Class<? extends Annotation>> annotations,
            final String... packageNames) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#findByFilter(org.apache.camel.spi.PackageScanFilter,
     *      java.lang.String[])
     */
    @Override
    public Set<Class<?>> findByFilter(final PackageScanFilter filter,
            final String... packageNames) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#findImplementations(java.lang.Class,
     *      java.lang.String[])
     */
    @Override
    public Set<Class<?>> findImplementations(final Class<?> parent,
            final String... packageNames) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#getClassLoaders()
     */
    @Override
    public Set<ClassLoader> getClassLoaders() {
        return null;
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#removeFilter(org.apache.camel.spi.PackageScanFilter)
     */
    @Override
    public void removeFilter(final PackageScanFilter filter) {
    }

    /**
     * @see org.apache.camel.spi.PackageScanClassResolver#setClassLoaders(java.util.Set)
     */
    @Override
    public void setClassLoaders(final Set<ClassLoader> classLoaders) {
    }
}
