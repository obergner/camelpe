/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.exception;

import java.io.Serializable;

import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;

/**
 * <p>
 * A <strong>checked</strong> exception indicating that a client passed a
 * <tt>Persistent Object</tt> or <tt>Detached Object</tt> into a mehod that
 * expects a <tt>Transient Object</tt>. A standard example is attempting to
 * {@link GenericJpaDao#updatePersistentState(Object) <code>save</code>} a
 * <tt>Persistent Object</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class ObjectNotTransientException extends
		IllegalDataAccessUsageException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9001184288093625924L;

	/**
	 * The <tt>Persistent Object</tt> or <tt>Transient Object</tt> that caused
	 * this exception to be thrown.
	 */
	private final Object nonTransientObject;

	/**
	 * @param nonTransientObject
	 *            The <tt>Persistent Object</tt> or <tt>Transient Object</tt>
	 *            that caused this exception to be thrown
	 */
	public ObjectNotTransientException(final Object nonTransientObject) {
		super("The object ['" + nonTransientObject + "'] is not transient.");
		this.nonTransientObject = nonTransientObject;
	}

	/**
	 * @param nonTransientObject
	 *            The <tt>Persistent Object</tt> or <tt>Transient Object</tt>
	 *            that caused this exception to be thrown
	 * @param message
	 */
	public ObjectNotTransientException(final Object nonTransientObject,
			final String message) {
		super(message);
		this.nonTransientObject = nonTransientObject;
	}

	/**
	 * @param nonTransientObject
	 *            The <tt>Persistent Object</tt> or <tt>Transient Object</tt>
	 *            that caused this exception to be thrown
	 * @param cause
	 */
	public ObjectNotTransientException(final Object nonTransientObject,
			final Throwable cause) {
		super(cause);
		this.nonTransientObject = nonTransientObject;
	}

	/**
	 * @param nonTransientObject
	 *            The <tt>Persistent Object</tt> or <tt>Transient Object</tt>
	 *            that caused this exception to be thrown
	 * @param message
	 * @param cause
	 */
	public ObjectNotTransientException(final Object nonTransientObject,
			final String message, final Throwable cause) {
		super(message, cause);
		this.nonTransientObject = nonTransientObject;
	}

	/**
	 * Look up and return the <tt>Persistent Object</tt> or
	 * <tt>Transient Object</tt> that caused this exception to be thrown.
	 * 
	 * @return The <tt>Persistent Object</tt> or <tt>Transient Object</tt> that
	 *         caused this exception to be thrown
	 */
	public Object getNonTransientObject() {
		return this.nonTransientObject;
	}

}
