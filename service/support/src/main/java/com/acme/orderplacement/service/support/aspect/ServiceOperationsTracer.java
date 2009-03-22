/**
 * 
 */
package com.acme.orderplacement.service.support.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.aspect.support.log.AbstractMethodTracer;

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
	@Pointcut("com.acme.orderplacement.service.support.meta.ServiceLayer.serviceOperations()")
	public void tracedMethods() {
	}
}
