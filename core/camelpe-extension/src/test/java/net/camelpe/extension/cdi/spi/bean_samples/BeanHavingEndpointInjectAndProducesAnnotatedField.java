/**
 * 
 */
package net.camelpe.extension.cdi.spi.bean_samples;

import javax.enterprise.inject.Produces;

import org.apache.camel.EndpointInject;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingNoEndpointInjectAnnotatedField
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanHavingEndpointInjectAndProducesAnnotatedField {

    @Produces
    @EndpointInject(uri = "ignore")
    public Object producedValue;
}
