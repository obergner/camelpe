/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * <p>
 * TODO: Insert short summary for SampleMessage
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SampleMessage implements Message {

	/**
	 * @see javax.jms.Message#acknowledge()
	 */
	@Override
	public void acknowledge() throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#clearBody()
	 */
	@Override
	public void clearBody() throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#clearProperties()
	 */
	@Override
	public void clearProperties() throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#getBooleanProperty(java.lang.String)
	 */
	@Override
	public boolean getBooleanProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return false;
	}

	/**
	 * @see javax.jms.Message#getByteProperty(java.lang.String)
	 */
	@Override
	public byte getByteProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getDoubleProperty(java.lang.String)
	 */
	@Override
	public double getDoubleProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getFloatProperty(java.lang.String)
	 */
	@Override
	public float getFloatProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getIntProperty(java.lang.String)
	 */
	@Override
	public int getIntProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getJMSCorrelationID()
	 */
	@Override
	public String getJMSCorrelationID() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
	 */
	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getJMSDeliveryMode()
	 */
	@Override
	public int getJMSDeliveryMode() throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getJMSDestination()
	 */
	@Override
	public Destination getJMSDestination() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getJMSExpiration()
	 */
	@Override
	public long getJMSExpiration() throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getJMSMessageID()
	 */
	@Override
	public String getJMSMessageID() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getJMSPriority()
	 */
	@Override
	public int getJMSPriority() throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getJMSRedelivered()
	 */
	@Override
	public boolean getJMSRedelivered() throws JMSException {
		// Intentionally left blank
		return false;
	}

	/**
	 * @see javax.jms.Message#getJMSReplyTo()
	 */
	@Override
	public Destination getJMSReplyTo() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getJMSTimestamp()
	 */
	@Override
	public long getJMSTimestamp() throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getJMSType()
	 */
	@Override
	public String getJMSType() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getLongProperty(java.lang.String)
	 */
	@Override
	public long getLongProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getObjectProperty(java.lang.String)
	 */
	@Override
	public Object getObjectProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getPropertyNames()
	 */
	@Override
	public Enumeration<String> getPropertyNames() throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#getShortProperty(java.lang.String)
	 */
	@Override
	public short getShortProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return 0;
	}

	/**
	 * @see javax.jms.Message#getStringProperty(java.lang.String)
	 */
	@Override
	public String getStringProperty(final String arg0) throws JMSException {
		// Intentionally left blank
		return null;
	}

	/**
	 * @see javax.jms.Message#propertyExists(java.lang.String)
	 */
	@Override
	public boolean propertyExists(final String arg0) throws JMSException {
		// Intentionally left blank
		return false;
	}

	/**
	 * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
	 */
	@Override
	public void setBooleanProperty(final String arg0, final boolean arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
	 */
	@Override
	public void setByteProperty(final String arg0, final byte arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
	 */
	@Override
	public void setDoubleProperty(final String arg0, final double arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
	 */
	@Override
	public void setFloatProperty(final String arg0, final float arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setIntProperty(java.lang.String, int)
	 */
	@Override
	public void setIntProperty(final String arg0, final int arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
	 */
	@Override
	public void setJMSCorrelationID(final String arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
	 */
	@Override
	public void setJMSCorrelationIDAsBytes(final byte[] arg0)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSDeliveryMode(int)
	 */
	@Override
	public void setJMSDeliveryMode(final int arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
	 */
	@Override
	public void setJMSDestination(final Destination arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSExpiration(long)
	 */
	@Override
	public void setJMSExpiration(final long arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSMessageID(java.lang.String)
	 */
	@Override
	public void setJMSMessageID(final String arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSPriority(int)
	 */
	@Override
	public void setJMSPriority(final int arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSRedelivered(boolean)
	 */
	@Override
	public void setJMSRedelivered(final boolean arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
	 */
	@Override
	public void setJMSReplyTo(final Destination arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSTimestamp(long)
	 */
	@Override
	public void setJMSTimestamp(final long arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setJMSType(java.lang.String)
	 */
	@Override
	public void setJMSType(final String arg0) throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setLongProperty(java.lang.String, long)
	 */
	@Override
	public void setLongProperty(final String arg0, final long arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setObjectProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void setObjectProperty(final String arg0, final Object arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setShortProperty(java.lang.String, short)
	 */
	@Override
	public void setShortProperty(final String arg0, final short arg1)
			throws JMSException {
		// Intentionally left blank

	}

	/**
	 * @see javax.jms.Message#setStringProperty(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setStringProperty(final String arg0, final String arg1)
			throws JMSException {
		// Intentionally left blank

	}

}
