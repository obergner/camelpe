/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/**
 * 
 */
package net.camelpe.examples.loanbroker.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.inject.spi.Extension;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.camelpe.examples.loanbroker.queue.test.JmsBroker;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author obergner
 * 
 */
@RunWith(Arquillian.class)
public class LoanBrokerQueueInContainerTest {

	// ------------------------------------------------------------------------
	// Start/stop HornetQ
	// ------------------------------------------------------------------------

	private static final JmsBroker jmsServer = new JmsBroker();

	/*
	 * HACK: I'd prefer to use @BeforeClass, but that's obviously called only
	 * AFTER Weld initializes the BDA. And that't too late.
	 */
	static {
		try {
			jmsServer.start();
		} catch (final Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void stopEmbeddedHornetQBroker() throws Exception {
		jmsServer.stop();
	}

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment(testable = true)
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackages(false, LoanBroker.class.getPackage())
				.addAsServiceProvider(Extension.class, loadCamelExtension())
				.addAsManifestResource(
						new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));

		return testModule;
	}

	private static Class<?> loadCamelExtension() {
		try {
			return Class.forName("net.camelpe.extension.CamelExtension");
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Failed to load CamelExtension: "
					+ e.getMessage(), e);
		}
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatLoanBrokerProcessesLoanRequest() throws Exception {
		final String ssn = "Client-A";
		final String loanRequest = "Request quote for lowest rate of lending bank";

		final CountDownLatch loanReplyReceived = new CountDownLatch(1);
		final AtomicReference<Message> receivedLoanReply = new AtomicReference<Message>();
		final MessageListener loanReplyListenerDelegate = new MessageListener() {
			@Override
			public void onMessage(final Message arg0) {
				receivedLoanReply.set(arg0);
				loanReplyReceived.countDown();
			}
		};
		final LoanReplyListener loanReplyListener = new LoanReplyListener(
				jmsServer, loanReplyListenerDelegate);
		loanReplyListener.start();

		final LoanRequestSender loanRequestSender = new LoanRequestSender(
				jmsServer);
		loanRequestSender.start();
		loanRequestSender.requestLoan(ssn, loanRequest);
		loanRequestSender.stop();

		loanReplyReceived.await();
		loanReplyListener.stop();
	}

	// -------------------------------------------------------------------------
	// JMS client
	// -------------------------------------------------------------------------

	private static class LoanRequestSender {

		private final Logger log = LoggerFactory.getLogger(getClass());

		private final JmsBroker embeddedBroker;

		private Connection connection;

		private MessageProducer messageProducer;

		private Session session;

		LoanRequestSender(final JmsBroker embeddedBroker) {
			this.embeddedBroker = embeddedBroker;
		}

		public void start() throws Exception {
			try {
				this.log.info("Connecting LoanRequestSender ...");

				final ConnectionFactory cf = this.embeddedBroker
						.getConnectionFactory();
				final Queue queue = this.embeddedBroker
						.queueBoundTo(JmsResources.LOAN_REQUEST_QUEUE
								.getBinding());
				this.log.info("Looked up queue [{}]", queue);

				this.connection = cf.createConnection();
				this.log.info("Connection [{}] created", this.connection);

				this.session = this.connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				this.log.info("Session [{}] created", this.session);

				this.messageProducer = this.session.createProducer(queue);
				this.log.info("MessageProducer [{}] created",
						this.messageProducer);

				this.connection.start();
				this.log.info(
						"Connection [{}] started. Ready to send messages.",
						this.connection);
			} catch (final Exception e) {
				if (this.connection != null) {
					this.connection.close();
					this.connection = null;
				}
				throw e;
			}
		}

		public void requestLoan(final String ssn, final String message)
				throws JMSException {
			this.log.info("Sending loan request [ssn = {}|message = {}] ...",
					ssn, message);
			final TextMessage loanRequest = this.session
					.createTextMessage(message);
			loanRequest.setStringProperty(Constants.PROPERTY_SSN, ssn);
			this.messageProducer.send(loanRequest);
			this.log.info(
					"Loan request [ssn = {}|message = {}] successfully sent",
					ssn, message);
		}

		public void stop() throws Exception {
			this.log.info("Stopping LoanRequestSender ...");

			this.connection.close();

			this.log.info("QueueClient LoanRequestSender");
		}
	}

	private static class LoanReplyListener implements MessageListener {

		private final Logger log = LoggerFactory.getLogger(getClass());

		private Connection connection;

		private final JmsBroker embeddedBroker;

		private final MessageListener delegate;

		LoanReplyListener(final JmsBroker embeddedBroker,
				final MessageListener delegate) {
			this.embeddedBroker = embeddedBroker;
			this.delegate = delegate;
		}

		@Override
		public void onMessage(final Message arg0) {
			this.log.info("Received loan reply [{}]", arg0);
			this.delegate.onMessage(arg0);
		}

		public void start() throws Exception {
			try {
				this.log.info("Connecting LoanReplyListener ...");

				final ConnectionFactory cf = this.embeddedBroker
						.getConnectionFactory();
				final Queue queue = this.embeddedBroker
						.queueBoundTo(JmsResources.LOAN_REPLY_QUEUE
								.getBinding());
				this.log.info("Looked up queue [{}]", queue);

				this.connection = cf.createConnection();
				this.log.info("Connection [{}] created", this.connection);

				final Session session = this.connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				this.log.info("Session [{}] created", session);

				final MessageConsumer messageConsumer = session
						.createConsumer(queue);
				messageConsumer.setMessageListener(this);
				this.log.info("MessageConsumer [{}] created", messageConsumer);

				this.connection.start();
				this.log.info(
						"Connection [{}] started. Ready to receive messages.",
						this.connection);
			} catch (final Exception e) {
				if (this.connection != null) {
					this.connection.close();
					this.connection = null;
				}
				throw e;
			}
		}

		public void stop() throws Exception {
			this.log.info("Stopping LoanReplyListener ...");

			this.connection.close();

			this.log.info("LoanReplyListener stopped");
		}
	}
}
