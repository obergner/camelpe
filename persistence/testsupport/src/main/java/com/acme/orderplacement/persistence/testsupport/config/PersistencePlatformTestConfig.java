/**
 * 
 */
package com.acme.orderplacement.persistence.testsupport.config;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.acme.orderplacement.persistence.config.JpaProviderConfig;
import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;
import com.acme.orderplacement.persistence.testsupport.database.spring.PrePopulatingInMemoryH2DataSourceFactory;

/**
 * <p>
 * TODO: Insert short summary for PersistencePlatformTestConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class PersistencePlatformTestConfig implements
		PlatformIntegrationConfig, JpaProviderConfig {

	@Value("#{ testEnvironment['persistence.testsupport.schemaClasspathLocation'] }")
	private String schemaClasspathLocation;

	@Value("#{ testEnvironment['persistence.testsupport.dataClasspathLocation'] }")
	private String dataClasspathLocation;

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#transactionManager()
	 */
	@Bean(name = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME)
	public PlatformTransactionManager transactionManager() throws Exception {
		final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		jpaTransactionManager.setDataSource(applicationDataSource());
		jpaTransactionManager.setJpaDialect(jpaDialect());

		return jpaTransactionManager;
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#applicationDataSource()
	 */
	@Bean(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	public DataSource applicationDataSource() throws Exception {
		final PrePopulatingInMemoryH2DataSourceFactory dataSourceFactory = new PrePopulatingInMemoryH2DataSourceFactory();
		dataSourceFactory.setDatabaseName("persistence.item.testDataBase");
		dataSourceFactory.setSchemaLocation(new ClassPathResource(
				this.schemaClasspathLocation));
		dataSourceFactory.setDataLocation(new ClassPathResource(
				this.dataClasspathLocation));

		return dataSourceFactory.getObject();
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#platformMBeanServer()
	 */
	@Bean(name = PlatformIntegrationConfig.MBEAN_SERVER_COMPONENT_NAME)
	public MBeanServer platformMBeanServer() {

		return ManagementFactory.getPlatformMBeanServer();
	}

	@Bean(name = JpaProviderConfig.EMF_COMPONENT_NAME)
	public EntityManagerFactory entityManagerFactory() throws Exception {
		final LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryFactory
				.setPersistenceUnitName("persistence.item.ItemPU");
		entityManagerFactoryFactory.setDataSource(applicationDataSource());
		entityManagerFactoryFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactoryFactory.setJpaDialect(jpaDialect());
		entityManagerFactoryFactory.afterPropertiesSet();

		return entityManagerFactoryFactory.getObject();
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.HibernateIntegrationConfig#jpaDialect()
	 */
	@Bean(name = JpaProviderConfig.JPA_DIALECT_COMPONENT_NAME)
	public JpaDialect jpaDialect() {
		return new HibernateJpaDialect();
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.HibernateIntegrationConfig#jpaVendorAdapter()
	 */
	@Bean(name = JpaProviderConfig.JPA_VENDOR_ADAPTER_COMPONENT_NAME)
	public JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.H2);
		hibernateJpaVendorAdapter.setShowSql(true);

		return hibernateJpaVendorAdapter;
	}
}
