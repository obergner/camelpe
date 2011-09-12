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
import javax.enterprise.context.ContextException;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import net.camelpe.extension.camel.spi.CdiInjector;
import net.camelpe.extension.camel.spi.CdiRegistry;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.Registry;
import org.jboss.seam.solder.bean.BeanBuilder;
import org.jboss.seam.solder.bean.ContextualLifecycle;
import org.jboss.seam.solder.literal.AnyLiteral;
import org.jboss.seam.solder.literal.DefaultLiteral;

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

	@Override
	public String toString() {
		return "CdiCamelContext@" + this.hashCode() + "[name = " + getName()
		        + "|version = " + getVersion() + "|status = " + getStatus()
		        + "|beanManager = " + this.beanManager + "]";
	}

	// -------------------------------------------------------------------------
	// This class wrapped in a CDI bean
	// -------------------------------------------------------------------------

	static Bean<CdiCamelContext> cdiBean(final BeanManager beanManager) {
		final AnnotatedType<CdiCamelContext> annotatedType = beanManager
		        .createAnnotatedType(CdiCamelContext.class);
		final InjectionTarget<CdiCamelContext> injectionTarget = beanManager
		        .createInjectionTarget(annotatedType);

		return new BeanBuilder<CdiCamelContext>(beanManager)
		        .name("cdiCamelContext")
		        .readFromType(annotatedType)
		        .scope(ApplicationScoped.class)
		        .addQualifiers(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE)
		        .addTypes(CdiCamelContext.class, CamelContext.class,
		                Object.class).alternative(false).nullable(false)
		        .injectionPoints(injectionTarget.getInjectionPoints())
		        .beanLifecycle(new ContextualLifecycle<CdiCamelContext>() {

			        @Override
			        public void destroy(
			                final Bean<CdiCamelContext> bean,
			                final CdiCamelContext instance,
			                final CreationalContext<CdiCamelContext> creationalContext) {
				        try {
					        injectionTarget.preDestroy(instance);
					        instance.stop();
					        injectionTarget.dispose(instance);
					        creationalContext.release();
				        } catch (final Exception e) {
					        throw new ContextException(
					                "Failed to destroy CamelContext ["
					                        + instance + "]: " + e.getMessage(),
					                e);
				        }
			        }

			        @Override
			        public CdiCamelContext create(
			                final Bean<CdiCamelContext> bean,
			                final CreationalContext<CdiCamelContext> creationalContext) {
				        final CdiCamelContext instance = injectionTarget
				                .produce(creationalContext);
				        injectionTarget.inject(instance, creationalContext);
				        injectionTarget.postConstruct(instance);

				        return instance;
			        }
		        }).create();
	}
}
