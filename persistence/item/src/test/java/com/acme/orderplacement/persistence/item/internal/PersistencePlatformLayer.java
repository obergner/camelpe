/**
 * 
 */
package com.acme.orderplacement.persistence.item.internal;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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

import com.acme.orderplacement.test.support.database.spring.PrePopulatingInMemoryH2DataSourceFactory;

/**
 * <p>
 * TODO: Insert short summary for PersistencePlatformLayer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class PersistencePlatformLayer {

	private DataSource applicationDataSource;

	@Bean(name = "persistence.support.platform.applicationEMF")
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

	@Bean(name = "persistence.support.platform.transactionManager")
	public PlatformTransactionManager transactionManager() throws Exception {
		final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		jpaTransactionManager.setDataSource(applicationDataSource());
		jpaTransactionManager.setJpaDialect(jpaDialect());

		return jpaTransactionManager;
	}

	@Bean(name = "persistence.support.platform.dataSource")
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
				"testdata/h2/persistence.itemDB-create.h2.ddl"));
		dataSourceFactory.setDataLocation(new ClassPathResource(
				"testdata/h2/persistence.itemDB-populate.h2.dml"));

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
