/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.contact.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.persistence.people.contact.PostalAddressDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaPostalAddressDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPostalAddressDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaPostalAddressDao")
public class JpaPostalAddressDao extends AbstractJpaDao<PostalAddress, Long>
		implements PostalAddressDao {

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.PostalAddressDao#findByStreetAndCityAndPostalCodeLike(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public List<PostalAddress> findByStreetAndCityAndPostalCodeLike(
			final String street, final String city, final String postalCode)
			throws IllegalArgumentException {
		Validate.notNull(street, "street");
		Validate.notNull(city, "city");
		Validate.notNull(postalCode, "postalCode");
		final Map<String, String> parameters = new HashMap<String, String>(3);
		parameters.put("street", "%" + street + "%");
		parameters.put("city", "%" + city + "%");
		parameters.put("postalCode", "%" + postalCode + "%");

		return findByNamedQuery(
				PostalAddress.Queries.BY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE,
				parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.contact.PostalAddressDao#findByStreetAndCityLike(java.lang.String,
	 *      java.lang.String)
	 */
	public List<PostalAddress> findByStreetAndCityLike(final String street,
			final String city) throws IllegalArgumentException {
		Validate.notNull(street, "street");
		Validate.notNull(city, "city");
		final Map<String, String> parameters = new HashMap<String, String>(2);
		parameters.put("street", "%" + street + "%");
		parameters.put("city", "%" + city + "%");

		return findByNamedQuery(PostalAddress.Queries.BY_STREET_AND_CITY_LIKE,
				parameters);
	}

}
