/**
 * 
 */
package com.acme.orderplacement.framework.persistence.testsupport.config;

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

import com.acme.orderplacement.framework.persistence.config.JpaEntityManagerFactoryConfig;
import com.acme.orderplacement.framework.persistence.config.PlatformIntegrationConfig;
import com.acme.orderplacement.framework.persistence.testsupport.database.spring.PrePopulatingInMemoryH2DataSourceFactory;

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
		PlatformIntegrationConfig, JpaEntityManagerFactoryConfig {

	@Value("#{ testEnvironment['persistence.testsupport.schemaClasspathLocation'] }")
	private String schemaClasspathLocation;

	@Value("#{ testEnvironment['persistence.testsupport.dataClasspathLocation'] }")
	private String dataClasspathLocation;

	@Value("#{ testEnvironment['persistence.testsupport.persistenceUnitName'] }")
	private String persistenceUnitName;

	@Value("#{ testEnvironment['persistence.testsupport.databaseName'] }")
	private String databaseName;

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.PlatformIntegrationConfig#transactionManager()
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
	 * @see com.acme.orderplacement.framework.persistence.config.PlatformIntegrationConfig#applicationDataSource()
	 */
	@Bean(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	public DataSource applicationDataSource() throws Exception {
		final PrePopulatingInMemoryH2DataSourceFactory dataSourceFactory = new PrePopulatingInMemoryH2DataSourceFactory();
		dataSourceFactory.setDatabaseName(this.databaseName);
		dataSourceFactory.setSchemaLocation(new ClassPathResource(
				this.schemaClasspathLocation));
		dataSourceFactory.setDataLocation(new ClassPathResource(
				this.dataClasspathLocation));

		return dataSourceFactory.getObject();
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.PlatformIntegrationConfig#platformMBeanServer()
	 */
	@Bean(name = PlatformIntegrationConfig.MBEAN_SERVER_COMPONENT_NAME)
	public MBeanServer platformMBeanServer() {

		return ManagementFactory.getPlatformMBeanServer();
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.JpaEntityManagerFactoryConfig#entityManagerFactory()
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() throws Exception {
		final LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryFactory
				.setPersistenceUnitName(this.persistenceUnitName);
		entityManagerFactoryFactory.setDataSource(applicationDataSource());
		entityManagerFactoryFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactoryFactory.setJpaDialect(jpaDialect());
		entityManagerFactoryFactory.afterPropertiesSet();

		return entityManagerFactoryFactory.getObject();
	}

	private JpaDialect jpaDialect() {
		return new HibernateJpaDialect();
	}

	private JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.H2);
		hibernateJpaVendorAdapter.setShowSql(true);

		return hibernateJpaVendorAdapter;
	}
}
