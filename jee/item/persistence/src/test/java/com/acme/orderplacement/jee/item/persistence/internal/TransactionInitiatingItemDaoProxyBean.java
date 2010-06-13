/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.acme.orderplacement.domain.item.Item;
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

	@EJB(lookup = "java:global/test/JpaItemDao")
	private ItemDao delegate;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void evict(final Item persistentObject) {
		this.delegate.evict(persistentObject);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Item> findAll() {
		return this.delegate.findAll();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item findById(final Long id, final boolean lock) {
		return this.delegate.findById(id, lock);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item findByItemNumber(final String itemNumber) {
		return this.delegate.findByItemNumber(itemNumber);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Item> findByNameLike(final String itemName) {
		return this.delegate.findByNameLike(itemName);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void flush() {
		this.delegate.flush();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item makePersistent(final Item transientObject) {
		return this.delegate.makePersistent(transientObject);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Item makePersistentOrUpdatePersistentState(final Item object) {
		return this.delegate.makePersistentOrUpdatePersistentState(object);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void makeTransient(final Item persistentOrDetachedObject) {
		this.delegate.makeTransient(persistentOrDetachedObject);
	}
}
