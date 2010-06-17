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
// @RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
// ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
// ApplicationUserRole.ROLE_ADMIN })
public interface ItemDao extends GenericJpaDao<Item, Long> {

	/**
	 * @param itemNumber
	 * @return
	 */
	// @RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
	// ApplicationUserRole.ROLE_EMPLOYEE,
	// ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	Item findByItemNumber(String itemNumber);

	/**
	 * @param itemName
	 * @return
	 */
	// @RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
	// ApplicationUserRole.ROLE_EMPLOYEE,
	// ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	List<Item> findByNameLike(String itemName);
}
