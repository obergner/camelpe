/**
 * 
 */
package com.acme.orderplacement.persistence.company;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for CompanyDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for CompanyDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface CompanyDao extends GenericJpaDao<Company, Long> {

	/**
	 * @param companyNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	Company findByCompanyNumber(String companyNumber)
			throws IllegalArgumentException;
}
