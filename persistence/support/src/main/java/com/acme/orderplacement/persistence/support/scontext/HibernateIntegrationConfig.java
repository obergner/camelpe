/**
 * 
 */
package com.acme.orderplacement.persistence.support.scontext;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;

/**
 * <p>
 * TODO: Insert short summary for HibernateIntegrationConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public interface HibernateIntegrationConfig {

	String EMF_COMPONENT_NAME = "persistence.support.platform.applicationEMF";

	String JPA_DIALECT_COMPONENT_NAME = "persistence.support.platform.jpaDialect";

	String JPA_VENDOR_ADAPTER_COMPONENT_NAME = "persistence.support.platform.jpaVendorAdapter";

	@Bean(name = HibernateIntegrationConfig.EMF_COMPONENT_NAME)
	EntityManagerFactory entityManagerFactory() throws Exception;

	@Bean(name = HibernateIntegrationConfig.JPA_DIALECT_COMPONENT_NAME)
	JpaDialect jpaDialect();

	@Bean(name = HibernateIntegrationConfig.JPA_VENDOR_ADAPTER_COMPONENT_NAME)
	JpaVendorAdapter jpaVendorAdapter();
}