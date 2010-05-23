/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.exception;

import java.io.Serializable;

import com.acme.orderplacement.framework.persistence.common.GenericJpaDao;

/**
 * <p>
 * A <strong>checked</strong> exception indicating that a client passed a
 * <tt>Transient Object</tt> into a mehod that expects a
 * <tt>Persistent Object</tt> or <tt>Detached Object</tt>. A standard example is
 * attempting to {@link GenericJpaDao#evict(Object) <code>evict</code>} a
 * <tt>Transient Object</tt> from a <code>Session</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class ObjectTransientException extends IllegalDataAccessUsageException
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6368561370218204475L;

	/**
	 * The <tt>Transient Object</tt> that caused this exception to be thrown.
	 */
	private final Object transientObject;

	/**
	 * @param transientObject
	 */
	public ObjectTransientException(final Object transientObject) {
		super("The object ['" + transientObject
				+ "'] is neither persistent nor detached.");
		this.transientObject = transientObject;
	}

	/**
	 * @param transientObject
	 * @param message
	 */
	public ObjectTransientException(final Object transientObject,
			final String message) {
		super(message);
		this.transientObject = transientObject;
	}

	/**
	 * @param transientObject
	 * @param cause
	 */
	public ObjectTransientException(final Object transientObject,
			final Throwable cause) {
		super(cause);
		this.transientObject = transientObject;
	}

	/**
	 * @param transientObject
	 * @param message
	 * @param cause
	 */
	public ObjectTransientException(final Object transientObject,
			final String message, final Throwable cause) {
		super(message, cause);
		this.transientObject = transientObject;
	}

	/**
	 * Look up and return the <code>transientObject</code>.
	 * 
	 * @return the transientObject
	 */
	public Object getTransientObject() {
		return this.transientObject;
	}

}
