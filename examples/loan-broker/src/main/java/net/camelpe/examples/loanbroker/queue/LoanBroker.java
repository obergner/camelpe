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

import static net.camelpe.examples.loanbroker.queue.JmsResources.BANK1_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.BANK2_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.BANK3_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.BANK_REPLY_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.CREDIT_REQUEST_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.CREDIT_RESPONSE_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.LOAN_REPLY_QUEUE;
import static net.camelpe.examples.loanbroker.queue.JmsResources.LOAN_REQUEST_QUEUE;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * The LoanBroker is a RouteBuilder which builds the whole loan message routing
 * rules
 * 
 * @version
 */
public class LoanBroker extends RouteBuilder {

	@Banks.Bank1
	@Inject
	private Bank bank1;

	@Banks.Bank2
	@Inject
	private Bank bank2;

	@Banks.Bank3
	@Inject
	private Bank bank3;

	@Inject
	private CreditAgency creditAgency;

	@Inject
	private BankResponseAggregationStrategy bankResponseAggregationStrategy;

	@Inject
	private Translator translator;

	/**
	 * Lets configure the Camel routing rules using Java code...
	 */
	@Override
	public void configure() {
		// Put the message from loanRequestQueue to the creditRequestQueue
		from(LOAN_REQUEST_QUEUE.getCamelUri()).routeId(
				"loanRequest2creditRequest").to(
				CREDIT_REQUEST_QUEUE.getCamelUri());

		// Now we can let the CreditAgency process the request, then the message
		// will be put into creditResponseQueue
		from(CREDIT_REQUEST_QUEUE.getCamelUri())
				.routeId("creditRequestProcessing").process(this.creditAgency)
				.to(CREDIT_RESPONSE_QUEUE.getCamelUri());

		// Here we use the multicast pattern to send the message to three
		// different bank queues
		from(CREDIT_RESPONSE_QUEUE.getCamelUri())
				.routeId("creditResponse2banks")
				.multicast()
				.to(BANK1_QUEUE.getCamelUri(), BANK2_QUEUE.getCamelUri(),
						BANK3_QUEUE.getCamelUri());

		// Each bank processor will process the message and put the response
		// message into the bankReplyQueue
		from(BANK1_QUEUE.getCamelUri()).routeId("bank1Processing")
				.process(this.bank1).to(BANK_REPLY_QUEUE.getCamelUri());
		from(BANK2_QUEUE.getCamelUri()).routeId("bank2Processing")
				.process(this.bank2).to(BANK_REPLY_QUEUE.getCamelUri());
		from(BANK3_QUEUE.getCamelUri()).routeId("bank3Processing")
				.process(this.bank3).to(BANK_REPLY_QUEUE.getCamelUri());

		// Now we aggregate the response message by using the
		// Constants.PROPERTY_SSN header.
		// The aggregation will be complete when all the three bank responses
		// are received
		// In Camel 2.0 the we use AGGERATED_SIZE instead of AGGERATED_COUNT as
		// the header
		// name of the aggregated message size.
		from(BANK_REPLY_QUEUE.getCamelUri())
				.routeId("banks2loanReply")
				.aggregate(header(Constants.PROPERTY_SSN),
						this.bankResponseAggregationStrategy)
				.completionPredicate(
						header(Exchange.AGGREGATED_SIZE).isEqualTo(3))
				// Here we do some translation and put the message back to
				// loanReplyQueue
				.process(this.translator).to(LOAN_REPLY_QUEUE.getCamelUri());
	}
}
