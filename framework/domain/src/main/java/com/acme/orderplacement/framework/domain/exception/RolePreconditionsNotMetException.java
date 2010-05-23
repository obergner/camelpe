/**
 * 
 */
package com.acme.orderplacement.framework.domain.exception;

/**
 * <p>
 * A <strong>checked</strong> exception signallng failure on behalf of an
 * <tt>Actor</tt> to meet preconditions necessary to be allowed to take on a
 * requested <tt>Role</tt>. A classical example is that of an underage
 * <tt>Person</tt> attempting to assume the <tt>Role</tt> of a <tt>Customer</tt>
 * .
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class RolePreconditionsNotMetException extends
		CollaborationPreconditionsNotMetException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8794178657177219488L;

	/**
	 * 
	 */
	public RolePreconditionsNotMetException() {
		// Intentionally left blank
	}

	/**
	 * @param message
	 */
	public RolePreconditionsNotMetException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RolePreconditionsNotMetException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RolePreconditionsNotMetException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
