/**
 * 
 */
package com.acme.orderplacement.persistence.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;

/**
 * <p>
 * TODO: Insert short summary for JpaProviderConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public interface JpaProviderConfig {

	String EMF_COMPONENT_NAME = "persistence.support.platform.applicationEMF";

	String JPA_DIALECT_COMPONENT_NAME = "persistence.support.platform.jpaDialect";

	String JPA_VENDOR_ADAPTER_COMPONENT_NAME = "persistence.support.platform.jpaVendorAdapter";

	@Bean(name = JpaProviderConfig.EMF_COMPONENT_NAME)
	EntityManagerFactory entityManagerFactory() throws Exception;

	@Bean(name = JpaProviderConfig.JPA_DIALECT_COMPONENT_NAME)
	JpaDialect jpaDialect();

	@Bean(name = JpaProviderConfig.JPA_VENDOR_ADAPTER_COMPONENT_NAME)
	JpaVendorAdapter jpaVendorAdapter();
}