/**
 * 
 */
package com.acme.orderplacement.aspect.support.log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.acme.orderplacement.aspect.support.log.joinpoint.JoinPointFormatter;

/**
 * <p>
 * A abstract <code>AspectJ</code> based {@link Aspect} for tracing method
 * executions.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractMethodTracer {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * The {@link Log} used to trace method executions.
	 */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Is this tracer enabled or not?
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
		if (this.log.isInfoEnabled()) {
			this.log.info("Method tracing has been enabled");
		}

		this.isEnabled = true;
	}

	/**
	 * Stop tracing method executions.
	 */
	@PreDestroy
	public void disable() {
		if (this.log.isInfoEnabled()) {
			this.log.info("Method tracing has been disabled");
		}

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
	 * trace. Concrete subclasses must override this method and must also not
	 * forget to add the <code>Pointcut</code> annotation.
	 */
	@Pointcut("execution(public * *(..))")
	public abstract void tracedMethods();

	// ------------------------------------------------------------------------
	// Advice
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Log entering and leaving all advised methods with a <em>Trace</em>
	 * loglevel. Exceptions will <strong>not</strong> be logged.
	 * </p>
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("tracedMethods()")
	public Object trace(final ProceedingJoinPoint joinPoint) throws Throwable {
		if (this.isEnabled) {
			Object retVal = null;
			final String formattedJoinPoint = new JoinPointFormatter(joinPoint)
					.format();
			try {
				this.log.trace(">>> " + formattedJoinPoint);
				retVal = joinPoint.proceed();

				return retVal;
			} finally {
				this.log.trace("<<< " + formattedJoinPoint + " - Returning: <"
						+ retVal + ">");
			}
		} else {

			return joinPoint.proceed();
		}
	}
}
