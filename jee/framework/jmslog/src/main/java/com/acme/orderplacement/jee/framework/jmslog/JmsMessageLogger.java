/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog;

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

	Long logJmsMessage(final JmsMessageDto jmsMessageDto)
			throws IllegalArgumentException, NoResultException;

	void completeJmsMessageExchange(final Long jmsMessageId,
			final boolean successful) throws IllegalArgumentException;
}
