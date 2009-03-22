/**
 * 
 */
package com.acme.orderplacement.service.support.meta;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>
 * A class defining {@link org.aspectj.lang.reflect.Pointcut
 * <code>Pointcut</code>}s using <code>AspectJ</code>s {@link Pointcut
 * <code>@Pointcut</code>} {@link Annotation <code>Annotation</code>}s that
 * comprehensively describe this application's <strong>Service Layer</strong>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Aspect
public final class ServiceLayer {

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	@Pointcut("within(com.acme.orderplacement.service..*)")
	public void inServiceLayer() {
		// Intentionally left blank
	}

	/**
	 * 
	 */
	@Pointcut("@annotation(com.acme.orderplacement.service.support.meta.annotation.ServiceOperation)")
	public void serviceOperations() {
		// Intentionally left blank
	}

	/**
	 * 
	 */
	@Pointcut("serviceOperations() && (@annotation(org.springframework.transaction.annotation.Transactional) "
			+ "|| @annotation(javax.ejb.TransactionAttribute))")
	public void transactionalServiceOperations() {
		// Intentionally left blank
	}
}
