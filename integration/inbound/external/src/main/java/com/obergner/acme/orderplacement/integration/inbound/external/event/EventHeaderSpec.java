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
			+ EventHeaderSpec.IDENTIFIER_REGEX, String.class),

	/**
	 * 
	 */
	EVENT_ID("EventID", "urn:event:" + EventHeaderSpec.UUID_REGEX, String.class),

	/**
	 * 
	 */
	CREATION_TIMESTAMP("CreationTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class),

	/**
	 * 
	 */
	EVENT_SOURCE_SYSTEM("EventSourceSystem", "urn:event-source:"
			+ EventHeaderSpec.IDENTIFIER_REGEX, String.class),

	/**
	 * 
	 */
	PROPAGATION_ID("PropagationID", "urn:event-propagation:"
			+ EventHeaderSpec.UUID_REGEX, String.class),

	/**
	 * 
	 */
	INFLOW_ID("InflowID", "urn:event-inflow:" + EventHeaderSpec.UUID_REGEX,
			String.class),

	/**
	 * 
	 */
	INFLOW_TIMESTAMP("InflowTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class),

	/**
	 * 
	 */
	PROCESSING_ID("ProcessingID", "urn:event-processing:"
			+ EventHeaderSpec.UUID_REGEX, String.class),

	/**
	 * 
	 */
	SEQUENCE_NUMBER("SequenceNumber", "\\d{1,3}", Integer.class),

	/**
	 * 
	 */
	INITIATION_TIMESTAMP("InitiationTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class),

	/**
	 * 
	 */
	COMPLETION_TIMESTAMP("CompletionTimestamp",
			EventHeaderSpec.ISO_8601_DATE_FORMAT_REGEX, Date.class),

	/**
	 * 
	 */
	PROCESSING_STATE("ProcessingState", "IN_PROGRESS|SUCCESSFUL|FAILED",
			ProcessingState.class);

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
	// Static
	// -------------------------------------------------------------------------

	static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public static EventHeaders newEventHeadersFrom(
			final Map<String, String> headers) throws IllegalArgumentException {
		Validate.notNull(headers, "headers");

		final Set<EventHeader<? extends Serializable>> eventHeaders = new HashSet<EventHeader<? extends Serializable>>(
				headers.size());
		for (final String headerName : headers.keySet()) {
			final EventHeaderSpec eventHeaderSpec = named(headerName);
			if (eventHeaderSpec != null) {
				eventHeaders.add(eventHeaderSpec.newEventHeaderFrom(headers
						.get(headerName)));
			}
		}

		return new EventHeaders(eventHeaders);
	}

	// ~~~~

	private static EventHeaderSpec named(final String aName) {
		for (final EventHeaderSpec candidate : values()) {
			if (candidate.isNamed(aName)) {
				return candidate;
			}
		}

		return null;
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final String headerName;

	private final Pattern legalValuePattern;

	private final Class<? extends Serializable> valueType;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private EventHeaderSpec(final String headerName,
			final String legalValuePattern,
			final Class<? extends Serializable> valueType) {
		this.headerName = headerName;
		this.legalValuePattern = Pattern.compile(legalValuePattern);
		this.valueType = valueType;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public String headerName() {
		return this.headerName;
	}

	public boolean isNamed(final String aName) {
		return this.headerName.equals(aName);
	}

	public String legalValuePattern() {
		return this.legalValuePattern.pattern();
	}

	public Class<? extends Serializable> valueType() {
		return this.valueType;
	}

	public <V extends Serializable> EventHeader<V> newEventHeaderFrom(
			final String valueAsString) throws IllegalArgumentException {
		Validate.notEmpty(valueAsString, "valueAsString");
		if (!this.legalValuePattern.matcher(valueAsString).matches()) {
			throw new IllegalArgumentException("The provided value ["
					+ valueAsString + "] does not match the regex ["
					+ this.legalValuePattern
					+ "] mandated by this MetaDatumType");
		}

		return newEventHeaderFromInternal(valueAsString);
	}

	// ~~~~~~~~

	private <V extends Serializable> EventHeader<V> newEventHeaderFromInternal(
			final String valueAsString) throws NumberFormatException,
			AssertionError, IllegalArgumentException {
		try {
			final V value = convert(valueAsString);

			return new EventHeader<V>(this, value);
		} catch (final ParseException e) {
			throw new IllegalArgumentException("Invalid date format ["
					+ valueAsString + "]: " + e.getMessage(), e);
		}
	}

	private <V extends Serializable> V convert(final String valueAsString)
			throws NumberFormatException, ParseException, AssertionError {
		final V value;
		if (this.valueType == String.class) {
			value = (V) valueAsString;
		} else if (this.valueType == Integer.class) {
			value = (V) Integer.valueOf(valueAsString);
		} else if (this.valueType == Date.class) {
			/*
			 * SimpleDateFormat is not thread safe. Usually, this instances of
			 * this class should be thread confined, i.e. accessed from only one
			 * thread. Therefore, promoting the SimpleDateFormat instance below
			 * to an instance variable should be safe. Yet you never know ...
			 */
			value = (V) new SimpleDateFormat(ISO_8601_DATE_FORMAT)
					.parse(valueAsString);
		} else if (this.valueType == ProcessingState.class) {
			value = (V) ProcessingState.valueOf(valueAsString);
		} else {
			throw new AssertionError();
		}

		return value;
	}
}
