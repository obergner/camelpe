/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang.Validate;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.framework.common.role.ApplicationUserRole;
import com.acme.orderplacement.jee.framework.persistence.jpa.AbstractJpaDao;
import com.acme.orderplacement.jee.framework.persistence.meta.annotation.ReadOnlyPersistenceOperation;
import com.acme.orderplacement.jee.item.persistence.ItemDao;

/**
 * @author o.bergner
 * 
 */
@DeclareRoles( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EXTERNAL_USER,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Stateless
@Local(ItemDao.class)
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
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	 * @see com.acme.orderplacement.jee.framework.persistence.jpa.AbstractJpaDao#entityManager()
	 */
	@Override
	protected EntityManager entityManager() {
		return this.entityManager;
	}
}
