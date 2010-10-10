/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext;

/**
 * <p>
 * TODO: Insert short summary for MessageExchangeCompletionLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MessageExchangeCompletionLogging
@ApplicationScoped
public class MessageExchangeCompletionLoggingProcessor extends
		AbstractJmsMessageExchangeLoggingProcessor {

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public MessageExchangeCompletionLoggingProcessor() {
		// Intentionally left blank
	}

	/**
	 * Used for testing.
	 * 
	 * @param cachedJmsMessageLogger
	 */
	MessageExchangeCompletionLoggingProcessor(
			final JmsMessageExchangeLogger cachedJmsMessageLogger,
			final EventProcessingContext eventProcessingContext) {
		super(cachedJmsMessageLogger, eventProcessingContext);
	}

	// -------------------------------------------------------------------------
	// org.apache.camel.Processor
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		final Exception error = (Exception) exchange
				.getProperty(Exchange.EXCEPTION_CAUGHT);
		final boolean successful = (error == null);
		try {
			final String messageGuid = messageIdFrom(exchange);
			this.log
					.trace(
							"About to set completion status of message exchange [Message-GUID = {}] to [successful = {}] in database ...",
							messageGuid, Boolean.valueOf(successful));

			if (successful) {
				eventProcessingContext().succeed();
			} else {
				eventProcessingContext().fail(error);
			}

			jmsMessageLogger().completeJmsMessageExchange(messageGuid, error);

			this.log
					.trace(
							"Completion status of message exchange [Message-GUID = {}] set to [successful = {}] in database",
							messageGuid, Boolean.valueOf(successful));
		} catch (final Exception e) {
			this.log.error(
					"Caught exception while attempting to complete [successful = "
							+ successful + "] message exchange [" + exchange
							+ "]: " + e.getMessage(), e);
		}
	}
}
