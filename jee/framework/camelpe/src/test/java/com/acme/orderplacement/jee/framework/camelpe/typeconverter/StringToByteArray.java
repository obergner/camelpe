/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.typeconverter;

/**
 * <p>
 * TODO: Insert short summary for StringToByteArray
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class StringToByteArray {

	byte[] toByteArray(final String stringToConvert) {
		return stringToConvert.getBytes();
	}
}
