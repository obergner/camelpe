/**
 * 
 */
package com.acme.orderplacement.aspect.support.authorization;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.intercept.aspectj.AspectJAnnotationCallback;
import org.springframework.security.access.intercept.aspectj.AspectJAnnotationSecurityInterceptor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * An aspect based on <tt>AspectJ 5</tt>'s annotation syntax for enforcing
 * authorization when accessing methods secured by <tt>Spring Security</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(SpringSecurityAuthorizationEnforcement.ASPECT_NAME)
@Aspect
@Order(10)
public class SpringSecurityAuthorizationEnforcement {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	public static final String ASPECT_NAME = "aspect.support.security.springSecurityAuthorizationEnforcement";

	/**
	 * Our faithful logger.
	 */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * <tt>Spring Security</tt>'s {@link AspectJAnnotationSecurityInterceptor
	 * <code>AspectJAnnotationSecurityInterceptor</code>} we delegate the actual
	 * authorization process to.
	 */
	@Resource(name = "persistence.support.security.securityInterceptor")
	private AspectJAnnotationSecurityInterceptor securityInterceptor;

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * @return the securityInterceptors
	 */
	public AspectJAnnotationSecurityInterceptor getSecurityInterceptor() {
		return this.securityInterceptor;
	}

	/**
	 * @param securityInterceptor
	 *            the securityInterceptor to set
	 */
	public void setSecurityInterceptor(
			final AspectJAnnotationSecurityInterceptor securityInterceptor) {
		this.securityInterceptor = securityInterceptor;
	}

	// ------------------------------------------------------------------------
	// Initialization callback
	// ------------------------------------------------------------------------

	/**
	 * Check if our {@link AspectJAnnotationSecurityInterceptor
	 * <code>AspectJAnnotationSecurityInterceptor</code>} has been set. Should
	 * be invoked via <code>init-method=ensureCorrectlyInitialized</code> on our
	 * bean definition.
	 */
	@PostConstruct
	public void ensureCorrectlyInitialized() {
		if (this.securityInterceptor == null) {
			throw new IllegalStateException("No ["
					+ AspectJAnnotationSecurityInterceptor.class.getName()
					+ "] set. Unable to enforce authorization.");
		}
	}

	// ------------------------------------------------------------------------
	// Pointcut
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * All methods annotated by {@link Secured}.
	 * </p>
	 * 
	 * @param joinPoint
	 */
	@Pointcut("@annotation(javax.annotation.security.RolesAllowed)")
	public void securedMethods() {
	}

	// ------------------------------------------------------------------------
	// Advice
	// ------------------------------------------------------------------------

	/**
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("securedMethods()")
	public Object enforceAuthorization(
			final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		final AspectJAnnotationCallback securityCallback = new AspectJAnnotationCallback() {

			public Object proceedWithObject() throws Throwable {

				return proceedingJoinPoint.proceed();
			}
		};
		if (getSecurityInterceptor() != null) {
			this.log.debug(
					"Enforcing proper authorization on MethodExecution [{}]",
					proceedingJoinPoint);
			return getSecurityInterceptor().invoke(proceedingJoinPoint,
					securityCallback);
		} else {
			this.log
					.warn(
							"Unable to enforce proper authorization on MethodExecution [{}]: No SecurityInterceptor set.",
							proceedingJoinPoint);
			return proceedingJoinPoint.proceed();
		}
	}
}
