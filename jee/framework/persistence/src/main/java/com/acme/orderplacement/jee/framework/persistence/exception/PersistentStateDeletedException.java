/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.exception;

import java.io.Serializable;

/**
 * <p>
 * An <strong>unchecked</strong> exception indicating that an attempted
 * operation on a <tt>Persistent Object</tt> failed because its representation
 * in the underlying datastore has been deleted since the given
 * <tt>Persistent Object</tt> had been loaded into memory. A standard example is
 * trying to update a <tt>Persistent Object</tt> in one <code>Session</code>
 * that has been deleted in another <code>Session</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class PersistentStateDeletedException extends DataAccessRuntimeException
		implements Serializable {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 6926960911610354588L;

	/**
	 * The <tt>Persistent Object</tt> that has been removed from the underlying
	 * datastore.
	 */
	private final Object deletedPersistentObject;

	/**
	 * @param deletedPersistentObject
	 *            The <tt>Persistent Object</tt> that has been removed from the
	 *            underlying datastore.
	 */
	public PersistentStateDeletedException(final Object deletedPersistentObject) {
		super("The Persistent Object ['" + deletedPersistentObject
				+ "'] has been removed from the underlying datastore.");
		this.deletedPersistentObject = deletedPersistentObject;
	}

	/**
	 * @param deletedPersistentObject
	 *            The <tt>Persistent Object</tt> that has been removed from the
	 *            underlying datastore.
	 * @param message
	 */
	public PersistentStateDeletedException(
			final Object deletedPersistentObject, final String message) {
		super(message);
		this.deletedPersistentObject = deletedPersistentObject;
	}

	/**
	 * @param deletedPersistentObject
	 *            The <tt>Persistent Object</tt> that has been removed from the
	 *            underlying datastore.
	 * @param cause
	 */
	public PersistentStateDeletedException(
			final Object deletedPersistentObject, final Throwable cause) {
		super(cause);
		this.deletedPersistentObject = deletedPersistentObject;
	}

	/**
	 * @param deletedPersistentObject
	 *            The <tt>Persistent Object</tt> that has been removed from the
	 *            underlying datastore.
	 * @param message
	 * @param cause
	 */
	public PersistentStateDeletedException(
			final Object deletedPersistentObject, final String message,
			final Throwable cause) {
		super(message, cause);
		this.deletedPersistentObject = deletedPersistentObject;
	}

	/**
	 * Look up and return the <tt>Persistent Object</tt> that has been removed
	 * from the underlying datastore.
	 * 
	 * @return The <tt>Persistent Object</tt> that has been removed from the
	 *         underlying datastore.
	 */
	public Object getDeletedPersistentObject() {
		return this.deletedPersistentObject;
	}

}
