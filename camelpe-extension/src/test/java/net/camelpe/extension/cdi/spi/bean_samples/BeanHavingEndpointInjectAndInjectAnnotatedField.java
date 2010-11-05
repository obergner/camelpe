/**
 * 
 */
package net.camelpe.extension.cdi.spi.bean_samples;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.camel.EndpointInject;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingNoEndpointInjectAnnotatedField
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanHavingEndpointInjectAndInjectAnnotatedField {

    @Inject
    @EndpointInject(uri = "ignore")
    public BeanManager injectionValue;
}
