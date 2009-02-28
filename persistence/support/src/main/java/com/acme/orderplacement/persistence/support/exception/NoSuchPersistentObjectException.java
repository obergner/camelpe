/**
 * 
 */
package com.acme.orderplacement.persistence.support.exception;

import java.io.Serializable;

/**
 * <p>
 * A <strong>checked</strong> exception to be thrown if a client tries to look
 * up a <tt>Persistent Object</tt> using a criterion for which business logic
 * guarantees that a matching instance exists yet none could be found. The
 * standard situation is a lookup by <code>ID</code> while no
 * <tt>Persistent Object</tt> with the specified <code>ID</code> exists.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class NoSuchPersistentObjectException extends
		IllegalDataAccessUsageException implements Serializable {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 7740312968248066174L;

	/**
	 * The {@link Class <code>Class</code>} of the entity the client tried to
	 * look up.
	 */
	final Class<?> entityType;

	/**
	 * The <code>ID</code> the client used to look up the entity.
	 */
	final Serializable entityId;

	/**
	 * 
	 */
	public NoSuchPersistentObjectException(final Class<?> entityType,
			final Serializable entityId) {
		super("No entity of type ['" + entityType.getName()
				+ "'] having the ID [" + entityId
				+ "] could be found in the underlying datastore.");
		this.entityType = entityType;
		this.entityId = entityId;
	}

	/**
	 * @param message
	 */
	public NoSuchPersistentObjectException(final Class<?> entityType,
			final Serializable entityId, final String message) {
		super(message);
		this.entityType = entityType;
		this.entityId = entityId;
	}

	/**
	 * @param cause
	 */
	public NoSuchPersistentObjectException(final Class<?> entityType,
			final Serializable entityId, final Throwable cause) {
		super(cause);
		this.entityType = entityType;
		this.entityId = entityId;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoSuchPersistentObjectException(final Class<?> entityType,
			final Serializable entityId, final String message,
			final Throwable cause) {
		super(message, cause);
		this.entityType = entityType;
		this.entityId = entityId;
	}

	/**
	 * Look up and return the <code>entityId</code>.
	 * 
	 * @return the entityId
	 */
	public Serializable getEntityId() {
		return this.entityId;
	}

	/**
	 * Look up and return the <code>entityType</code>.
	 * 
	 * @return the entityType
	 */
	public Class<?> getEntityType() {
		return this.entityType;
	}
}
