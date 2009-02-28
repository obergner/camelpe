/**
 * 
 */
package com.acme.orderplacement.domain.support.exception;

/**
 * <p>
 * A <strong>checked</strong> exception signalling failure on behalf of a
 * <tt>Domain Object</tt> to fulfill preconditions necessary for intiating a
 * requested <tt>Collaboration</tt> with another <tt>Domain Object</tt>. An
 * example is the attempt of a <tt>Customer</tt> to place an <tt>Order</tt> for
 * a handgun while being unable to produce a valid <tt>Firearms Certificate</tt>
 * .
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CollaborationPreconditionsNotMetException extends
		DomainRuleViolatedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5847718752259216960L;

	/**
	 * 
	 */
	public CollaborationPreconditionsNotMetException() {
		// Intentionally left blank
	}

	/**
	 * @param message
	 */
	public CollaborationPreconditionsNotMetException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CollaborationPreconditionsNotMetException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CollaborationPreconditionsNotMetException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
