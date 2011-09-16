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
package net.camelpe.examples.jboss7.loanbroker.queue;

import java.lang.management.ManagementFactory;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.camelpe.examples.loanbroker.queue.JmsResources;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.destination.DestinationResolutionException;
import org.springframework.jms.support.destination.DestinationResolver;

@ApplicationScoped
public class JBoss7CamelContextConfiguration {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connectionFactory;

	@Named("jms")
	@Produces
	public JmsComponent jmsComponent() {
		final JmsConfiguration jmsConfiguration = new JmsConfiguration();
		jmsConfiguration.setConnectionFactory(this.connectionFactory);
		jmsConfiguration.setTestConnectionOnStartup(false);
		jmsConfiguration.setDestinationResolver(new JndiDestinationResolver());
		this.log.info("Will return JmsComponent using configuration [{}]",
				jmsConfiguration);
		return new JmsComponent(jmsConfiguration);
	}

	@PostConstruct
	public void createQueues() throws Exception {
		this.log.info("Creating queues ...");
		final MBeanServer mbeanServer = ManagementFactory
				.getPlatformMBeanServer();
		this.log.info("Found PlatformMBeanServer [{}]", mbeanServer);
		this.log.info("JMX Domains: {}",
				Arrays.toString(mbeanServer.getDomains()));
		for (final JmsResources.QueueDefinition queueDef : JmsResources
				.loanBrokerQueues()) {
			mbeanServer.invoke(new ObjectName(
					"org.hornetq:module=JMS,type=Server"), "createQueue",
					new Object[] { queueDef.getName(), queueDef.getBinding() },
					new String[] { "java.lang.String", "java.lang.String" });
			this.log.info("Created queue [{}]", queueDef);
		}
	}

	private static final class JndiDestinationResolver implements
			DestinationResolver {

		private final Logger log = LoggerFactory.getLogger(getClass());

		private InitialContext initialContext;

		@Override
		public Destination resolveDestinationName(final Session session,
				final String destinationName, final boolean pubSubDomain)
				throws JMSException {
			try {
				this.log.info("Looking up destination [name = {}] ...",
						destinationName);
				final Destination resolvedDestination = Destination.class
						.cast(getInitialContext().lookup(
								JmsResources.QueueDefinition
										.getBindingFor(destinationName)));
				this.log.info("Destination name [{}] resolved to [{}]",
						destinationName, resolvedDestination);
				return resolvedDestination;
			} catch (final Exception e) {
				System.out
						.println("###################################### ERROR: "
								+ e.getLocalizedMessage());
				e.printStackTrace();
				throw new DestinationResolutionException(
						"Failed to resolve destination name = ["
								+ destinationName + "]: " + e.getMessage(), e);
			}
		}

		private synchronized InitialContext getInitialContext()
				throws NamingException {
			if (this.initialContext == null) {
				this.initialContext = new InitialContext();
			}

			return this.initialContext;
		}
	}
}
