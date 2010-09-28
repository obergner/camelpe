/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.ClassResolver;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.FactoryFinderResolver;
import org.apache.camel.spi.InflightRepository;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.ManagementStrategy;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.Registry;
import org.apache.camel.spi.ShutdownStrategy;
import org.apache.commons.lang.Validate;

import com.acme.orderplacement.jee.framework.camelpe.camel.packagescan.PackageScanClassResolverFactory;
import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiInjector;
import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiRegistry;
import com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor.CdiExecutorServiceStrategy;

/**
 * <p>
 * TODO: Insert short summary for CdiCamelContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
/**
 * <p>
 * TODO: Insert short summary for CdiCamelContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class CdiCamelContext extends DefaultCamelContext {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final BeanManager beanManager;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	CdiCamelContext(final BeanManager beanManager)
			throws IllegalArgumentException {
		Validate.notNull(beanManager, "beanManager");
		this.beanManager = beanManager;
		setExecutorServiceStrategy(new CdiExecutorServiceStrategy(this));
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

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#getPackageScanClassResolver()
	 */
	@Override
	public PackageScanClassResolver getPackageScanClassResolver() {
		return PackageScanClassResolverFactory.INSTANCE
				.getPackageScanClassResolver();
	}

	// -------------------------------------------------------------------------
	// CDI injection points allowing for further customizing this instance
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setClassResolver(org.apache.camel.spi.ClassResolver)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setClassResolver(final ClassResolver classResolver) {
		super.setClassResolver(classResolver);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setComponentResolver(org.apache.camel.spi.ComponentResolver)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setComponentResolver(final ComponentResolver componentResolver) {
		super.setComponentResolver(componentResolver);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setDataFormatResolver(org.apache.camel.spi.DataFormatResolver)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setDataFormatResolver(
			final DataFormatResolver dataFormatResolver) {
		super.setDataFormatResolver(dataFormatResolver);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setExecutorServiceStrategy(org.apache.camel.spi.ExecutorServiceStrategy)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setExecutorServiceStrategy(
			final ExecutorServiceStrategy executorServiceStrategy) {
		super.setExecutorServiceStrategy(executorServiceStrategy);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setFactoryFinderResolver(org.apache.camel.spi.FactoryFinderResolver)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setFactoryFinderResolver(final FactoryFinderResolver resolver) {
		super.setFactoryFinderResolver(resolver);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setInflightRepository(org.apache.camel.spi.InflightRepository)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setInflightRepository(final InflightRepository repository) {
		super.setInflightRepository(repository);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setManagementStrategy(org.apache.camel.spi.ManagementStrategy)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setManagementStrategy(
			final ManagementStrategy managementStrategy) {
		super.setManagementStrategy(managementStrategy);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setProcessorFactory(org.apache.camel.spi.ProcessorFactory)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setProcessorFactory(final ProcessorFactory processorFactory) {
		super.setProcessorFactory(processorFactory);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#setShutdownStrategy(org.apache.camel.spi.ShutdownStrategy)
	 */
	@Inject
	@CamelContextModifying
	@Override
	public void setShutdownStrategy(final ShutdownStrategy shutdownStrategy) {
		super.setShutdownStrategy(shutdownStrategy);
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CDI CamelContext [name = " + getName() + " | beanManager = "
				+ this.beanManager.getClass().getName() + "]";
	}

}
