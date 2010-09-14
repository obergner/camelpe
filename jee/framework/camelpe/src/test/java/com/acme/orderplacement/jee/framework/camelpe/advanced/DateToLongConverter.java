/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.advanced;

import java.util.Date;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for DateToLongConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class DateToLongConverter {

	@Converter
	public Long convert(final Date date) {
		return Long.valueOf(date.getTime());
	}
}
