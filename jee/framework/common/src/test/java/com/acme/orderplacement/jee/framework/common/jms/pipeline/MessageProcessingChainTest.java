/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Message;

import org.junit.Test;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingChainTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageProcessingChainTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingChain#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessCallsExceptionHandlerOnException()
			throws Exception {
		final MessageProcessingUnit<Message> exceptionThrowingProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				throw new RuntimeException("EXCPECTED");
			}
		};

		final AtomicBoolean exceptionHandlerCalled = new AtomicBoolean(false);
		final ExceptionHandler<Message> exceptionHandler = new ExceptionHandler<Message>() {
			@Override
			public ProcessingResult handle(final Message message,
					final Exception exception) throws Exception {
				exceptionHandlerCalled.set(true);

				return ProcessingResult.CONTINUE;
			}
		};

		final MessageProcessor<Message> classUnderTest = new MessageProcessingChain<Message>(
				exceptionThrowingProcessor, exceptionHandler);

		classUnderTest.process(new SampleMessage());

		assertTrue("process(null) did NOT call exception handler",
				exceptionHandlerCalled.get());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingChain#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public final void assertThatProcessRethrowsExceptionIfNoExceptionHandlerIsSet()
			throws Exception {
		final MessageProcessingUnit<Message> exceptionThrowingProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				throw new UnsupportedOperationException("SHOULD BE RETHROWN");
			}
		};

		final MessageProcessor<Message> classUnderTest = new MessageProcessingChain<Message>(
				exceptionThrowingProcessor);

		classUnderTest.process(new SampleMessage());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingChain#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessCallsNextProcessingStepIfProcessingShouldContinue()
			throws Exception {
		final AtomicBoolean nextProcessorCalled = new AtomicBoolean(false);
		final MessageProcessingUnit<Message> nextProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				nextProcessorCalled.set(true);

				return ProcessingResult.CONTINUE;
			}
		};
		final MessageProcessor<Message> nextProcessingStep = new MessageProcessingChain<Message>(
				nextProcessor);

		final MessageProcessingUnit<Message> continuingProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				return ProcessingResult.CONTINUE;
			}
		};
		final MessageProcessor<Message> classUnderTest = new MessageProcessingChain<Message>(
				nextProcessingStep, continuingProcessor);

		classUnderTest.process(new SampleMessage());

		assertTrue("process(null) did NOT call next processing step",
				nextProcessorCalled.get());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingChain#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatProcessDoesNotCallNextProcessingStepIfProcessingShouldAbort()
			throws Exception {
		final AtomicBoolean nextProcessorCalled = new AtomicBoolean(false);
		final MessageProcessingUnit<Message> nextProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				nextProcessorCalled.set(true);

				return ProcessingResult.CONTINUE;
			}
		};
		final MessageProcessor<Message> nextProcessingStep = new MessageProcessingChain<Message>(
				nextProcessor);

		final MessageProcessingUnit<Message> abortingProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				return ProcessingResult.ABORT;
			}
		};
		final MessageProcessor<Message> classUnderTest = new MessageProcessingChain<Message>(
				nextProcessingStep, abortingProcessor);

		classUnderTest.process(new SampleMessage());

		assertFalse(
				"process(null) called next processing step although processing has been aborted",
				nextProcessorCalled.get());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingChain#process(javax.jms.Message)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public final void assertThatProcessRethrowsExceptionThrownFromExceptionHandler()
			throws Exception {
		final MessageProcessingUnit<Message> exceptionThrowingProcessor = new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				throw new RuntimeException(
						"SHOULD BE HANDLED BY EXCEPTION HANDLER");
			}
		};

		final ExceptionHandler<Message> exceptionThrowingExceptionHandler = new ExceptionHandler<Message>() {
			@Override
			public ProcessingResult handle(final Message message,
					final Exception exception) throws Exception {
				throw new UnsupportedOperationException("SHOULD BE RETHROWN");
			}
		};

		final MessageProcessor<Message> classUnderTest = new MessageProcessingChain<Message>(
				exceptionThrowingProcessor, exceptionThrowingExceptionHandler);

		classUnderTest.process(new SampleMessage());
	}
}
