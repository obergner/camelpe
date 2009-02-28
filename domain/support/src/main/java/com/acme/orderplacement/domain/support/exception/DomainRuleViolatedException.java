/**
 * 
 */
package com.acme.orderplacement.domain.support.exception;

import java.io.Serializable;

import org.apache.commons.lang.exception.NestableException;

/**
 * <p>
 * Common base class for all <strong>checked</strong> exceptions that signal a
 * failure to meet an <tt>integrity constraint</tt> defined in the
 * <tt>domain</tt> at hand.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class DomainRuleViolatedException extends NestableException
		implements Serializable {

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 7353981944417152785L;

	/**
	 * 
	 */
	public DomainRuleViolatedException() {
		// Intentionally left blank
	}

	/**
	 * @param message
	 */
	public DomainRuleViolatedException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DomainRuleViolatedException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DomainRuleViolatedException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
