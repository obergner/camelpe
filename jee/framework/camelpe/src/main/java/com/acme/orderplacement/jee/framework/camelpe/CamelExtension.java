/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.jee.framework.camelpe.cdi.spi.CamelInjectionTargetWrapper;

/**
 * <p>
 * TODO: Insert short summary for CamelExtension
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class CamelExtension implements Extension {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private CamelContext cdiCamelContext;

	void intializeCamel(@Observes final BeforeBeanDiscovery bbd,
			final BeanManager beanManager) {
		this.log.debug("Initializing the Camel subsystem ...");

		this.cdiCamelContext = new CdiCamelContext(beanManager);

		this.log.debug("Finished initializing the Camel subsystem [{}]",
				this.cdiCamelContext);
	}

	<T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> pat,
			final BeanManager beanManager) throws Exception {
		this.log.debug("Processing type [{}] ...", pat.getAnnotatedType()
				.getJavaClass().getName());

		if (RouteBuilder.class.isAssignableFrom(pat.getAnnotatedType()
				.getJavaClass())) {
			processRouteBuilder(pat, beanManager);
		}
	}

	<T> void processInjectionTarget(
			@Observes final ProcessInjectionTarget<T> pit) {
		this.log.debug("Processing injection target [{}] ...", pit);

		pit.setInjectionTarget(CamelInjectionTargetWrapper.injectionTargetFor(
				pit.getAnnotatedType(), pit.getInjectionTarget(),
				this.cdiCamelContext));

		this.log.debug("Finished processing injection target [{}]", pit);
	}

	void registerCamelContext(@Observes final AfterBeanDiscovery abd) {
		abd.addBean(new CamelContextBean(this.cdiCamelContext));
		this.log
				.debug(
						"Camel subsystem [{}] has been registered with the BeanManager",
						this.cdiCamelContext);
	}

	void startCamel(@Observes final AfterDeploymentValidation adv) {
		try {
			this.log.debug("Starting the Camel subsystem [{}] ...",
					this.cdiCamelContext);

			this.cdiCamelContext.start();

			this.log.debug("Camel subsystem [{}] has been started",
					this.cdiCamelContext);
		} catch (final Exception e) {
			this.log.error(
					"Failed to start Camel subsystem: " + e.getMessage(), e);
			adv.addDeploymentProblem(e);
		}
	}

	private <T> void processRouteBuilder(final ProcessAnnotatedType<T> pat,
			final BeanManager beanManager) throws Exception {
		final AnnotatedType<RouteBuilder> routeBuilderType = (AnnotatedType<RouteBuilder>) pat
				.getAnnotatedType();

		this.log.debug("Processing RouteBuilder definition [{}] ...", pat
				.getAnnotatedType().getJavaClass().getName());

		final InjectionTarget<RouteBuilder> injectionTarget = beanManager
				.createInjectionTarget(routeBuilderType);
		final CreationalContext<RouteBuilder> creationalContext = beanManager
				.createCreationalContext(null);

		final RouteBuilder routeBuilder = injectionTarget
				.produce(creationalContext);
		injectionTarget.inject(routeBuilder, creationalContext);

		this.cdiCamelContext.addRoutes(routeBuilder);

		this.log.debug(
				"New RouteBuilder instance [{}] added to Camel context [{}]",
				routeBuilder, this.cdiCamelContext);
	}
}
