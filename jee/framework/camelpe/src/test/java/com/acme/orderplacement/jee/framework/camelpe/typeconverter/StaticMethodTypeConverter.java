/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for StaticMethodTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class StaticMethodTypeConverter {

	@Converter
	public static String convertToString(final Object objectToConvert) {
		return objectToConvert.toString();
	}
}
