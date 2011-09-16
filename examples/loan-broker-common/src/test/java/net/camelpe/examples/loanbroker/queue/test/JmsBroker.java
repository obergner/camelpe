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
package net.camelpe.examples.loanbroker.queue.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import net.camelpe.examples.loanbroker.queue.JmsResources;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.integration.logging.Log4jLogDelegateFactory;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.JMSQueueConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsBroker {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String CONNECTION_FACTORY_BINDING = "/cf";

	private static final String CONNECTION_FACTORY_NAME = "cf";

	private EmbeddedJMS jmsServer;

	/**
	 * @throws java.lang.Exception
	 */
	@PostConstruct
	public void start() throws Exception {
		startHornetQServer();
	}

	private void startHornetQServer() throws Exception {
		this.log.info("Starting embedded HornetQ server ...");

		org.hornetq.core.logging.Logger
				.setDelegateFactory(new Log4jLogDelegateFactory());

		final Configuration configuration = configureCore();

		final JMSConfiguration jmsConfig = configureJms();

		configureQueues(jmsConfig);

		this.jmsServer = new EmbeddedJMS();
		this.jmsServer.setConfiguration(configuration);
		this.jmsServer.setJmsConfiguration(jmsConfig);
		this.jmsServer.start();

		this.log.info("Embedded HornetQ server [{}] started", this.jmsServer);
	}

	private Configuration configureCore() {
		final Configuration configuration = new ConfigurationImpl();

		configuration.setPersistenceEnabled(false);
		configuration.setSecurityEnabled(false);
		configuration.getAcceptorConfigurations()
				.add(new TransportConfiguration(NettyAcceptorFactory.class
						.getName()));
		configuration.setJournalDirectory("target/data/journal");

		final TransportConfiguration connectorConfig = new TransportConfiguration(
				NettyConnectorFactory.class.getName());
		configuration.getConnectorConfigurations().put("connector",
				connectorConfig);

		return configuration;
	}

	private JMSConfiguration configureJms() {
		final JMSConfiguration jmsConfig = new JMSConfigurationImpl();

		// Step 3. Configure the JMS ConnectionFactory
		final List<String> connectorNames = new ArrayList<String>();
		connectorNames.add("connector");
		final ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl(
				CONNECTION_FACTORY_NAME, false, connectorNames,
				CONNECTION_FACTORY_BINDING);
		jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

		return jmsConfig;
	}

	private void configureQueues(final JMSConfiguration jmsConfig) {
		for (final JmsResources.QueueDefinition queueDefinition : JmsResources
				.loanBrokerQueues()) {
			final JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl(
					queueDefinition.getName(), null, false,
					queueDefinition.getBinding());
			jmsConfig.getQueueConfigurations().add(queueConfig);
			this.log.debug("Added {}", queueDefinition);
		}
	}

	public ConnectionFactory getConnectionFactory() {
		return (ConnectionFactory) this.jmsServer
				.lookup(CONNECTION_FACTORY_BINDING);
	}

	public Queue queueBoundTo(final String queueBinding) {
		return (Queue) this.jmsServer.lookup(queueBinding);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@PreDestroy
	public void stop() throws Exception {
		stopHornetQServer();
	}

	private void stopHornetQServer() throws Exception {
		this.log.info("Stopping embedded HornetQ server [{}] ...",
				this.jmsServer);

		this.jmsServer.stop();

		this.log.info("Embedded HornetQ server [{}] stopped", this.jmsServer);
	}
}
