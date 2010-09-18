/**
 * 
 */
package com.acme.orderplacement.framework.jmslog;

import javax.persistence.NoResultException;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchangeLogger
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface JmsMessageExchangeLogger {

	void logIncomingJmsMessageExchange(final JmsMessageExchangeDto jmsMessageDto)
			throws IllegalArgumentException, NoResultException;

	void completeJmsMessageExchange(final String jmsMessageGuid,
			final Exception error) throws IllegalArgumentException,
			NoResultException;
}
