/**
 * 
 */
package com.acme.orderplacement.jee.app.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * <p>
 * TODO: Insert short summary for ApplicationResources
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public interface ApplicationResources {

	String DATA_SOURCE_JNDI_NAME = "java:app/jdbc/orderplacement/domainDataSource";

	String PERSISTENCE_UNIT_NAME = "orderplacement.persistence.domainEntitiesPU";

	@ApplicationDataSource
	@Produces
	DataSource applicationDataSource();

	@ApplicationEntities
	@Produces
	EntityManager applicationEntities();

}