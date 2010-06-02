/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.jee.framework.persistence.exception.DataAccessRuntimeException;
import com.acme.orderplacement.jee.framework.persistence.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotPersistentException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateDeletedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateLockedException;
import com.acme.orderplacement.jee.item.persistence.ItemDao;

/**
 * <p>
 * A helper class that wraps
 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao
 * <code>JpaItemDao</code>}, starting a new transaction before delegating to the
 * wrapped <code>JpaItemDao</code>. This is so we may test said DAO although it
 * declares a {@link javax.ejb.TransactionAttributeType#MANDATORY
 * <code>MANDATORY</code>} {@link javax.ejb.TransactionAttribute
 * <code>TransactionAttribute</code>} on each state-modifying operation.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Stateless
@Local(ItemDao.class)
public class TransactionInitiatingItemDaoProxyBean implements ItemDao {

	@Inject
	private Principal user;

	@EJB(lookup = "java:global/test/JpaItemDao")
	private ItemDao delegate;

	/**
	 * @param persistentObject
	 * @throws DataAccessRuntimeException
	 * @throws ObjectNotPersistentException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#evict(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void evict(final Item persistentObject)
			throws DataAccessRuntimeException, ObjectNotPersistentException {
		this.delegate.evict(persistentObject);
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findAll()
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Item> findAll() throws DataAccessRuntimeException {
		System.out.println("USER -> " + this.user);
		return this.delegate.findAll();
	}

	/**
	 * @param id
	 * @param lock
	 * @return
	 * @throws NoSuchPersistentObjectException
	 * @throws DataAccessRuntimeException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findById(java.io.Serializable,
	 *      boolean)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item findById(final Long id, final boolean lock)
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		return this.delegate.findById(id, lock);
	}

	/**
	 * @param itemNumber
	 * @return
	 * @see com.acme.orderplacement.jee.item.persistence.ItemDao#findByItemNumber(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item findByItemNumber(final String itemNumber) {
		return this.delegate.findByItemNumber(itemNumber);
	}

	/**
	 * @param itemName
	 * @return
	 * @see com.acme.orderplacement.jee.item.persistence.ItemDao#findByNameLike(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Item> findByNameLike(final String itemName) {
		return this.delegate.findByNameLike(itemName);
	}

	/**
	 * @throws DataAccessRuntimeException
	 * @throws PersistentStateLockedException
	 * @throws PersistentStateConcurrentlyModifiedException
	 * @throws PersistentStateDeletedException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#flush()
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void flush() throws DataAccessRuntimeException,
			PersistentStateLockedException,
			PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException {
		this.delegate.flush();
	}

	/**
	 * @param transientObject
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws ObjectNotTransientException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item makePersistent(final Item transientObject)
			throws DataAccessRuntimeException, ObjectNotTransientException {
		return this.delegate.makePersistent(transientObject);
	}

	/**
	 * @param object
	 * @return
	 * @throws DataAccessRuntimeException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item makePersistentOrUpdatePersistentState(final Item object)
			throws DataAccessRuntimeException {
		return this.delegate.makePersistentOrUpdatePersistentState(object);
	}

	/**
	 * @param persistentOrDetachedObject
	 * @throws DataAccessRuntimeException
	 * @throws ObjectTransientException
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void makeTransient(final Item persistentOrDetachedObject)
			throws DataAccessRuntimeException, ObjectTransientException {
		this.delegate.makeTransient(persistentOrDetachedObject);
	}
}
