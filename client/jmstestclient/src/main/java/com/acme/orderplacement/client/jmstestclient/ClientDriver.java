/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Destination;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.milyn.Smooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * <p>
 * TODO: Insert short summary for ClientDriver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ClientDriver {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final String ITEM_CREATED_EVENT_TOPIC_JNDI = "/topic/orderplacement/ItemCreatedEventsTopic";

	// -------------------------------------------------------------------------
	// MAIN
	// -------------------------------------------------------------------------

	public static void main(final String[] args) throws NamingException,
			IOException, SAXException {
		new ClientDriver().run();
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public void run() throws IOException, SAXException, NamingException {
		this.log.info("Starting ...");
		final Smooks configuration = new Smooks("smooks-config.xml");

		final Context ctx = establishInitialContext();
		final TopicConnectionFactory tcf = lookupTopicConnectionFactory(ctx);
		final Destination outDest = lookupTopic(ctx);

		final ExecutorService itemCreatedEventProducers = Executors
				.newFixedThreadPool(2);
		final ExecutorService itemCreatedEventSenders = Executors
				.newFixedThreadPool(10);

		final ItemCreatedEventProducer itemCreatedEventProducer = new ItemCreatedEventProducer(
				tcf, outDest, itemCreatedEventSenders, configuration);
		itemCreatedEventProducers.execute(itemCreatedEventProducer);
		this.log.info("Started producing events ...");

		itemCreatedEventProducers.shutdown();
		this.log.info("Event producers have shut down");

		this.log.info("Terminated");
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
}
