/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel;

import java.util.List;

import org.apache.camel.model.RouteDefinition;

/**
 * <p>
 * TODO: Insert short summary for CamelEngine
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface CamelEngine {

	void initialize() throws Exception;

	void start() throws Exception;

	void stop() throws Exception;

	List<RouteDefinition> listRouteDefinitions();

	RouteDefinition lookupRouteDefinitionById(final String routeId)
			throws IllegalArgumentException;

	void startRoute(final String routeId) throws Exception;

	void stopRoute(final String routeId) throws Exception;
}
