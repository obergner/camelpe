/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto;
import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger;

/**
 * <p>
 * TODO: Insert short summary for IncomingMessageExchangeLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class IncomingMessageExchangeLoggingProcessor extends
		AbstractJmsMessageExchangeLoggingProcessor {

	private static final String MESSAGE_TYPE = "REGISTER_ITEM";

	public IncomingMessageExchangeLoggingProcessor() {
		// Intentionally left blank
	}

	/**
	 * Used for testing.
	 * 
	 * @param cachedJmsMessageLogger
	 */
	IncomingMessageExchangeLoggingProcessor(
			final JmsMessageExchangeLogger cachedJmsMessageLogger) {
		super(cachedJmsMessageLogger);
	}

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		try {
			final String messageGuid = messageIdFrom(exchange);
			this.log
					.trace(
							"About to log incoming message [type = {}|GUID = {}] to database ...",
							MESSAGE_TYPE, messageGuid);

			// FIXME: MessageType is currently hardcoded. Find a better
			// solution.
			jmsMessageLogger().logIncomingJmsMessageExchange(
					new JmsMessageExchangeDto(MESSAGE_TYPE, messageGuid, new Date(),
							exchange.getIn().getBody(String.class), exchange
									.getIn().getHeaders()));

			this.log
					.trace(
							"Incoming message [type = {}|GUID = {}] logged to database",
							MESSAGE_TYPE, messageGuid);
		} catch (final Exception e) {
			this.log.error(
					"Caught exception while attempting to log message exchange ["
							+ exchange + "]: " + e.getMessage(), e);
		}
	}
}
