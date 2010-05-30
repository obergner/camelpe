/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/com/acme/ItemCreatedEventsTopic") })
public class ItemCreatedEventChannelAdapterMdb implements MessageListener {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired(required = true)
	@Qualifier("integration.inbound.item.ProducerTemplate")
	private ProducerTemplate channelEndpoint;

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(final Message jmsMessage)
			throws IllegalArgumentException {
		try {
			if (!(jmsMessage instanceof TextMessage)) {
				throw new IllegalArgumentException(
						"Expected text message. Got: ["
								+ jmsMessage.getClass().getName());
			}
			this.channelEndpoint.sendBody(((TextMessage) jmsMessage).getText());
			this.log
					.debug(
							"Received JMS message [{}] propagated to integration layer",
							jmsMessage);
		} catch (final JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
