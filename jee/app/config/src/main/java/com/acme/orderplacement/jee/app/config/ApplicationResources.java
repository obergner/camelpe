/**
 * 
 */
package com.acme.orderplacement.jee.app.config;

import javax.annotation.sql.DataSourceDefinition;
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
@DataSourceDefinition(className = "org.apache.derby.jdbc.ClientDataSource", name = ApplicationResources.DATA_SOURCE_JNDI_NAME, user = "postgres", password = "postgres", databaseName = "orderplacement")
@ApplicationScoped
public interface ApplicationResources {

	String DATA_SOURCE_JNDI_NAME = "java:app/jdbc/orderplacement/domainDataSource";

	String PERSISTENCE_UNIT_NAME = "orderplacement.persistence.domainEntities";

	@ApplicationDataSource
	@Produces
	DataSource applicationDataSource();

	@ApplicationEntities
	@Produces
	EntityManager applicationEntities();

}