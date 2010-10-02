/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.typeconverter;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for StaticMethodTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Converter
public class StaticMethodTypeConverter {

	@Converter
	public static String convertToString(final Object objectToConvert) {
		return objectToConvert.toString();
	}
}
