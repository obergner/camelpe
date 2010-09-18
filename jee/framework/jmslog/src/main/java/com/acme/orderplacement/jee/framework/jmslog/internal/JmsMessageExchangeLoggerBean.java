/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal;

import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto;
import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageExchange;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageType;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchangeLoggerBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@PermitAll
@Stateless
@Local(JmsMessageExchangeLogger.class)
public class JmsMessageExchangeLoggerBean implements JmsMessageExchangeLogger {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * <p>
	 * Our {@link javax.persistence.EntityManager <em>JPA EntityManager</em>}.
	 * </p>
	 */
	@PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "jee.framework.jmsLogPU")
	private EntityManager entityManager;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger#logIncomingJmsMessageExchange(com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logIncomingJmsMessageExchange(
			final JmsMessageExchangeDto jmsMessageDto)
			throws IllegalArgumentException, NoResultException {
		Validate.notNull(jmsMessageDto, "jmsMessageDto");

		this.log.debug("About to log JMS message [GUID = {}] ...",
				jmsMessageDto.getGuid());

		final TypedQuery<JmsMessageType> jmsMessageTypeByName = this.entityManager
				.createNamedQuery(JmsMessageType.Queries.BY_NAME,
						JmsMessageType.class);
		jmsMessageTypeByName.setParameter("name", jmsMessageDto
				.getMessageType());
		final JmsMessageType referencedJmsMessageType = jmsMessageTypeByName
				.getSingleResult();

		final JmsMessageExchange jmsMessage = new JmsMessageExchange(
				jmsMessageDto.getGuid(), jmsMessageDto.getReceivedOn(),
				jmsMessageDto.getContent(), referencedJmsMessageType, Maps
						.transformValues(jmsMessageDto.getHeaders(),
								new Function<Object, String>() {
									@Override
									public String apply(final Object arg0) {
										return arg0 != null ? arg0.toString()
												: null;
									}
								}));
		this.entityManager.persist(jmsMessage);

		this.log.debug("JMS message [ID = {} |GUID = {}] successfully logged",
				jmsMessage.getId(), jmsMessageDto.getGuid());
	}

	/**
	 * @see com.acme.orderplacement.framework.jmslog.JmsMessageExchangeLogger#completeJmsMessageExchange(java.lang.String,java.lang.Exception)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void completeJmsMessageExchange(final String jmsMessageGuid,
			final Exception error) throws IllegalArgumentException,
			NoResultException {
		Validate.notNull(jmsMessageGuid, "jmsMessageGuid");
		final Date completedOn = new Date();

		this.log
				.debug(
						"About to complete JMS message exchange [GUID = {} | error = {}] ...",
						jmsMessageGuid, error == null ? null : error.getClass()
								.getName());

		final TypedQuery<JmsMessageExchange> jmsMessageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		final JmsMessageExchange jmsMessage = jmsMessageByGuid.setParameter(
				"guid", jmsMessageGuid).getSingleResult();

		jmsMessage
				.setProcessingState(error == null ? JmsMessageExchange.ProcessingState.SUCCESSFUL
						: JmsMessageExchange.ProcessingState.FAILED);
		jmsMessage.setError(error);
		jmsMessage.setCompletedOn(completedOn);

		this.log.debug(
				"JMS message exchange [GUID = {} | error = {}] completed",
				jmsMessageGuid, error == null ? null : error.getClass()
						.getName());
	}
}
