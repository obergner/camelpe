/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.scontext;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.management.MBeanServer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.acme.orderplacement.service.item.ItemStorageService;

/**
 * <p>
 * TODO: Insert short summary for IntegrationPlatformContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class IntegrationPlatformContext {

	public static final String ACTIVEMQ_JMS_COMPONENT_COMPONENT_NAME = "integration.inbound.item.ActiveMQ";

	public static final String JMS_COMPONENT_COMPONENT_NAME = "integration.inbound.item.Jms";

	public static final String GERONIMO_JMS_CONNECTION_FACTORY_COMPONENT_NAME = "integration.inbound.geronimo.JmsConnectionFactory";

	public static final String ITEM_CREATED_EVENTS_ERROR_QUEUE_COMPONENT_NAME = "integration.inbound.geronimo.ItemCreatedEventsErrorQueue";

	public static final String ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME = "integration.inbound.item.ItemStorageServiceDelegate";

	public static final String PLATFORM_MBEAN_SERVER_COMPONENT_NAME = "integration.inbound.item.jmx.MBeanServer";

	private static final String ITEM_STORAGE_SERVICE_JNDI_NAME = "java:comp/env/ejb/com/acme/orderplacment/item/ItemStorageService";

	private static final String ITEM_CREATED_EVENTS_ERROR_QUEUE_JNDI_NAME = "jca:/com.acme.orderplacement.jee/orderplacement.jee.ear/JCAAdminObject/jms/queue/com/acme/ItemCreatedEventsErrorQueue";

	private static final String GERONIMO_JMS_CONNECTION_FACTORY_JNDI_NAME = "jca:/com.acme.orderplacement.jee/orderplacement.jee.ear/JCAManagedConnectionFactory/jms/com/acme/ConnectionFactory";

	private static final String BROKER_URL = "tcp://localhost:61616";

	/**
	 * <tt>Geronimo</tt>'s <code>MBeanServer</code>'s <i>default domain</i>,
	 * used to select the appropriate <code>MBeanServer</code> from the list of
	 * available <code>MBeanServer</code>s.
	 */
	private static final String GERONIMO_DEFAULT_DOMAIN = "DefaultDomain";

	@Bean(name = IntegrationPlatformContext.ACTIVEMQ_JMS_COMPONENT_COMPONENT_NAME)
	public ActiveMQComponent activeMQJmsComponent() {
		final ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setBrokerURL(BROKER_URL);

		return activeMQComponent;
	}

	@Bean(name = IntegrationPlatformContext.JMS_COMPONENT_COMPONENT_NAME)
	public JmsComponent jmsComponent() {
		final JmsComponent jmsComponent = new JmsComponent();
		jmsComponent.setConnectionFactory(jmsConnectionFactory());

		return jmsComponent;
	}

	private ConnectionFactory jmsConnectionFactory() {

		return new ActiveMQConnectionFactory(BROKER_URL);
	}

	@Bean(name = IntegrationPlatformContext.GERONIMO_JMS_CONNECTION_FACTORY_COMPONENT_NAME)
	public ConnectionFactory geronimoJmsConnectionFactory() {
		final JndiObjectFactoryBean jndiLookup = new JndiObjectFactoryBean();
		jndiLookup.setJndiName(GERONIMO_JMS_CONNECTION_FACTORY_JNDI_NAME);
		jndiLookup.setExpectedType(ConnectionFactory.class);
		jndiLookup.setResourceRef(false);

		return (ConnectionFactory) jndiLookup.getObject();
	}

	@Bean(name = IntegrationPlatformContext.ITEM_CREATED_EVENTS_ERROR_QUEUE_COMPONENT_NAME)
	public Queue itemCreatedEventsErrorQueue() {
		final JndiObjectFactoryBean jndiLookup = new JndiObjectFactoryBean();
		jndiLookup.setJndiName(ITEM_CREATED_EVENTS_ERROR_QUEUE_JNDI_NAME);
		jndiLookup.setExpectedType(Queue.class);
		jndiLookup.setResourceRef(false);

		return (Queue) jndiLookup.getObject();
	}

	@Bean(name = IntegrationPlatformContext.ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME)
	public ItemStorageService itemStorageService() {
		final LocalStatelessSessionProxyFactoryBean jndiLookup = new LocalStatelessSessionProxyFactoryBean();
		jndiLookup.setJndiName(ITEM_STORAGE_SERVICE_JNDI_NAME);
		jndiLookup.setBusinessInterface(ItemStorageService.class);
		jndiLookup.setResourceRef(false);

		return (ItemStorageService) jndiLookup.getObject();
	}

	@Bean(name = IntegrationPlatformContext.PLATFORM_MBEAN_SERVER_COMPONENT_NAME)
	public MBeanServer platformBeanServer() {
		final MBeanServerFactoryBean mbeanServerLookup = new MBeanServerFactoryBean();
		mbeanServerLookup.setLocateExistingServerIfPossible(true);
		mbeanServerLookup.setDefaultDomain(GERONIMO_DEFAULT_DOMAIN);
		mbeanServerLookup.afterPropertiesSet();

		return mbeanServerLookup.getObject();
	}
}
