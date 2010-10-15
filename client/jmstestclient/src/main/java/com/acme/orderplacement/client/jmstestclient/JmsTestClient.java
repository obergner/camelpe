/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obergner.acme.orderplacement.integration.inbound.external.ExternalEventSourceSystems;
import com.obergner.acme.orderplacement.integration.inbound.external.ExternalEventTypes;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec;

/**
 * <p>
 * TODO: Insert short summary for JmsTestClient
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JmsTestClient {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final String ITEM_CREATED_EVENT_TOPIC_JNDI = "/topic/orderplacement/ItemCreatedEventsTopic";

	// -------------------------------------------------------------------------
	// MAIN
	// -------------------------------------------------------------------------

	public static void main(final String[] args) throws NamingException,
			JMSException, IOException {
		new JmsTestClient().run();
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public void run() throws NamingException, JMSException, IOException {
		this.log.info("Starting JMS test client ...");

		final Context ctx = establishInitialContext();

		final TopicConnectionFactory tcf = lookupTopicConnectionFactory(ctx);

		final Destination outDest = lookupTopic(ctx);

		final Connection connection = establishTopicConnection(tcf);

		final Session session = establishSession(connection);

		final MessageProducer destSender = session.createProducer(outDest);

		sendMessage(session, destSender);

		destSender.close();
		session.close();
		connection.close();

		this.log.info("JMS test client finished");
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	/**
	 * @return
	 * @throws NamingException
	 */
	private Context establishInitialContext() throws NamingException {
		this.log.info("Establishing initial context ...");
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		env
				.put(Context.URL_PKG_PREFIXES,
						"org.jboss.naming:org.jnp.interfaces");
		final Context ctx = new InitialContext(env);
		this.log.info("Initial context [{}] established", ctx);

		return ctx;
	}

	/**
	 * @param ctx
	 * @return
	 * @throws NamingException
	 */
	private TopicConnectionFactory lookupTopicConnectionFactory(
			final Context ctx) throws NamingException {
		this.log.info("Looking up (Topic)ConnectionFactory ...");
		final TopicConnectionFactory tcf = (TopicConnectionFactory) ctx
				.lookup("ConnectionFactory");
		this.log.info("Obtained (Topic)ConnectionFactory [{}]", tcf);

		return tcf;
	}

	/**
	 * @param ctx
	 * @return
	 * @throws NamingException
	 */
	private Destination lookupTopic(final Context ctx) throws NamingException {
		this.log.info("Looking up Topic [JNDI name = {}] ...",
				ITEM_CREATED_EVENT_TOPIC_JNDI);

		final Destination outDest = (Destination) ctx
				.lookup(ITEM_CREATED_EVENT_TOPIC_JNDI);
		this.log.info("Obtained Topic [{}]", outDest);

		return outDest;
	}

	/**
	 * @param tcf
	 * @return
	 * @throws JMSException
	 */
	private Connection establishTopicConnection(final TopicConnectionFactory tcf)
			throws JMSException {
		this.log.info("Establishing topic connection ...");
		final Connection connection = tcf.createConnection("guest", "guest");
		this.log.info("Topic connection [{}] established", connection);

		return connection;
	}

	/**
	 * @param connection
	 * @return
	 * @throws JMSException
	 */
	private Session establishSession(final Connection connection)
			throws JMSException {
		this.log.info("Establishing session ...");
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		this.log.info("Session [{}] established", session);

		return session;
	}

	/**
	 * @param session
	 * @param destSender
	 * @throws IOException
	 * @throws JMSException
	 */
	private void sendMessage(final Session session,
			final MessageProducer destSender) throws IOException, JMSException {
		this.log.info("Sending message ...");
		final String payload = readFile("ValidItemCreatedEventMsg.xml");

		final TextMessage outMessage = session.createTextMessage(payload);
		outMessage.setStringProperty(EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.headerName(), ExternalEventSourceSystems.JMS_TEST_CLIENT);
		outMessage.setStringProperty(EventHeaderSpec.EVENT_TYPE.headerName(),
				ExternalEventTypes.ITEM_CREATED);
		outMessage.setStringProperty(EventHeaderSpec.EVENT_ID.headerName(),
				"urn:event:" + UUID.randomUUID().toString());

		destSender.send(outMessage);
		this.log.info("Message [{}] sent", outMessage);
	}

	private String readFile(final String relativePath) throws IOException {
		BufferedReader reader = null;
		try {
			final StringBuffer fileData = new StringBuffer(1000);
			final InputStream is = JmsTestClient.class
					.getResourceAsStream(relativePath);
			reader = new BufferedReader(new InputStreamReader(is));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				final String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			return fileData.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	// -------------------------------------------------------------------------
	// Preserved for use with Glassfish
	// -------------------------------------------------------------------------

	// public static void main(final String[] args) throws NamingException,
	// JMSException, IOException {
	// System.out.println("JMSClient: Establishing initial context");
	// System.out.println(" Establishing initial context");
	// final Hashtable<String, String> env = new Hashtable<String, String>();
	// env.put(Context.INITIAL_CONTEXT_FACTORY,
	// "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
	// env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
	// final Context ctx = new InitialContext(env);
	// System.out.println("JMSClient: Looking up ConnectionFactory");
	// final ConnectionFactory cf = (ConnectionFactory) ctx
	// .lookup("ConnectionFactory");
	// System.out.println("JMSClient: Lookup Topic");
	// final Destination outDest = (Destination) ctx
	// .lookup("dynamicTopics/jms/topic/com/acme/ItemCreatedEventsTopic");
	// System.out.println("destination = " + outDest.toString());
	// System.out.println("JMSClient: Establishing connection");
	// final Connection connection = cf.createConnection();
	// System.out.println("JMSClient: Establishing session");
	// final Session session = connection.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// final MessageProducer destSender = session.createProducer(outDest);
	// System.out.println("JMSClient: Sending message");
	// final String payload = readFile("ValidItemCreatedEventMsg.xml");
	// final TextMessage toutMessage = session.createTextMessage(payload);
	// destSender.send(toutMessage);
	// System.out.println("JMSClient: Message sent!");
	// destSender.close();
	// session.close();
	// connection.close();
	// }
}
