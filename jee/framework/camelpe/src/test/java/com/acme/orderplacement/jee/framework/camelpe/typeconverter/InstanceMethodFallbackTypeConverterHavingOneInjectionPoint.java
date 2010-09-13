/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.spi.TypeConverterRegistry;

/**
 * <p>
 * TODO: Insert short summary for
 * InstanceMethodTypeConverterHavingOneInjectionPoint
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class InstanceMethodFallbackTypeConverterHavingOneInjectionPoint {

	@Inject
	private StringToByteArray stringToByteArray;

	@FallbackConverter
	public <T> T convertTo(final Class<T> typeToConvertTo,
			final Exchange exchange, final Object value,
			final TypeConverterRegistry registry) {
		if (typeToConvertTo == byte[].class) {
			return (T) this.stringToByteArray.toByteArray(value.toString());
		}

		return null;
	}
}
