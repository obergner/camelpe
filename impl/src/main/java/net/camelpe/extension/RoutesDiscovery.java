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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.Validate;
import org.jboss.seam.solder.bean.BeanBuilder;
import org.jboss.seam.solder.literal.AnyLiteral;
import org.jboss.seam.solder.literal.DefaultLiteral;

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

	static Bean<RoutesDiscovery> cdiBean(final BeanManager beanManager) {
		final AnnotatedType<RoutesDiscovery> annotatedType = beanManager
		        .createAnnotatedType(RoutesDiscovery.class);
		final InjectionTarget<RoutesDiscovery> injectionTarget = beanManager
		        .createInjectionTarget(annotatedType);

		return new BeanBuilder<RoutesDiscovery>(beanManager)
		        .name("routesDiscovery").readFromType(annotatedType)
		        .scope(ApplicationScoped.class)
		        .addQualifiers(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE)
		        .addTypes(RoutesDiscovery.class, Object.class)
		        .alternative(false).nullable(false)
		        .injectionPoints(injectionTarget.getInjectionPoints()).create();
	}
}
