/**
 * 
 */
package com.acme.orderplacement.framework.aspect.log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;

import com.acme.orderplacement.framework.aspect.log.joinpoint.JoinPointFormatter;

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
	 * The {@link Logger} used to trace method executions.
	 */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Is this tracer enabled or not?
	 */
	private volatile boolean isEnabled = false;

	// ------------------------------------------------------------------------
	// Enabling/Disabling this aspect
	// ------------------------------------------------------------------------

	/**
	 * Start tracing method executions.
	 */
	@PostConstruct
	@ManagedOperation(description = "Enable method tracing")
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
	@ManagedOperation(description = "Disable method tracing")
	public void disable() {
		if (this.log.isInfoEnabled()) {
			this.log.info("Method tracing has been disabled");
		}

		this.isEnabled = true;
	}

	/**
	 * Is method tracing enabled or not?
	 * 
	 * @return
	 */
	@ManagedAttribute(defaultValue = "true", description = "Is method tracing enabled?")
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
		}

		return joinPoint.proceed();
	}
}
