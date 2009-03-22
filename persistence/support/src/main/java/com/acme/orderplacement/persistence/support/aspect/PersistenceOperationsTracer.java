/**
 * 
 */
package com.acme.orderplacement.persistence.support.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.aspect.support.log.AbstractMethodTracer;

/**
 * <p>
 * An <code>Aspect</code> based on <a
 * href="http://www.eclipse.org/aspectj/">AspectJ</a> {@link Annotation} based
 * Aspect notation that traces all method calls within the persistence package.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Component(PersistenceOperationsTracer.ASPECT_NAME)
@Order(10)
@Aspect
public class PersistenceOperationsTracer extends AbstractMethodTracer {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String ASPECT_NAME = "persistence.support.aspect.PersistenceOperationsTracer";

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * @see de.obergner.soa.order.aspect.log.AbstractMethodTracer#tracedMethods()
	 */
	@Override
	@Pointcut("com.acme.orderplacement.persistence.support.meta.PersistenceLayer.persistenceOperations()")
	public void tracedMethods() {
		// Intentionally left blank
	}

}
