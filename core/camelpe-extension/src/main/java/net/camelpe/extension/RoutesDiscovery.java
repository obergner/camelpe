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
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for RoutesDiscovery
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class RoutesDiscovery {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private Instance<RouteBuilder> discoveredRoutes;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	@Inject
	void discoverRoutes(final Instance<RouteBuilder> routes) {
		this.discoveredRoutes = routes;
	}

	void registerDiscoveredRoutesIn(final CamelContext camelContext)
	        throws Exception {
		Validate.notNull(camelContext, "camelContext");
		if (this.discoveredRoutes == null) {
			throw new IllegalStateException(
			        "Routes discovery has not yet completed");
		}
		for (final RoutesBuilder routesBuilder : this.discoveredRoutes) {
			camelContext.addRoutes(routesBuilder);
		}
	}

	// -------------------------------------------------------------------------
	// This class wrapped in a CDI bean
	// -------------------------------------------------------------------------

	static class CdiBean implements Bean<RoutesDiscovery> {

		private final InjectionTarget<RoutesDiscovery> injectionTarget;

		/**
		 * @param beanManager
		 */
		CdiBean(final BeanManager beanManager) {
			final AnnotatedType<RoutesDiscovery> annotatedType = beanManager
			        .createAnnotatedType(RoutesDiscovery.class);
			this.injectionTarget = beanManager
			        .createInjectionTarget(annotatedType);
		}

		/**
		 * @see javax.enterprise.inject.spi.Bean#getBeanClass()
		 */
		@Override
		public Class<?> getBeanClass() {
			return RoutesDiscovery.class;
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
			return "routesDiscovery";
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
			types.add(RoutesDiscovery.class);
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
		public RoutesDiscovery create(
		        final CreationalContext<RoutesDiscovery> creationalContext) {
			final RoutesDiscovery instance = this.injectionTarget
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
		public void destroy(final RoutesDiscovery instance,
		        final CreationalContext<RoutesDiscovery> creationalContext) {
			this.injectionTarget.preDestroy(instance);
			this.injectionTarget.dispose(instance);
			creationalContext.release();
		}
	}
}
