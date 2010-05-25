/**
 * 
 */
package com.acme.orderplacement.framework.persistence.config;

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
public class DefaultDomainResources implements DomainResources {

	@Resource(name = DefaultDomainResources.DATA_SOURCE_JNDI_NAME)
	private DataSource domainDataSource;

	@PersistenceContext(unitName = DefaultDomainResources.PERSISTENCE_UNIT_NAME)
	private EntityManager domainEntities;

	/* (non-Javadoc)
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#domainDataSource()
	 */
	@DomainDataSource
	@Produces
	public DataSource domainDataSource() {
		return this.domainDataSource;
	}

	/* (non-Javadoc)
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#domainEntities()
	 */
	@DomainEntities
	@Produces
	public EntityManager domainEntities() {
		return this.domainEntities;
	}
}
