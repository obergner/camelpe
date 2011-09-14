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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Qualifier;

public class JmsResources {

	@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD,
			ElementType.PARAMETER, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public @interface Jms {
	}

	public static final class QueueDefinition {

		private final String name;

		private QueueDefinition(final String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getBinding() {
			return "/queue/" + this.name;
		}

		public String getCamelUri() {
			return "jms:queue:" + getName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((this.name == null) ? 0 : this.name.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final QueueDefinition other = (QueueDefinition) obj;
			if (this.name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!this.name.equals(other.name)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "QueueDefinition@" + hashCode() + "[name = " + this.name
					+ "|binding = " + getBinding() + "|camelUri = "
					+ getCamelUri() + "]";
		}
	}

	public static final QueueDefinition CREDIT_REQUEST_QUEUE = new QueueDefinition(
			"creditRequestQueue");

	public static final QueueDefinition CREDIT_RESPONSE_QUEUE = new QueueDefinition(
			"creditResponseQueue");

	public static final QueueDefinition LOAN_REQUEST_QUEUE = new QueueDefinition(
			"loanRequestQueue");

	public static final QueueDefinition LOAN_REPLY_QUEUE = new QueueDefinition(
			"loanReplyQueue");

	public static final QueueDefinition BANK1_QUEUE = new QueueDefinition(
			"bank1Queue");

	public static final QueueDefinition BANK2_QUEUE = new QueueDefinition(
			"bank12Queue");

	public static final QueueDefinition BANK3_QUEUE = new QueueDefinition(
			"bank3Queue");

	public static final QueueDefinition BANK_REPLY_QUEUE = new QueueDefinition(
			"bankReplyQueue");

	public static List<QueueDefinition> loanBrokerQueues() {
		return Collections.unmodifiableList(Arrays.asList(CREDIT_REQUEST_QUEUE,
				CREDIT_RESPONSE_QUEUE, LOAN_REQUEST_QUEUE, LOAN_REPLY_QUEUE,
				BANK1_QUEUE, BANK2_QUEUE, BANK3_QUEUE, BANK_REPLY_QUEUE));
	}
}
