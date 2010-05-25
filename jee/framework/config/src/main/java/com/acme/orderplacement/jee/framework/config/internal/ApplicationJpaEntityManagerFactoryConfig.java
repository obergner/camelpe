/**
 * 
 */
package com.acme.orderplacement.jee.framework.config.internal;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.Database;

import com.acme.orderplacement.framework.persistence.config.obsolete.AbstractJpaEntityManagerFactoryConfig;
import com.acme.orderplacement.framework.persistence.config.obsolete.JpaEntityManagerFactoryConfig;
import com.acme.orderplacement.framework.persistence.config.obsolete.PlatformIntegrationConfig;
import com.acme.orderplacement.jee.framework.config.ApplicationPersistenceUnit;

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
	 * @see com.acme.orderplacement.framework.persistence.config.obsolete.JpaEntityManagerFactoryConfig#entityManagerFactory()
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() throws Exception {

		return createEntityManagerFactory();
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.obsolete.AbstractJpaEntityManagerFactoryConfig#applicationDataSource()
	 */
	@Override
	protected DataSource applicationDataSource() {
		return this.applicationDataSource;
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.obsolete.AbstractJpaEntityManagerFactoryConfig#persistenceXmlLocation()
	 */
	@Override
	protected String persistenceXmlLocation() {
		return "classpath:/META-INF/jpa/persistence.xml";
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.obsolete.AbstractJpaEntityManagerFactoryConfig#persistenceUnitName()
	 */
	@Override
	protected String persistenceUnitName() {
		return ApplicationPersistenceUnit.NAME;
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.obsolete.AbstractJpaEntityManagerFactoryConfig#databaseType()
	 */
	@Override
	protected Database databaseType() {
		return Database.POSTGRESQL;
	}

}
