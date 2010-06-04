/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * <p>
 * TODO: Insert short summary for CdiBeansSharingTheSameSuperclass
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class CdiBeansSharingTheSameSuperclass {

	@Named
	@ApplicationScoped
	public static class Subclass1 extends CdiBeansSharingTheSameSuperclass {

	}

	@Named
	@ApplicationScoped
	public static class Subclass2 extends CdiBeansSharingTheSameSuperclass {

	}
}
