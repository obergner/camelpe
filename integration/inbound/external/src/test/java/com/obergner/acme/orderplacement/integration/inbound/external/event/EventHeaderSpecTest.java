/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import static junit.framework.Assert.assertEquals;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * <p>
 * TODO: Insert short summary for EventHeaderSpecTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EventHeaderSpecTest {

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatEventTypeMetaDatumTypeAcceptsWellformedEventType() {
		EventHeaderSpec.EVENT_TYPE
				.newEventHeaderFrom("urn:event-type:a.test01.004.Event_Type");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatEventTypeMetaDatumTypeRejectsMalformedEventType() {
		EventHeaderSpec.EVENT_TYPE
				.newEventHeaderFrom("urn:event-type:0a.*test01.004.Event_Type");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void assertThatEventIdMetaDatumTypeAcceptsWellformedEventId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.EVENT_ID.newEventHeaderFrom("urn:event:"
				+ UUID.nameUUIDFromBytes("urn:event:".getBytes("UTF-8"))
						.toString());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatEventIdMetaDatumTypeRejectsMalformedEventId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.EVENT_ID.newEventHeaderFrom("urn:event:"
				+ UUID.nameUUIDFromBytes("urn:event:".getBytes("UTF-8"))
						.toString() + "67");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatEventSourceSystemMetaDatumTypeAcceptsWellformedSourceSystem() {
		EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.newEventHeaderFrom("urn:event-source:a._more_or_les0a1_.legal.Event_Source_System");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatEventSourceSystemMetaDatumTypeRejectsMalformedSourceSystem() {
		EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.newEventHeaderFrom("urn:event-source:a._*definitely*_.illegal.Event_Source_System");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCreationTimestampMetaDatumTypeAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.CREATION_TIMESTAMP
				.newEventHeaderFrom("2001-10-26T21:32:52.12679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCreationTimestampMetaDatumTypeRejectsMalformedTimestamp() {
		EventHeaderSpec.CREATION_TIMESTAMP.newEventHeaderFrom("2001-10-26");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void assertThatPropagationIdMetaDatumTypeAcceptsWellformedPropagationId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.PROPAGATION_ID
				.newEventHeaderFrom("urn:event-propagation:"
						+ UUID.nameUUIDFromBytes("propagation:"
								.getBytes("UTF-8")));
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatPropagationIdMetaDatumTypeRejectsMalformedPropagationId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.PROPAGATION_ID
				.newEventHeaderFrom("urn:event-propagation:"
						+ UUID.nameUUIDFromBytes("propagation:"
								.getBytes("UTF-8")) + "pr");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void assertThatInflowIdMetaDatumTypeAcceptsWellformedInflowId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.INFLOW_ID.newEventHeaderFrom("urn:event-inflow:"
				+ UUID.nameUUIDFromBytes("inflow:".getBytes("UTF-8")));
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatInflowIdMetaDatumTypeRejectsMalformedInflowId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.INFLOW_ID.newEventHeaderFrom("urn:event-inflow:"
				+ UUID.nameUUIDFromBytes("inflow:".getBytes("UTF-8")) + "infl");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInflowTimestampMetaDatumTypeAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.INFLOW_TIMESTAMP
				.newEventHeaderFrom("1877-12-17T21:35:43.12679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatInflowTimestampMetaDatumTypeRejectsMalformedTimestamp() {
		EventHeaderSpec.INFLOW_TIMESTAMP.newEventHeaderFrom("2010-11-21T13");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void assertThatProcessingIdMetaDatumTypeAcceptsWellformedProcessingId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.PROCESSING_ID
				.newEventHeaderFrom("urn:event-processing:"
						+ UUID.nameUUIDFromBytes("processing:"
								.getBytes("UTF-8")));
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatProcessingIdMetaDatumTypeRejectsMalformedProcessingId()
			throws IllegalArgumentException, UnsupportedEncodingException {
		EventHeaderSpec.PROCESSING_ID
				.newEventHeaderFrom("urn:event-processing_:"
						+ UUID.nameUUIDFromBytes("processing:"
								.getBytes("UTF-8")));
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatSequenceNumberMetaDatumTypeAcceptsWellformedSequenceNumber() {
		EventHeaderSpec.SEQUENCE_NUMBER.newEventHeaderFrom("987");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatSequenceNumberMetaDatumTypeRejectsMalformedSequenceNumber() {
		EventHeaderSpec.SEQUENCE_NUMBER.newEventHeaderFrom("98,7");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInitiationTimestampMetaDatumTypeAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.INITIATION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.14679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatInitiationTimestampMetaDatumTypeRejectsMalformedTimestamp() {
		EventHeaderSpec.INITIATION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCompletionTimestampMetaDatumTypeAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.COMPLETION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.14679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCompletionTimestampMetaDatumTypeRejectsMalformedTimestamp() {
		EventHeaderSpec.COMPLETION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatProcessingStateMetaDatumTypeAcceptsAllKnownProcessingStates() {
		for (final ProcessingState knownProcessingState : ProcessingState
				.values()) {
			EventHeaderSpec.PROCESSING_STATE
					.newEventHeaderFrom(knownProcessingState.toString());
		}
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatProcessingStateMetaDatumTypeRejectsUnknownProcessingState() {
		EventHeaderSpec.PROCESSING_STATE
				.newEventHeaderFrom("UNKNOWN_PROCESSING_STATE");
	}

	// ~~~~~~~~~~~~

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeadersFrom(java.util.Map)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatNewEventHeadersFromRejectsNullHeaders() {
		EventHeaderSpec.newEventHeadersFrom(null);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeadersFrom(java.util.Map)}
	 * .
	 */
	@Test
	public final void assertThatNewEventHeadersFromIgnoresUnkownHeaders() {
		final Map<String, String> unknownHeaders = ImmutableMap
				.<String, String> builder().put("Header1", "Value1").put(
						"Header2", "Value2").put("Header3", "Value3").put(
						"Header4", "Value4").put("Header5", "Value5").build();

		final EventHeaders eventHeaders = EventHeaderSpec
				.newEventHeadersFrom(unknownHeaders);

		int eventHeadersSize = 0;
		for (final EventHeader<? extends Serializable> ignore : eventHeaders) {
			eventHeadersSize++;
		}
		assertEquals(
				"newEventHeadersFrom(unknownHeaders) did not ignore unknown headers",
				0, eventHeadersSize);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeadersFrom(java.util.Map)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatNewEventHeadersFromRejectsIllegalValues() {
		final Map<String, String> malformedValues = ImmutableMap
				.<String, String> builder().put("CreationTimestamp",
						"2010-01-09").build();
		EventHeaderSpec.newEventHeadersFrom(malformedValues);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeadersFrom(java.util.Map)}
	 * .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void assertThatNewEventHeadersFromAcceptsLegalHeaders()
			throws UnsupportedEncodingException {
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put("CreationTimestamp",
						"2001-10-26T21:32:52.12679").put(
						"EventID",
						"urn:event:"
								+ UUID.nameUUIDFromBytes(
										"urn:event:".getBytes("UTF-8"))
										.toString())
				.put("SequenceNumber", "78").put("Header4", "Value4").put(
						"Header5", "Value5").build();
		final EventHeaders eventHeaders = EventHeaderSpec
				.newEventHeadersFrom(legalHeaders);

		int eventHeadersSize = 0;
		for (final EventHeader<? extends Serializable> ignore : eventHeaders) {
			eventHeadersSize++;
		}
		assertEquals(
				"newEventHeadersFrom(legalHeaders) did not accept (all) legal headers",
				3, eventHeadersSize);
	}
}
