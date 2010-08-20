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

import com.acme.orderplacement.framework.jmslog.JmsMessageDto;
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

	private static final String NON_EXISTING_JMS_MESSAGE_GUID = "nonExistingJmsMessageGuid";

	private static final String EXISTING_JMS_MESSAGE_GUID = "UUID-123-67-8997-98";

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
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogJmsMessageRefusesToLogNullJmsMessage() {
		this.classUnderTest.logJmsMessage(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatJmsMessageRefusesToLogMessageReferencingANonExistingMessageType() {
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				NON_EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789",
				new Date(), "TEST", Collections.<String, Object> emptyMap());

		this.classUnderTest.logJmsMessage(jmsMessageDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageReferencingAnExistingMessageType() {
		final String jmsMessageGuid = "UUID-123456789";
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, jmsMessageGuid, new Date(),
				"TEST", Collections.<String, Object> emptyMap());

		this.classUnderTest.logJmsMessage(jmsMessageDto);

		final JmsMessage persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessage.Queries.BY_GUID, JmsMessage.class)
				.setParameter("guid", jmsMessageGuid).getSingleResult();
		assertNotNull("logJmsMessage(" + jmsMessageDto
				+ ") did NOT persist the passed on JMS message",
				persistedJmsMessage);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageHeaders() {
		final Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final String jmsMessageGuid = "UUID-123456789";
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, jmsMessageGuid, new Date(),
				"TEST", headers);

		this.classUnderTest.logJmsMessage(jmsMessageDto);

		final JmsMessage persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessage.Queries.BY_GUID, JmsMessage.class)
				.setParameter("guid", jmsMessageGuid).getSingleResult();
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
	@Test(expected = NoResultException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToCompleteNonExistingMessageExchange() {
		this.classUnderTest.completeJmsMessageExchange(
				NON_EXISTING_JMS_MESSAGE_GUID, true);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void assertThatCompleteJmsMessageExchangeSuccessfullySetsCorrectProcessingState() {
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, true);

		final JmsMessage persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessage.Queries.BY_GUID, JmsMessage.class)
				.setParameter("guid", EXISTING_JMS_MESSAGE_GUID)
				.getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
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
		this.classUnderTest.completeJmsMessageExchange(
				EXISTING_JMS_MESSAGE_GUID, false);

		final JmsMessage persistedJmsMessage = this.entityManager
				.createNamedQuery(JmsMessage.Queries.BY_GUID, JmsMessage.class)
				.setParameter("guid", EXISTING_JMS_MESSAGE_GUID)
				.getSingleResult();
		assertEquals("completeJmsMessageExchange(" + EXISTING_JMS_MESSAGE_GUID
				+ ", false) did not set correct processing state",
				JmsMessage.ProcessingState.FAILED, persistedJmsMessage
						.getProcessingState());
	}
}
