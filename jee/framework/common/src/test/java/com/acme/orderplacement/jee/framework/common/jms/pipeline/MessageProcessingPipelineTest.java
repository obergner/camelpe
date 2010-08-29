/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import static junit.framework.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Message;

import org.junit.Test;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingPipelineTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageProcessingPipelineTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatMessageProcessingPipelineCallsFirstMessageProcessor()
			throws Exception {
		final AtomicBoolean firstProcessorCalled = new AtomicBoolean(false);
		final MessageProcessor<Message> firstProcessor = new MessageProcessor<Message>() {
			@Override
			public void process(final Message message) throws Exception {
				firstProcessorCalled.set(true);
			}
		};

		final MessageProcessingPipeline<Message> classUnderTest = new MessageProcessingPipeline<Message>(
				firstProcessor);
		classUnderTest.process(new SampleMessage());

		assertTrue(
				"process(null) did NOT call the first processor in the processing chain",
				firstProcessorCalled.get());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatGlobalExceptionHandlerHandlesUncaughtException()
			throws Exception {
		final MessageProcessor<Message> firstProcessor = new MessageProcessor<Message>() {
			@Override
			public void process(final Message message) throws Exception {
				throw new IllegalStateException("UNHANDLED");
			}
		};

		final AtomicBoolean globalExceptionHandlerCalled = new AtomicBoolean(
				false);
		final ExceptionHandler<Message> globalExceptionHandler = new ExceptionHandler<Message>() {
			@Override
			public ProcessingResult handle(final Message message,
					final Exception exception) throws Exception {
				globalExceptionHandlerCalled.set(true);
				return ProcessingResult.CONTINUE;
			}
		};

		final MessageProcessingPipeline<Message> classUnderTest = new MessageProcessingPipeline<Message>(
				firstProcessor, globalExceptionHandler);
		classUnderTest.process(new SampleMessage());

		assertTrue("process(null) did NOT call the global exception handler",
				globalExceptionHandlerCalled.get());
	}
}
