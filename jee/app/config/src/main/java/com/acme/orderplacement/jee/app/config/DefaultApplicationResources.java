/**
 * 
 */
package com.acme.orderplacement.jee.app.config;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * <p>
 * A <code>CDI</code> producer for injecting the application's central
 * {@link javax.persistence.EntityManager <code>EntityManager</code>} into
 * clients.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class DefaultApplicationResources implements ApplicationResources {

	@Resource(name = ApplicationResources.DATA_SOURCE_JNDI_NAME)
	private DataSource domainDataSource;

	@PersistenceContext(unitName = ApplicationResources.PERSISTENCE_UNIT_NAME)
	private EntityManager domainEntities;

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#applicationDataSource()
	 */
	@ApplicationDataSource
	@Produces
	public DataSource applicationDataSource() {
		return this.domainDataSource;
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#applicationEntities()
	 */
	@ApplicationEntities
	@Produces
	public EntityManager applicationEntities() {
		return this.domainEntities;
	}
}
