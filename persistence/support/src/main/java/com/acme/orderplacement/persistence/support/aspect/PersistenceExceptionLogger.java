/**
 * 
 */
package com.acme.orderplacement.persistence.support.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@Component(PersistenceExceptionLogger.ASPECT_NAME)
@Order(30)
@Aspect
@ManagedResource(objectName = "com.acme.orderplacement:layer=PersistenceLayer,name=PersistenceExceptionLogger", description = "An aspect for logging exceptions thrown from the persistence layer")
public class PersistenceExceptionLogger extends AbstractExceptionLogger {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String ASPECT_NAME = "persistence.support.aspect.PersistenceExceptionLogger";

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * @see de.obergner.soa.order.aspect.log.AbstractExceptionLogger#exceptionLoggedMethods()
	 */
	@Override
	@Pointcut("com.acme.orderplacement.persistence.support.meta.PersistenceLayer.persistenceOperations()")
	public void exceptionLoggedMethods() {
		// Intentionally left blank
	}
}
