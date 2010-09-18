/**
 * 
 */
package com.acme.orderplacement.jee.framework.camel.jmslog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;

import com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageExchangeLoggerBean;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageExchange;

/**
 * <p>
 * TODO: Insert short summary for
 * MessageExchangeCompletionLoggingProcessorIntegrationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("MessageExchangeCompletionLoggingProcessorIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "jee.framework.hsqldb.jmsLogPU")
public class MessageExchangeCompletionLoggingProcessorIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_MESSAGE_GUID = "UUID-123-67-8997-98";

	@TestedObject
	private JmsMessageExchangeLoggerBean messageLoggerBean;

	@InjectIntoByType
	@PersistenceContext(unitName = "jee.framework.jmsLogPU")
	private EntityManager entityManager;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithoutErrorSetsCorrectProcessingState()
			throws Exception {
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertEquals("process(" + exchange
				+ ") did NOT set correct processing state",
				JmsMessageExchange.ProcessingState.SUCCESSFUL, storedMessage
						.getProcessingState());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithoutErrorSetsCompletionTime()
			throws Exception {
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertNotNull("process(" + exchange
				+ ") did NOT set time of completion", storedMessage
				.getCompletedOn());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithErrorSetsCorrectProcessingState()
			throws Exception {
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);
		exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertEquals("process(" + exchange
				+ ") did NOT set correct processing state",
				JmsMessageExchange.ProcessingState.FAILED, storedMessage
						.getProcessingState());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithErrorSetsCompletionTime()
			throws Exception {
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);
		exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertNotNull("process(" + exchange
				+ ") did NOT set time of completion", storedMessage
				.getCompletedOn());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithErrorSetsCorrectErrorType()
			throws Exception {
		final Exception error = new UnsupportedOperationException();
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);
		exchange.setProperty(Exchange.EXCEPTION_CAUGHT, error);

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertEquals(
				"process(" + exchange + ") did NOT set correct error type",
				error.getClass().getName(), storedMessage.getError()
						.getErrorType());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithErrorSetsCorrectErrorMessage()
			throws Exception {
		final Exception error = new UnsupportedOperationException(
				"TEST FOR ERROR MESSAGE");
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);
		exchange.setProperty(Exchange.EXCEPTION_CAUGHT, error);

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertEquals("process(" + exchange
				+ ") did NOT set correct error message", error.getMessage(),
				storedMessage.getError().getErrorMessage());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.camel.jmslog.MessageExchangeCompletionLoggingProcessor#process(org.apache.camel.Exchange)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessWithErrorSetsErrorStackTrace()
			throws Exception {
		final Exception error = new UnsupportedOperationException();
		final String body = "TEST";

		final DefaultMessage inMessage = new DefaultMessage();
		inMessage.setHeader("JMSMessageID", EXISTING_MESSAGE_GUID);
		inMessage.setBody(body, String.class);

		final Exchange exchange = new DefaultExchange(new DefaultCamelContext());
		exchange.setIn(inMessage);
		exchange.setProperty(Exchange.EXCEPTION_CAUGHT, error);

		final MessageExchangeCompletionLoggingProcessor classUnderTest = new MessageExchangeCompletionLoggingProcessor(
				this.messageLoggerBean);
		classUnderTest.process(exchange);

		final TypedQuery<JmsMessageExchange> messageByGuid = this.entityManager
				.createNamedQuery(JmsMessageExchange.Queries.BY_GUID,
						JmsMessageExchange.class);
		messageByGuid.setParameter("guid", EXISTING_MESSAGE_GUID);
		final JmsMessageExchange storedMessage = messageByGuid
				.getSingleResult();
		assertNotNull(
				"process(" + exchange + ") did NOT set error stack trace",
				storedMessage.getError().getErrorStackTrace());
	}
}
