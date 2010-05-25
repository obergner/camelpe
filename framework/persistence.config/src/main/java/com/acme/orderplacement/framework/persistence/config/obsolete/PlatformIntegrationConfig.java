/**
 * 
 */
package com.acme.orderplacement.framework.persistence.config.obsolete;

import javax.management.MBeanServer;
import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * <p>
 * TODO: Insert short summary for PlatformIntegrationConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface PlatformIntegrationConfig {

	String TXMANAGER_COMPONENT_NAME = "persistence.support.platform.transactionManager";

	String DATASOURCE_COMPONENT_NAME = "persistence.support.platform.dataSource";

	String MBEAN_SERVER_COMPONENT_NAME = "persistence.support.platform.mbeanServer";

	PlatformTransactionManager transactionManager() throws Exception;

	DataSource applicationDataSource() throws Exception;

	MBeanServer platformMBeanServer();
}