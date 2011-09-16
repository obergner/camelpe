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
package net.camelpe.examples.jboss7.loanbroker.queue;

import java.io.File;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import net.camelpe.examples.loanbroker.queue.Constants;
import net.camelpe.examples.loanbroker.queue.JmsResources;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.filter.ScopeFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author obergner
 * 
 */
@RunWith(Arquillian.class)
public class LoanBrokerJboss7InContainerTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoanBrokerJboss7InContainerTest.class);

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connectionFactory;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment(testable = true)
	public static WebArchive createTestArchive() throws Exception {
		final WebArchive testModule = ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClass(JBoss7CamelContextConfiguration.class)
				.addAsWebInfResource(
						new File("src/main/webapp/WEB-INF/beans.xml"))
				.addAsResource("log4j.xml")
				.addAsLibraries(
						DependencyResolvers
								.use(MavenDependencyResolver.class)
								.includeDependenciesFromPom("pom.xml")
								.resolveAs(JavaArchive.class,
										new ScopeFilter("compile", "runtime")));
		LOG.info("Deploy test module: " + testModule.toString(true));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatLoanBrokerProcessesLoanRequest() throws Exception {
		final String ssn = "Client-A";
		final String loanRequest = "Request quote for lowest rate of lending bank";

		final LoanReplyReceiver loanReplyListener = new LoanReplyReceiver(
				this.connectionFactory);
		loanReplyListener.start();

		final LoanRequestSender loanRequestSender = new LoanRequestSender(
				this.connectionFactory);
		loanRequestSender.start();
		loanRequestSender.requestLoan(ssn, loanRequest);
		loanRequestSender.stop();

		final TextMessage receivedLoanReply = loanReplyListener.receive(10000);
		LOG.info("Received reply {} - Body = [{}]", receivedLoanReply,
				receivedLoanReply.getText());

		loanReplyListener.stop();
	}

	// -------------------------------------------------------------------------
	// JMS client
	// -------------------------------------------------------------------------

	private static class LoanRequestSender {

		private final Logger log = LoggerFactory.getLogger(getClass());

		private final ConnectionFactory connectionFactory;

		private Connection connection;

		private MessageProducer messageProducer;

		private Session session;

		private LoanRequestSender(final ConnectionFactory connectionFactory) {
			super();
			this.connectionFactory = connectionFactory;
		}

		public void start() throws Exception {
			try {
				this.log.info("Connecting LoanRequestSender ...");

				this.connection = this.connectionFactory.createConnection();
				this.log.info("Connection [{}] created", this.connection);

				this.session = this.connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				this.log.info("Session [{}] created", this.session);

				final InitialContext ic = new InitialContext();
				final Queue loanRequestQueue = (Queue) ic
						.lookup(JmsResources.LOAN_REQUEST_QUEUE.getBinding());
				this.log.info("Looked up loanRequestQueue [{}]",
						loanRequestQueue);

				this.messageProducer = this.session
						.createProducer(loanRequestQueue);
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

	private static class LoanReplyReceiver {

		private final Logger log = LoggerFactory.getLogger(getClass());

		private final ConnectionFactory connectionFactory;

		private Connection connection;

		private MessageConsumer messageConsumer;

		LoanReplyReceiver(final ConnectionFactory connectionFactory) {
			this.connectionFactory = connectionFactory;
		}

		public void start() throws Exception {
			try {
				this.log.info("Connecting LoanReplyReceiver ...");

				this.connection = this.connectionFactory.createConnection();
				this.log.info("Connection [{}] created", this.connection);

				final Session session = this.connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				this.log.info("Session [{}] created", session);

				final InitialContext ic = new InitialContext();
				final Queue loanReplyQueue = (Queue) ic
						.lookup(JmsResources.LOAN_REPLY_QUEUE.getBinding());
				this.log.info("Looked up loanReplyQueue [{}]", loanReplyQueue);

				this.messageConsumer = session.createConsumer(loanReplyQueue);
				this.log.info("MessageConsumer [{}] created",
						this.messageConsumer);

				this.connection.start();
				this.log.info(
						"LoanReplyReceiver connected [connection = {}]. Ready to receive messages.",
						this.connection);
			} catch (final Exception e) {
				if (this.connection != null) {
					this.connection.close();
					this.connection = null;
				}
				throw e;
			}
		}

		public TextMessage receive(final long timeoutMillis)
				throws JMSException {
			return TextMessage.class.cast(this.messageConsumer
					.receive(timeoutMillis));
		}

		public void stop() throws Exception {
			this.log.info("Stopping LoanReplyReceiver ...");

			this.connection.close();

			this.log.info("LoanReplyReceiver stopped");
		}
	}
}
