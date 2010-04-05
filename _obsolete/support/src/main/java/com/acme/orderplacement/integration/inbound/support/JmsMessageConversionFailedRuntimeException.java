/**
 * 
 */
package com.acme.orderplacement.integration.inbound.support;

import javax.jms.Message;

import org.springframework.jms.support.converter.MessageConversionException;

/**
 * <p>
 * An <strong>unchecked</strong> exception indicating that an application
 * component failed to process a <tt>JMS message</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JmsMessageConversionFailedRuntimeException extends
		MessageConversionException {

	private static final long serialVersionUID = 1L;

	/**
	 * The <tt>JMS message</tt> that could not be converted.
	 */
	private final Message failedMessage;

	/**
	 * 
	 */
	public JmsMessageConversionFailedRuntimeException(
			final Message failedMessage) {
		this(failedMessage, "Failed to convert JMS message", null);
	}

	/**
	 * @param message
	 */
	public JmsMessageConversionFailedRuntimeException(
			final Message failedMessage, final String message) {
		this(failedMessage, message, null);
	}

	/**
	 * @param cause
	 */
	public JmsMessageConversionFailedRuntimeException(
			final Message failedMessage, final Throwable cause) {
		this(failedMessage, "Failed to convert JMS message", cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JmsMessageConversionFailedRuntimeException(
			final Message failedMessage, final String message,
			final Throwable cause) {
		super(message, cause);
		this.failedMessage = failedMessage;
	}

	/**
	 * <p>
	 * Return the <tt>JMS message</tt> that could not be converted.
	 * </p>
	 * 
	 * @return the failedMessage
	 */
	public Message getFailedMessage() {
		return this.failedMessage;
	}
}
