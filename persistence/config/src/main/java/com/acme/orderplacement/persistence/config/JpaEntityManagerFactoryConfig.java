/**
 * 
 */
package com.acme.orderplacement.persistence.config;

import javax.persistence.EntityManagerFactory;

/**
 * <p>
 * TODO: Insert short summary for JpaEntityManagerFactoryConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface JpaEntityManagerFactoryConfig {

	EntityManagerFactory entityManagerFactory() throws Exception;
}
