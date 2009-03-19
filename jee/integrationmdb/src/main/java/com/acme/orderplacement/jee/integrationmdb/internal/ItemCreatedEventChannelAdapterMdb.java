/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.message.StringMessage;

import com.acme.orderplacement.integration.inbound.item.Channels;

/**
 * <p>
 * A <tt>Message Driven Bean</tt> that serves as an adapter for the
 * <tt>Spring Integration</tt> managed <i>Item Created Event Channel</i>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MessageDriven(name = "ItemCreatedEventChannelAdapterMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "/com/acme/jms/ItemCreatedEventsTopic") })
@Interceptors( { SpringBeanAutowiringInterceptor.class })
public class ItemCreatedEventChannelAdapterMdb implements MessageListener {

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * The <tt>Spring Integration</tt> managed {@link MessageChannel
	 * <code>MessageChannel</code>} we are propagating all JMS messages to.
	 */
	@Autowired(required = true)
	@Qualifier(Channels.ITEM_CREATED_EVENTS_XML)
	private MessageChannel itemCreatedEventChannel;

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(final Message jmsMessage)
			throws IllegalArgumentException {
		try {
			Validate.isTrue(jmsMessage instanceof TextMessage,
					"Expected a JMS TextMessage. Got: ["
							+ jmsMessage.getClass().getName());
			final TextMessage jmsTextMessage = TextMessage.class
					.cast(jmsMessage);
			this.log.debug("Processing text message [{}] ...", jmsTextMessage);

			final StringMessage stringMessage = new StringMessage(
					jmsTextMessage.getText());
			this.itemCreatedEventChannel.send(stringMessage, 3000L);
			this.log.debug("Propagated message [{}] to channel [{}]",
					stringMessage, this.itemCreatedEventChannel);
		} catch (final JMSException e) {
			/*
			 * TODO: Rethink exception handling.
			 */
			throw new RuntimeException("Failed to process message: "
					+ e.getMessage(), e);
		}
	}
}
