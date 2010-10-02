/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.cdi.spi.bean_samples;

import javax.inject.Named;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingNoEndpointInjectAnnotatedField
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Named(BeanHavingEndpointInjectAnnotatedField.NAME)
public class BeanHavingEndpointInjectAnnotatedField {

	public static final String NAME = "beanHavingEndpointInjectAnnotatedField";

	public static final String ENDPOINT_URI = "mock://endpointInjectEndpoint";

	@EndpointInject(uri = BeanHavingEndpointInjectAnnotatedField.ENDPOINT_URI)
	public ProducerTemplate producerTemplate;
}
