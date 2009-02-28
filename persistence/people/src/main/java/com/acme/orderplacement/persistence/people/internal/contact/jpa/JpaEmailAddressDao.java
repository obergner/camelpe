/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.contact.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.persistence.people.contact.EmailAddressDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaEmailAddressDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaEmailAddressDao")
public class JpaEmailAddressDao extends AbstractJpaDao<EmailAddress, Long>
		implements EmailAddressDao {

	// ------------------------------------------------------------------------
	// Implementation of EmailAddressDao
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.EmailAddressDao#findByAddress(java.lang.String)
	 */
	public EmailAddress findByAddress(final String emailAddress)
			throws IllegalArgumentException {
		Validate.notNull(emailAddress, "emailAddress");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("address", emailAddress);

		return findUniqueByNamedQuery(EmailAddress.Queries.BY_ADDRESS,
				parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.EmailAddressDao#findByAddressLike(java.lang.String)
	 */
	public List<EmailAddress> findByAddressLike(final String emailAddress)
			throws IllegalArgumentException {
		Validate.notNull(emailAddress, "emailAddress");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("address", "%" + emailAddress + "%");

		return findByNamedQuery(EmailAddress.Queries.BY_ADDRESS_LIKE,
				parameters);
	}

}
