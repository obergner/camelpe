/**
 * 
 */
package com.acme.orderplacement.framework.service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.framework.aspect.log.AbstractMethodTracer;

/**
 * <p>
 * TODO: Insert short summary for ServiceOperationsTracer
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ServiceOperationsTracer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(ServiceOperationsTracer.ASPECT_NAME)
@Order(10)
@Aspect
@ManagedResource(objectName = "com.acme.orderplacement:layer=ServiceLayer,name=ServiceOperationsTracer", description = "An aspect for tracing the execution of operations within the service layer")
public class ServiceOperationsTracer extends AbstractMethodTracer {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String ASPECT_NAME = "service.support.aspect.ServiceOperationsTracer";

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * @see de.obergner.soa.order.aspect.log.AbstractMethodTracer#tracedMethods()
	 */
	@Override
	@Pointcut("com.acme.orderplacement.framework.service.meta.ServiceLayer.serviceOperations()")
	public void tracedMethods() {
	}
}
