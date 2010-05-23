/**
 * 
 */
package com.acme.orderplacement.framework.service.exception;

import org.apache.commons.lang.exception.NestableException;

/**
 * <p>
 * A <strong>checked</strong> exception indicating a client's failure to
 * properly use a service.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class IllegalServiceUsageException extends NestableException {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -1789616648878966363L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	protected IllegalServiceUsageException() {
		// Intentionally left blank
	}

	/**
	 * @param msg
	 */
	protected IllegalServiceUsageException(final String msg) {
		super(msg);
	}

	/**
	 * @param cause
	 */
	protected IllegalServiceUsageException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	protected IllegalServiceUsageException(final String msg,
			final Throwable cause) {
		super(msg, cause);
	}
}
