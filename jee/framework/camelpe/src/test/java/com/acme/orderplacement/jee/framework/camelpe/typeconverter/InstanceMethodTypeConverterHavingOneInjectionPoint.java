/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter;

import javax.inject.Inject;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for
 * InstanceMethodTypeConverterHavingOneInjectionPoint
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class InstanceMethodTypeConverterHavingOneInjectionPoint {

	@Inject
	private StringToByteArray stringToByteArray;

	@Converter
	public byte[] convertToByteArray(final String stringToConvert) {
		return this.stringToByteArray.toByteArray(stringToConvert);
	}
}
