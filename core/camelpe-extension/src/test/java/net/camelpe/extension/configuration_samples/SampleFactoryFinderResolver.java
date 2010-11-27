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

package net.camelpe.extension.configuration_samples;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.spi.ClassResolver;
import org.apache.camel.spi.FactoryFinder;
import org.apache.camel.spi.FactoryFinderResolver;

/**
 * <p>
 * TODO: Insert short summary for SampleFactoryFinderResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
public class SampleFactoryFinderResolver implements FactoryFinderResolver {

	/**
	 * @see org.apache.camel.spi.FactoryFinderResolver#resolveDefaultFactoryFinder(org.apache.camel.spi.ClassResolver)
	 */
	@Override
	public FactoryFinder resolveDefaultFactoryFinder(
	        final ClassResolver classResolver) {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.FactoryFinderResolver#resolveFactoryFinder(org.apache.camel.spi.ClassResolver,
	 *      java.lang.String)
	 */
	@Override
	public FactoryFinder resolveFactoryFinder(
	        final ClassResolver classResolver, final String resourcePath) {
		return null;
	}
}
