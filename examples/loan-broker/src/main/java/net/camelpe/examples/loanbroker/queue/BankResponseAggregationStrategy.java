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
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BankResponseAggregationStrategy implements AggregationStrategy {

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	// Here we put the bank response together
	@Override
	public Exchange aggregate(final Exchange oldExchange,
			final Exchange newExchange) {
		this.log.debug("Get the exchange to aggregate, older: " + oldExchange
				+ " newer:" + newExchange);

		// the first time we only have the new exchange
		if (oldExchange == null) {
			return newExchange;
		}

		final Message oldMessage = oldExchange.getIn();
		final Message newMessage = newExchange.getIn();

		final Double oldRate = oldMessage.getHeader(Constants.PROPERTY_RATE,
				Double.class);
		final Double newRate = newMessage.getHeader(Constants.PROPERTY_RATE,
				Double.class);

		final Exchange result;
		if (newRate >= oldRate) {
			result = oldExchange;
		} else {
			result = newExchange;
		}

		this.log.debug("Get the lower rate exchange " + result);

		return result;
	}

}
