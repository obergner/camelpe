/**
 * 
 */
package com.acme.orderplacement.persistence.people.contact;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for EmailAddressDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for EmailAddressDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface EmailAddressDao extends GenericJpaDao<EmailAddress, Long> {

	/**
	 * @param emailAddress
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	EmailAddress findByAddress(String emailAddress)
			throws IllegalArgumentException;

	/**
	 * @param emailAddress
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<EmailAddress> findByAddressLike(String emailAddress)
			throws IllegalArgumentException;
}
