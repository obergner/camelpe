/**
 * 
 */
package com.acme.orderplacement.persistence.people.employee;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.common.support.role.ApplicationUserRole;
import com.acme.orderplacement.domain.people.employee.Department;
import com.acme.orderplacement.domain.people.employee.Employee;
import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * TODO: Insert short summary for EmployeeDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for EmployeeDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
@Transactional
public interface EmployeeDao extends GenericJpaDao<Employee, Long> {

	/**
	 * @param employeeNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	Employee findByEmployeeNumber(String employeeNumber)
			throws IllegalArgumentException;

	/**
	 * @param employeeNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<Employee> findByEmployeeNumberLike(String employeeNumber)
			throws IllegalArgumentException;

	/**
	 * @param department
	 * @return
	 * @throws IllegalArgumentException
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@Transactional(readOnly = true)
	List<Employee> findByDepartment(Department department)
			throws IllegalArgumentException;
}
