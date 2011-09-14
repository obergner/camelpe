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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

@ApplicationScoped
public class Banks {

	@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD,
			ElementType.PARAMETER, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public @interface Bank1 {
	}

	@SuppressWarnings("unused")
	@Bank1
	@Produces
	private final Bank bank1 = new Bank("bank 1");

	@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD,
			ElementType.PARAMETER, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public @interface Bank2 {
	}

	@SuppressWarnings("unused")
	@Bank2
	@Produces
	private final Bank bank2 = new Bank("bank 2");

	@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD,
			ElementType.PARAMETER, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public @interface Bank3 {
	}

	@SuppressWarnings("unused")
	@Bank3
	@Produces
	private final Bank bank3 = new Bank("bank 3");
}
