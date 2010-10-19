/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.util.concurrent.ExecutorService;

import javax.jms.Destination;
import javax.jms.TopicConnectionFactory;

import org.milyn.Smooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.client.jmstestclient.model.ItemCreatedEventBean;

/**
 * <p>
 * TODO: Insert short summary for ItemCreatedEventProducer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemCreatedEventProducer implements Runnable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final TopicConnectionFactory topicConnectionFactory;

	private final Destination itemCreatedEventTopic;

	private final ExecutorService itemCreatedEventSenders;

	private final Smooks configuration;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param topicConnectionFactory
	 * @param itemCreatedEventTopic
	 * @param itemCreatedEventSenders
	 * @param configuration
	 */
	public ItemCreatedEventProducer(
			final TopicConnectionFactory topicConnectionFactory,
			final Destination itemCreatedEventTopic,
			final ExecutorService itemCreatedEventSenders,
			final Smooks configuration) {
		this.topicConnectionFactory = topicConnectionFactory;
		this.itemCreatedEventTopic = itemCreatedEventTopic;
		this.itemCreatedEventSenders = itemCreatedEventSenders;
		this.configuration = configuration;
	}

	// -------------------------------------------------------------------------
	// java.lang.Runnable
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.log.info("Starting to produce ItemCreatedEvents ....");
		for (int i = 0; i < 200; i++) {
			final ItemCreatedEventBean itemCreatedEvent = ItemCreatedEventBean.BUILDER
					.build();
			final PublishItemCreatedEventTask publishingTask = new PublishItemCreatedEventTask(
					this.topicConnectionFactory, this.itemCreatedEventTopic,
					this.configuration, itemCreatedEvent);

			this.itemCreatedEventSenders.execute(publishingTask);
			this.log.info("Produced ItemCreatedEvent [{}]", i);
		}
		this.itemCreatedEventSenders.shutdown();
		this.log.info("Finished producing ItemCreatedEvents");
	}
}
