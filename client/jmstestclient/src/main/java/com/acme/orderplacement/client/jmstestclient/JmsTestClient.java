/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>
 * TODO: Insert short summary for JmsTestClient
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JmsTestClient {

	public static void main(final String[] args) throws NamingException,
			JMSException, IOException {
		System.out.println("JMSClient: Establishing initial context");
		System.out.println(" Establishing initial context");
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		env
				.put(Context.URL_PKG_PREFIXES,
						"org.jboss.naming:org.jnp.interfaces");
		final Context ctx = new InitialContext(env);
		System.out.println("JMSClient: Looking up ConnectionFactory");
		final ConnectionFactory cf = (ConnectionFactory) ctx
				.lookup("ConnectionFactory");
		System.out.println("JMSClient: Lookup Topic");
		final Destination outDest = (Destination) ctx
				.lookup("/topic/orderplacement/ItemCreatedEventsTopic");
		System.out.println("destination = " + outDest.toString());
		System.out.println("JMSClient: Establishing connection");
		final Connection connection = cf.createConnection();
		System.out.println("JMSClient: Establishing session");
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		final MessageProducer destSender = session.createProducer(outDest);
		System.out.println("JMSClient: Sending message");
		final String payload = readFile("ValidItemCreatedEventMsg.xml");
		final TextMessage toutMessage = session.createTextMessage(payload);
		destSender.send(toutMessage);
		System.out.println("JMSClient: Message sent!");
		destSender.close();
		session.close();
		connection.close();
	}

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

	private static String readFile(final String relativePath)
			throws IOException {
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
}
