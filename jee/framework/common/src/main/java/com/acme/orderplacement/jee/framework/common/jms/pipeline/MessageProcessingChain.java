/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import javax.jms.Message;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingChain
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: CURRENTLY NOT USED
 */
public class MessageProcessingChain<T extends Message> implements
		MessageProcessor<T> {

	private final MessageProcessor<T> nextProcessor;

	private final MessageProcessingUnit<T> processingUnit;

	private final ExceptionHandler<T> exceptionHandler;

	public MessageProcessingChain(final MessageProcessingUnit<T> processingUnit)
			throws IllegalArgumentException {
		this(null, processingUnit, null);
	}

	public MessageProcessingChain(
			final MessageProcessingUnit<T> processingUnit,
			final ExceptionHandler<T> exceptionHandler)
			throws IllegalArgumentException {
		this(null, processingUnit, exceptionHandler);
	}

	public MessageProcessingChain(final MessageProcessor<T> nextProcessor,
			final MessageProcessingUnit<T> processingUnit)
			throws IllegalArgumentException {
		this(nextProcessor, processingUnit, null);
	}

	public MessageProcessingChain(final MessageProcessor<T> nextProcessor,
			final MessageProcessingUnit<T> processingUnit,
			final ExceptionHandler<T> exceptionHandler)
			throws IllegalArgumentException {
		Validate.notNull(processingUnit, "processingUnit");
		this.nextProcessor = nextProcessor;
		this.processingUnit = processingUnit;
		this.exceptionHandler = exceptionHandler != null ? exceptionHandler
				: ExceptionHandler.FACTORY.<T> newNullHandler();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessor#process(T)
	 */
	public final void process(final T message) throws Exception {
		Validate.notNull(message, "message");
		ProcessingResult result;
		try {
			result = this.processingUnit.process(message);
		} catch (final Exception e) {
			result = this.exceptionHandler.handle(message, e);
		}
		if ((result == ProcessingResult.CONTINUE)
				&& (this.nextProcessor != null)) {
			this.nextProcessor.process(message);
		}
	}
}
