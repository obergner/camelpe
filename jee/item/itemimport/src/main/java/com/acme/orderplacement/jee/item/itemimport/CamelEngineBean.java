/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.camel.model.RouteDefinition;

import com.acme.orderplacement.framework.common.auth.cdi.Preferred;
import com.acme.orderplacement.jee.framework.camel.CamelEngine;

/**
 * <p>
 * TODO: Insert short summary for CamelEngineBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Singleton
@Startup
@Local(CamelEngine.class)
public class CamelEngineBean implements CamelEngine {

	@Preferred
	@Inject
	private CamelEngine camelEngine;

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#start()
	 */
	@PostConstruct
	public void start() throws Exception {
		this.camelEngine.initialize();
		this.camelEngine.start();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#stop()
	 */
	@PreDestroy
	public void stop() throws Exception {
		this.camelEngine.stop();
	}

	/**
	 * @throws Exception
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#initialize()
	 */
	public void initialize() throws Exception {
		this.camelEngine.initialize();
	}

	/**
	 * @return
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#listRouteDefinitions()
	 */
	public List<RouteDefinition> listRouteDefinitions() {
		return this.camelEngine.listRouteDefinitions();
	}

	/**
	 * @param routeId
	 * @return
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#lookupRouteDefinitionById(java.lang.String)
	 */
	public RouteDefinition lookupRouteDefinitionById(final String routeId)
			throws IllegalArgumentException {
		return this.camelEngine.lookupRouteDefinitionById(routeId);
	}

	/**
	 * @param routeId
	 * @throws Exception
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#startRoute(java.lang.String)
	 */
	public void startRoute(final String routeId) throws Exception {
		this.camelEngine.startRoute(routeId);
	}

	/**
	 * @param routeId
	 * @throws Exception
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#stopRoute(java.lang.String)
	 */
	public void stopRoute(final String routeId) throws Exception {
		this.camelEngine.stopRoute(routeId);
	}

}
