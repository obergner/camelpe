/**
 * 
 */
package com.acme.orderplacement.framework.jmslog;

import javax.persistence.NoResultException;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageLogger
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface JmsMessageLogger {

	void logJmsMessage(final JmsMessageDto jmsMessageDto)
			throws IllegalArgumentException, NoResultException;

	void completeJmsMessageExchange(final String jmsMessageGuid,
			final boolean successful) throws IllegalArgumentException,
			NoResultException;
}
