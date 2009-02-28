/**
 * 
 */
package com.acme.orderplacement.persistence.support.exception;

import java.io.Serializable;

/**
 * <p>
 * An <strong>unchecked</strong> exception to be thrown if the user tries to
 * modify the <tt>Peristent State</tt> of a <tt>Persistent</tt> or
 * <tt>Detached Object</tt> that has been concurrently modified by another user
 * (optimistic locking).
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class PersistentStateConcurrentlyModifiedException extends
		DataAccessRuntimeException implements Serializable {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = -3688240108104912359L;

	/**
	 * String to be returned if the <i>ID</i> of the <tt>Persistent Object</tt>
	 * whose <tt>Persistent State</tt> has been concurrently modified by another
	 * user is not known.
	 */
	public static final String UNKNOWN_PERSISTENT_OBJECT_ID = "<UNKNOWN PERSISTENT OBJECT ID>";

	/**
	 * String to be returned if the <i>name</i> of the
	 * <tt>Persistent Object</tt> whose <tt>Persistent State</tt> has been
	 * concurrently modified by another user is not known.
	 */
	public static final String UNKNOWN_PERSISTENT_OBJECT_NAME = "<UNKNOWN PERSISTENT OBJECT NAME>";

	/**
	 * The <code>ID</code> of the <tt>Persistent Object</tt> that has been
	 * concurrently modified.
	 */
	private final Serializable concurrentlyModifiedObjectId;

	/**
	 * The <i>name</i> of the <tt>Persistent Object</tt> whose
	 * <tt>Persistent State</tt> has been concurrently modified.
	 */
	private final String concurrentlyModifiedObjectName;

	/**
	 * 
	 */
	public PersistentStateConcurrentlyModifiedException(
			final Serializable concurrentlyModifiedObjectId,
			final String concurrentlyModifiedObjectName) {
		super("The Persistent State of the Persistent Object [ID = "
				+ concurrentlyModifiedObjectId + ", name = '"
				+ concurrentlyModifiedObjectName
				+ "'] has been concurrently modified by another user.");
		this.concurrentlyModifiedObjectId = concurrentlyModifiedObjectId != null ? concurrentlyModifiedObjectId
				: UNKNOWN_PERSISTENT_OBJECT_ID;
		this.concurrentlyModifiedObjectName = concurrentlyModifiedObjectName != null ? concurrentlyModifiedObjectName
				: UNKNOWN_PERSISTENT_OBJECT_NAME;
	}

	/**
	 * @param message
	 */
	public PersistentStateConcurrentlyModifiedException(
			final Serializable concurrentlyModifiedObjectId,
			final String concurrentlyModifiedObjectName, final String message) {
		super(message);
		this.concurrentlyModifiedObjectId = concurrentlyModifiedObjectId != null ? concurrentlyModifiedObjectId
				: UNKNOWN_PERSISTENT_OBJECT_ID;
		this.concurrentlyModifiedObjectName = concurrentlyModifiedObjectName != null ? concurrentlyModifiedObjectName
				: UNKNOWN_PERSISTENT_OBJECT_NAME;
	}

	/**
	 * @param cause
	 */
	public PersistentStateConcurrentlyModifiedException(
			final Serializable concurrentlyModifiedObjectId,
			final String concurrentlyModifiedObjectName, final Throwable cause) {
		super(cause);
		this.concurrentlyModifiedObjectId = concurrentlyModifiedObjectId != null ? concurrentlyModifiedObjectId
				: UNKNOWN_PERSISTENT_OBJECT_ID;
		this.concurrentlyModifiedObjectName = concurrentlyModifiedObjectName != null ? concurrentlyModifiedObjectName
				: UNKNOWN_PERSISTENT_OBJECT_NAME;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PersistentStateConcurrentlyModifiedException(
			final Serializable concurrentlyModifiedObjectId,
			final String concurrentlyModifiedObjectName, final String message,
			final Throwable cause) {
		super(message, cause);
		this.concurrentlyModifiedObjectId = concurrentlyModifiedObjectId != null ? concurrentlyModifiedObjectId
				: UNKNOWN_PERSISTENT_OBJECT_ID;
		this.concurrentlyModifiedObjectName = concurrentlyModifiedObjectName != null ? concurrentlyModifiedObjectName
				: UNKNOWN_PERSISTENT_OBJECT_NAME;
	}

	/**
	 * Look up and return the <code>concurrentlyModifiedObjectName</code>.
	 * 
	 * @return the concurrentlyModifiedObjectName
	 */
	public String getConcurrentlyModifiedObjectName() {
		return this.concurrentlyModifiedObjectName;
	}

	/**
	 * Look up and return the <code>concurrentlyModifiedObjectId</code>.
	 * 
	 * @return the concurrentlyModifiedObjectId
	 */
	public Serializable getConcurrentlyModifiedObjectId() {
		return this.concurrentlyModifiedObjectId;
	}

}
