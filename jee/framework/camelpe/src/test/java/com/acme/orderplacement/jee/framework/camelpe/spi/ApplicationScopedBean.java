/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * <p>
 * TODO: Insert short summary for ApplicationScopedBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Named(ApplicationScopedBean.NAME)
@ApplicationScoped
public class ApplicationScopedBean {

	public static final String NAME = "applicationScopedBean";

}
