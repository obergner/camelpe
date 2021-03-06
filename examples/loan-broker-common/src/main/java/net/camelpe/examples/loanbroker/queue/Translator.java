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
package net.camelpe.examples.loanbroker.queue;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Translator implements Processor {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void process(final Exchange exchange) throws Exception {
		this.log.info("Processing [{}] ...", exchange.getIn());

		final String bank = (String) exchange.getIn().getHeader(
				Constants.PROPERTY_BANK);
		final Double rate = (Double) exchange.getIn().getHeader(
				Constants.PROPERTY_RATE);
		final String ssn = (String) exchange.getIn().getHeader(
				Constants.PROPERTY_SSN);
		exchange.getOut().setBody(
				"Loan quotion for Client " + ssn + "."
						+ " The lowest rate bank is " + bank + ", the rate is "
						+ rate);

		this.log.info("Finished processing [{}]. Response: [{}]",
				exchange.getIn(), exchange.getOut());
	}
}
