/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter_samples;

import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.spi.TypeConverterRegistry;

/**
 * <p>
 * TODO: Insert short summary for StaticMethodFallbackTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@FallbackConverter
public class StaticMethodFallbackTypeConverter {

	@FallbackConverter
	public static <T> T convertTo(final Class<T> typeToConvertTo,
			final Exchange exchange, final Object value,
			final TypeConverterRegistry registry) {
		if (typeToConvertTo == String.class) {
			return (T) value.toString();
		}

		return null;
	}
}
