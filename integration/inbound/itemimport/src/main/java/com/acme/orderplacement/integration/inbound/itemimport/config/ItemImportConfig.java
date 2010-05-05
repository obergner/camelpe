/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.config;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acme.orderplacement.service.item.ItemStorageService;

/**
 * <p>
 * TODO: Insert short summary for ItemImportConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public interface ItemImportConfig {

	/**
	 * Must be &quot;activemq&quot; since this is the component name used by
	 * Camel.
	 */
	String ACTIVEMQ_COMPONENT_COMPONENT_NAME = "activemq";

	String ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME = "integration.inbound.item.ItemStorageServiceDelegate";

	@Bean(name = ItemImportConfig.ACTIVEMQ_COMPONENT_COMPONENT_NAME)
	ActiveMQComponent activeMQComponent();

	@Bean(name = ItemImportConfig.ITEM_STORAGE_SERVICE_DELEGATE_SERVICE_NAME)
	ItemStorageService itemStorageService();

}