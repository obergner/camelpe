/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto;
import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext;

/**
 * <p>
 * TODO: Insert short summary for IncomingMessageExchangeLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@IncomingMessageExchangeLogging
@ApplicationScoped
public class IncomingMessageExchangeLoggingProcessor extends
		AbstractJmsMessageExchangeLoggingProcessor {

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public IncomingMessageExchangeLoggingProcessor() {
		// Intentionally left blank
	}

	/**
	 * Used for testing.
	 * 
	 * @param jmsMessageLogger
	 */
	IncomingMessageExchangeLoggingProcessor(
			final JmsMessageExchangeLogger jmsMessageLogger,
			final EventProcessingContext eventProcessingContext) {
		super(jmsMessageLogger, eventProcessingContext);
	}

	// -------------------------------------------------------------------------
	// org.apache.camel.Processor
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		try {
			final String messageGuid = messageIdFrom(exchange);
			final String eventType = eventProcessingContext().getEventType();
			this.log
					.trace(
							"About to log incoming message [type = {}|GUID = {}] to database ...",
							eventType, messageGuid);

			jmsMessageLogger().logIncomingJmsMessageExchange(
					new JmsMessageExchangeDto(eventType, messageGuid,
							new Date(), exchange.getIn().getBody(String.class),
							exchange.getIn().getHeaders()));

			this.log
					.trace(
							"Incoming message [type = {}|GUID = {}] logged to database",
							eventType, messageGuid);
		} catch (final Exception e) {
			this.log.error(
					"Caught exception while attempting to log message exchange ["
							+ exchange + "]: " + e.getMessage(), e);
		}
	}
}
