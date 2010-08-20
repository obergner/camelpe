/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal;

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

import com.acme.orderplacement.framework.jmslog.JmsMessageDto;
import com.acme.orderplacement.framework.jmslog.JmsMessageLogger;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessage;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageType;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageLoggerBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Stateless
@Local(JmsMessageLogger.class)
public class JmsMessageLoggerBean implements JmsMessageLogger {

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
	 * @see com.acme.orderplacement.framework.jmslog.JmsMessageLogger#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logJmsMessage(final JmsMessageDto jmsMessageDto)
			throws IllegalArgumentException, NoResultException {
		Validate.notNull(jmsMessageDto, "jmsMessageDto");

		this.log.debug("About to log JMS message [{}] ...", jmsMessageDto);

		final TypedQuery<JmsMessageType> jmsMessageTypeByName = this.entityManager
				.createNamedQuery(JmsMessageType.Queries.BY_NAME,
						JmsMessageType.class);
		jmsMessageTypeByName.setParameter("name", jmsMessageDto
				.getMessageType());
		final JmsMessageType referencedJmsMessageType = jmsMessageTypeByName
				.getSingleResult();

		final JmsMessage jmsMessage = new JmsMessage(jmsMessageDto.getGuid(),
				jmsMessageDto.getReceivedOn(), jmsMessageDto.getContent(),
				referencedJmsMessageType, Maps.transformValues(jmsMessageDto
						.getHeaders(), new Function<Object, String>() {
					@Override
					public String apply(final Object arg0) {
						return arg0.toString();
					}
				}));
		this.entityManager.persist(jmsMessage);

		this.log.debug("JMS message [ID = {} | {}] successfully logged",
				jmsMessage.getId(), jmsMessageDto);
	}

	/**
	 * @see com.acme.orderplacement.framework.jmslog.JmsMessageLogger#completeJmsMessageExchange(java.lang.String,boolean)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void completeJmsMessageExchange(final String jmsMessageGuid,
			final boolean successful) throws IllegalArgumentException,
			NoResultException {
		Validate.notNull(jmsMessageGuid, "jmsMessageGuid");

		this.log
				.debug(
						"About to complete JMS message exchange [GUID = {} | successful = {}] ...",
						jmsMessageGuid, Boolean.valueOf(successful));

		final TypedQuery<JmsMessage> jmsMessageByGuid = this.entityManager
				.createNamedQuery(JmsMessage.Queries.BY_GUID, JmsMessage.class);
		final JmsMessage jmsMessage = jmsMessageByGuid.setParameter("guid",
				jmsMessageGuid).getSingleResult();

		jmsMessage
				.setProcessingState(successful ? JmsMessage.ProcessingState.SUCCESSFUL
						: JmsMessage.ProcessingState.FAILED);

		this.log
				.debug(
						"JMS message exchange [GUID = {} | successful = {}] completed successfully",
						jmsMessageGuid, Boolean.valueOf(successful));
	}
}
