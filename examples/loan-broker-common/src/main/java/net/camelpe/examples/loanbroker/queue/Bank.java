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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bank implements Processor {

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	private final String bankName;

	private final double primeRate;

	public Bank(final String name) {
		this.bankName = name;
		this.primeRate = 3.5;
	}

	@Override
	public void process(final Exchange exchange) throws Exception {
		final String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN,
				String.class);
		final Integer historyLength = exchange.getIn().getHeader(
				Constants.PROPERTY_HISTORYLENGTH, Integer.class);
		final double rate = this.primeRate + (double) (historyLength / 12) / 10
				+ (Math.random() * 10) / 10;
		this.log.info("The bank: " + this.bankName + " for client: " + ssn
				+ " 's rate " + rate);

		exchange.getOut().setHeader(Constants.PROPERTY_RATE, new Double(rate));
		exchange.getOut().setHeader(Constants.PROPERTY_BANK, this.bankName);
		exchange.getOut().setHeader(Constants.PROPERTY_SSN, ssn);
		exchange.getOut().setBody("Bank processed the request.");

		// Sleep some time
		try {
			Thread.sleep((long) (Math.random() * 10) * 100);
		} catch (final InterruptedException e) {
			// Discard
		}
	}

}
