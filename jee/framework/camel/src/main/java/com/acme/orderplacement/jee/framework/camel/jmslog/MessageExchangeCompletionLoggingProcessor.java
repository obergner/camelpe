/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;

/**
 * <p>
 * TODO: Insert short summary for MessageExchangeCompletionLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class MessageExchangeCompletionLoggingProcessor extends
		AbstractJmsMessageExchangeLoggingProcessor {

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

			jmsMessageLogger().completeJmsMessageExchange(messageGuid,
					successful);

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
