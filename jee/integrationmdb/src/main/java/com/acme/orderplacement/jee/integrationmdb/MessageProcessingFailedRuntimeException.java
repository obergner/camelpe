/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb;

import javax.jms.Message;

/**
 * <p>
 * An <strong>unchecked</strong> exception indicating that an application
 * component failed to process a <tt>JMS message</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageProcessingFailedRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * The <tt>JMS message</tt> that could not be processed.
	 */
	private final Message failedMessage;

	/**
	 * 
	 */
	public MessageProcessingFailedRuntimeException(final Message failedMessage) {
		this.failedMessage = failedMessage;
	}

	/**
	 * @param message
	 */
	public MessageProcessingFailedRuntimeException(final Message failedMessage,
			final String message) {
		super(message);
		this.failedMessage = failedMessage;
	}

	/**
	 * @param cause
	 */
	public MessageProcessingFailedRuntimeException(final Message failedMessage,
			final Throwable cause) {
		super(cause);
		this.failedMessage = failedMessage;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageProcessingFailedRuntimeException(final Message failedMessage,
			final String message, final Throwable cause) {
		super(message, cause);
		this.failedMessage = failedMessage;
	}

	/**
	 * <p>
	 * Return the <tt>JMS message</tt> that could not be processed.
	 * </p>
	 * 
	 * @return the failedMessage
	 */
	public Message getFailedMessage() {
		return this.failedMessage;
	}
}
