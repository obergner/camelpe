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

import com.acme.orderplacement.jee.framework.jmslog.JmsMessageDto;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessage;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageLoggerBeanIntegrationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("JmsMessageLoggerBeanIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "jee.framework.hsqldb.jmsLogPU")
public class JmsMessageLoggerBeanIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_JMS_MESSAGE_TYPE_NAME = "REGISTER_ITEM";

	private static final String NON_EXISTING_JMS_MESSAGE_TYPE_NAME = "nonExistingJmsMessageType";

	private static final Long NON_EXISTING_JMS_MESSAGE_ID = Long.valueOf(666L);

	private static final Long EXISTING_JMS_MESSAGE_ID = Long.valueOf(-1L);

	@TestedObject
	private JmsMessageLoggerBean classUnderTest;

	@InjectIntoByType
	@PersistenceContext(unitName = "jee.framework.jmsLogPU")
	private EntityManager entityManager;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.jee.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogJmsMessageRefusesToLogNullJmsMessage() {
		this.classUnderTest.logJmsMessage(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.jee.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatJmsMessageRefusesToLogMessageReferencingANonExistingMessageType() {
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				NON_EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789",
				new Date(), "TEST", Collections.<String, String> emptyMap());

		this.classUnderTest.logJmsMessage(jmsMessageDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.jee.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageReferencingAnExistingMessageType() {
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789", new Date(),
				"TEST", Collections.<String, String> emptyMap());

		final Long requestId = this.classUnderTest.logJmsMessage(jmsMessageDto);

		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") returned a NULL request id", requestId);

		final JmsMessage persistedJmsMessage = this.entityManager.find(
				JmsMessage.class, requestId);
		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") did NOT persist the passed on JMS message",
				persistedJmsMessage);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.jee.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageHeaders() {
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789", new Date(),
				"TEST", headers);

		final Long requestId = this.classUnderTest.logJmsMessage(jmsMessageDto);

		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") returned a NULL request id", requestId);

		final JmsMessage persistedJmsMessage = this.entityManager.find(
				JmsMessage.class, requestId);
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
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToLogNullMessageId() {
		this.classUnderTest.completeJmsMessageExchange(null, true);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToCompleteNonExistingMessageExchange() {
		this.classUnderTest.completeJmsMessageExchange(
				NON_EXISTING_JMS_MESSAGE_ID, true);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeSuccessfullySetsCorrectProcessingState() {
		this.classUnderTest.completeJmsMessageExchange(EXISTING_JMS_MESSAGE_ID,
				true);

		final JmsMessage persistedJmsMessage = this.entityManager.find(
				JmsMessage.class, EXISTING_JMS_MESSAGE_ID);
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_ID
				+ ", true) did not set correct processing state",
				JmsMessage.ProcessingState.SUCCESSFUL, persistedJmsMessage
						.getProcessingState());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeUnsuccessfullySetsCorrectProcessingState() {
		this.classUnderTest.completeJmsMessageExchange(EXISTING_JMS_MESSAGE_ID,
				false);

		final JmsMessage persistedJmsMessage = this.entityManager.find(
				JmsMessage.class, EXISTING_JMS_MESSAGE_ID);
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_ID
				+ ", false) did not set correct processing state",
				JmsMessage.ProcessingState.FAILED, persistedJmsMessage
						.getProcessingState());
	}
}
