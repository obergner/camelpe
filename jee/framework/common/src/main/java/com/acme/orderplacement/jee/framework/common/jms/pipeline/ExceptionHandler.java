/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import javax.jms.Message;

/**
 * <p>
 * TODO: Insert short summary for ExceptionHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: CURRENTLY NOT USED
 */
public interface ExceptionHandler<T extends Message> {

	Factory FACTORY = new Factory();

	ProcessingResult handle(T message, Exception exception) throws Exception;

	public static class Factory {

		private Factory() {
			// Intentionally left blank
		}

		public <M extends Message> ExceptionHandler<M> newNullHandler() {
			return new ExceptionHandler<M>() {
				@Override
				public ProcessingResult handle(final M message,
						final Exception exception) throws Exception {
					throw exception;
				}
			};
		}
	}
}
