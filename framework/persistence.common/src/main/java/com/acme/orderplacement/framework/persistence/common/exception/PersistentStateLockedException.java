/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.exception;

import java.io.Serializable;

/**
 * <p>
 * An <strong>unchecked</strong> exception indicating that an attempted
 * operation on a <tt>Persistent Object</tt> or <tt>Detached Object</tt> failed
 * because its representation in the underlying datastore is
 * <strong>locked</strong>. A standard example is trying to update a
 * <tt>Detached Object</tt> in one <code>Session</code> that is locked by
 * another <code>Session</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class PersistentStateLockedException extends DataAccessRuntimeException
		implements Serializable {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 7863976408667421785L;

	/**
	 * The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as passed in
	 * by the client that is currently locked.
	 */
	private final Object lockedObject;

	/**
	 * @param lockedObject
	 *            The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as
	 *            passed in by the client that is currently locked
	 */
	public PersistentStateLockedException(final Object lockedObject) {
		super("The object ['" + lockedObject + "'] is currently locked.");
		this.lockedObject = lockedObject;
	}

	/**
	 * @param lockedObject
	 *            The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as
	 *            passed in by the client that is currently locked
	 * @param message
	 */
	public PersistentStateLockedException(final Object lockedObject,
			final String message) {
		super(message);
		this.lockedObject = lockedObject;
	}

	/**
	 * @param lockedObject
	 *            The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as
	 *            passed in by the client that is currently locked
	 * @param cause
	 */
	public PersistentStateLockedException(final Object lockedObject,
			final Throwable cause) {
		super(cause);
		this.lockedObject = lockedObject;
	}

	/**
	 * @param lockedObject
	 *            The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as
	 *            passed in by the client that is currently locked
	 * @param message
	 * @param cause
	 */
	public PersistentStateLockedException(final Object lockedObject,
			final String message, final Throwable cause) {
		super(message, cause);
		this.lockedObject = lockedObject;
	}

	/**
	 * Look up and return the <tt>Persistent Object</tt> or
	 * <tt>Detached Object</tt> as passed in by the client that is currently
	 * locked.
	 * 
	 * @return The <tt>Persistent Object</tt> or <tt>Detached Object</tt> as
	 *         passed in by the client that is currently locked
	 */
	public Object getLockedObject() {
		return this.lockedObject;
	}

}
