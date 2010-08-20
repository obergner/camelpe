/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageLogger;

/**
 * <p>
 * TODO: Insert short summary for MessageExchangeCompletionLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageExchangeCompletionLoggingProcessor implements Processor {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@EndpointInject(uri = "ejb:orderplacement.jee.ear-1.0-SNAPSHOT/JmsMessageLoggerBean/local?method=completeJmsMessageExchange")
	private JmsMessageLogger jmsMessageLoggerEndpoint;

	private final boolean successful;

	/**
	 * @param successful
	 */
	public MessageExchangeCompletionLoggingProcessor(final boolean successful) {
		this.successful = successful;
	}

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		this.log.trace(
				"About to log incoming messange exchange [{}] to database ...",
				exchange);

		this.jmsMessageLoggerEndpoint.completeJmsMessageExchange(exchange
				.getExchangeId(), this.successful);

		this.log.trace("Incoming messange exchange [{}] logged to database",
				exchange);
	}

}
