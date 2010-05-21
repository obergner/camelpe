/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.contact.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.persistence.people.contact.PhoneNumberDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaPhoneNumberDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPhoneNumberDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaPhoneNumberDao")
public class JpaPhoneNumberDao extends AbstractJpaDao<PhoneNumber, Long>
		implements PhoneNumberDao {

	// ------------------------------------------------------------------------
	// Implementation of PhoneNumberDao
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.PhoneNumberDao#findByNumber(java.lang.String)
	 */
	public PhoneNumber findByNumber(final String phoneNumber)
			throws IllegalArgumentException {
		Validate.notNull(phoneNumber, "phoneNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("number", phoneNumber);

		return findUniqueByNamedQuery(PhoneNumber.Queries.BY_NUMBER, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.PhoneNumberDao#findByNumberLike(java.lang.String)
	 */
	public List<PhoneNumber> findByNumberLike(final String phoneNumber)
			throws IllegalArgumentException {
		Validate.notNull(phoneNumber, "phoneNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("number", "%" + phoneNumber + "%");

		return findByNamedQuery(PhoneNumber.Queries.BY_NUMBER_LIKE, parameters);
	}

}
