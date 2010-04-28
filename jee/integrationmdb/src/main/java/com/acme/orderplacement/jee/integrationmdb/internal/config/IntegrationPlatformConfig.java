/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.acme.orderplacement.service.item.ItemStorageService;

/**
 * <p>
 * TODO: Insert short summary for IntegrationPlatformConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: I *suspect* that some of the components defined here are
 *         redundant. Clean up.
 */
@Configuration
public class IntegrationPlatformConfig {

	public static final String ACTIVEMQ_JMS_COMPONENT_COMPONENT_NAME = "integration.inbound.item.ActiveMQ";

	public static final String JMS_COMPONENT_COMPONENT_NAME = "integration.inbound.item.Jms";

	public static final String GERONIMO_JMS_CONNECTION_FACTORY_COMPONENT_NAME = "integration.inbound.geronimo.JmsConnectionFactory";

	public static final String ITEM_CREATED_EVENTS_ERROR_QUEUE_COMPONENT_NAME = "integration.inbound.geronimo.ItemCreatedEventsErrorQueue";

	public static final String ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME = "integration.inbound.item.ItemStorageServiceDelegate";

	private static final String ITEM_STORAGE_SERVICE_JNDI_NAME = "java:comp/env/ejb/com/acme/orderplacment/item/ItemStorageService";

	private static final String ITEM_CREATED_EVENTS_ERROR_QUEUE_JNDI_NAME = "jca:/com.acme.orderplacement.jee/orderplacement.jee.ear/JCAAdminObject/jms/queue/com/acme/ItemCreatedEventsErrorQueue";

	private static final String GERONIMO_JMS_CONNECTION_FACTORY_JNDI_NAME = "jca:/com.acme.orderplacement.jee/orderplacement.jee.ear/JCAManagedConnectionFactory/jms/com/acme/ConnectionFactory";

	private static final String BROKER_URL = "tcp://localhost:61616";

	@Bean(name = IntegrationPlatformConfig.ACTIVEMQ_JMS_COMPONENT_COMPONENT_NAME)
	public ActiveMQComponent activeMQJmsComponent() {
		final ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setBrokerURL(BROKER_URL);

		return activeMQComponent;
	}

	@Bean(name = IntegrationPlatformConfig.JMS_COMPONENT_COMPONENT_NAME)
	public JmsComponent jmsComponent() {
		final JmsComponent jmsComponent = new JmsComponent();
		jmsComponent.setConnectionFactory(jmsConnectionFactory());

		return jmsComponent;
	}

	private ConnectionFactory jmsConnectionFactory() {

		return new ActiveMQConnectionFactory(BROKER_URL);
	}

	@Bean(name = IntegrationPlatformConfig.GERONIMO_JMS_CONNECTION_FACTORY_COMPONENT_NAME)
	public ConnectionFactory geronimoJmsConnectionFactory() {
		final JndiObjectFactoryBean jndiLookup = new JndiObjectFactoryBean();
		jndiLookup.setJndiName(GERONIMO_JMS_CONNECTION_FACTORY_JNDI_NAME);
		jndiLookup.setExpectedType(ConnectionFactory.class);
		jndiLookup.setResourceRef(false);

		return (ConnectionFactory) jndiLookup.getObject();
	}

	@Bean(name = IntegrationPlatformConfig.ITEM_CREATED_EVENTS_ERROR_QUEUE_COMPONENT_NAME)
	public Queue itemCreatedEventsErrorQueue() {
		final JndiObjectFactoryBean jndiLookup = new JndiObjectFactoryBean();
		jndiLookup.setJndiName(ITEM_CREATED_EVENTS_ERROR_QUEUE_JNDI_NAME);
		jndiLookup.setExpectedType(Queue.class);
		jndiLookup.setResourceRef(false);

		return (Queue) jndiLookup.getObject();
	}

	@Bean(name = IntegrationPlatformConfig.ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME)
	public ItemStorageService itemStorageService() {
		final LocalStatelessSessionProxyFactoryBean jndiLookup = new LocalStatelessSessionProxyFactoryBean();
		jndiLookup.setJndiName(ITEM_STORAGE_SERVICE_JNDI_NAME);
		jndiLookup.setBusinessInterface(ItemStorageService.class);
		jndiLookup.setResourceRef(false);

		return (ItemStorageService) jndiLookup.getObject();
	}
}
