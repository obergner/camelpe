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
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.jboss.seam.solder.bean.BeanBuilder;
import org.jboss.seam.solder.literal.AnyLiteral;
import org.jboss.seam.solder.literal.DefaultLiteral;
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

	static Bean<CdiCamelContextLifecycleAdapter> cdiBean(
	        final BeanManager beanManager) {
		final AnnotatedType<CdiCamelContextLifecycleAdapter> annotatedType = beanManager
		        .createAnnotatedType(CdiCamelContextLifecycleAdapter.class);
		final InjectionTarget<CdiCamelContextLifecycleAdapter> injectionTarget = beanManager
		        .createInjectionTarget(annotatedType);

		return new BeanBuilder<CdiCamelContextLifecycleAdapter>(beanManager)
		        .name("cdiCamelContextLifecycleAdapter")
		        .readFromType(annotatedType).scope(ApplicationScoped.class)
		        .addQualifiers(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE)
		        .addTypes(CdiCamelContextLifecycleAdapter.class, Object.class)
		        .alternative(false).nullable(false)
		        .injectionPoints(injectionTarget.getInjectionPoints()).create();
	}
}
