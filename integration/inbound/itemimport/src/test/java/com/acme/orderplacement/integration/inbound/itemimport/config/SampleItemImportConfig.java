/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.config;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class SampleItemImportConfig implements ItemImportConfig {

	private static final String BROKER_URL = "vm://localhost?broker.persistent=false";

	@Resource(name = ItemStorageService.SERVICE_NAME)
	private ItemStorageService itemStorageService;

	/**
	 * @see com.acme.orderplacement.integration.inbound.itemimport.config.ItemImportConfig#activeMQComponent()
	 */
	@Bean(name = ItemImportConfig.ACTIVEMQ_COMPONENT_COMPONENT_NAME)
	public ActiveMQComponent activeMQComponent() {
		final ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(jmsConnectionFactory());

		return activeMQComponent;
	}

	private ConnectionFactory jmsConnectionFactory() {
		return new ActiveMQConnectionFactory(BROKER_URL);
	}

	/**
	 * @see com.acme.orderplacement.integration.inbound.itemimport.config.ItemImportConfig#itemStorageService()
	 */
	@Bean(name = ItemImportConfig.ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME)
	public ItemStorageService itemStorageService() {

		return this.itemStorageService;
	}
}
