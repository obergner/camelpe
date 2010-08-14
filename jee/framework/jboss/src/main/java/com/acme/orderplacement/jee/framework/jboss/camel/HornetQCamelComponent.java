/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.jms.client.HornetQConnectionFactory;

/**
 * <p>
 * TODO: Insert short summary for HornetQCamelComponent
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class HornetQCamelComponent extends JmsComponent {

	public HornetQCamelComponent() {
		setConnectionFactory(createHornetQConnectionFactory());
	}

	public HornetQCamelComponent(final CamelContext context) {
		super(context);
		setConnectionFactory(createHornetQConnectionFactory());
	}

	private HornetQConnectionFactory createHornetQConnectionFactory() {
		return HornetQJMSClient
				.createConnectionFactory(new TransportConfiguration(
						"org.hornetq.core.remoting.impl.netty.NettyConnectorFactory"));
	}
}
