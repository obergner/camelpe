/**
 * 
 */
package com.acme.orderplacement.jee.app.config.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.jee.framework.camelpe.weld.jms.WeldRequestContextInitiatingJmsConfiguration;
import com.acme.orderplacement.jee.framework.jboss.camel.HornetQCamelComponent;

/**
 * <p>
 * TODO: Insert short summary for ComponentFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
class ComponentFactory {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Produces
	@Named(HornetQCamelComponent.URI_PREFIX)
	public Component newHornetQComponent(final CamelContext camelContext) {
		this.log
				.trace(
						"About to construct a new customized [{}] instance using CamelContext [{}] ...",
						HornetQCamelComponent.class.getName(), camelContext);

		/*
		 * Make this JMS component use a custom JmsConfiguration which takes
		 * care of creating a new Weld RequestContext (optionally terminating an
		 * already active one) before each task execution.
		 */
		final HornetQCamelComponent hornetQComponent = new HornetQCamelComponent(
				camelContext,
				new WeldRequestContextInitiatingJmsConfiguration());

		this.log.trace(
				"Successfully constructed a new customized [{}] instance [{}]",
				HornetQCamelComponent.class.getName(), hornetQComponent);

		return hornetQComponent;
	}
}
