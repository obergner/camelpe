/**
 * 
 */
package com.acme.orderplacement.persistence.company.internal.jpa;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.persistence.company.CompanyDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaCompanyDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaCompanyDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.company.JpaCompanyDao")
public final class JpaCompanyDao extends AbstractJpaDao<Company, Long>
		implements CompanyDao {

	/**
	 * @see com.acme.orderplacement.persistence.company.CompanyDao#findByCompanyNumber(java.lang.String)
	 */
	public Company findByCompanyNumber(final String companyNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(companyNumber, "companyNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("companyNumber", companyNumber);

		return findUniqueByNamedQuery(Company.Queries.BY_COMPANY_NUMBER,
				parameters);
	}
}
