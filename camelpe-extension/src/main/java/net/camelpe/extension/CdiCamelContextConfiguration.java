/**
 * 
 */
package net.camelpe.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AmbiguousResolutionException;
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

import net.camelpe.api.CamelContextModifying;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.ClassResolver;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.FactoryFinderResolver;
import org.apache.camel.spi.InflightRepository;
import org.apache.camel.spi.ManagementStrategy;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.ShutdownStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiCamelContextConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class CdiCamelContextConfiguration {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ClassResolver classResolver;

    private PackageScanClassResolver packageScanClassResolver;

    private DataFormatResolver dataFormatResolver;

    private ExecutorServiceStrategy executorServiceStrategy;

    private FactoryFinderResolver factoryFinderResolver;

    private InflightRepository inflightRepository;

    private ManagementStrategy managementStrategy;

    private ProcessorFactory processorFactory;

    private ShutdownStrategy shutdownStrategy;

    // -------------------------------------------------------------------------
    // CDI injection points allowing for customization of CdiCamelContext
    // -------------------------------------------------------------------------

    @Inject
    public void registerClassResolver(
            final @CamelContextModifying Instance<ClassResolver> classResolvers)
            throws AmbiguousResolutionException {
        for (final ClassResolver match : classResolvers) {
            if (this.classResolver != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + ClassResolver.class.getName() + "] found: ["
                                + this.classResolver + "] and [" + match + "]");
            }
            this.classResolver = match;
        }
    }

    @Inject
    public void registerPackageScanClassResolver(
            final @CamelContextModifying Instance<PackageScanClassResolver> packageScanClassResolvers)
            throws AmbiguousResolutionException {
        for (final PackageScanClassResolver match : packageScanClassResolvers) {
            if (this.packageScanClassResolver != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + PackageScanClassResolver.class.getName()
                                + "] found: [" + this.packageScanClassResolver
                                + "] and [" + match + "]");
            }
            this.packageScanClassResolver = match;
        }
    }

    @Inject
    public void registerDataFormatResolver(
            final @CamelContextModifying Instance<DataFormatResolver> dataFormatResolvers)
            throws AmbiguousResolutionException {
        for (final DataFormatResolver match : dataFormatResolvers) {
            if (this.dataFormatResolver != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + DataFormatResolver.class.getName()
                                + "] found: [" + this.dataFormatResolver
                                + "] and [" + match + "]");
            }
            this.dataFormatResolver = match;
        }
    }

    @Inject
    public void registerExecutorServiceStrategy(
            final @CamelContextModifying Instance<ExecutorServiceStrategy> executorServiceStrategies)
            throws AmbiguousResolutionException {
        for (final ExecutorServiceStrategy match : executorServiceStrategies) {
            if (this.executorServiceStrategy != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + ExecutorServiceStrategy.class.getName()
                                + "] found: [" + this.executorServiceStrategy
                                + "] and [" + match + "]");
            }
            this.executorServiceStrategy = match;
        }
    }

    @Inject
    public void registerFactoryFinderResolver(
            final @CamelContextModifying Instance<FactoryFinderResolver> factoryFinderResolvers)
            throws AmbiguousResolutionException {
        for (final FactoryFinderResolver match : factoryFinderResolvers) {
            if (this.factoryFinderResolver != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + FactoryFinderResolver.class.getName()
                                + "] found: [" + this.factoryFinderResolver
                                + "] and [" + match + "]");
            }
            this.factoryFinderResolver = match;
        }
    }

    @Inject
    public void registerInflightRepository(
            final @CamelContextModifying Instance<InflightRepository> inflightRepositories)
            throws AmbiguousResolutionException {
        for (final InflightRepository match : inflightRepositories) {
            if (this.inflightRepository != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + InflightRepository.class.getName()
                                + "] found: [" + this.inflightRepository
                                + "] and [" + match + "]");
            }
            this.inflightRepository = match;
        }
    }

    @Inject
    public void registerManagementStrategy(
            final @CamelContextModifying Instance<ManagementStrategy> managementStrategies)
            throws AmbiguousResolutionException {
        for (final ManagementStrategy match : managementStrategies) {
            if (this.managementStrategy != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + ManagementStrategy.class.getName()
                                + "] found: [" + this.managementStrategy
                                + "] and [" + match + "]");
            }
            this.managementStrategy = match;
        }
    }

    @Inject
    public void registerProcessorFactory(
            final @CamelContextModifying Instance<ProcessorFactory> processorFactories)
            throws AmbiguousResolutionException {
        for (final ProcessorFactory match : processorFactories) {
            if (this.processorFactory != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + ProcessorFactory.class.getName()
                                + "] found: [" + this.processorFactory
                                + "] and [" + match + "]");
            }
            this.processorFactory = match;
        }
    }

    @Inject
    public void registerShutdownStrategy(
            final @CamelContextModifying Instance<ShutdownStrategy> shutdownStrategies)
            throws AmbiguousResolutionException {
        for (final ShutdownStrategy match : shutdownStrategies) {
            if (this.shutdownStrategy != null) {
                throw new AmbiguousResolutionException(
                        "More than one implementation of ["
                                + ShutdownStrategy.class.getName()
                                + "] found: [" + this.shutdownStrategy
                                + "] and [" + match + "]");
            }
            this.shutdownStrategy = match;
        }
    }

    // -------------------------------------------------------------------------
    // Configuring a new CdiCamelContext
    // -------------------------------------------------------------------------

    void configure(final CamelContext camelContext) {
        if (this.classResolver != null) {
            camelContext.setClassResolver(this.classResolver);
            this.log.trace(
                    "Set custom ClassResolver [{}] on CamelContext [{}]",
                    this.classResolver, camelContext);
        }
        if (this.packageScanClassResolver != null) {
            camelContext
                    .setPackageScanClassResolver(this.packageScanClassResolver);
            this.log.trace(
                    "Set custom PackageScanClassResolver [{}] on CamelContext [{}]",
                    this.packageScanClassResolver, camelContext);
        }
        if (this.dataFormatResolver != null) {
            camelContext.setDataFormatResolver(this.dataFormatResolver);
            this.log.trace(
                    "Set custom DataFormatResolver [{}] on CamelContext [{}]",
                    this.dataFormatResolver, camelContext);
        }
        if (this.executorServiceStrategy != null) {
            camelContext
                    .setExecutorServiceStrategy(this.executorServiceStrategy);
            this.log.trace(
                    "Set custom ExecutorServiceStrategy [{}] on CamelContext [{}]",
                    this.executorServiceStrategy, camelContext);
        }
        if (this.factoryFinderResolver != null) {
            camelContext.setFactoryFinderResolver(this.factoryFinderResolver);
            this.log.trace(
                    "Set custom FactoryFinderResolver [{}] on CamelContext [{}]",
                    this.factoryFinderResolver, camelContext);
        }
        if (this.inflightRepository != null) {
            camelContext.setInflightRepository(this.inflightRepository);
            this.log.trace(
                    "Set custom InflightRepository [{}] on CamelContext [{}]",
                    this.inflightRepository, camelContext);
        }
        if (this.managementStrategy != null) {
            camelContext.setManagementStrategy(this.managementStrategy);
            this.log.trace(
                    "Set custom ManagementStrategy [{}] on CamelContext [{}]",
                    this.managementStrategy, camelContext);
        }
        if (this.processorFactory != null) {
            camelContext.setProcessorFactory(this.processorFactory);
            this.log.trace(
                    "Set custom ProcessorFactory [{}] on CamelContext [{}]",
                    this.processorFactory, camelContext);
        }
        if (this.shutdownStrategy != null) {
            camelContext.setShutdownStrategy(this.shutdownStrategy);
            this.log.trace(
                    "Set custom ShutdownStrategy [{}] on CamelContext [{}]",
                    this.shutdownStrategy, camelContext);
        }
        this.log.debug("Configured CamelContext [{}]", camelContext);
    }

    // -------------------------------------------------------------------------
    // This class wrapped in a CDI bean
    // -------------------------------------------------------------------------

    static class CdiBean implements Bean<CdiCamelContextConfiguration> {

        private final AnnotatedType<CdiCamelContextConfiguration> configurationAnnotatedType;

        private final InjectionTarget<CdiCamelContextConfiguration> configurationInjectionTarget;

        /**
         * @param beanManager
         */
        CdiBean(final BeanManager beanManager) {
            this.configurationAnnotatedType = beanManager
                    .createAnnotatedType(CdiCamelContextConfiguration.class);
            this.configurationInjectionTarget = beanManager
                    .createInjectionTarget(this.configurationAnnotatedType);
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getBeanClass()
         */
        @Override
        public Class<?> getBeanClass() {
            return CdiCamelContextConfiguration.class;
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getInjectionPoints()
         */
        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return this.configurationInjectionTarget.getInjectionPoints();
        }

        /**
         * @see javax.enterprise.inject.spi.Bean#getName()
         */
        @Override
        public String getName() {
            return "cdiCamelContextConfiguration";
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
            types.add(CdiCamelContextConfiguration.class);
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
        public CdiCamelContextConfiguration create(
                final CreationalContext<CdiCamelContextConfiguration> creationalContext) {
            final CdiCamelContextConfiguration instance = this.configurationInjectionTarget
                    .produce(creationalContext);
            this.configurationInjectionTarget.inject(instance,
                    creationalContext);
            this.configurationInjectionTarget.postConstruct(instance);

            return instance;
        }

        /**
         * @see javax.enterprise.context.spi.Contextual#destroy(java.lang.Object,
         *      javax.enterprise.context.spi.CreationalContext)
         */
        @Override
        public void destroy(
                final CdiCamelContextConfiguration instance,
                final CreationalContext<CdiCamelContextConfiguration> creationalContext) {
            this.configurationInjectionTarget.preDestroy(instance);
            this.configurationInjectionTarget.dispose(instance);
            creationalContext.release();
        }
    }
}
