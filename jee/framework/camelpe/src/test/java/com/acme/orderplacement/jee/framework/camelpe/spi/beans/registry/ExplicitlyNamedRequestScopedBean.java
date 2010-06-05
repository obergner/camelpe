/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi.beans.registry;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * <p>
 * TODO: Insert short summary for ExplicitlyNamedApplicationScopedBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Named(ExplicitlyNamedRequestScopedBean.NAME)
@RequestScoped
public class ExplicitlyNamedRequestScopedBean {

	public static final String NAME = "explicitlyNamedRequestScopedBean";

}
