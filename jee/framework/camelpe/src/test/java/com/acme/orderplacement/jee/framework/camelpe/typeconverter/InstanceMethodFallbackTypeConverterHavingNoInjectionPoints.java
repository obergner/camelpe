/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter;

import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.spi.TypeConverterRegistry;

/**
 * <p>
 * TODO: Insert short summary for
 * InstanceMethodFallbackTypeConverterHavingNoInjectionPoints
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class InstanceMethodFallbackTypeConverterHavingNoInjectionPoints {

	@FallbackConverter
	public <T> T convertTo(final Class<T> typeToConvertTo,
			final Exchange exchange, final Object value,
			final TypeConverterRegistry registry) {
		if (typeToConvertTo == String.class) {
			return (T) value.toString();
		}

		return null;
	}
}
