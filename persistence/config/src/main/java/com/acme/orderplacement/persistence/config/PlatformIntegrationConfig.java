/**
 * 
 */
package com.acme.orderplacement.persistence.config;

import javax.management.MBeanServer;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * <p>
 * TODO: Insert short summary for PlatformIntegrationConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public interface PlatformIntegrationConfig {

	String TXMANAGER_COMPONENT_NAME = "persistence.support.platform.transactionManager";

	String DATASOURCE_COMPONENT_NAME = "persistence.support.platform.dataSource";

	String MBEAN_SERVER_COMPONENT_NAME = "persistence.support.platform.mbeanServer";

	@Bean(name = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME)
	PlatformTransactionManager transactionManager() throws Exception;

	@Bean(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	DataSource applicationDataSource() throws Exception;

	@Bean(name = PlatformIntegrationConfig.MBEAN_SERVER_COMPONENT_NAME)
	MBeanServer platformMBeanServer();
}