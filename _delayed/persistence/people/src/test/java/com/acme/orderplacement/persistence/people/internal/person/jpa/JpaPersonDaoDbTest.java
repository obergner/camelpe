/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.person.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
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

import com.acme.orderplacement.domain.people.account.Account;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.people.person.Gender;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.people.person.Salutation;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.people.person.PersonDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * Test {@link JpaPersonDao <code>JpaPersonDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/persistence.support.applicationLayer.scontext",
		"classpath:/META-INF/spring/persistence.people.test.platformLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.aspectLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.daoLayer.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		PrincipalRegistrationTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "persistence.support.platform.transactionManager", defaultRollback = true)
@Transactional
public class JpaPersonDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final Long EXISTING_PERSON_ID = Long.valueOf(0L);

	private static final Long NON_EXISTING_PERSON_ID = Long.valueOf(-1);

	private static final String EXISTING_PERSON_FIRST_NAME = "Test";

	private static final String EXISTING_PERSON_FIRST_NAME_PART = "es";

	private static final String EXISTING_PERSON_LAST_NAME = "Tester";

	private static final String EXISTING_PERSON_LAST_NAME_PART = "este";

	private static final Long NO_ROLES_PERSON_ID = Long.valueOf(1L);

	private JdbcTemplate jdbcTemplate;

	@Resource
	private PersonDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.people.person.internal.JpaPersonDao#findByFirstNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByFirstNameLikeShouldReturnOneExactMatch() {
		final List<Person> matchingPersons = this.classUnderTest
				.findByFirstNameLike(EXISTING_PERSON_FIRST_NAME);

		assertNotNull("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME
				+ "') returned <null>", matchingPersons);
		assertEquals("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME
				+ "') returned wrong number of matches", 1, matchingPersons
				.size());
		assertEquals("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME
				+ "') returned wrong Person", EXISTING_PERSON_FIRST_NAME,
				matchingPersons.get(0).getFirstName());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.person.internal.JpaPersonDao#findByFirstNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByFirstNameLikeShouldReturnOneInexactMatch() {
		final List<Person> matchingPersons = this.classUnderTest
				.findByFirstNameLike(EXISTING_PERSON_FIRST_NAME_PART);

		assertNotNull("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME_PART
				+ "') returned <null>", matchingPersons);
		assertEquals("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME_PART
				+ "') returned wrong number of matches", 1, matchingPersons
				.size());
		assertTrue("findByFirstNameLike('" + EXISTING_PERSON_FIRST_NAME_PART
				+ "') returned wrong Person", matchingPersons.get(0)
				.getFirstName().contains(EXISTING_PERSON_FIRST_NAME_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.person.internal.JpaPersonDao#findByLastNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByLastNameLikeShouldReturnOneExactMatch() {
		final List<Person> matchingPersons = this.classUnderTest
				.findByLastNameLike(EXISTING_PERSON_LAST_NAME);

		assertNotNull("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME
				+ "') returned <null>", matchingPersons);
		assertEquals("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME
				+ "') returned wrong number of matches", 1, matchingPersons
				.size());
		assertEquals("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME
				+ "') returned wrong Person", EXISTING_PERSON_LAST_NAME,
				matchingPersons.get(0).getLastName());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.person.internal.JpaPersonDao#findByLastNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByLastNameLikeShouldReturnOneInexactMatch() {
		final List<Person> matchingPersons = this.classUnderTest
				.findByLastNameLike(EXISTING_PERSON_LAST_NAME_PART);

		assertNotNull("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME_PART
				+ "') returned <null>", matchingPersons);
		assertEquals("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME_PART
				+ "') returned wrong number of matches", 1, matchingPersons
				.size());
		assertTrue("findByLastNameLike('" + EXISTING_PERSON_LAST_NAME_PART
				+ "') returned wrong Person", matchingPersons.get(0)
				.getLastName().contains(EXISTING_PERSON_LAST_NAME_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws NoSuchPersistentObjectException
	 * @throws ObjectNotPersistentException
	 * @throws DataAccessRuntimeException
	 */
	@Test
	public final void evictShouldNotThrowAnyExceptions()
			throws DataAccessRuntimeException, ObjectNotPersistentException,
			NoSuchPersistentObjectException {
		this.classUnderTest.evict(createDetachedTestPerson());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllPersons() {
		final List<Person> allPersons = this.classUnderTest.findAll();

		assertNotNull("findAll() returned <null>", allPersons);
		assertEquals("findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.PERSON"),
				allPersons.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void findByIdShouldFindOnePerson()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Person person = this.classUnderTest.findById(EXISTING_PERSON_ID,
				false);

		assertNotNull("findById(" + EXISTING_PERSON_ID
				+ ", false) returned <null>", person);
		assertEquals("findById(" + EXISTING_PERSON_ID
				+ ", false) returned wrong person", EXISTING_PERSON_ID, person
				.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_PERSON_ID, false);
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
	 * 
	 * @throws ObjectNotTransientException
	 * @throws DataAccessRuntimeException
	 */
	@Test
	public final void makePersistentShouldPersistTransientPerson()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final Person person = createTransientPersonToSaveWithTransientHomeAddress();

		final int numberOfPersonsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		this.classUnderTest.makePersistent(person);
		this.classUnderTest.flush();

		final int numberOfPersonsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		assertEquals("makePersistent('" + person
				+ "') did NOT persist transient Person",
				numberOfPersonsBefore + 1, numberOfPersonsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistent(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotTransientException
	 * @throws DataAccessRuntimeException
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	@Test
	public final void makePersistentShouldAlsoPersistTransientAssociations()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			CollaborationPreconditionsNotMetException, IllegalArgumentException {
		final Person person = createTransientPersonToSaveWithTransientAccountAndHomeAddressAndAndPhoneNumbers();

		final int numberOfAccountsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON_ACCOUNT");
		final int numberOfPostalAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");
		final int numberOfPhoneNumbersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		this.classUnderTest.makePersistent(person);
		this.classUnderTest.flush();

		final int numberOfAccountsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON_ACCOUNT");
		final int numberOfPostalAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");
		final int numberOfPhoneNumbersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		assertEquals("makePersistent('" + person
				+ "') did NOT persist transient Account",
				numberOfAccountsBefore + 1, numberOfAccountsAfter);
		assertEquals("makePersistent('" + person
				+ "') did NOT persist transient PostalAddress",
				numberOfPostalAddressesBefore + 1, numberOfPostalAddressesAfter);
		assertEquals("makePersistent('" + person
				+ "') did NOT persist transient PhoneNumbers",
				numberOfPhoneNumbersBefore + 3, numberOfPhoneNumbersAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientPerson()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Person person = createTransientPersonToMerge();

		final int numberOfPersonsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		this.classUnderTest.makePersistentOrUpdatePersistentState(person);
		this.classUnderTest.flush();

		final int numberOfPersonsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		assertEquals("makePersistentOrUpdatePersistentState('" + person
				+ "') did NOT persist transient Person",
				numberOfPersonsBefore + 1, numberOfPersonsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentPerson()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Person person = createDetachedTestPerson();
		final String updatedFirstName = "FIRST_NAME_UTEST";
		person.setFirstName(updatedFirstName);

		this.classUnderTest.makePersistentOrUpdatePersistentState(person);
		this.classUnderTest.flush();

		assertEquals("makePersistentOrUpdatePersistentState('" + person
				+ "') did NOT update persistent Person", updatedFirstName,
				this.jdbcTemplate.queryForObject(
						"SELECT FIRST_NAME FROM PEOPLE.PERSON WHERE ID = ?",
						new Object[] { person.getId() }, String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws ObjectTransientException
	 */
	@Test
	public final void makeTransientShouldRemovePersonFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final Person person = this.classUnderTest.findById(NO_ROLES_PERSON_ID,
				false);

		final int numberOfPersonsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		this.classUnderTest.makeTransient(person);
		this.classUnderTest.flush();

		final int numberOfPersonsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PERSON");

		assertEquals("makeTransient('" + person
				+ "') did NOT remove Person from database",
				numberOfPersonsBefore - 1, numberOfPersonsAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Person createTransientPersonToSaveWithTransientHomeAddress() {
		final Person person = new Person();

		person.setFirstName("UTEST");
		person.setLastName("UTESTER");
		person.setSalutation(Salutation.MISS);
		person.setGender(Gender.FEMALE);
		final Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(1980, 10, 11);
		person.setDateOfBirth(dateOfBirth.getTime());

		final PostalAddress homeAddress = new PostalAddress();
		homeAddress.setCity("Hamburg");
		homeAddress.setName("Home Address");
		homeAddress.setPostalCode("22345");
		homeAddress.setStreet("Homestreet");
		homeAddress.setStreetNumber("666");
		person.setHomeAddress(homeAddress);

		return person;
	}

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Person createTransientPersonToSaveWithTransientAccountAndHomeAddressAndAndPhoneNumbers()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Person person = new Person();

		person.setFirstName("UTEST");
		person.setLastName("UTESTER");
		person.setSalutation(Salutation.MISS);
		person.setGender(Gender.FEMALE);
		final Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(1980, 10, 11);
		person.setDateOfBirth(dateOfBirth.getTime());

		final Account account = new Account();
		person.setAccount(account);
		account.setPassword("PASSWORT");
		account.setUsername("USERNAME");
		account.setSecretQuestion("FRAGE?");
		account.setSecretAnswer("ANTWORT!");
		account.setPasswordLastChangedOn(new Date());

		final PostalAddress homeAddress = new PostalAddress();
		homeAddress.setCity("Hamburg");
		homeAddress.setName("Home Address");
		homeAddress.setPostalCode("22345");
		homeAddress.setStreet("Homestreet");
		homeAddress.setStreetNumber("666");
		person.setHomeAddress(homeAddress);

		final PhoneNumber privateResidentialPhone = new PhoneNumber();
		person.setPrivateResidentialPhone(privateResidentialPhone);
		privateResidentialPhone.setName("TEST");
		privateResidentialPhone.setType(PhoneNumber.Type.LANDLINE);
		privateResidentialPhone.setPhoneNumber("0123/675444");

		final PhoneNumber privateMobilePhone = new PhoneNumber();
		person.setPrivateMobilePhone(privateMobilePhone);
		privateMobilePhone.setName("TEST");
		privateMobilePhone.setType(PhoneNumber.Type.MOBILE);
		privateMobilePhone.setPhoneNumber("0169/67544478");

		final PhoneNumber privateFax = new PhoneNumber();
		person.setPrivateFax(privateFax);
		privateFax.setName("TEST");
		privateFax.setType(PhoneNumber.Type.LANDLINE);
		privateFax.setPhoneNumber("0123/67544478");

		return person;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Person createTransientPersonToMerge()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Person person = new Person();

		person.setFirstName("UTEST");
		person.setLastName("UTESTER");
		person.setSalutation(Salutation.MISS);
		person.setGender(Gender.FEMALE);
		final Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(1980, 10, 11);
		person.setDateOfBirth(dateOfBirth.getTime());

		final Person existingPersistentPerson = this.classUnderTest.findById(
				EXISTING_PERSON_ID, false);
		person.setHomeAddress(existingPersistentPerson.getHomeAddress());

		return person;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Person createDetachedTestPerson()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Person existingPersistentPerson = this.classUnderTest.findById(
				EXISTING_PERSON_ID, false);

		final Person person = new Person();
		person.setId(EXISTING_PERSON_ID);
		person.setFirstName(getClass().getSimpleName());
		person.setLastName(getClass().getSimpleName());
		person.setGender(Gender.FEMALE);
		person.setSalutation(Salutation.MISS);
		final Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(1980, 10, 11);
		person.setDateOfBirth(dateOfBirth.getTime());

		person.setHomeAddress(existingPersistentPerson.getHomeAddress());

		person.setAuditInfo(new AuditInfo("TESTER", new Date(), "TESTER",
				new Date()));
		person.setVersion(Integer.valueOf(0));

		return person;
	}
}
