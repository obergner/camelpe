/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.person.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.persistence.people.person.PersonDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaPersonDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPersonDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaPersonDao")
public class JpaPersonDao extends AbstractJpaDao<Person, Long> implements
		PersonDao {

	// ------------------------------------------------------------------------
	// Implementation of PersonDao
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.persistence.people.person.PersonDao#findByFirstNameLike(java.lang.String)
	 */
	public List<Person> findByFirstNameLike(final String firstName) {
		Validate.notNull(firstName, "firstName");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("firstName", "%" + firstName + "%");

		return findByNamedQuery(Person.Queries.BY_FIRST_NAME_LIKE, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.person.PersonDao#findByLastNameLike(java.lang.String)
	 */
	public List<Person> findByLastNameLike(final String lastName) {
		Validate.notNull(lastName, "lastName");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("lastName", "%" + lastName + "%");

		return findByNamedQuery(Person.Queries.BY_LAST_NAME_LIKE, parameters);
	}

}
