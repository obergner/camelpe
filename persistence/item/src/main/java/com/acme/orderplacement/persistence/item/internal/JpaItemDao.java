/**
 * 
 */
package com.acme.orderplacement.persistence.item.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.persistence.item.ItemDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * @author o.bergner
 * 
 */
@Repository(ItemDao.SERVICE_NAME)
public class JpaItemDao extends AbstractJpaDao<Item, Long> implements ItemDao {

	// ------------------------------------------------------------------------
	// Implementation of ItemDao
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.persistence.item.ItemDao.domain.ItemDAO#findByItemNumber(java.lang.String)
	 */
	public Item findByItemNumber(final String itemNumber) {
		Validate.notNull(itemNumber, "itemNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("itemNumber", itemNumber);

		return findUniqueByNamedQuery(Item.Queries.BY_ITEM_NUMBER, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.item.ItemDao.domain.ItemDAO#findByNameLike(java.lang.String)
	 */
	public List<Item> findByNameLike(final String itemName) {
		Validate.notNull(itemName, "itemName");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("name", "%" + itemName + "%");

		return findByNamedQuery(Item.Queries.BY_NAME_LIKE, parameters);
	}

}
