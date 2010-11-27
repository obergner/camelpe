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

package net.camelpe.extension.advanced_samples;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * <p>
 * TODO: Insert short summary for AdvancedProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class AdvancedProcessor implements Processor {

	private final AtomicInteger counter = new AtomicInteger(0);

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception {
		this.counter.incrementAndGet();
	}

	public AtomicInteger getCounter() {
		return this.counter;
	}
}
