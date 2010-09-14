/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.apache.camel.CamelContext;
import org.apache.camel.Converter;
import org.apache.camel.FallbackConverter;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.jee.framework.camelpe.cdi.spi.CamelInjectionTargetWrapper;
import com.acme.orderplacement.jee.framework.camelpe.typeconverter.CdiTypeConverterBuilder;
import com.acme.orderplacement.jee.framework.camelpe.typeconverter.TypeConverterHolder;

/**
 * <p>
 * TODO: Insert short summary for CamelExtension
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CamelExtension implements Extension {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private CdiTypeConverterBuilder typeConverterBuilder;

	private CamelContext cdiCamelContext;

	// -------------------------------------------------------------------------
	// Lifecycle callbacks
	// -------------------------------------------------------------------------

	void intializeCamelContext(@Observes final BeforeBeanDiscovery bbd,
			final BeanManager beanManager) {
		getLog().debug("Initializing CamelContext ...");

		this.cdiCamelContext = new CdiCamelContext(beanManager);
		this.typeConverterBuilder = new CdiTypeConverterBuilder(beanManager,
				this.cdiCamelContext.getTypeConverterRegistry());

		getLog().debug("Finished initializing CamelContext [{}].",
				this.cdiCamelContext);
	}

	<T> void customizeInjectionTargetForEndpointInjection(
			@Observes final ProcessInjectionTarget<T> pit) {
		pit.setInjectionTarget(CamelInjectionTargetWrapper.injectionTargetFor(
				pit.getAnnotatedType(), pit.getInjectionTarget(),
				this.cdiCamelContext));

		getLog().debug(
				"Customized InjectionTarget [{}] for AnnotatedType [{}] in "
						+ "order to enable Camel endpoint injection.", pit,
				pit.getAnnotatedType());
	}

	<T> void registerDiscoveredTypeConvertersInCamelContext(
			@Observes final ProcessInjectionTarget<T> pit) {
		final AnnotatedType<T> annotatedType = pit.getAnnotatedType();
		if (!isAnnotationPresentOnClassOrMethod(annotatedType, Converter.class)
				&& !isAnnotationPresentOnClassOrMethod(annotatedType,
						FallbackConverter.class)) {
			// We are only interested in classes annotated with @Converter or
			// @FallbackConverter, i.e. we are only interested in Camel
			// converters.
			return;
		}

		final Class<T> typeConverterClass = pit.getAnnotatedType()
				.getJavaClass();
		final Set<TypeConverterHolder> typeConverterHolders = this.typeConverterBuilder
				.buildTypeConvertersFrom(typeConverterClass);
		for (final TypeConverterHolder typeConverterHolder : typeConverterHolders) {
			if (typeConverterHolder.isFallback()) {
				this.cdiCamelContext.getTypeConverterRegistry()
						.addFallbackTypeConverter(
								typeConverterHolder.getTypeConverter(),
								typeConverterHolder.isCanPromote());
			} else {
				this.cdiCamelContext.getTypeConverterRegistry()
						.addTypeConverter(typeConverterHolder.getToType(),
								typeConverterHolder.getFromType(),
								typeConverterHolder.getTypeConverter());
			}
		}

		getLog()
				.debug(
						"Registered [{}] TypeConverters built from AnnotatedType [{}].",
						typeConverterHolders.size(), pit.getAnnotatedType());
	}

	void registerCamelContext(@Observes final AfterBeanDiscovery abd) {
		abd.addBean(new CamelContextBean(this.cdiCamelContext));

		getLog().debug(
				"CamelContext [{}] has been registered with the BeanManager.",
				this.cdiCamelContext);
	}

	void afterDeploymentValidation(
			@Observes final AfterDeploymentValidation adv,
			final BeanManager beanManager) {
		registerDiscoveredRoutes(adv, beanManager);
		startCamelContext(adv);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private void startCamelContext(final AfterDeploymentValidation adv) {
		try {
			getLog().debug("Starting CamelContext [{}] ...",
					this.cdiCamelContext);

			this.cdiCamelContext.start();

			getLog().debug("CamelContext [{}] has been started.",
					this.cdiCamelContext);
		} catch (final Exception e) {
			getLog()
					.error("Failed to start CamelContext: " + e.getMessage(), e);
			adv.addDeploymentProblem(e);
		}
	}

	private void registerDiscoveredRoutes(final AfterDeploymentValidation adv,
			final BeanManager beanManager) {
		try {
			getLog().debug(
					"Registering discovered Routes with CamelContext [{}] ...",
					this.cdiCamelContext);

			final Set<Bean<?>> routeBuilderBeans = beanManager
					.getBeans(RouteBuilder.class);
			for (final Bean<?> routeBuilderBean : routeBuilderBeans) {
				registerOneDiscoveredRoute(beanManager, routeBuilderBean);
			}

			getLog()
					.debug(
							"Registered [{}] discovered Route(s) with CamelContext [{}].",
							Integer.valueOf(routeBuilderBeans.size()),
							this.cdiCamelContext);
		} catch (final Exception e) {
			getLog().error(
					"Failed to register discovered Route with CamelContext: "
							+ e.getMessage(), e);
			adv.addDeploymentProblem(e);
		}
	}

	private void registerOneDiscoveredRoute(final BeanManager beanManager,
			final Bean<?> routeBuilderBean) throws Exception {
		final CreationalContext<?> creationalContext = beanManager
				.createCreationalContext(routeBuilderBean);
		final RouteBuilder routeBuilderInstance = RouteBuilder.class
				.cast(beanManager.getReference(routeBuilderBean,
						RouteBuilder.class, creationalContext));
		this.cdiCamelContext.addRoutes(routeBuilderInstance);
		getLog().debug(
				"Registered discovered Route [{}] with CamelContext [{}].",
				routeBuilderInstance, this.cdiCamelContext);
	}

	private <X> boolean isAnnotationPresentOnClassOrMethod(
			final AnnotatedType<X> annotatedTypeToInspect,
			final Class<? extends Annotation> annotationType) {
		if (annotatedTypeToInspect.isAnnotationPresent(annotationType)) {
			return true;
		}
		final Set<AnnotatedMethod<? super X>> annotatedMethods = annotatedTypeToInspect
				.getMethods();
		for (final AnnotatedMethod<? super X> annotatedMethod : annotatedMethods) {
			if (annotatedMethod.isAnnotationPresent(annotationType)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return the log
	 */
	private Logger getLog() {
		return this.log;
	}
}
