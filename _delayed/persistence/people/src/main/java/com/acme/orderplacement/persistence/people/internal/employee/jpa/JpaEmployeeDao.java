/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.employee.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.employee.Department;
import com.acme.orderplacement.domain.people.employee.Employee;
import com.acme.orderplacement.persistence.people.employee.EmployeeDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaEmployeeDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaEmployeeDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaEmployeeDao")
public class JpaEmployeeDao extends AbstractJpaDao<Employee, Long> implements
		EmployeeDao {

	/**
	 * @see com.acme.orderplacement.persistence.people.employee.EmployeeDao#findByDepartment(com.acme.orderplacement.domain.people.employee.Department)
	 */
	public List<Employee> findByDepartment(final Department department)
			throws IllegalArgumentException {
		Validate.notNull(department, "department");
		final Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("department", department);

		return findByNamedQuery(Employee.Queries.BY_DEPARTMENT, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.employee.EmployeeDao#findByEmployeeNumber(java.lang.String)
	 */
	public Employee findByEmployeeNumber(final String employeeNumber)
			throws IllegalArgumentException {
		Validate.notNull(employeeNumber, "employeeNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("employeeNumber", employeeNumber);

		return findUniqueByNamedQuery(Employee.Queries.BY_EMPLOYEE_NUMBER,
				parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.employee.EmployeeDao#findByEmployeeNumberLike(java.lang.String)
	 */
	public List<Employee> findByEmployeeNumberLike(final String employeeNumber)
			throws IllegalArgumentException {
		Validate.notNull(employeeNumber, "employeeNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("employeeNumber", "%" + employeeNumber + "%");

		return findByNamedQuery(Employee.Queries.BY_EMPLOYEE_NUMBER_LIKE,
				parameters);
	}

}
