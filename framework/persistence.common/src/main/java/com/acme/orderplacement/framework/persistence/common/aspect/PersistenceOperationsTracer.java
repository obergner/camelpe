/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.framework.aspect.log.AbstractMethodTracer;

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
@ManagedResource(objectName = "com.acme.orderplacement:layer=PersistenceLayer,name=PersistenceOperationsTracer", description = "An aspect for tracing the execution of operations within the persistence layer")
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
	@Pointcut("com.acme.orderplacement.framework.persistence.common.meta.PersistenceLayer.persistenceOperations()")
	public void tracedMethods() {
		// Intentionally left blank
	}

}
