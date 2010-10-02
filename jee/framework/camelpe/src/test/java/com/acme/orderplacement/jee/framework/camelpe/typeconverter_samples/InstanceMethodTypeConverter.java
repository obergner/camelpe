/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter_samples;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for InstanceMethodTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Converter
public class InstanceMethodTypeConverter {

	@Converter
	public String convertToString(final Object objectToConvert) {
		return objectToConvert.toString();
	}
}
