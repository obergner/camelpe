/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.framework.jmslog.JmsMessageDto;
import com.acme.orderplacement.framework.jmslog.JmsMessageLogger;
import com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessage;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class JmsMessageLoggerClientModeIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_JMS_MESSAGE_TYPE_NAME = "REGISTER_ITEM";

	private static final String NON_EXISTING_JMS_MESSAGE_TYPE_NAME = "nonExistingJmsMessageType";

	private static final String NON_EXISTING_JMS_MESSAGE_GUID = "nonExistingJmsMessageGuid";

	private JmsMessageLogger cachedJmsMessageLogger;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive deployment = ShrinkWrap.create("test.jar",
				JavaArchive.class).addPackages(true,
				Validate.class.getPackage(),
				JmsMessageLogger.class.getPackage(),
				JmsMessageLoggerBean.class.getPackage(),
				JmsMessage.class.getPackage()).addManifestResource(
				"META-INF/glassfish/persistence.xml",
				ArchivePaths.create("persistence.xml")).addManifestResource(
				"META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml"))
				.addManifestResource("META-INF/glassfish/sun-ejb-jar.xml",
						ArchivePaths.create("sun-ejb-jar.xml"));

		return deployment;
	}

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogJmsMessageRefusesToLogNullJmsMessage()
			throws NoResultException, IllegalArgumentException, NamingException {
		lookupJmsMessageLogger().logJmsMessage(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatJmsMessageRefusesToLogMessageReferencingANonExistingMessageType()
			throws Exception {
		try {
			final JmsMessageDto jmsMessageDto = new JmsMessageDto(
					NON_EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789",
					new Date(), "TEST", Collections.<String, Object> emptyMap());

			lookupJmsMessageLogger().logJmsMessage(jmsMessageDto);
		} catch (final EJBException e) {
			throw e.getCausedByException();
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageReferencingAnExistingMessageType()
			throws NoResultException, IllegalArgumentException, NamingException {
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-123456789", new Date(),
				"TEST", Collections.<String, Object> emptyMap());

		lookupJmsMessageLogger().logJmsMessage(jmsMessageDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#logJmsMessage(com.acme.orderplacement.framework.jmslog.JmsMessageDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test
	public final void assertThatLogJmsMessageLogsMessageHeaders()
			throws NoResultException, IllegalArgumentException, NamingException {
		final Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final JmsMessageDto jmsMessageDto = new JmsMessageDto(
				EXISTING_JMS_MESSAGE_TYPE_NAME, "UUID-345678912", new Date(),
				"TEST", headers);

		lookupJmsMessageLogger().logJmsMessage(jmsMessageDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToLogNullMessageId()
			throws IllegalArgumentException, NamingException {
		lookupJmsMessageLogger().completeJmsMessageExchange(null, true);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.jmslog.internal.JmsMessageLoggerBean#completeJmsMessageExchange(java.lang.Long, boolean)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatCompleteJmsMessageExchangeRefusesToCompleteNonExistingMessageExchange()
			throws Exception {
		try {
			lookupJmsMessageLogger().completeJmsMessageExchange(
					NON_EXISTING_JMS_MESSAGE_GUID, true);
		} catch (final EJBException e) {
			throw e.getCausedByException();
		}
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	private JmsMessageLogger lookupJmsMessageLogger() throws NamingException {
		if (this.cachedJmsMessageLogger == null) {
			this.cachedJmsMessageLogger = (JmsMessageLogger) new InitialContext()
					.lookup("java:global/test/JmsMessageLoggerBean");
		}

		return this.cachedJmsMessageLogger;
	}
}
