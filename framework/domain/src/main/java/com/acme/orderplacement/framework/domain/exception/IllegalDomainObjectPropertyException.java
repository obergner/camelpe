/**
 * 
 */
package com.acme.orderplacement.framework.domain.exception;

/**
 * <p>
 * A <strong>checked</strong> exception signalling a client an attempt to set a
 * property on a <tt>Domain Object</tt> to a value that does not comply with the
 * business rules at hand. An example is the attempt to set the due date on an
 * invoice to a date in the past.
 * </p>
 * <p>
 * <strong>Important</strong> This exception is <strong>not</strong> meant to
 * guard against mistakes on the behalf of a programmer, i.e. against setting a
 * property to <code>null</code>. In these cases, a standard
 * {@link RuntimeException <code>RuntimeException</code>} like
 * {@link IllegalArgumentException <code>IllegalArgumentException</code>} is
 * more apropriate. Rather, the error conditions reported through an
 * <code>IllegalDomainObjectPropertyException</code> should be meaningful in the
 * context of the relevant <tt>Domain</tt>.
 * </p>
 * <p>
 * This eception explicitly concerns itself solely with <i>simple
 * properties</i>, <strong>not</strong> with <tt>Collaborators</tt> of a given
 * <tt>Domain Object</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class IllegalDomainObjectPropertyException extends
		DomainRuleViolatedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3400811809854677857L;

	/**
	 * 
	 */
	public IllegalDomainObjectPropertyException() {
		// Intentionally left blank
	}

	/**
	 * @param message
	 */
	public IllegalDomainObjectPropertyException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IllegalDomainObjectPropertyException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalDomainObjectPropertyException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
