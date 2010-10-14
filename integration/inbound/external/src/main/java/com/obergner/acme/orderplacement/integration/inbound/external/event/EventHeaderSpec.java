/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

import com.obergner.acme.orderplacement.integration.inbound.external.ExternalEventTypes;

/**
 * <p>
 * TODO: Insert short summary for EventHeaderSpec
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public enum EventHeaderSpec {

	/**
	 * 
	 */
	EVENT_TYPE("EventType", "urn:event-type:"
			+ EventHeaderSpec.IDENTIFIER_REGEX, String.class,
			ExternalEventTypes.UNKNOWN),

	/**
	 * 
	 */
	EVENT_ID("EventID", "urn:event:" + EventHeaderSpec.UUID_REGEX,
			String.class, "urn:event:" + EventHeaderSpec.UNKNOWN_UUID),

	/**
	 * 
	 */
	CREATION_TIMESTAMP("CreationTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class,
			EventHeaderSpec.UNKNOWN_TIMESTAMP),

	/**
	 * 
	 */
	EVENT_SOURCE_SYSTEM("EventSourceSystem", "urn:event-source:"
			+ EventHeaderSpec.IDENTIFIER_REGEX, String.class,
			"urn:event-source:" + EventHeaderSpec.UNKNOWN_IDENTIFIER),

	/**
	 * 
	 */
	PROPAGATION_ID("PropagationID", "urn:event-propagation:"
			+ EventHeaderSpec.UUID_REGEX, String.class,
			"urn:event-propagation:" + EventHeaderSpec.UNKNOWN_UUID),

	/**
	 * 
	 */
	INFLOW_ID("InflowID", "urn:event-inflow:" + EventHeaderSpec.UUID_REGEX,
			String.class, "urn:event-inflow:" + EventHeaderSpec.UNKNOWN_UUID),

	/**
	 * 
	 */
	INFLOW_TIMESTAMP("InflowTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class,
			EventHeaderSpec.UNKNOWN_TIMESTAMP),

	/**
	 * 
	 */
	PROCESSING_ID("ProcessingID", "urn:event-processing:"
			+ EventHeaderSpec.UUID_REGEX, String.class, "urn:event-processing:"
			+ EventHeaderSpec.UNKNOWN_UUID),

	/**
	 * 
	 */
	SEQUENCE_NUMBER("SequenceNumber", "\\d{1,3}", Integer.class,
			EventHeaderSpec.UNKNOWN_SEQUENCE_NUMBER),

	/**
	 * 
	 */
	INITIATION_TIMESTAMP("InitiationTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class,
			EventHeaderSpec.UNKNOWN_TIMESTAMP),

	/**
	 * 
	 */
	COMPLETION_TIMESTAMP("CompletionTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class,
			EventHeaderSpec.UNKNOWN_TIMESTAMP),

	/**
	 * 
	 */
	PROCESSING_STATE("ProcessingState", "IN_PROGRESS|SUCCESSFUL|FAILED",
			ProcessingState.class, ProcessingState.IN_PROGRESS.name());

	// -------------------------------------------------------------------------
	// Regular expressions instrumental in defining the legal format for event
	// header values.
	// -------------------------------------------------------------------------

	public static final String UUID_REGEX = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";

	public static final String ISO_8601_DATE_FORMAT_REGEX = "^([\\+-]?\\d{4}(?!\\d{2}\\b))((-?)((0[1-9]|1[0-2])(\\3([12]\\d|0[1-9]|3[01]))?"
			+ "|W([0-4]\\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\\d|[12]\\d{2}|3([0-5]\\d|6[1-6])))([T\\s]"
			+ "((([01]\\d|2[0-3])((:?)[0-5]\\d)?|24\\:?00)([\\.,]\\d+(?!:))?)?(\\17[0-5]\\d"
			+ "([\\.,]\\d+)?)?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)?$";

	public static final String IDENTIFIER_REGEX = "[a-zA-Z]{1}[0-9a-zA-Z._]{5,100}";

	// -------------------------------------------------------------------------
	// Default values
	// -------------------------------------------------------------------------

	private static final String UNKNOWN_IDENTIFIER = "<UNKNOWN>";

	private static final String UNKNOWN_UUID = "<UNKNOWN>";

	private static final String UNKNOWN_TIMESTAMP = "0000-01-01T00:00:00.000";

	private static final String UNKNOWN_SEQUENCE_NUMBER = "-1";

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	static EventHeaders newEventHeadersFrom(final Map<String, String> headers)
			throws IllegalArgumentException {
		Validate.notNull(headers, "headers");

		final Set<EventHeader> eventHeaders = new HashSet<EventHeader>(headers
				.size());
		for (final EventHeaderSpec specification : EventHeaderSpec.values()) {
			final String correspondingValue = headers
					.get(specification.headerName);
			final EventHeader eventHeader = correspondingValue != null ? specification
					.newEventHeaderFrom(correspondingValue)
					: specification.newDefaultEventHeader();
			eventHeaders.add(eventHeader);
		}

		return new EventHeaders(eventHeaders);
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final String headerName;

	private final Pattern legalValuePattern;

	private final Class<? extends Serializable> valueType;

	private final String defaultValue;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private EventHeaderSpec(final String headerName,
			final String legalValuePattern,
			final Class<? extends Serializable> valueType,
			final String defaultValue) {
		this.headerName = headerName;
		this.legalValuePattern = Pattern.compile(legalValuePattern);
		this.valueType = valueType;
		this.defaultValue = defaultValue;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public String headerName() {
		return this.headerName;
	}

	public Serializable defaultValue() {
		return convert(this.defaultValue);
	}

	EventHeader newEventHeaderFrom(final String valueAsString)
			throws IllegalArgumentException {
		Validate.notEmpty(valueAsString, "valueAsString");
		final String possiblyNormalizedValueAsString = this.legalValuePattern
				.matcher(valueAsString).matches() ? valueAsString
				: this.defaultValue;

		return newEventHeaderFromInternal(possiblyNormalizedValueAsString);
	}

	EventHeader newDefaultEventHeader() {

		return newEventHeaderFromInternal(this.defaultValue);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private EventHeader newEventHeaderFromInternal(final String valueAsString)
			throws RuntimeException {
		final Serializable value = convert(valueAsString);

		return new EventHeader(this, value);
	}

	private Serializable convert(final String valueAsString)
			throws RuntimeException {
		try {
			final Serializable value;
			if (this.valueType == String.class) {
				value = valueAsString;
			} else if (this.valueType == Integer.class) {
				value = Integer.valueOf(valueAsString);
			} else if (this.valueType == Date.class) {
				/*
				 * SimpleDateFormat is not thread safe. Usually, this instances
				 * of this class should be thread confined, i.e. accessed from
				 * only one thread. Therefore, promoting the SimpleDateFormat
				 * instance below to an instance variable should be safe. Yet
				 * you never know ...
				 */
				value = new SimpleDateFormat(ISO_8601_DATE_FORMAT)
						.parse(valueAsString);
			} else if (this.valueType == ProcessingState.class) {
				value = ProcessingState.valueOf(valueAsString);
			} else {
				throw new AssertionError();
			}

			return value;
		} catch (final ParseException e) {
			throw new RuntimeException("Failed to parse supplied date ["
					+ valueAsString + "]: " + e.getMessage(), e);
		}
	}
}
