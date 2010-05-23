/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.exception;

import java.io.Serializable;

import com.acme.orderplacement.framework.persistence.common.GenericJpaDao;

/**
 * <p>
 * A <strong>checked</strong> exception indicating that a client passed a
 * <tt>Transient Object</tt> or <tt>Detached Object</tt> into a mehod that
 * expects a <tt>Persistent Object</tt>. A standard example is attempting to
 * {@link GenericJpaDao#updatePersistentState(Object) <code>update</code>} a
 * <tt>Transient Object</tt>'s <tt>Persistent State</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class ObjectNotPersistentException extends
		IllegalDataAccessUsageException implements Serializable {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = -8377706926604436967L;

	/**
	 * The <tt>Transient Object</tt> or <tt>Detached Object</tt> that caused
	 * this exception to be thrown.
	 */
	private final Object nonPersistentObject;

	/**
	 * @param nonPersistentObject
	 *            The <tt>Transient Object</tt> or <tt>Detached Object</tt> that
	 *            caused this exception to be thrown
	 */
	public ObjectNotPersistentException(final Object nonPersistentObject) {
		super("The object ['" + nonPersistentObject + "'] is not persistent.");
		this.nonPersistentObject = nonPersistentObject;
	}

	/**
	 * @param nonPersistentObject
	 *            The <tt>Transient Object</tt> or <tt>Detached Object</tt> that
	 *            caused this exception to be thrown
	 * @param message
	 */
	public ObjectNotPersistentException(final Object nonPersistentObject,
			final String message) {
		super(message);
		this.nonPersistentObject = nonPersistentObject;
	}

	/**
	 * @param nonPersistentObject
	 *            The <tt>Transient Object</tt> or <tt>Detached Object</tt> that
	 *            caused this exception to be thrown
	 * @param cause
	 */
	public ObjectNotPersistentException(final Object nonPersistentObject,
			final Throwable cause) {
		super(cause);
		this.nonPersistentObject = nonPersistentObject;
	}

	/**
	 * @param nonPersistentObject
	 *            The <tt>Transient Object</tt> or <tt>Detached Object</tt> that
	 *            caused this exception to be thrown
	 * @param message
	 * @param cause
	 */
	public ObjectNotPersistentException(final Object nonPersistentObject,
			final String message, final Throwable cause) {
		super(message, cause);
		this.nonPersistentObject = nonPersistentObject;
	}

	/**
	 * Look up and return the <tt>Transient Object</tt> or
	 * <tt>Detached Object</tt> that caused this exception to be thrown.
	 * 
	 * @return the nonPersistentObject
	 */
	public Object getNonPersistentObject() {
		return this.nonPersistentObject;
	}

}
