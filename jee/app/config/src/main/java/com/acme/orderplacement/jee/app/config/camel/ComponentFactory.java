/**
 * 
 */
package com.acme.orderplacement.jee.app.config.camel;

import java.util.concurrent.Executor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.support.TaskExecutorAdapter;

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
		final HornetQCamelComponent hornetQComponent = new HornetQCamelComponent(
				camelContext);

		final Executor camelDefaultExecutor = camelContext
				.getExecutorServiceStrategy().lookup(this,
						"JMSMessageListenerContainer",
						"defaultThreadPoolProfile");
		hornetQComponent.setTaskExecutor(new TaskExecutorAdapter(
				camelDefaultExecutor));
		this.log.trace(
				"Successfully constructed a new customized [{}] instance [{}]",
				HornetQCamelComponent.class.getName(), hornetQComponent);

		return hornetQComponent;
	}
}
