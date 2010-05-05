/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.config;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;

import com.acme.orderplacement.integration.inbound.itemimport.config.ItemImportConfig;
import com.acme.orderplacement.service.item.ItemStorageService;

/**
 * <p>
 * TODO: Insert short summary for SampleItemImportConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class IntegrationPlatformConfig implements ItemImportConfig {

	private static final String ITEM_STORAGE_SERVICE_JNDI_NAME = "java:comp/env/ejb/com/acme/orderplacment/item/ItemStorageService";

	private static final String BROKER_URL = "tcp://localhost:61616";

	@Bean(name = ItemImportConfig.ACTIVEMQ_COMPONENT_COMPONENT_NAME)
	public ActiveMQComponent activeMQComponent() {
		final ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setBrokerURL(BROKER_URL);

		return activeMQComponent;
	}

	@Bean(name = ItemImportConfig.ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME)
	public ItemStorageService itemStorageService() {
		final LocalStatelessSessionProxyFactoryBean jndiLookup = new LocalStatelessSessionProxyFactoryBean();
		jndiLookup.setJndiName(ITEM_STORAGE_SERVICE_JNDI_NAME);
		jndiLookup.setBusinessInterface(ItemStorageService.class);
		jndiLookup.setResourceRef(false);

		return (ItemStorageService) jndiLookup.getObject();
	}
}
