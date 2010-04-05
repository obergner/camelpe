/**
 * 
 */
package com.acme.orderplacement.integration.inbound.support.adapter;

import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.Message;
import org.springframework.integration.message.ErrorMessage;

/**
 * <p>
 * Test {@link MessageTypeEnsuringJmsChannelAdapter}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageTypeEnsuringJmsChannelAdapterTest {

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#MessageTypeEnsuringJmsChannelAdapter(java.lang.Class, org.springframework.integration.core.MessageChannel, org.springframework.integration.core.MessageChannel)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void fullMessageTypeEnsuringJmsChannelAdapterCtorShouldRejectNullArguments() {
		new MessageTypeEnsuringJmsChannelAdapter(null, null, null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#convertAndPropagate(javax.jms.Message)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void convertAndPropagateShouldRejectNullJmsMessage() {
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter(
				TextMessage.class, new DirectChannel(), new DirectChannel());
		classUnderTest.convertAndPropagate(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#convertAndPropagate(javax.jms.Message)}
	 * .
	 */
	@Test
	public final void convertAndPropagateShouldPropagateJmsMessageOfWrongTypeToErrorChannel() {
		final LinkedBlockingQueue<Message<?>> errorQueue = new LinkedBlockingQueue<Message<?>>();
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter(
				StreamMessage.class, new QueueChannel(), new QueueChannel(
						errorQueue));

		final TextMessage wrongTypeJmsMessage = new TestTextMessage("TEST");
		classUnderTest.convertAndPropagate(wrongTypeJmsMessage);

		Assert
				.assertEquals(
						"convertAndPropagate(wrongTypeJmsMessage) did not propagate any message to the error channel",
						1, errorQueue.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#convertAndPropagate(javax.jms.Message)}
	 * .
	 */
	@Test
	public final void convertAndPropagateShouldConvertJmsMessageOfWrongTypeIntoErrorMessage() {
		final LinkedBlockingQueue<Message<?>> errorQueue = new LinkedBlockingQueue<Message<?>>();
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter(
				StreamMessage.class, new QueueChannel(), new QueueChannel(
						errorQueue));

		final TextMessage wrongTypeJmsMessage = new TestTextMessage("TEST");
		classUnderTest.convertAndPropagate(wrongTypeJmsMessage);

		Assert
				.assertEquals(
						"convertAndPropagate(wrongTypeJmsMessage) did not propagate any message to the error channel",
						1, errorQueue.size());
		final Message<?> propagatedMessage = errorQueue.poll();
		Assert
				.assertTrue(
						"convertAndPropagate(wrongTypeJmsMessage) did not convert JMS message into ErrorMessage",
						propagatedMessage instanceof ErrorMessage);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#convertAndPropagate(javax.jms.Message)}
	 * .
	 */
	@Test
	public final void convertAndPropagateShouldPropagateJmsMessageOExpectedTypeToSuccessChannel() {
		final LinkedBlockingQueue<Message<?>> successQueue = new LinkedBlockingQueue<Message<?>>();
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter(
				TextMessage.class, new QueueChannel(successQueue),
				new QueueChannel());

		final TextMessage expectedTypeJmsMessage = new TestTextMessage("TEST");
		classUnderTest.convertAndPropagate(expectedTypeJmsMessage);

		Assert
				.assertEquals(
						"convertAndPropagate(expectedTypeJmsMessage) did not propagate any message to the success channel",
						1, successQueue.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#setExpectedMessageType(java.lang.Class)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setExpectedMessageTypeShouldRejectNullMessageType() {
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter();
		classUnderTest.setExpectedMessageType(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#setSuccessChannel(org.springframework.integration.core.MessageChannel)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setSuccessChannelShouldRejectNullSuccessChannel() {
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter();
		classUnderTest.setSuccessChannel(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#setErrorChannel(org.springframework.integration.core.MessageChannel)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setErrorChannelShouldRejectNullErrorChannel() {
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter();
		classUnderTest.setErrorChannel(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.support.adapter.MessageTypeEnsuringJmsChannelAdapter#ensureRequiredDependenciesSet()}
	 * .
	 */
	@Test(expected = IllegalStateException.class)
	public final void ensureRequiredDependenciesSetShouldComplainAboutMissingExpectedMessageType() {
		final MessageTypeEnsuringJmsChannelAdapter classUnderTest = new MessageTypeEnsuringJmsChannelAdapter();
		classUnderTest.setErrorChannel(new DirectChannel());
		classUnderTest.setSuccessChannel(new DirectChannel());

		classUnderTest.ensureRequiredDependenciesSet();
	}

	private static class TestTextMessage implements TextMessage {

		private String payload;

		/**
		 * @param payload
		 */
		public TestTextMessage(final String payload) {
			this.payload = payload;
		}

		/**
		 * @see javax.jms.TextMessage#getText()
		 */
		public String getText() throws JMSException {
			return this.payload;
		}

		/**
		 * @see javax.jms.TextMessage#setText(java.lang.String)
		 */
		public void setText(final String arg0) throws JMSException {
			this.payload = arg0;
		}

		/**
		 * @see javax.jms.Message#acknowledge()
		 */
		public void acknowledge() throws JMSException {
		}

		/**
		 * @see javax.jms.Message#clearBody()
		 */
		public void clearBody() throws JMSException {
		}

		/**
		 * @see javax.jms.Message#clearProperties()
		 */
		public void clearProperties() throws JMSException {
		}

		/**
		 * @see javax.jms.Message#getBooleanProperty(java.lang.String)
		 */
		public boolean getBooleanProperty(final String arg0)
				throws JMSException {
			return false;
		}

		/**
		 * @see javax.jms.Message#getByteProperty(java.lang.String)
		 */
		public byte getByteProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getDoubleProperty(java.lang.String)
		 */
		public double getDoubleProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getFloatProperty(java.lang.String)
		 */
		public float getFloatProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getIntProperty(java.lang.String)
		 */
		public int getIntProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getJMSCorrelationID()
		 */
		public String getJMSCorrelationID() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
		 */
		public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getJMSDeliveryMode()
		 */
		public int getJMSDeliveryMode() throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getJMSDestination()
		 */
		public Destination getJMSDestination() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getJMSExpiration()
		 */
		public long getJMSExpiration() throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getJMSMessageID()
		 */
		public String getJMSMessageID() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getJMSPriority()
		 */
		public int getJMSPriority() throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getJMSRedelivered()
		 */
		public boolean getJMSRedelivered() throws JMSException {
			return false;
		}

		/**
		 * @see javax.jms.Message#getJMSReplyTo()
		 */
		public Destination getJMSReplyTo() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getJMSTimestamp()
		 */
		public long getJMSTimestamp() throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getJMSType()
		 */
		public String getJMSType() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getLongProperty(java.lang.String)
		 */
		public long getLongProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getObjectProperty(java.lang.String)
		 */
		public Object getObjectProperty(final String arg0) throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getPropertyNames()
		 */
		public Enumeration<?> getPropertyNames() throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#getShortProperty(java.lang.String)
		 */
		public short getShortProperty(final String arg0) throws JMSException {
			return 0;
		}

		/**
		 * @see javax.jms.Message#getStringProperty(java.lang.String)
		 */
		public String getStringProperty(final String arg0) throws JMSException {
			return null;
		}

		/**
		 * @see javax.jms.Message#propertyExists(java.lang.String)
		 */
		public boolean propertyExists(final String arg0) throws JMSException {
			return false;
		}

		/**
		 * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
		 */
		public void setBooleanProperty(final String arg0, final boolean arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
		 */
		public void setByteProperty(final String arg0, final byte arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
		 */
		public void setDoubleProperty(final String arg0, final double arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
		 */
		public void setFloatProperty(final String arg0, final float arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setIntProperty(java.lang.String, int)
		 */
		public void setIntProperty(final String arg0, final int arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
		 */
		public void setJMSCorrelationID(final String arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
		 */
		public void setJMSCorrelationIDAsBytes(final byte[] arg0)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSDeliveryMode(int)
		 */
		public void setJMSDeliveryMode(final int arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
		 */
		public void setJMSDestination(final Destination arg0)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSExpiration(long)
		 */
		public void setJMSExpiration(final long arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSMessageID(java.lang.String)
		 */
		public void setJMSMessageID(final String arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSPriority(int)
		 */
		public void setJMSPriority(final int arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSRedelivered(boolean)
		 */
		public void setJMSRedelivered(final boolean arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
		 */
		public void setJMSReplyTo(final Destination arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSTimestamp(long)
		 */
		public void setJMSTimestamp(final long arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setJMSType(java.lang.String)
		 */
		public void setJMSType(final String arg0) throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setLongProperty(java.lang.String, long)
		 */
		public void setLongProperty(final String arg0, final long arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setObjectProperty(java.lang.String,
		 *      java.lang.Object)
		 */
		public void setObjectProperty(final String arg0, final Object arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setShortProperty(java.lang.String, short)
		 */
		public void setShortProperty(final String arg0, final short arg1)
				throws JMSException {
		}

		/**
		 * @see javax.jms.Message#setStringProperty(java.lang.String,
		 *      java.lang.String)
		 */
		public void setStringProperty(final String arg0, final String arg1)
				throws JMSException {
		}
	}
}
