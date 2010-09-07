/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.jee.framework.persistence.event.Created;
import com.acme.orderplacement.jee.framework.persistence.event.Deleted;
import com.acme.orderplacement.jee.framework.persistence.event.Updated;
import com.acme.orderplacement.jee.item.persistence.ItemDao;

/**
 * <p>
 * TODO: Insert short summary for StateChangeEventPropagatingItemDaoDecorator
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Decorator
public abstract class StateChangeEventPropagatingItemDaoDecorator implements
		ItemDao {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	@Delegate
	@Any
	private ItemDao delegate;

	@Inject
	@Created
	private Event<Item> entityCreatedEvent;

	@Inject
	@Updated
	private Event<Item> entityUpdatedEvent;

	@Inject
	@Deleted
	private Event<Item> entityDeletedEvent;

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
	 */
	@Override
	public Item makePersistent(final Item transientObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		final Item persistentObject = getDelegate().makePersistent(
				transientObject);

		this.entityCreatedEvent.fire(persistentObject);
		getLog().trace("Fired CREATED event for persistent object [{}]",
				persistentObject);

		return persistentObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
	 */
	@Override
	public Item makePersistentOrUpdatePersistentState(final Item object)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		final Item persistedOrUpdatedObject = getDelegate()
				.makePersistentOrUpdatePersistentState(object);

		this.entityUpdatedEvent.fire(persistedOrUpdatedObject);
		getLog().trace("Fired UPDATED event for persistent object [{}]",
				persistedOrUpdatedObject);

		return persistedOrUpdatedObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
	 */
	@Override
	public void makeTransient(final Item persistentOrDetachedObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		getDelegate().makeTransient(persistentOrDetachedObject);

		this.entityDeletedEvent.fire(persistentOrDetachedObject);
		getLog().trace("Fired DELETED event for persistent object [{}]",
				persistentOrDetachedObject);
	}

	protected Logger getLog() {
		return this.log;
	}

	protected ItemDao getDelegate() {
		return this.delegate;
	}
}
