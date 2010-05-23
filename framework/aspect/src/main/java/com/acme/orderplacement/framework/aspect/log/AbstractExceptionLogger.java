/**
 * 
 */
package com.acme.orderplacement.framework.aspect.log;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;

import com.acme.orderplacement.framework.common.exception.ExceptionFormatter;

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
	 * The {@link Logger} used to trace method executions.
	 */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Is this logger enabled or not?
	 */
	private volatile boolean isEnabled = false;

	/**
	 * How many exceptions has this component logged since (a) creation time or
	 * (b) the last time this counter has been reset?
	 */
	private final AtomicInteger exceptionCount = new AtomicInteger();

	// ------------------------------------------------------------------------
	// Enabling/Disabling this aspect
	// ------------------------------------------------------------------------

	/**
	 * Start tracing method executions.
	 */
	@PostConstruct
	@ManagedOperation(description = "Enable logging exceptions")
	public void enable() {
		this.log.info("Logging exceptions has been enabled");
		this.isEnabled = true;
	}

	/**
	 * Stop tracing method executions.
	 */
	@PreDestroy
	@ManagedOperation(description = "Disable logging exceptions")
	public void disable() {
		this.log.info("Logging exceptions has been disabled");
		this.isEnabled = true;
	}

	/**
	 * Is this error logger enabled or not?
	 * 
	 * @return
	 */
	@ManagedAttribute(defaultValue = "true", description = "Is logging exceptions currently enabled?")
	public boolean isEnabled() {

		return this.isEnabled;
	}

	/**
	 * Reset this aspect's {@link #getExceptionCount()
	 * <code>exceptionCount</code>} property to <code>0</code>.
	 */
	@ManagedOperation(description = "Reset this MBean's 'exceptionCount' property to 0 (zero)")
	public void resetExceptionCount() {

	}

	/**
	 * How many exceptions has this component logged since (a) creation time or
	 * (b) the last time this counter has been reset?
	 * 
	 * @return
	 */
	@ManagedAttribute(description = "The number of exceptions logged by this MBean since startup "
			+ "or since the last time resetExceptionCount() has been called on it")
	public int getExceptionCount() {

		return this.exceptionCount.get();
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
			this.exceptionCount.incrementAndGet();
			this.log.error("\n\n"
					+ ExceptionFormatter.packException(
							"Exception thrown while executing within\n\n\t<"
									+ joinPoint.getSignature().toLongString()
									+ ">", ex, true));
		}
	}
}
