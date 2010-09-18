/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;

import com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageExchange;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchangeLoggerBeanIntegrationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("JmsMessageExchangeLoggerBeanIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "jee.framework.hsqldb.jmsLogPU")
public class JmsMessageExchangeLoggerBeanIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_JMS_MESSAGE_TYPE_NAME = "REGISTER_ITEM";

	private static final String NON_EXISTING_JMS_MESSAGE_TYPE_NAME = "nonExistingJmsMessageType";

	private static final String NON_EXISTING_JMS_MESSAGE_GUID = "nonExistingJmsMessageGuid";

	private static final String EXISTING_JMS_MESSAGE_GUID = "UUID-123-67-8997-98";

	@TestedObject
	private JmsMessageExchangeLoggerBean classUnderTest;

	@InjectIntoByType
	@PersistenceContext(unitName = "jee.framework.jmsLogPU")
	private EntityManager entityManager;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#logIncomingJmsMessageExchange(com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogJmsMessageRefusesToLogNullJmsMessage() {
		this.classUnderTest.logIncomingJmsMessageExchange(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#logIncomingJmsMessageExchange(com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatJmsMessageRefusesToLogMessageReferencingANonExistingMessageType() {
		final JmsMessageExchangeDto jmsMessageDto = new JmsMessageExchangeDto(
				NON_EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789",
				new Date(), "TEST", Collections.<String, Object> emptyMap());

		this.classUnderTest.logIncomingJmsMessageExchange(jmsMessageDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#logIncomingJmsMessageExchange(com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageReferencingAnExistingMessageType() {
		final String jmsMessageGuid = "UUID-123456789";
		final JmsMessageExchangeDto jmsMessageDto = new JmsMessageExchangeDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, jmsMessageGuid, new Date(),
				"TEST", Collections.<String, Object> emptyMap());

		this.classUnderTest.logIncomingJmsMessageExchange(jmsMessageDto);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						jmsMessageGuid).getSingleResult();
		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") did NOT persist the passed on JMS message",
				persistedJmsMessage);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#logIncomingJmsMessageExchange(com.acme.orderplacement.framework.jmslog.JmsMessageExchangeDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageHeaders() {
		final Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final String jmsMessageGuid = "UUID-123456789";
		final JmsMessageExchangeDto jmsMessageDto = new JmsMessageExchangeDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, jmsMessageGuid, new Date(),
				"TEST", headers);

		this.classUnderTest.logIncomingJmsMessageExchange(jmsMessageDto);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						jmsMessageGuid).getSingleResult();
		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") did NOT persist the passed on JMS message",
				persistedJmsMessage);
		assertEquals(
				"logJmsMessage("
						+ jmsMessageDto
						+ ") did NOT persist the headers associated with the JMS message passed in",
				2, persistedJmsMessage.getHeaders().size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToLogNullMessageId() {
		this.classUnderTest.completeJmsMessageExchange(null, null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToCompleteNonExistingMessageExchange() {
		this.classUnderTest.completeJmsMessageExchange(
				NON_EXISTING_JMS_MESSAGE_GUID, null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeSuccessfullyPersistsCompletionTime() {
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, null);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertNotNull("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", null) did not persist time of completion",
				persistedJmsMessage.getCompletedOn());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeSuccessfullySetsCorrectProcessingState() {
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, null);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", null) did not set correct processing state",
				JmsMessageExchange.ProcessingState.SUCCESSFUL,
				persistedJmsMessage.getProcessingState());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeWithErrorSetsCorrectProcessingState() {
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, new Exception());

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", new Exception()) did not set correct processing state",
				JmsMessageExchange.ProcessingState.FAILED, persistedJmsMessage
						.getProcessingState());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeWithErrorPersistsCompletionTime() {
		final Exception error = new IllegalArgumentException();
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, error);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertNotNull("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", " + error + ") did not persist time of completion",
				persistedJmsMessage.getCompletedOn());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeWithErrorPersistsErrorType() {
		final Exception error = new IllegalArgumentException();
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, error);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", " + error + ") did not persist error type", error
				.getClass().getName(), persistedJmsMessage.getError()
				.getErrorType());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeWithErrorPersistsErrorMessage() {
		final Exception error = new IllegalArgumentException(
				"TEST THAT ERROR MESSAGE IS PERSISTED");
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, error);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", " + error + ") did not persist error message", error
				.getMessage(), persistedJmsMessage.getError().getErrorMessage());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeWithErrorPersistsErrorStackTrace() {
		final Exception error = new IllegalArgumentException();
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, error);

		final JmsMessageExchange persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class).setParameter("guid",
						EXISTING_JMS_MESSAGE_GUID).getSingleResult();
		assertNotNull("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", " + error + ") did not persist error stack trace",
				persistedJmsMessage.getError().getErrorStackTrace());
	}
}
