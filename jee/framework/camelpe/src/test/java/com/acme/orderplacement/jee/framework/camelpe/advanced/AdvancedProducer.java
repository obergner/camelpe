/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.advanced;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

/**
 * <p>
 * TODO: Insert short summary for AdvancedProducer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class AdvancedProducer {

	@Produce(uri = AdvancedRoutes.ADVANCED_SOURCE_EP)
	private ProducerTemplate template;

	public void sendBody(final Date date) {
		this.template.sendBody(date);
	}
}
