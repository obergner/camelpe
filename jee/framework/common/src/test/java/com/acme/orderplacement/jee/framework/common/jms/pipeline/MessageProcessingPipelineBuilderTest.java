/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import static junit.framework.Assert.assertEquals;

import javax.jms.Message;

import org.junit.Test;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingPipelineBuilderTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageProcessingPipelineBuilderTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline.Builder#withGlobalExceptionHandler(com.acme.orderplacement.jee.framework.common.jms.pipeline.ExceptionHandler)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatWithGlobalExceptionHandlerRejectsNullExceptionHandler() {
		final MessageProcessingPipeline.Builder<Message> classUnderTest = MessageProcessingPipeline
				.builder();

		classUnderTest.withGlobalExceptionHandler(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline.Builder#withGlobalExceptionHandler(com.acme.orderplacement.jee.framework.common.jms.pipeline.ExceptionHandler)}
	 * .
	 */
	@Test(expected = IllegalStateException.class)
	public final void assertThatWithGlobalExceptionHandlerRejectsAttemptToSetHandlerMoreThanOnce() {
		final MessageProcessingPipeline.Builder<Message> classUnderTest = MessageProcessingPipeline
				.builder();

		classUnderTest
				.withGlobalExceptionHandler(new ExceptionHandler<Message>() {
					@Override
					public ProcessingResult handle(final Message message,
							final Exception exception) throws Exception {
						return null;
					}
				});

		classUnderTest
				.withGlobalExceptionHandler(new ExceptionHandler<Message>() {
					@Override
					public ProcessingResult handle(final Message message,
							final Exception exception) throws Exception {
						return null;
					}
				});
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline.Builder#append(com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingUnit, com.acme.orderplacement.jee.framework.common.jms.pipeline.ExceptionHandler)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatAppendRejectsNullMessageProcessingUnit() {
		final MessageProcessingPipeline.Builder<Message> classUnderTest = MessageProcessingPipeline
				.builder();

		classUnderTest.append(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jms.pipeline.MessageProcessingPipeline.Builder#build()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void assertThatBuildConstructsChainInCorrectOrder()
			throws Exception {
		final StringBuilder chainOrder = new StringBuilder();

		final MessageProcessingPipeline.Builder<Message> classUnderTest = MessageProcessingPipeline
				.builder();

		classUnderTest.append(new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				chainOrder.append('1');
				return ProcessingResult.CONTINUE;
			}
		});
		classUnderTest.append(new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				chainOrder.append('2');
				return ProcessingResult.CONTINUE;
			}
		});
		classUnderTest.append(new MessageProcessingUnit<Message>() {
			@Override
			public ProcessingResult process(final Message message)
					throws Exception {
				chainOrder.append('3');
				return ProcessingResult.CONTINUE;
			}
		});

		final MessageProcessingPipeline<Message> messageProcessingPipeline = classUnderTest
				.build();
		messageProcessingPipeline.process(new SampleMessage());

		assertEquals("build() did NOT build processing chain in correct order",
				"123", chainOrder.toString());
	}

}
