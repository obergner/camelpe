/**
 * 
 */
package com.acme.orderplacement.persistence.people.contact;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for PhoneNumberDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PhoneNumberDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface PhoneNumberDao extends GenericJpaDao<PhoneNumber, Long> {

	/**
	 * @param phoneNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	PhoneNumber findByNumber(String phoneNumber)
			throws IllegalArgumentException;

	/**
	 * @param phoneNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<PhoneNumber> findByNumberLike(String phoneNumber)
			throws IllegalArgumentException;
}
