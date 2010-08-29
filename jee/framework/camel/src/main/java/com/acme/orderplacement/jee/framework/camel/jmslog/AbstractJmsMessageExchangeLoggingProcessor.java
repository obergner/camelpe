/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageLogger;

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

	private static final String JMS_MESSAGE_LOGGER_BEAN_JNDI_NAME = "orderplacement.jee.ear-1.0-SNAPSHOT/JmsMessageLoggerBean/local";

	private static final String JMS_MESSAGE_ID_HEADER = "JMSMessageID";

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private JmsMessageLogger cachedJmsMessageLogger;

	protected AbstractJmsMessageExchangeLoggingProcessor() {
		// Intentionally left blank
	}

	AbstractJmsMessageExchangeLoggingProcessor(
			final JmsMessageLogger jmsMessageLogger) {
		Validate.notNull(jmsMessageLogger, "jmsMessageLogger");
		this.cachedJmsMessageLogger = jmsMessageLogger;
	}

	protected synchronized JmsMessageLogger jmsMessageLogger()
			throws NamingException {
		if (this.cachedJmsMessageLogger == null) {
			final InitialContext ic = new InitialContext();
			this.cachedJmsMessageLogger = (JmsMessageLogger) ic
					.lookup(JMS_MESSAGE_LOGGER_BEAN_JNDI_NAME);
		}

		return this.cachedJmsMessageLogger;
	}

	protected String messageIdFrom(final Exchange exchange) {

		return exchange.getIn().getHeader(JMS_MESSAGE_ID_HEADER, String.class);
	}
}