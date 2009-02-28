/**
 * 
 */
package com.acme.orderplacement.service.support.exception.entity;

import com.acme.orderplacement.service.support.exception.IllegalServiceUsageException;

/**
 * <p>
 * A <strong>checked</strong> <code>Exception</code> to be thrown if a client
 * attempts to register an <code>Entity</code> that has already been registered.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EntityAlreadyRegisteredException extends
		IllegalServiceUsageException {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 8673260231958963957L;

	private final Object alreadyRegisteredEntity;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public EntityAlreadyRegisteredException(final Object alreadyRegisteredEntity) {
		super("The entity [" + alreadyRegisteredEntity
				+ "] has already been registered");
		this.alreadyRegisteredEntity = alreadyRegisteredEntity;
	}

	/**
	 * @param msg
	 */
	public EntityAlreadyRegisteredException(
			final Object alreadyRegisteredEntity, final String msg) {
		super(msg);
		this.alreadyRegisteredEntity = alreadyRegisteredEntity;
	}

	/**
	 * @param cause
	 */
	public EntityAlreadyRegisteredException(
			final Object alreadyRegisteredEntity, final Throwable cause) {
		super(cause);
		this.alreadyRegisteredEntity = alreadyRegisteredEntity;
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public EntityAlreadyRegisteredException(
			final Object alreadyRegisteredEntity, final String msg,
			final Throwable cause) {
		super(msg, cause);
		this.alreadyRegisteredEntity = alreadyRegisteredEntity;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the alreadyRegisteredEntity
	 */
	public final Object getAlreadyRegisteredEntity() {
		return this.alreadyRegisteredEntity;
	}
}
