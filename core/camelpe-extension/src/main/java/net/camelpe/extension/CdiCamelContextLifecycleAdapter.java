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

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiCamelContextLifecycleAdapter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class CdiCamelContextLifecycleAdapter {

	// -------------------------------------------------------------------------
	// Injection points
	// -------------------------------------------------------------------------

	@Inject
	private CamelContext camelContext;

	@Inject
	private CdiCamelContextConfiguration camelContextConfiguration;

	@Inject
	private TypeConverterDiscovery typeConverterRegistration;

	@Inject
	private RoutesDiscovery routesDiscovery;

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	void afterDeploymentValidation() throws Exception {
		this.log.info(
		        "Received AfterDeployment event - About to configure and start CamelContext [{}] ...",
		        this.camelContext);
		this.camelContextConfiguration.configure(this.camelContext);

		this.typeConverterRegistration.registerIn(this.camelContext);

		this.routesDiscovery.registerDiscoveredRoutesIn(this.camelContext);

		this.camelContext.start();
		this.log.info("Successfully configured and started CamelContext [{}]",
		        this.camelContext);
	}

	void beforeShutdown() throws Exception {
		this.log.info(
		        "Received BeforeShutdown event - About to stop CamelContext [{}] ...",
		        this.camelContext);
		this.camelContext.stop();
		this.log.info("Successfully stopped CamelContext [{}] ...",
		        this.camelContext);
	}

	// -------------------------------------------------------------------------
	// This class wrapped in a CDI bean
	// -------------------------------------------------------------------------

	static class CdiBean implements Bean<CdiCamelContextLifecycleAdapter> {

		private final InjectionTarget<CdiCamelContextLifecycleAdapter> injectionTarget;

		/**
		 * @param beanManager
		 */
		CdiBean(final BeanManager beanManager) {
			final AnnotatedType<CdiCamelContextLifecycleAdapter> annotatedType = beanManager
			        .createAnnotatedType(CdiCamelContextLifecycleAdapter.class);
			this.injectionTarget = beanManager
			        .createInjectionTarget(annotatedType);
		}

		/**
		 * @see javax.enterprise.inject.spi.Bean#getBeanClass()
		 */
		@Override
		public Class<?> getBeanClass() {
			return CdiCamelContextLifecycleAdapter.class;
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
			return "cdiCamelContextLifecycle";
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
			types.add(CdiCamelContextLifecycleAdapter.class);
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
		public CdiCamelContextLifecycleAdapter create(
		        final CreationalContext<CdiCamelContextLifecycleAdapter> creationalContext) {
			final CdiCamelContextLifecycleAdapter instance = this.injectionTarget
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
		        final CdiCamelContextLifecycleAdapter instance,
		        final CreationalContext<CdiCamelContextLifecycleAdapter> creationalContext) {
			this.injectionTarget.preDestroy(instance);
			this.injectionTarget.dispose(instance);
			creationalContext.release();
		}
	}
}
