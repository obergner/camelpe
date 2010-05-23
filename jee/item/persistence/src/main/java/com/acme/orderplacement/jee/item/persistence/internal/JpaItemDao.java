/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.jee.item.persistence.ItemDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;
import com.acme.orderplacement.persistence.support.meta.annotation.ReadOnlyPersistenceOperation;

/**
 * @author o.bergner
 * 
 */
@Repository(ItemDao.REPOSITORY_NAME)
public class JpaItemDao extends AbstractJpaDao<Item, Long> implements ItemDao {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Our {@link javax.persistence.EntityManager <em>JPA EntityManager</em>}.
	 * </p>
	 */
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	// ------------------------------------------------------------------------
	// Implementation of ItemDao
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.jee.item.persistence.ItemDao.domain.ItemDAO#findByItemNumber(java.lang.String)
	 */
	@ReadOnlyPersistenceOperation
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Item findByItemNumber(final String itemNumber) {
		Validate.notNull(itemNumber, "itemNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("itemNumber", itemNumber);

		return findUniqueByNamedQuery(Item.Queries.BY_ITEM_NUMBER, parameters);
	}

	/**
	 * @see com.acme.orderplacement.jee.item.persistence.ItemDao.domain.ItemDAO#findByNameLike(java.lang.String)
	 */
	@ReadOnlyPersistenceOperation
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Item> findByNameLike(final String itemName) {
		Validate.notNull(itemName, "itemName");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("name", "%" + itemName + "%");

		return findByNamedQuery(Item.Queries.BY_NAME_LIKE, parameters);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#entityManager()
	 */
	@Override
	protected EntityManager entityManager() {
		return this.entityManager;
	}
}
