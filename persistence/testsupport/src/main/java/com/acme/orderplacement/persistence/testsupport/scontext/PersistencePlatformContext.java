/**
 * 
 */
package com.acme.orderplacement.persistence.testsupport.scontext;

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

import com.acme.orderplacement.persistence.testsupport.database.spring.PrePopulatingInMemoryH2DataSourceFactory;

/**
 * <p>
 * TODO: Insert short summary for PersistencePlatformContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class PersistencePlatformContext {

	public static final String EMF_COMPONENT_NAME = "persistence.support.platform.applicationEMF";

	public static final String TXMANAGER_COMPONENT_NAME = "persistence.support.platform.transactionManager";

	public static final String DATASOURCE_COMPONENT_NAME = "persistence.support.platform.dataSource";

	@Value("#{ testEnvironment['persistence.testsupport.schemaClasspathLocation'] }")
	private String schemaClasspathLocation;

	@Value("#{ testEnvironment['persistence.testsupport.dataClasspathLocation'] }")
	private String dataClasspathLocation;

	/*
	 * We have to cache the created test datasource since obviously the spring
	 * test support creates a new data source for each test run. This, however,
	 * results in an exception since we will try to recreate the database schema
	 * in an already initialized in-memory H2 database.
	 */
	private DataSource applicationDataSource;

	@Bean(name = PersistencePlatformContext.EMF_COMPONENT_NAME)
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

	@Bean(name = PersistencePlatformContext.TXMANAGER_COMPONENT_NAME)
	public PlatformTransactionManager transactionManager() throws Exception {
		final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		jpaTransactionManager.setDataSource(applicationDataSource());
		jpaTransactionManager.setJpaDialect(jpaDialect());

		return jpaTransactionManager;
	}

	@Bean(name = PersistencePlatformContext.DATASOURCE_COMPONENT_NAME)
	public DataSource applicationDataSource() throws Exception {
		if (this.applicationDataSource == null) {
			this.applicationDataSource = newApplicationDataSource();
		}

		return this.applicationDataSource;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private DataSource newApplicationDataSource() throws Exception {
		final PrePopulatingInMemoryH2DataSourceFactory dataSourceFactory = new PrePopulatingInMemoryH2DataSourceFactory();
		dataSourceFactory.setDatabaseName("persistence.item.testDataBase");
		dataSourceFactory.setSchemaLocation(new ClassPathResource(
				this.schemaClasspathLocation));
		dataSourceFactory.setDataLocation(new ClassPathResource(
				this.dataClasspathLocation));

		return dataSourceFactory.getObject();
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
