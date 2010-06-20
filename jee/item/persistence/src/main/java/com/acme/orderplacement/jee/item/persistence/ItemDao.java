/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence;

import java.util.List;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;

/**
 * @author o.bergner
 * 
 */
public interface ItemDao extends GenericJpaDao<Item, Long> {

	/**
	 * @param itemNumber
	 * @return
	 */
	Item findByItemNumber(String itemNumber);

	/**
	 * @param itemName
	 * @return
	 */
	List<Item> findByNameLike(String itemName);
}
