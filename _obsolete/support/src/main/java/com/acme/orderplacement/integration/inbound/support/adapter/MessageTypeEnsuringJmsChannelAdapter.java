/**
 * 
 */
package com.acme.orderplacement.integration.inbound.support.adapter;

import javax.annotation.PostConstruct;
import javax.jms.Message;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.jms.HeaderMappingMessageConverter;
import org.springframework.integration.message.ErrorMessage;

import com.acme.orderplacement.integration.inbound.support.JmsMessageConversionFailedRuntimeException;

/**
 * <p>
 * A <tt>Spring Integration</tt> <code>JMS</code> channel adapter that
 * <ol>
 * <li>
 * takes a {@link javax.jms.Message <code>javax.jms.Message</code>},</li>
 * <li>
 * ensures that its exact type - {@link javax.jms.TextMessage <code>
 * javax.jms.TextMessage</code>}, {@link javax.jms.ObjectMessage <code>
 * javax.jms.ObjectMessage</code>} etc. - is as expected, and</li>
 * <li>
 * tries to convert the received <code>javax.jms.Message</code> into an
 * equivalent {@link org.springframework.integration.core.Message <code>
 * org.springframework.integration.core.Message</code>}.</li>
 * </ol>
 * </p>
 * <p>
 * If all of the above steps succeed the resulting
 * <tt>Spring Integration Message</tt> is propagated to a configurable
 * {@link org.springframework.integration.core.MessageChannel
 * <code>org.springframework.integration.core.MessageChannel</code>} meant to
 * only receive valid messages, otherwise to an equally configurable, different
 * {@link org.springframework.integration.core.MessageChannel
 * <code>org.springframework.integration.core.MessageChannel</code>} meant to
 * propagate any errors.
 * </p>
 * <p>
 * This components acts effectively as a cross between a
 * <code>ChannelAdapter</code> and a <code>Router</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageTypeEnsuringJmsChannelAdapter {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "integration.inbound.item.adapter.JmsChannelAdapter";

	/**
	 * The default timeout for all
	 * {@link org.springframework.integration.core.Message
	 * <code>org.springframework.integration.core.Message</code>}s when being
	 * propagated.
	 */
	private static final long DEFAULT_TIMEOUT = 2000L;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * <tt>Spring Integration</tt>'s own {@link HeaderMappingMessageConverter
	 * <code>HeaderMappingMessageConverter</code>} we delegate to.
	 */
	private final HeaderMappingMessageConverter delegate = new HeaderMappingMessageConverter();

	/**
	 * The concrete <code>javax.jms.Message</code> subtype we expect the
	 * <code>javax.jms.Message</code> we want to convert to have.
	 */
	private Class<? extends javax.jms.Message> expectedMessageType;

	/**
	 * The {@link MessageChannel <code>MessageChannel</code>} successfully
	 * converted <code>javax.jms.Message</code>s will be propagated to.
	 */
	private MessageChannel successChannel;

	/**
	 * The {@link MessageChannel <code>MessageChannel</code>}
	 * <code>javax.jms.Message</code>s that could not be converted will be
	 * propagated to.
	 */
	private MessageChannel errorChannel;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Default constructor.
	 */
	public MessageTypeEnsuringJmsChannelAdapter() {
		super();
	}

	/**
	 * @param expectedMessageType
	 * @param successChannel
	 * @param errorChannel
	 * @throws IllegalArgumentException
	 */
	public MessageTypeEnsuringJmsChannelAdapter(
			final Class<? extends Message> expectedMessageType,
			final MessageChannel successChannel,
			final MessageChannel errorChannel) throws IllegalArgumentException {
		Validate.notNull(expectedMessageType, "expectedMessageType");
		Validate.notNull(successChannel, "successChannel");
		Validate.notNull(errorChannel, "errorChannel");
		this.expectedMessageType = expectedMessageType;
		this.successChannel = successChannel;
		this.errorChannel = errorChannel;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param jmsMessage
	 * @throws IllegalArgumentException
	 */
	public void convertAndPropagate(final Message jmsMessage)
			throws IllegalArgumentException {
		Validate.notNull(jmsMessage, "jmsMessage");
		try {
			this.log.debug(
					"Attempting to convert and propagate JMS message [{}] ...",
					jmsMessage);
			ensureMessageOfExpectedType(jmsMessage);

			final org.springframework.integration.core.Message<?> convertedMessage = (org.springframework.integration.core.Message<?>) this.delegate
					.fromMessage(jmsMessage);
			this.log
					.trace(
							"JMS message [{}] has been successfully converted into [{}]",
							jmsMessage, convertedMessage);

			propagateSuccessfullyConvertedMessageToSuccessChannel(convertedMessage);
			this.log.debug(
					"JMS message [{}] successfully converted and propagated",
					jmsMessage);
		} catch (final Exception e) {

			propagateErrorToErrorChannel(e);
		}
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the expectedMessageType
	 */
	public Class<? extends javax.jms.Message> getExpectedMessageType() {
		return this.expectedMessageType;
	}

	/**
	 * @param expectedMessageType
	 *            the expectedMessageType to set
	 * @throws IllegalArgumentException
	 */
	public void setExpectedMessageType(
			final Class<? extends javax.jms.Message> expectedMessageType)
			throws IllegalArgumentException {
		Validate.notNull(expectedMessageType, "expectedMessageType");
		this.expectedMessageType = expectedMessageType;
	}

	/**
	 * @return the successChannel
	 */
	public MessageChannel getSuccessChannel() {
		return this.successChannel;
	}

	/**
	 * @param successChannel
	 *            the successChannel to set
	 * @throws IllegalArgumentException
	 */
	public void setSuccessChannel(final MessageChannel successChannel)
			throws IllegalArgumentException {
		Validate.notNull(successChannel, "successChannel");
		this.successChannel = successChannel;
	}

	/**
	 * @return the errorChannel
	 */
	public MessageChannel getErrorChannel() {
		return this.errorChannel;
	}

	/**
	 * @param errorChannel
	 *            the errorChannel to set
	 * @throws IllegalArgumentException
	 */
	public void setErrorChannel(final MessageChannel errorChannel)
			throws IllegalArgumentException {
		Validate.notNull(errorChannel, "errorChannel");
		this.errorChannel = errorChannel;
	}

	// -------------------------------------------------------------------------
	// Supporting infrastructure
	// -------------------------------------------------------------------------

	/**
	 * @throws IllegalArgumentException
	 */
	@PostConstruct
	public void ensureRequiredDependenciesSet() throws IllegalArgumentException {
		if (this.expectedMessageType == null) {
			throw new IllegalStateException("No 'expectedMessageType' set");
		}
		if (this.successChannel == null) {
			throw new IllegalStateException("No 'successChannel' set");
		}
		if (this.errorChannel == null) {
			throw new IllegalStateException("No 'errorChannel' set");
		}
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private void ensureMessageOfExpectedType(final Message jmsMessage)
			throws JmsMessageConversionFailedRuntimeException {
		if (!this.expectedMessageType.isAssignableFrom(jmsMessage.getClass())) {
			this.log
					.error(
							"The received JMS message [{}] is not of the expected type [{}] but rather of type [{}]",
							new Object[] { jmsMessage,
									this.expectedMessageType.getName(),
									jmsMessage.getClass().getName() });

			throw new JmsMessageConversionFailedRuntimeException(jmsMessage,
					"The received JMS message is not of the expected type ["
							+ this.expectedMessageType.getName()
							+ "] but of type ["
							+ jmsMessage.getClass().getName() + "]");
		}
	}

	private void propagateErrorToErrorChannel(final Exception jmsConversionError) {
		final ErrorMessage errorMessage = new ErrorMessage(jmsConversionError);
		if (this.errorChannel.send(errorMessage, DEFAULT_TIMEOUT)) {
			this.log
					.debug(
							"Error [{}] has been successfully propagated to the error channel [{}]",
							jmsConversionError, this.errorChannel.getName());
		} else {
			this.log
					.error(
							"Error [{}] could not be propagated to the error channel [{}]."
									+ " Either the timeout of [{}] ms elapsed, or the send was interrupted.",
							new Object[] { jmsConversionError,
									this.errorChannel.getName(),
									Long.valueOf(DEFAULT_TIMEOUT) });
		}
	}

	private void propagateSuccessfullyConvertedMessageToSuccessChannel(
			final org.springframework.integration.core.Message<?> springIntegrationMessage) {
		if (this.successChannel.send(springIntegrationMessage, DEFAULT_TIMEOUT)) {
			this.log
					.debug(
							"Converted message [{}] has been successfully propagated to the success channel [{}]",
							springIntegrationMessage, this.successChannel
									.getName());
		} else {
			this.log
					.error(
							"Converted message [{}] could not be propagated to the success channel [{}]."
									+ " Either the timeout of [{}] ms elapsed, or the send was interrupted.",
							new Object[] { springIntegrationMessage,
									this.successChannel.getName(),
									Long.valueOf(DEFAULT_TIMEOUT) });
		}
	}
}
