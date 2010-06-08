/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

/**
 * <p>
 * TODO: Insert short summary for SampleProducer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class SampleProducer {

	@Produce(uri = SampleRoutes.SAMPLE_SOURCE_EP)
	private ProducerTemplate template;

	public void sendBody(final String body) {
		this.template.sendBody(body);
	}
}
