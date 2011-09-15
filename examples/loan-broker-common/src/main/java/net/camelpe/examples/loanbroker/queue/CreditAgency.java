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
public class CreditAgency implements Processor {

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void process(final Exchange exchange) throws Exception {
		this.log.info("Processing credit agency request [{}] ...",
				exchange.getIn());

		final String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN,
				String.class);
		final int score = (int) (Math.random() * 600 + 300);
		final int hlength = (int) (Math.random() * 19 + 1);
		exchange.getOut().setHeader(Constants.PROPERTY_SCORE, score);
		exchange.getOut().setHeader(Constants.PROPERTY_HISTORYLENGTH, hlength);
		exchange.getOut().setHeader(Constants.PROPERTY_SSN, ssn);
		exchange.getOut().setBody("CreditAgency processed the request.");

		this.log.info("Processed credit agency request [{}]. Response: [{}]",
				exchange.getOut());
	}
}
