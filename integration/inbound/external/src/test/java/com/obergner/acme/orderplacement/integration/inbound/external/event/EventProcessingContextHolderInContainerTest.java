/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

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

import com.google.common.collect.ImmutableMap;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContextHolderInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class EventProcessingContextHolderInContainerTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final int EXPECTED_SEQUENCE_NUMBER = 78;

	@Inject
	private EventProcessingContextHolder classUnderTest;

	@Inject
	private EventProcessingContextAwareBean sampleClient;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap.create(JavaArchive.class,
				"test.jar").addPackages(false,
				EventProcessingContextHolder.class.getPackage())
				.addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder#initialize(com.obergner.acme.orderplacement.integration.inbound.external.event.ImmutableEventProcessingContext)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatInitializeRejectsNullEventProcessingContext() {
		this.classUnderTest.initialize(null);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder#initialize(com.obergner.acme.orderplacement.integration.inbound.external.event.ImmutableEventProcessingContext)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test(expected = IllegalStateException.class)
	public final void assertThatInitializeRejectsAttemptToInitializeEventProcessingContextHolderTwice()
			throws UnsupportedEncodingException {
		final EventProcessingContext delegate = eventProcessingContextDelegate();

		this.classUnderTest.initialize(delegate);
		this.classUnderTest.initialize(delegate);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder#initialize(com.obergner.acme.orderplacement.integration.inbound.external.event.ImmutableEventProcessingContext)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void assertThatInitializeProducesAUsableEventProcessingContext()
			throws UnsupportedEncodingException {
		final EventProcessingContext delegate = eventProcessingContextDelegate();
		final UnsupportedOperationException expectedError = new UnsupportedOperationException();

		this.classUnderTest.initialize(delegate);
		this.sampleClient.failProcessing(expectedError);

		assertSame(
				"initialize(delegate) did not produce a usable EventProcesingContext instance",
				expectedError, this.classUnderTest.getError());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder#eventProcessingContext()}
	 * .
	 */
	@Test(expected = IllegalStateException.class)
	public final void assertThatEventProcessingContextRejectsPrematureAttemptToObtainAnEventProcessingContext() {
		this.classUnderTest.eventProcessingContext();
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder#eventProcessingContext()}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void assertThatEventProcessingContextReturnsAUsableEventProcessingContextAfterProperInitialization()
			throws UnsupportedEncodingException {
		final EventProcessingContext delegate = eventProcessingContextDelegate();
		this.classUnderTest.initialize(delegate);

		final EventProcessingContext eventProcessingContext = this.classUnderTest
				.eventProcessingContext();

		assertNotNull(
				"eventProcessingContext() returned null even after proper initialization",
				eventProcessingContext);
		assertEquals(
				"eventProcessingContext() returned an unusable EventProcessingContext even after proper initialization",
				EXPECTED_SEQUENCE_NUMBER, eventProcessingContext
						.getSequenceNumber());
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private EventProcessingContext eventProcessingContextDelegate()
			throws UnsupportedEncodingException {
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put("CreationTimestamp",
						"2001-10-26T21:32:52.12679").put(
						"EventID",
						"urn:event:"
								+ UUID.nameUUIDFromBytes(
										"urn:event:".getBytes("UTF-8"))
										.toString()).put("SequenceNumber",
						String.valueOf(EXPECTED_SEQUENCE_NUMBER)).put(
						"Header4", "Value4").put("Header5", "Value5").build();

		return ImmutableEventProcessingContext.BUILDER.buildFrom(legalHeaders);
	}
}
