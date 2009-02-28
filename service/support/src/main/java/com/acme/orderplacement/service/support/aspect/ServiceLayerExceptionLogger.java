/**
 * 
 */
package com.acme.orderplacement.service.support.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.aspect.support.log.AbstractExceptionLogger;

/**
 * <p>
 * An <code>Aspect</code> based on <a
 * href="http://www.eclipse.org/aspectj/">AspectJ</a> {@link Annotation} based
 * Aspect notation that logs all exception thrown at the <tt>Service Layer</tt>
 * boundary.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(ServiceLayerExceptionLogger.COMPONENT_NAME)
@Order(30)
@Aspect
public class ServiceLayerExceptionLogger extends AbstractExceptionLogger {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "service.aspect.ServiceLayerExceptionLogger";

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * @see de.obergner.soa.order.aspect.log.AbstractExceptionLogger#exceptionLoggedMethods()
	 */
	@Override
	@Pointcut("com.acme.orderplacement.service.support.meta.ServiceLayer.serviceOperations()")
	public void exceptionLoggedMethods() {
	}
}
