/**
 * 
 */
package com.acme.orderplacement.domain.support.exception;

/**
 * <p>
 * A <strong>checked</strong> exception for signalling that a given
 * <tt>Domain Object</tt> is not in a proper <em>state</em> to fulfill a
 * requested operation. An example is an <tt>Order</tt> asked to deliver itself
 * while it has in fact been cancelled.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class IllegalDomainObjectStateException extends
		DomainRuleViolatedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4176578666771631081L;

	/**
	 * 
	 */
	public IllegalDomainObjectStateException() {
		// Intentionally left blank
	}

	/**
	 * @param message
	 */
	public IllegalDomainObjectStateException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IllegalDomainObjectStateException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalDomainObjectStateException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
