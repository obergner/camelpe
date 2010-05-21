/**
 * 
 */
package com.acme.orderplacement.jee.framework.config.internal;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.Database;

import com.acme.orderplacement.jee.framework.config.ApplicationPersistenceUnit;
import com.acme.orderplacement.persistence.config.AbstractJpaEntityManagerFactoryConfig;
import com.acme.orderplacement.persistence.config.JpaEntityManagerFactoryConfig;
import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;

/**
 * <p>
 * TODO: Insert short summary for ApplicationJpaEntityManagerFactoryConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ApplicationJpaEntityManagerFactoryConfig extends
		AbstractJpaEntityManagerFactoryConfig implements
		JpaEntityManagerFactoryConfig {

	@Resource(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	private DataSource applicationDataSource;

	/**
	 * @see com.acme.orderplacement.persistence.config.JpaEntityManagerFactoryConfig#entityManagerFactory()
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() throws Exception {

		return createEntityManagerFactory();
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.AbstractJpaEntityManagerFactoryConfig#applicationDataSource()
	 */
	@Override
	protected DataSource applicationDataSource() {
		return this.applicationDataSource;
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.AbstractJpaEntityManagerFactoryConfig#persistenceXmlLocation()
	 */
	@Override
	protected String persistenceXmlLocation() {
		return "classpath:/META-INF/jpa/persistence.xml";
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.AbstractJpaEntityManagerFactoryConfig#persistenceUnitName()
	 */
	@Override
	protected String persistenceUnitName() {
		return ApplicationPersistenceUnit.NAME;
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.AbstractJpaEntityManagerFactoryConfig#databaseType()
	 */
	@Override
	protected Database databaseType() {
		return Database.POSTGRESQL;
	}

}
