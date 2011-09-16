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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import net.camelpe.hornetq.HornetQCamelComponent;

import org.apache.camel.component.jms.JmsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CamelContextConfiguration {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Named("jms")
	@Produces
	public HornetQCamelComponent jmsComponent() {
		final JmsConfiguration jmsConfiguration = new JmsConfiguration();
		jmsConfiguration.setTestConnectionOnStartup(false);
		this.log.info(
				"Will return HornetQCamelComponent using configuration [{}]",
				jmsConfiguration);
		return new HornetQCamelComponent(jmsConfiguration);
	}
}
