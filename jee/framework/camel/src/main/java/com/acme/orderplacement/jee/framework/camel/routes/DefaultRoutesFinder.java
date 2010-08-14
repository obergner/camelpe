/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.routes;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.camel.RoutesBuilder;

/**
 * <p>
 * TODO: Insert short summary for DefaultRoutesFinder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class DefaultRoutesFinder implements RoutesFinder {

	private Set<RoutesBuilder> cachedRoutes;

	/**
	 * @see com.acme.orderplacement.jee.framework.camel.routes.RoutesFinder#findRoutes()
	 */
	@Override
	public Set<RoutesBuilder> findRoutes() {
		synchronized (this) {
			if (this.cachedRoutes == null) {
				this.cachedRoutes = new HashSet<RoutesBuilder>();
				final ServiceLoader<RoutesBuilder> routesBuilderLoader = ServiceLoader
						.load(RoutesBuilder.class);
				for (final RoutesBuilder routesBuilder : routesBuilderLoader) {
					this.cachedRoutes.add(routesBuilder);
				}
			}
		}

		return this.cachedRoutes;
	}
}
