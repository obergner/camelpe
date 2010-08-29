/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import org.apache.camel.Exchange;

/**
 * <p>
 * TODO: Insert short summary for MessageExchangeCompletionLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageExchangeCompletionLoggingProcessor extends
		AbstractJmsMessageExchangeLoggingProcessor {

	private final boolean successful;

	/**
	 * @param successful
	 */
	public MessageExchangeCompletionLoggingProcessor(final boolean successful) {
		super();
		this.successful = successful;
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
							"About to set completion status of message exchange [Message-GUID = {}] to [successful = {}] in database ...",
							messageGuid, Boolean.valueOf(this.successful));

			jmsMessageLogger().completeJmsMessageExchange(messageGuid,
					this.successful);

			this.log
					.trace(
							"Completion status of message exchange [Message-GUID = {}] set to [successful = {}] in database",
							messageGuid, Boolean.valueOf(this.successful));
		} catch (final Exception e) {
			this.log.error(
					"Caught exception while attempting to complete [successful = "
							+ this.successful + "] message exchange ["
							+ exchange + "]: " + e.getMessage(), e);
		}
	}
}
