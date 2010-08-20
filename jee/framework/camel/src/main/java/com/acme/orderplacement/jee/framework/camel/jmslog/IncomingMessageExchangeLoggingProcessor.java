/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import java.util.Date;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageDto;

/**
 * <p>
 * TODO: Insert short summary for IncomingMessageExchangeLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class IncomingMessageExchangeLoggingProcessor implements Processor {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@EndpointInject(uri = "ejb:orderplacement.jee.ear-1.0-SNAPSHOT/JmsMessageLoggerBean/local?method=logJmsMessage")
	private ProducerTemplate jmsMessageLoggerEndpoint;

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		this.log.trace(
				"About to log incoming messange exchange [{}] to database ...",
				exchange);

		// FIXME: MessageType is currently hardcoded. Find a better solution.
		this.jmsMessageLoggerEndpoint.sendBody(new JmsMessageDto(
				"REGISTER_ITEM", exchange.getExchangeId(), new Date(), exchange
						.getIn(String.class), exchange.getIn().getHeaders()));

		this.log.trace("Incoming messange exchange [{}] logged to database",
				exchange);
	}
}
