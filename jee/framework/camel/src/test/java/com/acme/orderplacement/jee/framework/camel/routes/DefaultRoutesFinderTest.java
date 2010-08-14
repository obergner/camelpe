/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.routes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.apache.camel.RoutesBuilder;
import org.junit.Test;

/**
 * <p>
 * TODO: Insert short summary for DefaultRoutesFinderTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class DefaultRoutesFinderTest {

	private final DefaultRoutesFinder classUnderTest = new DefaultRoutesFinder();

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.routes.DefaultRoutesFinder#findRoutes()}
	 * .
	 */
	@Test
	public final void assertThatFindRoutesCanLoadRoutesFromServiceFile() {
		final Set<RoutesBuilder> routes = this.classUnderTest.findRoutes();

		assertNotNull("findRoutes() returned null", routes);
		assertEquals("findRoutes() found wrong number of routes", 1, routes
				.size());
	}

}
