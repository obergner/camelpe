/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventProcessingContext;

/**
 * <p>
 * TODO: Insert short summary for AbstractJmsMessageExchangeLoggingProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractJmsMessageExchangeLoggingProcessor implements
		Processor {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final String JMS_MESSAGE_ID_HEADER = "JMSMessageID";

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private JmsMessageExchangeLogger jmsMessageLogger;

	@Inject
	private EventProcessingContext eventProcessingContext;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected AbstractJmsMessageExchangeLoggingProcessor() {
		// Intentionally left blank
	}

	AbstractJmsMessageExchangeLoggingProcessor(
			final JmsMessageExchangeLogger jmsMessageLogger,
			final EventProcessingContext eventProcessingContext) {
		Validate.notNull(jmsMessageLogger, "jmsMessageLogger");
		Validate.notNull(eventProcessingContext, "eventProcessingContext");
		this.jmsMessageLogger = jmsMessageLogger;
		this.eventProcessingContext = eventProcessingContext;
	}

	// -------------------------------------------------------------------------
	// Protected
	// -------------------------------------------------------------------------

	protected JmsMessageExchangeLogger jmsMessageLogger() {

		return this.jmsMessageLogger;
	}

	protected EventProcessingContext eventProcessingContext() {
		return this.eventProcessingContext;
	}

	protected String messageIdFrom(final Exchange exchange) {

		return exchange.getIn().getHeader(JMS_MESSAGE_ID_HEADER, String.class);
	}
}