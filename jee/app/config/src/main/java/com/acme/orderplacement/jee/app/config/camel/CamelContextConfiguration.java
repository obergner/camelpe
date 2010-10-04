/**
 * 
 */
package com.acme.orderplacement.jee.app.config.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.camel.spi.PackageScanClassResolver;

import com.acme.orderplacement.jee.framework.camel.packagescan.PackageScanClassResolverFactory;
import com.acme.orderplacement.jee.framework.camelpe.CamelContextModifying;

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
	public PackageScanClassResolver packageScanClassResolver() {
		return PackageScanClassResolverFactory.INSTANCE
				.getPackageScanClassResolver();
	}
}
