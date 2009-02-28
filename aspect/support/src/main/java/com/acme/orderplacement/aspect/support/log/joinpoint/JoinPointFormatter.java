/**
 * 
 */
package com.acme.orderplacement.aspect.support.log.joinpoint;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <p>
 * Helper class for nicely formatting a {@link JoinPoint <code>JoinPoint</code>}
 * , e.g. for logging it.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class JoinPointFormatter {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * The {@link JoinPoint <code>JoinPoint</code>} we this instance is supposed
	 * to format.
	 */
	private final JoinPoint joinPoint;

	// ------------------------------------------------------------------------
	// Ctor
	// ------------------------------------------------------------------------

	/**
	 * @param joinPoint
	 */
	public JoinPointFormatter(final JoinPoint joinPoint) {
		Validate.notNull(joinPoint, "joinPoint");
		this.joinPoint = joinPoint;
	}

	// ------------------------------------------------------------------------
	// Public API
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Nicely format this instance's {@link JoinPoint <code>JoinPoint</code>},
	 * using the supplied <code>buffer</code> to write the formatted output to.
	 * If <code>includeExceptions</code> is <code>true</code>, the throws clause
	 * will be included in the output.
	 * </p>
	 * 
	 * @param buffer
	 * @param includeExceptions
	 *            If the throws clause should be included in the output
	 */
	public void format(final StringBuilder buffer,
			final boolean includeExceptions) {
		Validate.notNull(buffer, "buffer");
		final Signature signature = this.joinPoint.getSignature();
		if (signature instanceof MethodSignature) {
			formatMethodExecutionJoinPoint(buffer, includeExceptions, signature);
		}
	}

/**
	 * <p>
	 * Equivalent to
	 * {@link #format(StringBuilder, boolean) <code>format(buffer, false)</code>.
	 * </p>
	 * 
	 * @param buffer
	 */
	public void format(final StringBuilder buffer) {
		format(buffer, false);
	}

	/**
	 * <p>
	 * Nicely format this instance's {@link JoinPoint <code>JoinPoint</code>},
	 * returning the result as a <code>String</code>. If
	 * <code>includeExceptions</code> is <code>true</code>, the throws clause
	 * will be included in the output.
	 * </p>
	 * 
	 * @param includeExceptions
	 *            If the throws clause should be included in the output
	 * @return The formatted <code>JoinPoint</code>
	 */
	public String format(final boolean includeExceptions) {
		final StringBuilder buffer = new StringBuilder();
		format(buffer, includeExceptions);

		return buffer.toString();
	}

/**
	 * <p>
	 * Equivalent to {@link #format(boolean) <code>format(false)</code>.
	 * </p>
	 * 
	 * @return
	 */
	public String format() {

		return format(false);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @param buffer
	 * @param includeExceptions
	 * @param signature
	 */
	private void formatMethodExecutionJoinPoint(final StringBuilder buffer,
			final boolean includeExceptions, final Signature signature) {
		final MethodSignature methodSignature = MethodSignature.class
				.cast(signature);

		buffer.append(methodSignature.getReturnType().getName());
		buffer.append(' ');

		buffer.append(this.joinPoint.getTarget().getClass().getName());
		buffer.append('.');
		buffer.append(methodSignature.getMethod().getName());
		buffer.append('(');

		final Class[] argTypes = methodSignature.getParameterTypes();
		final Object[] argValues = this.joinPoint.getArgs();
		for (int i = 0; i < argValues.length; i++) {
			buffer.append(argTypes[i].getSimpleName());
			buffer.append(" <");
			buffer.append(argValues[i]);
			buffer.append('>');
			if (i < argValues.length - 1) {
				buffer.append(", ");
			}
		}

		buffer.append(')');

		if (includeExceptions) {
			final Class[] exceptionTypes = methodSignature.getExceptionTypes();
			if (exceptionTypes.length > 0) {
				buffer.append(" throws ");
				for (int i = 0; i < exceptionTypes.length; i++) {
					buffer.append(exceptionTypes[i].getSimpleName());
					if (i < exceptionTypes.length - 1) {
						buffer.append(", ");
					}
				}
			}
		}
	}
}
