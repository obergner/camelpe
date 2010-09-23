/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContextTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EventProcessingContextTest {

	/**
	 * Test method for {@link
	 * com.obergner.acme.orderplacement.integration.inbound.external.event.
	 * EventProcessingContext.Builder.buildFrom(java.util.Map<String,String>)} .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatBuilderRejectsNullHeaders() {
		EventProcessingContext.BUILDER.buildFrom(null);
	}

	/**
	 * Test method for {@link
	 * com.obergner.acme.orderplacement.integration.inbound.external.event.
	 * EventProcessingContext.Builder.buildFrom(java.util.Map<String,String>)} .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void assertThatBuilderAcceptsLegalIncompleteHeaders()
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

		final EventProcessingContext eventProcessingContext = EventProcessingContext.BUILDER
				.buildFrom(legalHeaders);

		assertNotNull("Builder.buildFrom(" + legalHeaders + ") returned null",
				eventProcessingContext);
	}

	/**
	 * Test method for {@link
	 * com.obergner.acme.orderplacement.integration.inbound.external.event.
	 * EventProcessingContext.Builder.buildFrom(java.util.Map<String,String>)} .
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void assertThatBuilderCorrectlySetsEventId()
			throws UnsupportedEncodingException {
		final String expectedEventId = "urn:event:"
				+ UUID.nameUUIDFromBytes("urn:event:".getBytes("UTF-8"))
						.toString();
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put("EventID", expectedEventId)
				.build();

		final EventProcessingContext eventProcessingContext = EventProcessingContext.BUILDER
				.buildFrom(legalHeaders);

		assertEquals("Builder.buildFrom(" + legalHeaders
				+ ") did not set correct event id", expectedEventId,
				eventProcessingContext.getEventId());
	}

	/**
	 * Test method for {@link
	 * com.obergner.acme.orderplacement.integration.inbound.external.event.
	 * EventProcessingContext.Builder.buildFrom(java.util.Map<String,String>)} .
	 */
	@Test
	public final void assertThatBuilderCorrectlySetsCreationTimestamp() {
		final Date expectedCreationTimestamp = new Date();
		final String expectedCreationTimestampStr = new SimpleDateFormat(
				EventHeaderSpec.ISO_8601_DATE_FORMAT)
				.format(expectedCreationTimestamp);
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put("CreationTimestamp",
						expectedCreationTimestampStr).build();

		final EventProcessingContext eventProcessingContext = EventProcessingContext.BUILDER
				.buildFrom(legalHeaders);

		assertEquals("Builder.buildFrom(" + legalHeaders
				+ ") did not set correct correct creation timestamp",
				expectedCreationTimestamp, eventProcessingContext
						.getCreationTimestamp());
	}

	/**
	 * Test method for {@link
	 * com.obergner.acme.orderplacement.integration.inbound.external.event.
	 * EventProcessingContext.Builder.buildFrom(java.util.Map<String,String>)} .
	 */
	@Test
	public final void assertThatBuilderCorrectlySetsProcessingState() {
		final ProcessingState expectedProcessingState = ProcessingState.IN_PROGRESS;
		final Map<String, String> legalHeaders = ImmutableMap
				.<String, String> builder().put("ProcessingState",
						expectedProcessingState.toString()).build();

		final EventProcessingContext eventProcessingContext = EventProcessingContext.BUILDER
				.buildFrom(legalHeaders);

		assertEquals("Builder.buildFrom(" + legalHeaders
				+ ") did not set correct correct ProcessingState",
				expectedProcessingState, eventProcessingContext
						.getProcessingState());
	}
}
