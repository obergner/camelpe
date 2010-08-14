/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel;

import java.util.List;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang.Validate;

import com.acme.orderplacement.jee.framework.camel.routes.RoutesFinder;

/**
 * <p>
 * TODO: Insert short summary for AbstractCamelEngine
 * </p>
 * <p>
 * <strong>Important</strong>: This class and its subclasses must not declare
 * any methods - be they public, protected or private - as final. Otherwise,
 * Weld will be unable to proxy any concrete class derived from this class. This
 * in turn obviates any possibility to put such a derived class in any scope but
 * the dependent scope.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractCamelEngine implements CamelEngine {

	private final CamelContext camelContext = new DefaultCamelContext();

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		beforeAddingRoutes();

		addRoutes();

		afterAddingRoutes();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#start()
	 */
	@Override
	public void start() throws Exception {
		camelContext().start();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#stop()
	 */
	@Override
	public void stop() throws Exception {
		camelContext().stop();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#listRouteDefinitions()
	 */
	@Override
	public List<RouteDefinition> listRouteDefinitions() {
		return camelContext().getRouteDefinitions();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#lookupRouteDefinitionById(java.lang.String)
	 */
	@Override
	public RouteDefinition lookupRouteDefinitionById(final String routeId)
			throws IllegalArgumentException {
		Validate.notEmpty(routeId, "routeId");
		return camelContext().getRouteDefinition(routeId);
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#startRoute(java.lang.String)
	 */
	@Override
	public void startRoute(final String routeId) throws Exception {
		Validate.notEmpty(routeId, "routeId");
		camelContext().startRoute(routeId);
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.CamelEngine#stopRoute(java.lang.String)
	 */
	@Override
	public void stopRoute(final String routeId) throws Exception {
		Validate.notEmpty(routeId, "routeId");
		camelContext().stopRoute(routeId);
	}

	protected CamelContext camelContext() {
		return this.camelContext;
	}

	protected void addRoutes() throws Exception {
		final Set<RoutesBuilder> routesBuilders = RoutesFinder.INSTANCE
				.findRoutes();
		for (final RoutesBuilder routesBuilder : routesBuilders) {
			camelContext().addRoutes(routesBuilder);
		}
	}

	protected void beforeAddingRoutes() throws Exception {

	}

	protected void afterAddingRoutes() throws Exception {

	}
}
