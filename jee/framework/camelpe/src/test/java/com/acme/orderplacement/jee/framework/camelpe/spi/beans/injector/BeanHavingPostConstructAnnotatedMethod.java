/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi.beans.injector;

import javax.annotation.PostConstruct;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingPostConstructAnnotatedMethod
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanHavingPostConstructAnnotatedMethod {

	public Object postConstructed;

	@PostConstruct
	public void postConstruct() {
		this.postConstructed = new Object();
	}
}
