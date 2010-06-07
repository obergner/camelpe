/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import static junit.framework.Assert.assertFalse;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiRegistry;
import com.acme.orderplacement.jee.framework.camelpe.routes.SampleRoutes;

/**
 * <p>
 * Test {@link CdiRegistry <code>CdiRegistry</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class CamelExtensionInContainerTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@Inject
	private CamelContext camelContext;

	@EndpointInject(uri = "mock:sampleTarget")
	private MockEndpoint resultEndpoint;

	@Produce(uri = "direct:testOut")
	private ProducerTemplate template;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap
				.create("test.jar", JavaArchive.class)
				.addPackages(false, CamelExtension.class.getPackage(),
						SampleRoutes.class.getPackage())
				.addManifestResource(
						"META-INF/services/javax.enterprise.inject.spi.Extension",
						ArchivePaths
								.create("services/javax.enterprise.inject.spi.Extension"))
				.addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));
		System.out.println(testModule.toString(true));

		return testModule;
	}

	public void addMockEndpointsToCamelContext() {

	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatCamelExtensionRegistersNewlyDiscoveredRouteBuilder() {
		assertFalse(this.camelContext.getRouteDefinitions().isEmpty());
	}
}
