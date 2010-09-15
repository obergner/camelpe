/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.typeconverter;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for
 * InstanceMethodTypeConverterHavingNoInjectionPoints
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class InstanceMethodTypeConverterHavingNoInjectionPoints {

	@Converter
	public String convertToString(final Object objectToConvert) {
		return objectToConvert.toString();
	}
}
