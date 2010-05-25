/**
 * 
 */
package com.acme.orderplacement.framework.persistence.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * <p>
 * TODO: Insert short summary for DomainResources
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public interface DomainResources {

	String DATA_SOURCE_JNDI_NAME = "java:app/jdbc/orderplacement/domainDataSource";

	String PERSISTENCE_UNIT_NAME = "orderplacement.persistence.domainEntities";

	@DomainDataSource
	@Produces
	DataSource domainDataSource();

	@DomainEntities
	@Produces
	EntityManager domainEntities();

}