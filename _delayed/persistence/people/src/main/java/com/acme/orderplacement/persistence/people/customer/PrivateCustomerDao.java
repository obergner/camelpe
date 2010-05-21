/**
 * 
 */
package com.acme.orderplacement.persistence.people.customer;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for PrivateCustomerDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PrivateCustomerDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface PrivateCustomerDao extends
		GenericJpaDao<PrivateCustomer, Long> {

	/**
	 * @param customerNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	PrivateCustomer findByCustomerNumber(String customerNumber)
			throws IllegalArgumentException;

	/**
	 * @param customerNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PrivateCustomer> findByCustomerNumberLike(String customerNumber)
			throws IllegalArgumentException;

	/**
	 * @param shippingStreet
	 * @param shippingCity
	 * @param shippingPostalCode
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PrivateCustomer> findByShippingStreetAndCityAndPostalCodeLike(
			String shippingStreet, String shippingCity,
			String shippingPostalCode) throws IllegalArgumentException;

	/**
	 * @param invoiceStreet
	 * @param invoiceCity
	 * @param invoicePostalCode
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PrivateCustomer> findByInvoiceStreetAndCityAndPostalCodeLike(
			String invoiceStreet, String invoiceCity, String invoicePostalCode)
			throws IllegalArgumentException;
}
