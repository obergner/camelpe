/**
 * 
 */
package com.acme.orderplacement.jee.app.scontext;

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

import com.acme.orderplacement.persistence.support.scontext.HibernateIntegrationConfig;
import com.acme.orderplacement.persistence.support.scontext.PlatformIntegrationConfig;

/**
 * <p>
 * TODO: Insert short summary for ApplicationHibernateIntegrationConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class ApplicationHibernateIntegrationConfig implements
		HibernateIntegrationConfig {

	@Resource(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	private DataSource applicationDataSource;

	@Value("#{ jpaProperties }")
	private Properties jpaProperties;

	/**
	 * @see com.acme.orderplacement.persistence.support.scontext.HibernateIntegrationConfig#entityManagerFactory()
	 */
	@Bean(name = HibernateIntegrationConfig.EMF_COMPONENT_NAME)
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
	 * @see com.acme.orderplacement.persistence.support.scontext.HibernateIntegrationConfig#jpaDialect()
	 */
	@Bean(name = HibernateIntegrationConfig.JPA_DIALECT_COMPONENT_NAME)
	public JpaDialect jpaDialect() {
		return new HibernateJpaDialect();
	}

	/**
	 * @see com.acme.orderplacement.persistence.support.scontext.HibernateIntegrationConfig#jpaVendorAdapter()
	 */
	@Bean(name = HibernateIntegrationConfig.JPA_VENDOR_ADAPTER_COMPONENT_NAME)
	public JpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		hibernateJpaVendorAdapter.setShowSql(true);

		return hibernateJpaVendorAdapter;
	}
}
