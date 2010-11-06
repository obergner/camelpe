/**
 * 
 */
package net.camelpe.extension.camel.typeconverter;

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
public class InstanceMethodTypeConverterHavingNoInjectionPoints {

    @Converter
    public String convertToString(final Object objectToConvert) {
        return objectToConvert.toString();
    }
}
