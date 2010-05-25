/**
 * 
 */
package com.acme.orderplacement.framework.persistence.testsupport.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.acme.orderplacement.framework.persistence.config.DomainDataSource;
import com.acme.orderplacement.framework.persistence.config.DomainEntities;
import com.acme.orderplacement.framework.persistence.config.DomainResources;

/**
 * <p>
 * TODO: Insert short summary for TestingDomainResources
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class TestingDomainResources implements DomainResources {

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#domainDataSource()
	 */
	@DomainDataSource
	@Produces
	public DataSource domainDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.acme.orderplacement.framework.persistence.config.DomainResources#domainEntities()
	 */
	@DomainEntities
	@Produces
	public EntityManager domainEntities() {
		// TODO Auto-generated method stub
		return null;
	}

}
