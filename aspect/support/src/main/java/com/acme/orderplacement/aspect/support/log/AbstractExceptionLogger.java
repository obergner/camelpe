/**
 * 
 */
package com.acme.orderplacement.aspect.support.log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.common.support.exception.ExceptionFormatter;

/**
 * <p>
 * A abstract <code>AspectJ</code> based {@link Aspect} for logging exceptions.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractExceptionLogger {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * The {@link Log} used to trace method executions.
	 */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Is this logger enabled or not?
	 */
	private boolean isEnabled = false;

	// ------------------------------------------------------------------------
	// Enabling/Disabling this aspect
	// ------------------------------------------------------------------------

	/**
	 * Start tracing method executions.
	 */
	@PostConstruct
	public void enable() {
		this.log.info("Logging exceptions has been enabled");
		this.isEnabled = true;
	}

	/**
	 * Stop tracing method executions.
	 */
	@PreDestroy
	public void disable() {
		this.log.info("Logging exceptions has been disabled");
		this.isEnabled = true;
	}

	/**
	 * Is this error logger enabled or not?
	 * 
	 * @return
	 */
	public boolean isEnabled() {

		return this.isEnabled;
	}

	// ------------------------------------------------------------------------
	// Abstract Pointcut
	// ------------------------------------------------------------------------

	/**
	 * Abstract {@link org.aspectj.lang.reflect.Pointcut <code>Pointcut</code>}
	 * defining the method executions concrete subclasses of this aspect should
	 * advise. Concrete subclasses must override this method and must also not
	 * forget to add the <code>Pointcut</code> annotation.
	 */
	@Pointcut("execution(public * *(..))")
	public abstract void exceptionLoggedMethods();

	// ------------------------------------------------------------------------
	// Advice
	// ------------------------------------------------------------------------

	/**
	 * @param joinPoint
	 * @param ex
	 */
	@AfterThrowing(pointcut = "exceptionLoggedMethods()", throwing = "ex")
	public void logExceptions(final JoinPoint joinPoint, final Exception ex) {
		if (this.isEnabled) {
			this.log.error("\n\n"
					+ ExceptionFormatter.packException(
							"Exception thrown while executing within\n\n\t<"
									+ joinPoint.getSignature().toLongString()
									+ ">", ex, true));
		}
	}
}
