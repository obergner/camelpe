/**
 * 
 */
package com.acme.orderplacement.persistence.support.exception;

import java.io.Serializable;

import org.apache.commons.lang.exception.NestableException;

/**
 * <p>
 * Base class for all <strong>checked</strong> exceptions signalling that a
 * client failed to correctly use the Persistence API, e.g. tried to look up a
 * non-existing entity by ID.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public abstract class IllegalDataAccessUsageException extends NestableException
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3573356112504961146L;

	/**
	 * 
	 */
	public IllegalDataAccessUsageException() {
		super();
	}

	/**
	 * @param message
	 */
	public IllegalDataAccessUsageException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IllegalDataAccessUsageException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalDataAccessUsageException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
