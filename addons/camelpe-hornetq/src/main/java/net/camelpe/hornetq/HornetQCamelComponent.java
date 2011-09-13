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
package net.camelpe.hornetq;

import java.util.concurrent.Executor;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.commons.lang.Validate;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.jms.client.HornetQJMSConnectionFactory;
import org.springframework.core.task.support.TaskExecutorAdapter;

/**
 * <p>
 * TODO: Insert short summary for HornetQCamelComponent
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class HornetQCamelComponent extends JmsComponent {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	public static final String URI_PREFIX = "hornetq";

	private static final String NETTY_CONNECTOR_FACTORY = "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory";

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public HornetQCamelComponent(final CamelContext context) {
		super(context);
		setConnectionFactory(createHornetQConnectionFactory());
	}

	public HornetQCamelComponent(final CamelContext context,
			final JmsConfiguration configuration) {
		super(context);
		final HornetQJMSConnectionFactory connectionFactory = createHornetQConnectionFactory();
		setConnectionFactory(connectionFactory);
		configuration.setConnectionFactory(connectionFactory);
		setConfiguration(configuration);
	}

	// -------------------------------------------------------------------------
	// Additional properties
	// -------------------------------------------------------------------------

	public void setExecutor(final Executor executor)
			throws IllegalArgumentException {
		Validate.notNull(executor, "executor");
		getConfiguration().setTaskExecutor(new TaskExecutorAdapter(executor));
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private HornetQJMSConnectionFactory createHornetQConnectionFactory() {
		return HornetQJMSConnectionFactory.class.cast(HornetQJMSClient
				.createConnectionFactoryWithoutHA(JMSFactoryType.CF,
						new TransportConfiguration(NETTY_CONNECTOR_FACTORY)));
	}
}
