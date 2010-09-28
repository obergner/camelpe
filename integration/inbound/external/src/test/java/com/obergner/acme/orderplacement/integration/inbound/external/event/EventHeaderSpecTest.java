/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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
	public final void assertThatEventTypeSpecAcceptsWellformedEventType() {
		EventHeaderSpec.EVENT_TYPE
				.newEventHeaderFrom("urn:event-type:a.test01.004.Event_Type");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatEventTypeSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "urn:event-type:0a.*test01.004.Event_Type";
		final EventHeader eventHeader = EventHeaderSpec.EVENT_TYPE
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.EVENT_TYPE.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
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
	public final void assertThatEventIdSpecAcceptsWellformedEventId()
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
	@Test
	public final void assertThatEventIdSpecUsesDefaultValueForMalformedInput()
			throws IllegalArgumentException, UnsupportedEncodingException {
		final String malformedInput = "urn:event:"
				+ UUID.nameUUIDFromBytes("urn:event:".getBytes("UTF-8"))
						.toString() + "67";
		final EventHeader eventHeader = EventHeaderSpec.EVENT_ID
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.EVENT_ID.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatEventSourceSystemSpecAcceptsWellformedSourceSystem() {
		EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.newEventHeaderFrom("urn:event-source:a._more_or_les0a1_.legal.Event_Source_System");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatEventSourceSystemSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "urn:event-source:a._*definitely*_.illegal.Event_Source_System";
		final EventHeader eventHeader = EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.EVENT_SOURCE_SYSTEM.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCreationTimestampSpecAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.CREATION_TIMESTAMP
				.newEventHeaderFrom("2001-10-26T21:32:52.12679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCreationTimestampSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "2001-10-26R";
		final EventHeader eventHeader = EventHeaderSpec.CREATION_TIMESTAMP
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.CREATION_TIMESTAMP.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
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
	public final void assertThatPropagationIdSpecAcceptsWellformedPropagationId()
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
	@Test
	public final void assertThatPropagationIdSpecUsesDefaultValueForMalformedInput()
			throws IllegalArgumentException, UnsupportedEncodingException {
		final String malformedInput = "urn:event-propagation:"
				+ UUID.nameUUIDFromBytes("propagation:".getBytes("UTF-8"))
				+ "pr";
		final EventHeader eventHeader = EventHeaderSpec.PROPAGATION_ID
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.PROPAGATION_ID.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
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
	public final void assertThatInflowIdSpecAcceptsWellformedInflowId()
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
	@Test
	public final void assertThatInflowIdSpecUsesDefaultValueForMalformedInput()
			throws IllegalArgumentException, UnsupportedEncodingException {
		final String malformedInput = "urn:event-inflow:"
				+ UUID.nameUUIDFromBytes("inflow:".getBytes("UTF-8")) + "infl";
		final EventHeader eventHeader = EventHeaderSpec.INFLOW_ID
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.INFLOW_ID.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInflowTimestampSpecAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.INFLOW_TIMESTAMP
				.newEventHeaderFrom("1877-12-17T21:35:43.12679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInflowTimestampSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "2010-11-21T25";
		final EventHeader eventHeader = EventHeaderSpec.INFLOW_TIMESTAMP
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.INFLOW_TIMESTAMP.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
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
	public final void assertThatProcessingIdSpecAcceptsWellformedProcessingId()
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
	@Test
	public final void assertThatProcessingIdSpecUsesDefaultValueForMalformedInput()
			throws IllegalArgumentException, UnsupportedEncodingException {
		final String malformedInput = "urn:event-processing_:"
				+ UUID.nameUUIDFromBytes("processing:".getBytes("UTF-8"));
		final EventHeader eventHeader = EventHeaderSpec.PROCESSING_ID
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.PROCESSING_ID.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatSequenceNumberSpecAcceptsWellformedSequenceNumber() {
		EventHeaderSpec.SEQUENCE_NUMBER.newEventHeaderFrom("987");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatSequenceNumberSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "98,7";
		final EventHeader eventHeader = EventHeaderSpec.SEQUENCE_NUMBER
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.SEQUENCE_NUMBER.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInitiationTimestampSpecAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.INITIATION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.14679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatInitiationTimestampSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "0001-12-17Z21:35:43.";
		final EventHeader eventHeader = EventHeaderSpec.INITIATION_TIMESTAMP
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.INITIATION_TIMESTAMP.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCompletionTimestampSpecAcceptsISO8601FormattedTimestamp() {
		EventHeaderSpec.COMPLETION_TIMESTAMP
				.newEventHeaderFrom("0001-12-17T21:35:43.14679");
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatCompletionTimestampSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "0001-12-17R21:35:43.1";
		final EventHeader eventHeader = EventHeaderSpec.COMPLETION_TIMESTAMP
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.COMPLETION_TIMESTAMP.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeaderFrom(java.lang.String)}
	 * .
	 */
	@Test
	public final void assertThatProcessingStateSpecAcceptsAllKnownProcessingStates() {
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
	@Test
	public final void assertThatProcessingStateSpecUsesDefaultValueForMalformedInput() {
		final String malformedInput = "UNKNOWN_PROCESSING_STATE";
		final EventHeader eventHeader = EventHeaderSpec.PROCESSING_STATE
				.newEventHeaderFrom(malformedInput);

		assertTrue("EventHeaderSpec.PROCESSING_STATE.newEventHeaderFrom("
				+ malformedInput + ") did not supply default value",
				eventHeader.hasDefaultValue());
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
	public final void assertThatNewEventHeadersFromBuildsCompleteHeadersEvenFromUnknownInput() {
		final Map<String, String> unknownHeaders = ImmutableMap
				.<String, String> builder().put("Header1", "Value1").put(
						"Header2", "Value2").put("Header3", "Value3").put(
						"Header4", "Value4").put("Header5", "Value5").build();

		final EventHeaders eventHeaders = EventHeaderSpec
				.newEventHeadersFrom(unknownHeaders);

		int eventHeadersSize = 0;
		for (final EventHeader ignore : eventHeaders) {
			eventHeadersSize++;
		}
		assertEquals(
				"newEventHeadersFrom(unknownHeaders) did not build complete EventHeaders from unknown headers",
				12, eventHeadersSize);
	}

	/**
	 * Test method for
	 * {@link com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec#newEventHeadersFrom(java.util.Map)}
	 * .
	 */
	@Test
	public final void assertThatNewEventHeadersFromReplacesMalformedInputWithDefaultValue() {
		final Map<String, String> malformedValues = ImmutableMap
				.<String, String> builder().put("CreationTimestamp",
						"2010-01-09Z").build();

		final EventHeaders eventHeaders = EventHeaderSpec
				.newEventHeadersFrom(malformedValues);

		final EventHeader creationTimestampHeader = eventHeaders
				.specifiedBy(EventHeaderSpec.CREATION_TIMESTAMP);
		assertTrue(
				"EventHeaderSpec.newEventHeadersFrom(malformedValues) did not replace malformed timestamp with default value",
				creationTimestampHeader.hasDefaultValue());
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
		final String creationTimestamp = "2001-10-26T21:32:52.12679";
		final String eventId = "urn:event:"
				+ UUID.nameUUIDFromBytes("urn:event:".getBytes("UTF-8"))
						.toString();
		final String sequenceNumber = "78";
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put(
						EventHeaderSpec.CREATION_TIMESTAMP.headerName(),
						creationTimestamp).put(
						EventHeaderSpec.EVENT_ID.headerName(), eventId).put(
						EventHeaderSpec.SEQUENCE_NUMBER.headerName(),
						sequenceNumber).put("Header4", "Value4").put("Header5",
						"Value5").build();
		final EventHeaders eventHeaders = EventHeaderSpec
				.newEventHeadersFrom(legalHeaders);

		final EventHeader eventIdHeader = eventHeaders
				.specifiedBy(EventHeaderSpec.EVENT_ID);
		assertEquals(
				"newEventHeadersFrom(legalHeaders) did not accept (all) legal headers",
				eventId, eventIdHeader.getValue());
	}
}
