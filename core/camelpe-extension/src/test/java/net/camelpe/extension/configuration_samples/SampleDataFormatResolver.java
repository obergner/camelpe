/**
 * 
 */
package net.camelpe.extension.configuration_samples;

import net.camelpe.api.CamelContextModifying;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;

/**
 * <p>
 * TODO: Insert short summary for SampleDataFormatResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SampleDataFormatResolver implements DataFormatResolver {

    /**
     * @see org.apache.camel.spi.DataFormatResolver#resolveDataFormat(java.lang.String,
     *      org.apache.camel.CamelContext)
     */
    @Override
    public DataFormat resolveDataFormat(final String name,
            final CamelContext context) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.DataFormatResolver#resolveDataFormatDefinition(java.lang.String,
     *      org.apache.camel.CamelContext)
     */
    @Override
    public DataFormatDefinition resolveDataFormatDefinition(final String name,
            final CamelContext context) {
        return null;
    }
}
