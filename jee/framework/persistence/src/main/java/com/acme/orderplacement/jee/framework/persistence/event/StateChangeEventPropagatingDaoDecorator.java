/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.event;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;

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
	@Created
	private Event<T> entityCreatedEvent;

	@Inject
	@Updated
	private Event<T> entityUpdatedEvent;

	@Inject
	@Deleted
	private Event<T> entityDeletedEvent;

	// -------------------------------------------------------------------------
	// Decorated methods
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
	 */
	@Override
	public T makePersistent(final T transientObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		final T persistentObject = getDelegate()
				.makePersistent(transientObject);

		this.entityCreatedEvent.fire(persistentObject);

		return persistentObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
	 */
	@Override
	public T makePersistentOrUpdatePersistentState(final T object)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		final T persistedOrUpdatedObject = getDelegate()
				.makePersistentOrUpdatePersistentState(object);

		this.entityUpdatedEvent.fire(persistedOrUpdatedObject);

		return persistedOrUpdatedObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
	 */
	@Override
	public void makeTransient(final T persistentOrDetachedObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		getDelegate().makeTransient(persistentOrDetachedObject);

		this.entityDeletedEvent.fire(persistentOrDetachedObject);
	}

	// -------------------------------------------------------------------------
	// Implementation methods
	// -------------------------------------------------------------------------

	protected abstract GenericJpaDao<T, ID> getDelegate();
}
