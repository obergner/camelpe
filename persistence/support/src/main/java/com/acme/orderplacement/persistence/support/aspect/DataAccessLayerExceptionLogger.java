/**
 * 
 */
package com.acme.orderplacement.persistence.support.aspect;

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
 * Aspect notation that logs all exception thrown at the
 * <tt>Data Access Layer</tt> boundary.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Component(DataAccessLayerExceptionLogger.COMPONENT_NAME)
@Order(30)
@Aspect
public class DataAccessLayerExceptionLogger extends AbstractExceptionLogger {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "persistence.aspect.DataAccessLayerExceptionLogger";

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * @see de.obergner.soa.order.aspect.log.AbstractExceptionLogger#exceptionLoggedMethods()
	 */
	@Override
	@Pointcut("com.acme.orderplacement.persistence.support.meta.DataAccessLayer.dataAccessOperations()")
	public void exceptionLoggedMethods() {
	}
}
