/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.routes;

import java.util.Set;

import org.apache.camel.RoutesBuilder;

/**
 * <p>
 * TODO: Insert short summary for RoutesFinder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface RoutesFinder {

	RoutesFinder INSTANCE = new DefaultRoutesFinder();

	Set<RoutesBuilder> findRoutes();
}
