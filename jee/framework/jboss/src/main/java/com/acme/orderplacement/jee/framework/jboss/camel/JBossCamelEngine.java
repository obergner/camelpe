/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.camel;

import javax.enterprise.context.ApplicationScoped;

import com.acme.orderplacement.framework.common.auth.cdi.Preferred;
import com.acme.orderplacement.jee.framework.camel.AbstractCamelEngine;
import com.acme.orderplacement.jee.framework.jboss.camel.integration.JBossPackageScanClassResolver;

/**
 * <p>
 * TODO: Insert short summary for JBossCamelEngine
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Preferred
@ApplicationScoped
public class JBossCamelEngine extends AbstractCamelEngine {

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.AbstractCamelEngine#beforeAddingRoutes()
	 */
	@Override
	protected void beforeAddingRoutes() throws Exception {
		super.beforeAddingRoutes();

		camelContext().setPackageScanClassResolver(
				new JBossPackageScanClassResolver());
	}
}
