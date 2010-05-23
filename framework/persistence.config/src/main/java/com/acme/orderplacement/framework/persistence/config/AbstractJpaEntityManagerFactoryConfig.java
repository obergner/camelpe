/**
 * 
 */
package com.acme.orderplacement.framework.persistence.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * <p>
 * TODO: Insert short summary for AbstractJpaEntityManagerFactoryConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractJpaEntityManagerFactoryConfig implements
		JpaEntityManagerFactoryConfig {

	@Value("#{ jpaProperties }")
	private Properties jpaProperties;

	protected EntityManagerFactory createEntityManagerFactory()
			throws Exception {
		final LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryFactory
				.setPersistenceXmlLocation(persistenceXmlLocation());
		entityManagerFactoryFactory
				.setPersistenceUnitName(persistenceUnitName());
		entityManagerFactoryFactory.setDataSource(applicationDataSource());
		entityManagerFactoryFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactoryFactory.setJpaDialect(jpaDialect());
		entityManagerFactoryFactory.setJpaProperties(jpaProperties());
		entityManagerFactoryFactory.afterPropertiesSet();

		return entityManagerFactoryFactory.getObject();
	}

	private JpaDialect jpaDialect() {
		return new HibernateJpaDialect();
	}

	private JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(databaseType());
		hibernateJpaVendorAdapter.setShowSql(true);

		return hibernateJpaVendorAdapter;
	}

	private Properties jpaProperties() {
		return this.jpaProperties;
	}

	protected abstract DataSource applicationDataSource();

	protected abstract String persistenceXmlLocation();

	protected abstract String persistenceUnitName();

	protected abstract Database databaseType();
}
