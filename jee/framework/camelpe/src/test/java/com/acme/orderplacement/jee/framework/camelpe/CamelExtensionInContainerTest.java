/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import static junit.framework.Assert.assertFalse;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiRegistry;

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

	@Inject
	private SampleProducer sampleProducer;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackages(false, CamelExtension.class.getPackage(),
						SampleRoutes.class.getPackage())
				.addManifestResource(
						"META-INF/services/javax.enterprise.inject.spi.Extension",
						ArchivePaths
								.create("services/javax.enterprise.inject.spi.Extension"))
				.addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatCamelExtensionDiscoversAndRegistersRoute() {
		assertFalse(
				"Camel CDI extension should have registered at least one Route "
						+ "with CamelContext. This, however, is not the case.",
				this.camelContext.getRouteDefinitions().isEmpty());
	}

	@Test
	public void assertThatRouteDiscoveredAndRegisteredByCamelExtensionBasicallyWorks()
			throws Exception {
		final String testMessage = "Test message";

		final MockEndpoint mockEndpoint = this.camelContext.getEndpoint(
				SampleRoutes.SAMPLE_TARGET_EP, MockEndpoint.class);
		mockEndpoint.expectedMinimumMessageCount(1);

		final ProducerTemplate producerTemplate = this.camelContext
				.createProducerTemplate();
		producerTemplate.sendBodyAndHeader(SampleRoutes.SAMPLE_SOURCE_EP,
				testMessage, "foo", "bar");

		mockEndpoint.assertIsSatisfied();
	}

	@Test
	public void assertThatCdiConfiguredProducerBasicallyWorks()
			throws Exception {
		final String testMessage = "Test message";

		final MockEndpoint mockEndpoint = this.camelContext.getEndpoint(
				SampleRoutes.SAMPLE_TARGET_EP, MockEndpoint.class);
		mockEndpoint.expectedMinimumMessageCount(1);

		this.sampleProducer.sendBody(testMessage);

		mockEndpoint.assertIsSatisfied();
	}
}
