/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.contact.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.people.contact.PostalAddressDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * TODO: Insert short summary for JpaPostalAddressDaoDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPostalAddressDaoDbTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/persistence.support.applicationLayer.scontext",
		"classpath:/META-INF/spring/persistence.people.test.platformLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.securityLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.aspectLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.daoLayer.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		PrincipalRegistrationTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "persistence.support.platform.transactionManager", defaultRollback = true)
@Transactional
public class JpaPostalAddressDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final Long EXISTING_POSTAL_ADDRESS_ID = Long.valueOf(1L);

	private static final Long NON_EXISTING_POSTAL_ADDRESS_ID = Long.valueOf(-1);

	private static final String EXISTING_STREET_PART = "eststr";

	private static final String EXISTING_CITY_PART = "ambur";

	private static final String EXISTING_POSTAL_CODE_PART = "234";

	private static final Long UNREFERENCED_POSTAL_ADDRESS_ID = Long.valueOf(6L);

	private JdbcTemplate jdbcTemplate;

	@Resource
	private PostalAddressDao classUnderTest;

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * @param dataSource
	 */
	@Resource(name = "persistence.support.platform.dataSource")
	public void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaPostalAddressDao#findByStreetAndCityAndPostalCodeLike(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void findByStreetAndCityAndPostalCodeLikeShouldReturnOneInexactMatch() {
		final List<PostalAddress> inexactMatches = this.classUnderTest
				.findByStreetAndCityAndPostalCodeLike(EXISTING_STREET_PART,
						EXISTING_CITY_PART, EXISTING_POSTAL_CODE_PART);

		assertNotNull("findByStreetAndCityAndPostalCodeLike('"
				+ EXISTING_STREET_PART + ", " + EXISTING_CITY_PART + ", "
				+ EXISTING_POSTAL_CODE_PART + "') returned <null>",
				inexactMatches);
		assertEquals("findByStreetAndCityAndPostalCodeLike('"
				+ EXISTING_STREET_PART + ", " + EXISTING_CITY_PART + ", "
				+ EXISTING_POSTAL_CODE_PART
				+ "') returned wrong number of matches", 1, inexactMatches
				.size());
		assertTrue(
				"findByStreetAndCityAndPostalCodeLike('" + EXISTING_STREET_PART
						+ ", " + EXISTING_CITY_PART + ", "
						+ EXISTING_POSTAL_CODE_PART
						+ "') returned wrong PostalAddress", inexactMatches
						.get(0).getStreet().contains(EXISTING_STREET_PART));
		assertTrue(
				"findByStreetAndCityAndPostalCodeLike('" + EXISTING_STREET_PART
						+ ", " + EXISTING_CITY_PART + ", "
						+ EXISTING_POSTAL_CODE_PART
						+ "') returned wrong PostalAddress", inexactMatches
						.get(0).getCity().contains(EXISTING_CITY_PART));
		assertTrue(
				"findByStreetAndCityAndPostalCodeLike('" + EXISTING_STREET_PART
						+ ", " + EXISTING_CITY_PART + ", "
						+ EXISTING_POSTAL_CODE_PART
						+ "') returned wrong PostalAddress", inexactMatches
						.get(0).getPostalCode().contains(
								EXISTING_POSTAL_CODE_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaPostalAddressDao#findByStreetAndCityLike(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void findByStreetAndCityLikeShouldReturnOneInexactMatch() {
		final List<PostalAddress> inexactMatches = this.classUnderTest
				.findByStreetAndCityLike(EXISTING_STREET_PART,
						EXISTING_CITY_PART);

		assertNotNull("findByStreetAndCityLike('" + EXISTING_STREET_PART + ", "
				+ EXISTING_CITY_PART + "') returned <null>", inexactMatches);
		assertEquals("findByStreetAndCityLike('" + EXISTING_STREET_PART + ", "
				+ EXISTING_CITY_PART + "') returned wrong number of matches",
				1, inexactMatches.size());
		assertTrue("findByStreetAndCityLike('" + EXISTING_STREET_PART + ", "
				+ EXISTING_CITY_PART + "') returned wrong PostalAddress",
				inexactMatches.get(0).getStreet()
						.contains(EXISTING_STREET_PART));
		assertTrue("findByStreetAndCityLike('" + EXISTING_STREET_PART + ", "
				+ EXISTING_CITY_PART + "') returned wrong PostalAddress",
				inexactMatches.get(0).getCity().contains(EXISTING_CITY_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#evict(java.lang.Object)}
	 * .
	 */
	@Test
	public final void evictShouldNotThrowAnyExceptions()
			throws DataAccessRuntimeException, ObjectNotPersistentException,
			NoSuchPersistentObjectException {
		this.classUnderTest.evict(createDetachedTestPostalAddress());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllPostalAddresses() {
		final List<PostalAddress> allPostalAddresses = this.classUnderTest
				.findAll();

		assertNotNull("findAll() returned <null>", allPostalAddresses);
		assertEquals(
				"findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.POSTAL_ADDRESS"),
				allPostalAddresses.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findByIdShouldFindOnePostalAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final PostalAddress postalAddress = this.classUnderTest.findById(
				EXISTING_POSTAL_ADDRESS_ID, false);

		assertNotNull("findById(" + EXISTING_POSTAL_ADDRESS_ID
				+ ", false) returned <null>", postalAddress);
		assertEquals("findById(" + EXISTING_POSTAL_ADDRESS_ID
				+ ", false) returned wrong PostalAddress",
				EXISTING_POSTAL_ADDRESS_ID, postalAddress.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_POSTAL_ADDRESS_ID, false);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#flush()}
	 * .
	 */
	@Test
	public final void flushShouldNotThrowAnyExceptions() {
		this.classUnderTest.flush();
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistent(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentShouldPersistTransientPostalAddress()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final PostalAddress transientPostalAddress = createTransientPostalAddressToPersist();

		final int numberOfPostalAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		this.classUnderTest.makePersistent(transientPostalAddress);
		this.classUnderTest.flush();

		final int numberOfPostalAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		assertEquals("makePersistent('" + transientPostalAddress
				+ "') did NOT persist transient PostalAddress",
				numberOfPostalAddressesBefore + 1, numberOfPostalAddressesAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientPostalAddress()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final PostalAddress transientPostalAddress = createTransientPostalAddressToPersist();

		final int numberOfPostalAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(transientPostalAddress);
		this.classUnderTest.flush();

		final int numberOfPostalAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ transientPostalAddress
				+ "') did NOT persist transient PostalAddress",
				numberOfPostalAddressesBefore + 1, numberOfPostalAddressesAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentPersistentPostalAddress()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NoSuchPersistentObjectException {
		final PostalAddress detachedPostalAddress = createDetachedTestPostalAddress();
		final String updatedCity = "UPDATED";
		detachedPostalAddress.setCity(updatedCity);

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(detachedPostalAddress);
		this.classUnderTest.flush();

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ detachedPostalAddress
				+ "') did NOT update persistent PostalAddress", updatedCity,
				this.jdbcTemplate.queryForObject(
						"SELECT CITY FROM PEOPLE.POSTAL_ADDRESS WHERE ID = ?",
						new Object[] { detachedPostalAddress.getId() },
						String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makeTransientShouldRemovePostalAddressFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final PostalAddress postalAddress = this.classUnderTest.findById(
				UNREFERENCED_POSTAL_ADDRESS_ID, false);

		final int numberOfPostalAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		this.classUnderTest.makeTransient(postalAddress);
		this.classUnderTest.flush();

		final int numberOfPostalAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");

		assertEquals("makeTransient('" + postalAddress
				+ "') did NOT remove PostalAddress from database",
				numberOfPostalAddressesBefore - 1, numberOfPostalAddressesAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PostalAddress createTransientPostalAddressToPersist() {
		final PostalAddress postalAddress = new PostalAddress();
		postalAddress.setName("TESTADDRESS");
		postalAddress.setCity("HAMBURG");
		postalAddress.setPostalCode("22345");
		postalAddress.setStreet("Teststreet");
		postalAddress.setStreetNumber("56/a");

		postalAddress.setVersion(Integer.valueOf(0));

		return postalAddress;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PostalAddress createDetachedTestPostalAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final PostalAddress postalAddress = new PostalAddress();
		postalAddress.setId(EXISTING_POSTAL_ADDRESS_ID);
		postalAddress.setName("TESTADDRESS");
		postalAddress.setCity("HAMBURG");
		postalAddress.setPostalCode("22345");
		postalAddress.setStreet("Teststreet");
		postalAddress.setStreetNumber("56/a");
		postalAddress.setAuditInfo(new AuditInfo("TESTER", new Date(),
				"TESTER", new Date()));
		postalAddress.setVersion(Integer.valueOf(0));

		return postalAddress;
	}
}
