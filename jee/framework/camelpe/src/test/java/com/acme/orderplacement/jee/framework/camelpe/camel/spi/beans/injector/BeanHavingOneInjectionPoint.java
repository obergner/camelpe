/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi.beans.injector;

import javax.inject.Inject;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingOneInjectionPoint
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanHavingOneInjectionPoint {

	@Inject
	public BeanHavingNoInjectionPoints injectionPoint;
}
