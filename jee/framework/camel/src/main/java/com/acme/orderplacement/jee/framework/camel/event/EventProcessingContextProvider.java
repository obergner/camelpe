/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContextHolder;
import com.obergner.acme.orderplacement.integration.inbound.external.event.ImmutableEventProcessingContext;

/**
 * <p>
 * TODO: Insert short summary for EventProcessingContextProvider
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class EventProcessingContextProvider implements Processor {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private EventProcessingContextHolder eventProcessingContextHolder;

	// -------------------------------------------------------------------------
	// org.apache.camel.Processor
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		this.log
				.trace(
						"About to populate EventProcessingContext from exchange [{}] ...",
						exchange);

		final EventProcessingContext eventProcessingContext = eventProcessingContextFrom(exchange
				.getIn());
		this.eventProcessingContextHolder.initialize(eventProcessingContext);

		this.log.trace("Successfully populated EventProcessingContext [{}]",
				eventProcessingContext);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private EventProcessingContext eventProcessingContextFrom(
			final Message message) {
		final Map<String, String> eventHeaders = EventHeaderToJmsHeaderMapping
				.eventHeadersFrom(message);

		return ImmutableEventProcessingContext.BUILDER.buildFrom(eventHeaders);
	}

	private static class EventHeaderToJmsHeaderMapping {

		// -------------------------------------------------------------------------
		// Static
		// -------------------------------------------------------------------------

		private static final EventHeaderToJmsHeaderMapping EVENT_TYPE_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.EVENT_TYPE, "EventType");

		private static final EventHeaderToJmsHeaderMapping EVENT_ID_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.EVENT_ID, "EventID");

		private static final EventHeaderToJmsHeaderMapping CREATION_TIMESTAMP_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.CREATION_TIMESTAMP, "CreationTimestamp");

		private static final EventHeaderToJmsHeaderMapping EVENT_SOURCE_SYSTEM_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.EVENT_SOURCE_SYSTEM, "EventSourceSystem");

		private static final EventHeaderToJmsHeaderMapping INFLOW_ID_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.INFLOW_ID, "InflowID");

		private static final EventHeaderToJmsHeaderMapping INFLOW_TIMESTAMP_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.INFLOW_TIMESTAMP, "InflowTimestamp");

		private static final EventHeaderToJmsHeaderMapping SEQUENCE_NUMBER_MAPPING = new EventHeaderToJmsHeaderMapping(
				EventHeaderSpec.SEQUENCE_NUMBER, "JMSXDeliveryCount");

		private static final Set<EventHeaderToJmsHeaderMapping> ALL = ImmutableSet
				.of(EVENT_TYPE_MAPPING, EVENT_ID_MAPPING,
						CREATION_TIMESTAMP_MAPPING,
						EVENT_SOURCE_SYSTEM_MAPPING, INFLOW_ID_MAPPING,
						INFLOW_TIMESTAMP_MAPPING, SEQUENCE_NUMBER_MAPPING);

		private static final Map<String, EventHeaderToJmsHeaderMapping> JMS_HEADER_TO_JMS_HEADER_MAPPING = Maps
				.uniqueIndex(ALL,
						new Function<EventHeaderToJmsHeaderMapping, String>() {

							@Override
							public String apply(
									final EventHeaderToJmsHeaderMapping from) {
								return from.jmsHeaderName;
							}
						});

		static Map<String, String> eventHeadersFrom(final Message message) {
			final Map<String, String> eventHeaders = new HashMap<String, String>();
			for (final String jmsHeader : JMS_HEADER_TO_JMS_HEADER_MAPPING
					.keySet()) {
				final EventHeaderToJmsHeaderMapping mapping = JMS_HEADER_TO_JMS_HEADER_MAPPING
						.get(jmsHeader);
				final Object jmsHeaderValue = message.getHeader(jmsHeader);
				if (jmsHeaderValue != null) {
					eventHeaders.put(mapping.eventHeaderName, String
							.valueOf(jmsHeaderValue));
				}
			}

			return eventHeaders;
		}

		// -------------------------------------------------------------------------
		// Fields
		// -------------------------------------------------------------------------

		final String eventHeaderName;

		final String jmsHeaderName;

		// -------------------------------------------------------------------------
		// Constructors
		// -------------------------------------------------------------------------

		private EventHeaderToJmsHeaderMapping(
				final EventHeaderSpec eventHeaderSpec,
				final String jmsHeaderName) {
			this.eventHeaderName = eventHeaderSpec.headerName();
			this.jmsHeaderName = jmsHeaderName;
		}
	}
}
