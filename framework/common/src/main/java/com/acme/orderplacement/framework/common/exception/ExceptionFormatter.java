/**
 * 
 */
package com.acme.orderplacement.framework.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * <p>
 * Collection of static utility methods for nicely formatting
 * <code>Exception</code>s.
 * </p>
 * <p>
 * <strong>Implementation Note</strong> This code has been shamelessly copied
 * from the now defunct <strong>Apache Avalon</strong> project.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public final class ExceptionFormatter {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final String HEADER = "----";

	private static final String EXCEPTION = HEADER + " Exception Report ";

	private static final String COMPOSITE = HEADER + " Composite Report ";

	private static final String RUNTIME = HEADER + " Runtime Exception Report ";

	private static final String ERROR = HEADER + " Error Report ";

	private static final String CAUSE = HEADER + " Cause ";

	private static final String TRACE = HEADER + " Stack Trace ";

	private static final String END = "";

	private static final int WIDTH = 80;

	// ------------------------------------------------------------------------
	// Public API
	// ------------------------------------------------------------------------

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param e
	 *            the exception
	 * @return String the formatting string
	 */
	public static String packException(final Throwable e) {
		return packException(null, e);
	}

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param e
	 *            the exception
	 * @param stack
	 *            TRUE to generate a stack trace
	 * @return String the formatting string
	 */
	public static String packException(final Throwable e, final boolean stack) {
		return packException(null, e, stack);
	}

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param message
	 *            the header message
	 * @param e
	 *            the exception
	 * @return String the formatting string
	 */
	public static String packException(final String message, final Throwable e) {
		return packException(message, e, false);
	}

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param message
	 *            the header message
	 * @param e
	 *            the exception
	 * @param stack
	 *            TRUE to generate a stack trace
	 * @return String the formatting string
	 */
	public static String packException(final String message, final Throwable e,
			final boolean stack) {
		final StringBuilder buffer = new StringBuilder();
		packException(buffer, 0, message, e, stack);
		buffer.append(getLine(END));
		return buffer.toString();
	}

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param message
	 *            the header message
	 * @param e
	 *            the exceptions
	 * @param stack
	 *            TRUE to generate a stack trace
	 * @return String the formatting string
	 */
	public static String packException(final String message,
			final Throwable[] e, final boolean stack) {
		final String lead = COMPOSITE + "(" + e.length + " entries) ";
		final StringBuilder buffer = new StringBuilder(getLine(lead));
		if (null != message) {
			buffer.append("\n");
			buffer.append(message);
			buffer.append("\n");
		}
		for (int i = 0; i < e.length; i++) {
			packException(buffer, i + 1, null, e[i], stack);
		}
		buffer.append(getLine(END));
		return buffer.toString();
	}

	// ------------------------------------------------------------------------
	// Internal helper methods
	// ------------------------------------------------------------------------

	/**
	 * Returns the exception and causal exceptions as a formatted string.
	 * 
	 * @param message
	 *            the header message
	 * @param e
	 *            the exception
	 * @param stack
	 *            TRUE to generate a stack trace
	 * @return String the formatting string
	 */
	private static void packException(final StringBuilder buffer, final int j,
			final String message, final Throwable e, final boolean stack) {
		if (e instanceof Error) {
			buffer.append(getLine(ERROR, j));
		} else if (e instanceof RuntimeException) {
			buffer.append(getLine(RUNTIME, j));
		} else {
			buffer.append(getLine(EXCEPTION, j));
		}

		if (null != message) {
			buffer.append(message);
			buffer.append("\n\n");
		}
		if (e == null) {
			return;
		}

		buffer.append("Exception: " + e.getClass().getName() + "\n");
		buffer.append("Message: " + e.getMessage() + "\n\n");
		packCause(buffer, getCause(e)).toString();
		buffer.append("\n");
		final Throwable root = getLastThrowable(e);
		if ((root != null) && stack) {
			buffer.append(getLine(TRACE));
			final String[] trace = captureStackTrace(root);
			for (final String element : trace) {
				buffer.append(element + "\n");
			}
		}
	}

	private static StringBuilder packCause(final StringBuilder buffer,
			final Throwable cause) {
		if (cause == null) {
			return buffer;
		}
		buffer.append(getLine(CAUSE));
		buffer.append("Exception: " + cause.getClass().getName() + "\n");
		buffer.append("Message: " + cause.getMessage() + "\n");
		return packCause(buffer, getCause(cause));
	}

	private static Throwable getLastThrowable(final Throwable exception) {
		final Throwable cause = getCause(exception);
		if (cause != null) {
			return getLastThrowable(cause);
		}
		return exception;
	}

	private static Throwable getCause(final Throwable exception) {
		if (exception == null) {
			throw new NullPointerException("exception");
		}

		try {
			final Method method = exception.getClass().getMethod("getCause",
					new Class[0]);
			return (Throwable) method.invoke(exception, new Object[0]);
		} catch (final Throwable e) {
			return null;
		}
	}

	/**
	 * Captures the stack trace associated with this exception.
	 * 
	 * @param throwable
	 *            a <code>Throwable</code>
	 * @return an array of Strings describing stack frames.
	 */
	private static String[] captureStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw, true));
		return splitString(sw.toString(), LINE_SEPARATOR);
	}

	/**
	 * Splits the string on every token into an array of stack frames.
	 * 
	 * @param string
	 *            the string to split
	 * @param onToken
	 *            the token to split on
	 * @return the resultant array
	 */
	private static String[] splitString(final String string,
			final String onToken) {
		final StringTokenizer tokenizer = new StringTokenizer(string, onToken);
		final String[] result = new String[tokenizer.countTokens()];

		for (int i = 0; i < result.length; i++) {
			final String token = tokenizer.nextToken();
			if (token.startsWith("\tat ")) {
				result[i] = token.substring(4);
			} else {
				result[i] = token;
			}
		}

		return result;
	}

	private static String getLine(final String lead) {
		return getLine(lead, 0);
	}

	private static String getLine(final String lead, final int count) {
		final StringBuilder buffer = new StringBuilder(lead);
		int q = 0;
		if (count > 0) {
			final String v = "" + count + " ";
			buffer.append("" + count);
			buffer.append(" ");
			q = v.length() + 1;
		}
		final int j = WIDTH - (lead.length() + q);
		for (int i = 0; i < j; i++) {
			buffer.append("-");
		}
		buffer.append("\n");
		return buffer.toString();
	}
}
