/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.exception;

import java.io.Serializable;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * <p>
 * Base class of all <strong>unchecked</strong> exceptions signalling failure to
 * access the underlying datastore due to reasons the client has no control
 * over, e.g. network failure.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class DataAccessRuntimeException extends NestableRuntimeException
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2700752567189625406L;

	/**
	 * 
	 */
	public DataAccessRuntimeException() {
		super();
	}

	/**
	 * @param message
	 */
	public DataAccessRuntimeException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataAccessRuntimeException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataAccessRuntimeException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
