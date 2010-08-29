/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.jms.Message;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingPipeline
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: CURRENTLY NOT USED
 */
public class MessageProcessingPipeline<T extends Message> implements
		MessageProcessor<T> {

	public static <M extends Message> Builder<M> builder() {
		return new Builder<M>();
	}

	public static class Builder<M extends Message> {

		private static class ProcessorAndExceptionHandlerHolder<M extends Message> {
			private final MessageProcessingUnit<M> messageProcessingUnit;

			private final ExceptionHandler<M> exceptionHandler;

			ProcessorAndExceptionHandlerHolder(
					final MessageProcessingUnit<M> messageProcessingUnit,
					final ExceptionHandler<M> exceptionHandler) {
				this.messageProcessingUnit = messageProcessingUnit;
				this.exceptionHandler = exceptionHandler;
			}

			MessageProcessingChain<M> chain(final MessageProcessingChain<M> next) {
				return new MessageProcessingChain<M>(next,
						this.messageProcessingUnit, this.exceptionHandler);
			}
		}

		private final Deque<ProcessorAndExceptionHandlerHolder<M>> buffer = new ArrayDeque<ProcessorAndExceptionHandlerHolder<M>>();

		private ExceptionHandler<M> globalExceptionHandler;

		Builder() {
			// Intentionally left blank
		}

		public Builder<M> withGlobalExceptionHandler(
				final ExceptionHandler<M> globalExceptionHandler)
				throws IllegalArgumentException, IllegalStateException {
			Validate.notNull(globalExceptionHandler, "globalExceptionHandler");
			if (this.globalExceptionHandler != null) {
				throw new IllegalStateException(
						"Global exception handler has already been set");
			}
			this.globalExceptionHandler = globalExceptionHandler;

			return this;
		}

		public Builder<M> append(
				final MessageProcessingUnit<M> messageProcessingUnit,
				final ExceptionHandler<M> exceptionHandler)
				throws IllegalArgumentException {
			Validate.notNull(messageProcessingUnit, "messageProcessingUnit");
			this.buffer.addFirst(new ProcessorAndExceptionHandlerHolder<M>(
					messageProcessingUnit, exceptionHandler));

			return this;
		}

		public Builder<M> append(
				final MessageProcessingUnit<M> messageProcessingUnit)
				throws IllegalArgumentException {
			return append(messageProcessingUnit, null);
		}

		public MessageProcessingPipeline<M> build() {
			MessageProcessingChain<M> next = null;
			while (!this.buffer.isEmpty()) {
				next = this.buffer.removeFirst().chain(next);
			}

			return new MessageProcessingPipeline<M>(next,
					this.globalExceptionHandler);
		}
	}

	private final MessageProcessor<T> firstProcessor;

	private final ExceptionHandler<T> globalExceptionHandler;

	public MessageProcessingPipeline(final MessageProcessor<T> firstProcessor)
			throws IllegalArgumentException {
		this(firstProcessor, null);
	}

	public MessageProcessingPipeline(final MessageProcessor<T> firstProcessor,
			final ExceptionHandler<T> globalExceptionHandler)
			throws IllegalArgumentException {
		Validate.notNull(firstProcessor, "firstProcessor");
		this.firstProcessor = firstProcessor;
		this.globalExceptionHandler = globalExceptionHandler != null ? globalExceptionHandler
				: ExceptionHandler.FACTORY.<T> newNullHandler();
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessor#process(javax.jms.Message)
	 */
	@Override
	public void process(final T message) throws Exception {
		Validate.notNull(message, "message");
		try {
			this.firstProcessor.process(message);
		} catch (final Exception e) {
			this.globalExceptionHandler.handle(message, e);
		}
	}

}
