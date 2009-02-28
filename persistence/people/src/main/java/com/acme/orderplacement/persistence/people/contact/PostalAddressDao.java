/**
 * 
 */
package com.acme.orderplacement.persistence.people.contact;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for PostalAddressDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PostalAddressDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface PostalAddressDao extends GenericJpaDao<PostalAddress, Long> {

	/**
	 * @param street
	 * @param city
	 * @param postalCode
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PostalAddress> findByStreetAndCityAndPostalCodeLike(String street,
			String city, String postalCode) throws IllegalArgumentException;

	/**
	 * @param street
	 * @param city
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PostalAddress> findByStreetAndCityLike(String street, String city)
			throws IllegalArgumentException;
}
