/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.event;

import static junit.framework.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
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
import org.slf4j.MDC;

import com.acme.orderplacement.framework.common.qualifier.Public;
import com.google.common.collect.ImmutableMap;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContextProviderInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class EventProcessingContextProviderInContainerTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXPECTED_EVENT_ID = "urn:event:"
			+ UUID.randomUUID().toString();

	private static final int EXPECTED_SEQUENCE_NUMBER = 78;

	@Inject
	private EventProcessingContextProvider classUnderTest;

	@Inject
	@Public
	private EventProcessingContext eventProcessingContext;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap.create(JavaArchive.class,
				"test.jar").addPackages(false,
				EventProcessingContextProvider.class.getPackage(),
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
	 * {@link com.acme.orderplacement.jee.framework.camel.event.EventProcessingContextProvider#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessPopulatesCompleteEventProcessingContextFromCompleteJmsMessage()
			throws Exception {
		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeaders(completeWellFormedJmsHeaders());
		inMessage.setBody("IGNORE", String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);

		this.classUnderTest.process(exchange);

		assertEquals("process(exchange) did not set correct EventID",
				EXPECTED_EVENT_ID, this.eventProcessingContext.getEventId());
		assertEquals("process(exchange) did not set correct SequenceNumber",
				EXPECTED_SEQUENCE_NUMBER, this.eventProcessingContext
						.getSequenceNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.event.EventProcessingContextProvider#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessPopulatesMessageDiagnosticContextFromCompleteJmsMessage()
			throws Exception {
		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeaders(completeWellFormedJmsHeaders());
		inMessage.setBody("IGNORE", String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);

		this.classUnderTest.process(exchange);

		assertEquals("process(exchange) did not set correct EventID in MDC",
				EXPECTED_EVENT_ID, MDC.get(EventHeaderSpec.EVENT_ID
						.headerName()));
		assertEquals(
				"process(exchange) did not set correct SequenceNumber in MDC",
				String.valueOf(EXPECTED_SEQUENCE_NUMBER), MDC
						.get(EventHeaderSpec.SEQUENCE_NUMBER.headerName()));
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private Map<String, Object> completeWellFormedJmsHeaders()
			throws UnsupportedEncodingException {
		final Map<String, Object> legalHeaders = ImmutableMap
				.<String, Object> builder().put("CreationTimestamp",
						"2001-10-26T21:32:52.12679").put("EventID",
						EXPECTED_EVENT_ID).put("JMSXDeliveryCount",
						Integer.valueOf(EXPECTED_SEQUENCE_NUMBER)).put(
						"EventType", "urn:event-type:an.event.Type").put(
						"InflowID",
						"urn:event-inflow:"
								+ UUID.nameUUIDFromBytes("urn:event-inflow:"
										.getBytes("UTF-8"))).put(
						"InflowTimestamp", "2001-10-26T21:32:52.12679")
				.put("EventSourceSystem", "urn:event-source:an.event.Source")
				.build();

		return legalHeaders;
	}
}
