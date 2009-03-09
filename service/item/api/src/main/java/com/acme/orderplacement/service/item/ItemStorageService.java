/**
 * 
 */
package com.acme.orderplacement.service.item;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.support.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.service.support.meta.annotation.ServiceOperation;

/**
 * <p>
 * <tt>Entity Service</tt> for registering, updating and removing
 * {@link com.acme.orderplacement.domain.item.Item <code>Item</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface ItemStorageService {

	String SERVICE_NAME = "service.item.ItemStorageService";

	/**
	 * @param newItemToRegister
	 * @throws EntityAlreadyRegisteredException
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@ServiceOperation(idempotent = true)
	void registerItem(ItemDto newItemToRegister)
			throws EntityAlreadyRegisteredException, IllegalArgumentException;
}
