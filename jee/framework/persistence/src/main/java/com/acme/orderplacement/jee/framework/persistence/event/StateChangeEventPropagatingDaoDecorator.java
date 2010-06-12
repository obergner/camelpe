/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.event;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;
import com.acme.orderplacement.jee.framework.persistence.exception.DataAccessRuntimeException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectTransientException;

/**
 * <p>
 * TODO: Insert short summary for StateChangeEventPropagatingDaoDecorator
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class StateChangeEventPropagatingDaoDecorator<T, ID extends Serializable>
		implements GenericJpaDao<T, ID> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	@Inject
	private Event<T> stateChangeEvents;

	// -------------------------------------------------------------------------
	// Decorated methods
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
	 */
	@Override
	public T makePersistent(final T transientObject)
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final T persistentObject = getDelegate()
				.makePersistent(transientObject);

		this.stateChangeEvents.fire(persistentObject);

		return persistentObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
	 */
	@Override
	public T makePersistentOrUpdatePersistentState(final T object)
			throws DataAccessRuntimeException {
		final T persistedOrUpdatedObject = getDelegate()
				.makePersistentOrUpdatePersistentState(object);

		this.stateChangeEvents.fire(persistedOrUpdatedObject);

		return persistedOrUpdatedObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
	 */
	@Override
	public void makeTransient(final T persistentOrDetachedObject)
			throws DataAccessRuntimeException, ObjectTransientException {
		getDelegate().makeTransient(persistentOrDetachedObject);

		this.stateChangeEvents.fire(persistentOrDetachedObject);
	}

	// -------------------------------------------------------------------------
	// Implementation methods
	// -------------------------------------------------------------------------

	protected abstract GenericJpaDao<T, ID> getDelegate();
}
