/**
 * 
 */
package com.acme.orderplacement.jee.app.config.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.PackageScanClassResolver;

import com.acme.orderplacement.jee.framework.camelpe.CamelContextModifying;
import com.acme.orderplacement.jee.framework.camelpe.camel.packagescan.PackageScanClassResolverFactory;
import com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor.weld.CdiExecutorServiceStrategy;

/**
 * <p>
 * TODO: Insert short summary for CamelContextConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class CamelContextConfiguration {

	@Produces
	@CamelContextModifying
	public ExecutorServiceStrategy executorServiceStrategy(
			final CamelContext camelContext) {
		return new CdiExecutorServiceStrategy(camelContext);
	}

	@Produces
	@CamelContextModifying
	public PackageScanClassResolver packageScanClassResolver() {
		return PackageScanClassResolverFactory.INSTANCE
				.getPackageScanClassResolver();
	}
}
