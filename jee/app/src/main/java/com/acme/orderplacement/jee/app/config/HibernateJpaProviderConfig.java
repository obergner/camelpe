/**
 * 
 */
package com.acme.orderplacement.jee.app.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.acme.orderplacement.persistence.config.JpaProviderConfig;
import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;

/**
 * <p>
 * TODO: Insert short summary for HibernateJpaProviderConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class HibernateJpaProviderConfig implements JpaProviderConfig {

	@Resource(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	private DataSource applicationDataSource;

	@Value("#{ jpaProperties }")
	private Properties jpaProperties;

	/**
	 * @see com.acme.orderplacement.persistence.config.HibernateIntegrationConfig#entityManagerFactory()
	 */
	@Bean(name = JpaProviderConfig.EMF_COMPONENT_NAME)
	public EntityManagerFactory entityManagerFactory() throws Exception {
		final LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryFactory
				.setPersistenceXmlLocation("classpath:/META-INF/jpa/persistence.xml");
		entityManagerFactoryFactory
				.setPersistenceUnitName("persistence.support.ApplicationPU");
		entityManagerFactoryFactory.setDataSource(this.applicationDataSource);
		entityManagerFactoryFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactoryFactory.setJpaDialect(jpaDialect());
		entityManagerFactoryFactory.setJpaProperties(this.jpaProperties);
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
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		hibernateJpaVendorAdapter.setShowSql(true);

		return hibernateJpaVendorAdapter;
	}
}
