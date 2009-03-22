/**
 * 
 */
package com.acme.orderplacement.persistence.item;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * @author o.bergner
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
public interface ItemDao extends GenericJpaDao<Item, Long> {

	String REPOSITORY_NAME = "persistence.item.ItemDao";

	/**
	 * @param itemNumber
	 * @return
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	Item findByItemNumber(String itemNumber);

	/**
	 * @param itemName
	 * @return
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	List<Item> findByNameLike(String itemName);
}
