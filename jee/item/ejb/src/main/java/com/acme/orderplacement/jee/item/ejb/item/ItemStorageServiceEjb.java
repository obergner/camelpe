package com.acme.orderplacement.jee.item.ejb.item;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.jee.item.ejb.internal.spring.JndiEnabledServiceLayerSpringBeanAutowiringInterceptor;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.support.exception.entity.EntityAlreadyRegisteredException;

/**
 * <p>
 * <tt>EJB 3.0</tt> facade for the {@link ItemStorageService
 * <code>ItemStorageService</code>}
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@DeclareRoles( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EXTERNAL_USER,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Stateless(name = ItemStorageServiceEjb.BEAN_NAME)
@Local( { ItemStorageService.class })
@Interceptors( { JndiEnabledServiceLayerSpringBeanAutowiringInterceptor.class })
public class ItemStorageServiceEjb implements ItemStorageService {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String BEAN_NAME = "ItemStorageServiceEJB";

	/**
	 * The <tt>Spring</tt> managed <code>ItemStorageService</code>
	 * implementation we delegate all service requests to.
	 */
	@Autowired(required = true)
	@Qualifier(ItemStorageService.SERVICE_NAME)
	private ItemStorageService delegate;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @see ItemStorageService#registerItem(ItemDto)
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerItem(final ItemDto newItemToRegister)
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.delegate.registerItem(newItemToRegister);
	}
}
