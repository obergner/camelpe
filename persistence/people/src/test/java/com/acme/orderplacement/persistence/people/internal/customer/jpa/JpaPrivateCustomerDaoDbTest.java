/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.customer.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
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
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.people.person.Gender;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.people.person.Salutation;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;
import com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.SpringBasedAuthenticationProvidingTestExecutionListener;

/**
 * <p>
 * TODO: Insert short summary for JpaPrivateCustomerDaoDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPrivateCustomerDaoDbTest
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
		SpringBasedAuthenticationProvidingTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "persistence.support.platform.transactionManager", defaultRollback = true)
@Transactional
public class JpaPrivateCustomerDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_CUSTOMER_NUMBER = "PRVCUST-908-786";

	private static final String EXISTING_CUSTOMER_NUMBER_PART = "CUST-908";

	private static final Long EXISTING_PRIVATE_CUSTOMER_ID = Long.valueOf(0L);

	private static final Long NON_EXISTING_PRIVATE_CUSTOMER_ID = Long
			.valueOf(-1);

	private static final String EXISTING_SHIPPING_STREET_PART = "ingstr";

	private static final String EXISTING_SHIPPING_CITY_PART = "pingCi";

	private static final String EXISTING_SHIPPING_POSTAL_CODE_PART = "567";

	private static final String EXISTING_INVOICING_STREET_PART = "oicestre";

	private static final String EXISTING_INVOICING_CITY_PART = "voiceCi";

	private static final String EXISTING_INVOICING_POSTAL_CODE_PART = "456";

	private static final Long NO_PRIVATE_CUSTOMER_PERSON_ID = Long.valueOf(1L);

	private static final Long UNREFERENCED_POSTAL_ADDRESS_ID = Long.valueOf(6L);

	private JdbcTemplate jdbcTemplate;

	/**
	 * <p>
	 * Our {@link javax.persistence.EntityManager <em>JPA EntityManager</em>}.
	 * </p>
	 */
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@Resource
	private PrivateCustomerDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.people.internal.customer.jpa.JpaPrivateCustomerDao#findByCustomerNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByCustomerNumberShouldReturnOneExactMatch() {
		final PrivateCustomer exactMatch = this.classUnderTest
				.findByCustomerNumber(EXISTING_CUSTOMER_NUMBER);

		assertNotNull("findByCustomerNumber(" + EXISTING_CUSTOMER_NUMBER
				+ ") returned <null>", exactMatch);
		assertEquals("findByCustomerNumber(" + EXISTING_CUSTOMER_NUMBER
				+ ") returned wrong Employee", exactMatch.getCustomerNumber(),
				EXISTING_CUSTOMER_NUMBER);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.customer.jpa.JpaPrivateCustomerDao#findByCustomerNumberLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByCustomerNumberLikeShouldReturnAllInexactMatches() {
		final List<PrivateCustomer> allInexactMatches = this.classUnderTest
				.findByCustomerNumberLike(EXISTING_CUSTOMER_NUMBER_PART);

		assertNotNull("findByCustomerNumberLike("
				+ EXISTING_CUSTOMER_NUMBER_PART + ") returned <null>",
				allInexactMatches);
		assertEquals(
				"findByCustomerNumberLike(" + EXISTING_CUSTOMER_NUMBER_PART
						+ ") returned wrong matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.PRIVATE_CUSTOMER WHERE CUSTOMER_NUMBER LIKE '%"
								+ EXISTING_CUSTOMER_NUMBER_PART + "%'"),
				allInexactMatches.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.customer.jpa.JpaPrivateCustomerDao#findByInvoiceStreetAndCityAndPostalCodeLike(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void findByInvoiceStreetAndCityAndPostalCodeLikeShouldReturnOneInexactMatch() {
		final List<PrivateCustomer> inexactMatches = this.classUnderTest
				.findByInvoiceStreetAndCityAndPostalCodeLike(
						EXISTING_INVOICING_STREET_PART,
						EXISTING_INVOICING_CITY_PART,
						EXISTING_INVOICING_POSTAL_CODE_PART);

		assertNotNull("findByInvoiceStreetAndCityAndPostalCodeLike('"
				+ EXISTING_INVOICING_STREET_PART + ", "
				+ EXISTING_INVOICING_CITY_PART + ", "
				+ EXISTING_INVOICING_POSTAL_CODE_PART + "') returned <null>",
				inexactMatches);
		assertEquals("findByInvoiceStreetAndCityAndPostalCodeLike('"
				+ EXISTING_INVOICING_STREET_PART + ", "
				+ EXISTING_INVOICING_CITY_PART + ", "
				+ EXISTING_INVOICING_POSTAL_CODE_PART
				+ "') returned wrong number of matches", 1, inexactMatches
				.size());
		assertTrue("findByInvoiceStreetAndCityAndPostalCodeLike('"
				+ EXISTING_INVOICING_STREET_PART + ", "
				+ EXISTING_INVOICING_CITY_PART + ", "
				+ EXISTING_INVOICING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getInvoiceAddress().getStreet().contains(
						EXISTING_INVOICING_STREET_PART));
		assertTrue("findByInvoiceStreetAndCityAndPostalCodeLike('"
				+ EXISTING_INVOICING_STREET_PART + ", "
				+ EXISTING_INVOICING_CITY_PART + ", "
				+ EXISTING_INVOICING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getInvoiceAddress().getCity().contains(
						EXISTING_INVOICING_CITY_PART));
		assertTrue("findByInvoiceStreetAndCityAndPostalCodeLike('"
				+ EXISTING_INVOICING_STREET_PART + ", "
				+ EXISTING_INVOICING_CITY_PART + ", "
				+ EXISTING_INVOICING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getInvoiceAddress().getPostalCode().contains(
						EXISTING_INVOICING_POSTAL_CODE_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.customer.jpa.JpaPrivateCustomerDao#findByShippingStreetAndCityAndPostalCodeLike(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void findByShippingStreetAndCityAndPostalCodeLikeShouldReturnOneInexactMatch() {
		final List<PrivateCustomer> inexactMatches = this.classUnderTest
				.findByShippingStreetAndCityAndPostalCodeLike(
						EXISTING_SHIPPING_STREET_PART,
						EXISTING_SHIPPING_CITY_PART,
						EXISTING_SHIPPING_POSTAL_CODE_PART);

		assertNotNull("findByShippingStreetAndCityAndPostalCodeLike('"
				+ EXISTING_SHIPPING_STREET_PART + ", "
				+ EXISTING_SHIPPING_CITY_PART + ", "
				+ EXISTING_SHIPPING_POSTAL_CODE_PART + "') returned <null>",
				inexactMatches);
		assertEquals("findByShippingStreetAndCityAndPostalCodeLike('"
				+ EXISTING_SHIPPING_STREET_PART + ", "
				+ EXISTING_SHIPPING_CITY_PART + ", "
				+ EXISTING_SHIPPING_POSTAL_CODE_PART
				+ "') returned wrong number of matches", 1, inexactMatches
				.size());
		assertTrue("findByShippingStreetAndCityAndPostalCodeLike('"
				+ EXISTING_SHIPPING_STREET_PART + ", "
				+ EXISTING_SHIPPING_CITY_PART + ", "
				+ EXISTING_SHIPPING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getShippingAddress().getStreet().contains(
						EXISTING_SHIPPING_STREET_PART));
		assertTrue("findByShippingStreetAndCityAndPostalCodeLike('"
				+ EXISTING_SHIPPING_STREET_PART + ", "
				+ EXISTING_SHIPPING_CITY_PART + ", "
				+ EXISTING_SHIPPING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getShippingAddress().getCity().contains(
						EXISTING_SHIPPING_CITY_PART));
		assertTrue("findByShippingStreetAndCityAndPostalCodeLike('"
				+ EXISTING_SHIPPING_STREET_PART + ", "
				+ EXISTING_SHIPPING_CITY_PART + ", "
				+ EXISTING_SHIPPING_POSTAL_CODE_PART
				+ "') returned wrong PrivateCustomer", inexactMatches.get(0)
				.getShippingAddress().getPostalCode().contains(
						EXISTING_SHIPPING_POSTAL_CODE_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#evict(java.lang.Object)}
	 * .
	 */
	@Test
	public final void evictShouldNotThrowAnyExceptions()
			throws DataAccessRuntimeException, ObjectNotPersistentException,
			NoSuchPersistentObjectException, RolePreconditionsNotMetException,
			IllegalArgumentException, IllegalDomainObjectStateException {
		this.classUnderTest.evict(createDetachedTestPrivateCustomer());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllPrivateCustomers() {
		final List<PrivateCustomer> allPrivateCustomers = this.classUnderTest
				.findAll();

		assertNotNull("findAll() returned <null>", allPrivateCustomers);
		assertEquals(
				"findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.PRIVATE_CUSTOMER"),
				allPrivateCustomers.size());
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
	public final void findByIdShouldReturnOnePrivateCustomer()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final PrivateCustomer privateCustomer = this.classUnderTest.findById(
				EXISTING_PRIVATE_CUSTOMER_ID, false);

		assertNotNull("findById(" + EXISTING_PRIVATE_CUSTOMER_ID
				+ ", false) returned <null>", privateCustomer);
		assertEquals("findById(" + EXISTING_PRIVATE_CUSTOMER_ID
				+ ", false) returned wrong PrivateCustomer",
				EXISTING_PRIVATE_CUSTOMER_ID, privateCustomer.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_PRIVATE_CUSTOMER_ID, false);
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
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	@Test
	public final void makePersistentShouldPersistTransientPrivateCustomer()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			CollaborationPreconditionsNotMetException, IllegalArgumentException {
		final PrivateCustomer transientPrivateCustomer = createTransientPrivateCustomerToPersist();

		final int numberOfPrivateCustomersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		this.classUnderTest.makePersistent(transientPrivateCustomer);
		this.classUnderTest.flush();

		final int numberOfPrivateCustomersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		assertEquals("makePersistent('" + transientPrivateCustomer
				+ "') did NOT persist transient PrivateCustomer",
				numberOfPrivateCustomersBefore + 1,
				numberOfPrivateCustomersAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws ObjectNotTransientException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientPrivateCustomer()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			IllegalArgumentException {
		final PrivateCustomer transientPrivateCustomer = createTransientPrivateCustomerToMerge();

		final int numberOfPrivateCustomersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(transientPrivateCustomer);
		this.classUnderTest.flush();

		final int numberOfPrivateCustomersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ transientPrivateCustomer
				+ "') did NOT persist transient PrivateCustomer",
				numberOfPrivateCustomersBefore + 1,
				numberOfPrivateCustomersAfter);
	}

	// /**
	// * Test method for
	// * {@link
	// com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	// * .
	// *
	// * @throws DataAccessRuntimeException
	// * @throws ObjectNotTransientException
	// * @throws NoSuchPersistentObjectException
	// * @throws IllegalArgumentException
	// * @throws IllegalDomainObjectStateException
	// */
	// @Test
	// public final void
	// makePersistentOrUpdatePersistentStateShouldUpdatePersistentPrivateCustomer()
	// throws DataAccessRuntimeException, ObjectNotTransientException,
	// NoSuchPersistentObjectException, IllegalArgumentException,
	// IllegalDomainObjectStateException {
	// final PrivateCustomer detachedPrivateCustomer =
	// createDetachedTestPrivateCustomer();
	//
	// this.classUnderTest
	// .makePersistentOrUpdatePersistentState(detachedPrivateCustomer);
	// this.classUnderTest.flush();
	//
	// assertEquals(
	// "makePersistentOrUpdatePersistentState('"
	// + detachedPrivateCustomer
	// + "') did NOT update persistent PrivateCustomer",
	// updatedDepartment.toString(),
	// this.jdbcTemplate
	// .queryForObject(
	// "SELECT DEPARTMENT FROM PEOPLE.PRIVATE_CUSTOMER WHERE ID = ?",
	// new Object[] { detachedPrivateCustomer.getId() },
	// String.class));
	// }

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makeTransientShouldRemovePrivateCustomerFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final PrivateCustomer privateCustomer = this.classUnderTest.findById(
				EXISTING_PRIVATE_CUSTOMER_ID, false);

		final int numberOfPrivateCustomersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		this.classUnderTest.makeTransient(privateCustomer);
		this.classUnderTest.flush();

		final int numberOfPrivateCustomersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.PRIVATE_CUSTOMER");

		assertEquals("makeTransient('" + privateCustomer
				+ "') did NOT remove PrivateCustomer from database",
				numberOfPrivateCustomersBefore - 1,
				numberOfPrivateCustomersAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PrivateCustomer createTransientPrivateCustomerToPersist()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Person roleOwner = new Person();

		roleOwner.setFirstName("PRVIVATE");
		roleOwner.setLastName("CUSTOMER");
		roleOwner.setSalutation(Salutation.MISTER);
		roleOwner.setGender(Gender.MALE);
		final Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(1980, 10, 11);
		roleOwner.setDateOfBirth(dateOfBirth.getTime());

		final Account account = new Account();
		roleOwner.setAccount(account);
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
		roleOwner.setHomeAddress(homeAddress);

		final PrivateCustomer privateCustomer = new PrivateCustomer();
		privateCustomer.setParent(roleOwner);
		privateCustomer.setCustomerNumber("PRVCUST-009-897665");

		privateCustomer.setVersion(Integer.valueOf(0));

		final PostalAddress invoiceAddress = new PostalAddress();
		privateCustomer.setInvoiceAddress(invoiceAddress);
		invoiceAddress.setCity("Hamburg");
		invoiceAddress.setName("Invoice Address");
		invoiceAddress.setPostalCode("22111");
		invoiceAddress.setStreet("Invoicestreet");
		invoiceAddress.setStreetNumber("777");

		final PostalAddress shippingAddress = new PostalAddress();
		privateCustomer.setShippingAddress(shippingAddress);
		shippingAddress.setCity("Hamburg");
		shippingAddress.setName("Shipping Address");
		shippingAddress.setPostalCode("22111");
		shippingAddress.setStreet("Shippingstreet");
		shippingAddress.setStreetNumber("777");

		return privateCustomer;
	}

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PrivateCustomer createTransientPrivateCustomerToMerge()
			throws IllegalArgumentException {
		final Person persistentRoleOwner = this.entityManager.find(
				Person.class, NO_PRIVATE_CUSTOMER_PERSON_ID);

		final PostalAddress persistentAddress = this.entityManager.find(
				PostalAddress.class, UNREFERENCED_POSTAL_ADDRESS_ID);

		final PrivateCustomer privateCustomer = new PrivateCustomer();
		privateCustomer.setParent(persistentRoleOwner);
		privateCustomer.setInvoiceAddress(persistentAddress);
		privateCustomer.setCustomerNumber("PRVCUST-1090-99");

		privateCustomer.setVersion(Integer.valueOf(0));

		return privateCustomer;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws IllegalArgumentException
	 * @throws IllegalDomainObjectStateException
	 */
	private PrivateCustomer createDetachedTestPrivateCustomer()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			IllegalArgumentException, IllegalDomainObjectStateException {
		final PrivateCustomer correspondingPersistentPrivateCustomer = this.classUnderTest
				.findById(EXISTING_PRIVATE_CUSTOMER_ID, false);

		final PrivateCustomer privateCustomer = new PrivateCustomer();
		privateCustomer.setId(correspondingPersistentPrivateCustomer.getId());
		privateCustomer
				.setCustomerNumber(correspondingPersistentPrivateCustomer
						.getCustomerNumber());
		privateCustomer.setAuditInfo(correspondingPersistentPrivateCustomer
				.getAuditInfo());
		privateCustomer.setVersion(correspondingPersistentPrivateCustomer
				.getVersion());

		privateCustomer
				.setInvoiceAddress(correspondingPersistentPrivateCustomer
						.getInvoiceAddress());

		privateCustomer.setParent(correspondingPersistentPrivateCustomer
				.getParent());

		return privateCustomer;
	}
}
