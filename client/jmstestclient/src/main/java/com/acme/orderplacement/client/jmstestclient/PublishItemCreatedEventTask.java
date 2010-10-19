/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnectionFactory;
import javax.xml.transform.stream.StreamResult;

import org.milyn.Smooks;
import org.milyn.payload.JavaSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.client.jmstestclient.model.ItemCreatedEventBean;
import com.obergner.acme.orderplacement.integration.inbound.external.ExternalEventSourceSystems;
import com.obergner.acme.orderplacement.integration.inbound.external.ExternalEventTypes;
import com.obergner.acme.orderplacement.integration.inbound.external.event.EventHeaderSpec;

/**
 * <p>
 * TODO: Insert short summary for PublishItemCreatedEventTask
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PublishItemCreatedEventTask implements Runnable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final TopicConnectionFactory topicConnectionFactory;

	private final Destination itemCreatedEventTopic;

	private final Smooks configuration;

	private final ItemCreatedEventBean itemCreatedEvent;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param topicConnectionFactory
	 * @param itemCreatedEventTopic
	 * @param configuration
	 * @param itemCreatedEvent
	 */
	public PublishItemCreatedEventTask(
			final TopicConnectionFactory topicConnectionFactory,
			final Destination itemCreatedEventTopic,
			final Smooks configuration,
			final ItemCreatedEventBean itemCreatedEvent) {
		this.topicConnectionFactory = topicConnectionFactory;
		this.itemCreatedEventTopic = itemCreatedEventTopic;
		this.configuration = configuration;
		this.itemCreatedEvent = itemCreatedEvent;
	}

	// -------------------------------------------------------------------------
	// java.lang.Runnable
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Connection connection = null;
		Session session = null;
		MessageProducer destSender = null;
		try {
			connection = establishTopicConnection(this.topicConnectionFactory);

			session = establishSession(connection);

			destSender = session.createProducer(this.itemCreatedEventTopic);

			sendMessage(session, destSender);
		} catch (final Exception e) {
			this.log.warn(
					"Caught exception while trying to publish ItemCreatedEvent: "
							+ e.getMessage(), e);
		} finally {
			closeResources(connection, session, destSender);
		}
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private String createMessageFromItemCreatedEvent(
			final ItemCreatedEventBean anItemCreatedEvent) {
		final JavaSource sampleInput = new JavaSource(anItemCreatedEvent);
		sampleInput.setEventStreamRequired(false);
		final Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("itemCreatedEvent", anItemCreatedEvent);
		sampleInput.setBeans(beans);

		final StringWriter output = new StringWriter();

		this.configuration.filterSource(sampleInput, new StreamResult(output));

		return output.toString();
	}

	private Connection establishTopicConnection(final TopicConnectionFactory tcf)
			throws JMSException {
		this.log.info("Establishing topic connection ...");
		final Connection connection = tcf.createConnection("guest", "guest");
		this.log.info("Topic connection [{}] established", connection);

		return connection;
	}

	private Session establishSession(final Connection connection)
			throws JMSException {
		this.log.info("Establishing session ...");
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		this.log.info("Session [{}] established", session);

		return session;
	}

	private void sendMessage(final Session session,
			final MessageProducer destSender) throws JMSException {
		this.log.info("Sending message ...");

		final TextMessage outMessage = session
				.createTextMessage(createMessageFromItemCreatedEvent(this.itemCreatedEvent));
		outMessage.setStringProperty(EventHeaderSpec.EVENT_SOURCE_SYSTEM
				.headerName(), ExternalEventSourceSystems.JMS_TEST_CLIENT);
		outMessage.setStringProperty(EventHeaderSpec.EVENT_TYPE.headerName(),
				ExternalEventTypes.ITEM_CREATED);
		outMessage.setStringProperty(EventHeaderSpec.EVENT_ID.headerName(),
				this.itemCreatedEvent.getHeaders().getEventId());

		destSender.send(outMessage);
		this.log.info("Message [{}] sent", outMessage);
	}

	/**
	 * @param connection
	 * @param session
	 * @param destSender
	 */
	private void closeResources(final Connection connection,
			final Session session, final MessageProducer destSender) {
		if (destSender != null) {
			try {
				destSender.close();
			} catch (final JMSException e) {
				this.log.warn(
						"Caught exception while trying to close MessageProducer: "
								+ e.getMessage(), e);
			}
		}
		if (session != null) {
			try {
				session.close();
			} catch (final JMSException e) {
				this.log.warn(
						"Caught exception while trying to close Session: "
								+ e.getMessage(), e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (final JMSException e) {
				this.log.warn(
						"Caught exception while trying to close Connection: "
								+ e.getMessage(), e);
			}
		}
	}
}
