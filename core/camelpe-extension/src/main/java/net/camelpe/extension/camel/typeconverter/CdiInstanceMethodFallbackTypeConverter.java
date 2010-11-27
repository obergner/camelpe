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

package net.camelpe.extension.camel.typeconverter;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.util.ObjectHelper;

/**
 * <p>
 * TODO: Insert short summary for CdiInstanceMethodFallbackTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiInstanceMethodFallbackTypeConverter implements TypeConverter {

	private final Injector injector;

	private final Method method;

	private final boolean useExchange;

	private final TypeConverterRegistry registry;

	public CdiInstanceMethodFallbackTypeConverter(final Injector injector,
	        final Method method, final TypeConverterRegistry registry) {
		this.injector = injector;
		this.method = method;
		this.useExchange = method.getParameterTypes().length == 4;
		this.registry = registry;
	}

	@Override
	public <T> T convertTo(final Class<T> type, final Object value) {
		return convertTo(type, null, value);
	}

	@Override
	public <T> T convertTo(final Class<T> type, final Exchange exchange,
	        final Object value) {
		final Object instance = this.injector.newInstance(this.method
		        .getDeclaringClass());
		if (instance == null) {
			throw new RuntimeCamelException(
			        "Could not instantiate an instance of: "
			                + type.getCanonicalName());
		}
		return this.useExchange ? (T) ObjectHelper.invokeMethod(this.method,
		        instance, type, exchange, value, this.registry)
		        : (T) ObjectHelper.invokeMethod(this.method, instance, type,
		                value, this.registry);
	}

	@Override
	public <T> T mandatoryConvertTo(final Class<T> type, final Object value) {
		return convertTo(type, null, value);
	}

	@Override
	public <T> T mandatoryConvertTo(final Class<T> type,
	        final Exchange exchange, final Object value) {
		return convertTo(type, null, value);
	}

	@Override
	public String toString() {
		return "CdiInstanceMethodFallbackTypeConverter[method = " + this.method
		        + " | useExchange = " + this.useExchange + "]";
	}
}
