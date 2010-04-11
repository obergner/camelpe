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

import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.people.contact.PhoneNumberDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * TODO: Insert short summary for JpaPhoneNumberDaoDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPhoneNumberDaoDbTest
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
public class JpaPhoneNumberDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final Long EXISTING_PHONE_NUMBER_ID = Long.valueOf(2L);

	/**
	 * 
	 */
	private static final Long NON_EXISTING_PHONE_NUMBER_ID = Long.valueOf(-1);

	/**
	 * 
	 */
	private static final String EXISTING_PHONE_NUMBER = "0456743566";

	/**
	 * 
	 */
	private static final String EXISTING_PHONE_NUMBER_PART = "5674356";

	/**
	 * 
	 */
	private static final Long UNREFERENCED_PHONE_NUMBER_ID = Long.valueOf(5L);

	/**
	 * 
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	@Resource
	private PhoneNumberDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaPhoneNumberDao#findByNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByNumberShouldReturnOneExactMatch() {
		final PhoneNumber exactMatch = this.classUnderTest
				.findByNumber(EXISTING_PHONE_NUMBER);

		assertNotNull("findByNumber('" + EXISTING_PHONE_NUMBER
				+ "') returned <null>", exactMatch);
		assertEquals("findByNumber('" + EXISTING_PHONE_NUMBER
				+ "') returned wrong PhoneNumber", EXISTING_PHONE_NUMBER,
				exactMatch.getPhoneNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaPhoneNumberDao#findByNumberLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByNumberLikeShouldReturnOneInexactMatch() {
		final List<PhoneNumber> inexactMatches = this.classUnderTest
				.findByNumberLike(EXISTING_PHONE_NUMBER_PART);

		assertNotNull("findByNumberLike('" + EXISTING_PHONE_NUMBER_PART
				+ "') returned <null>", inexactMatches);
		assertEquals("findByNumberLike('" + EXISTING_PHONE_NUMBER_PART
				+ "') returned wrong number of matches", 1, inexactMatches
				.size());
		assertTrue("findByNumberLike('" + EXISTING_PHONE_NUMBER_PART
				+ "') returned wrong PhoneNumber", inexactMatches.get(0)
				.getPhoneNumber().contains(EXISTING_PHONE_NUMBER_PART));
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
		this.classUnderTest.evict(createDetachedTestPhoneNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllPhoneNumbers() {
		final List<PhoneNumber> allPhoneNumbers = this.classUnderTest.findAll();

		assertNotNull("findAll() returned <null>", allPhoneNumbers);
		assertEquals(
				"findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.TELEPHONE_NUMBER"),
				allPhoneNumbers.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test
	public final void findByIdShouldFindOnePhoneNumber()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final PhoneNumber phoneNumber = this.classUnderTest.findById(
				EXISTING_PHONE_NUMBER_ID, false);

		assertNotNull("findById(" + EXISTING_PHONE_NUMBER_ID
				+ ", false) returned <null>", phoneNumber);
		assertEquals("findById(" + EXISTING_PHONE_NUMBER_ID
				+ ", false) returned wrong PhoneNumber",
				EXISTING_PHONE_NUMBER_ID, phoneNumber.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_PHONE_NUMBER_ID, false);
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
	public final void makePersistentShouldPersistTransientPhoneNumber()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final PhoneNumber transientEmailAddress = createTransientPhoneNumberToPersist();

		final int numberOfPhoneNumbersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		this.classUnderTest.makePersistent(transientEmailAddress);
		this.classUnderTest.flush();

		final int numberOfPhoneNumbersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		assertEquals("makePersistent('" + transientEmailAddress
				+ "') did NOT persist transient PhoneNumber",
				numberOfPhoneNumbersBefore + 1, numberOfPhoneNumbersAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientPhoneNumber()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final PhoneNumber transientPhoneNumber = createTransientPhoneNumberToPersist();

		final int numberOfPhoneNumbersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(transientPhoneNumber);
		this.classUnderTest.flush();

		final int numberOfPhoneNumbersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ transientPhoneNumber
				+ "') did NOT persist transient PhoneNumber",
				numberOfPhoneNumbersBefore + 1, numberOfPhoneNumbersAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentPhoneNumber()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NoSuchPersistentObjectException {
		final PhoneNumber detachedPhoneNumber = createDetachedTestPhoneNumber();
		final String updatedPhoneNumber = "0897/66656";
		detachedPhoneNumber.setPhoneNumber(updatedPhoneNumber);

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(detachedPhoneNumber);
		this.classUnderTest.flush();

		assertEquals(
				"makePersistentOrUpdatePersistentState('" + detachedPhoneNumber
						+ "') did NOT update persistent PhoneNumber",
				updatedPhoneNumber,
				this.jdbcTemplate
						.queryForObject(
								"SELECT PHONE_NUMBER FROM PEOPLE.TELEPHONE_NUMBER WHERE ID = ?",
								new Object[] { detachedPhoneNumber.getId() },
								String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makeTransientShouldRemovePhoneNumberFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final PhoneNumber phoneNumber = this.classUnderTest.findById(
				UNREFERENCED_PHONE_NUMBER_ID, false);

		final int numberOfPhoneNumbersBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		this.classUnderTest.makeTransient(phoneNumber);
		this.classUnderTest.flush();

		final int numberOfPhoneNumbersAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");

		assertEquals("makeTransient('" + phoneNumber
				+ "') did NOT remove PhoneNumber from database",
				numberOfPhoneNumbersBefore - 1, numberOfPhoneNumbersAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PhoneNumber createTransientPhoneNumberToPersist() {
		final PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setName("TESTPHONE");
		phoneNumber.setPhoneNumber("04567/675443");
		phoneNumber.setVersion(Integer.valueOf(0));

		return phoneNumber;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private PhoneNumber createDetachedTestPhoneNumber()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final PhoneNumber emailAddress = new PhoneNumber();
		emailAddress.setId(EXISTING_PHONE_NUMBER_ID);
		emailAddress.setName("TESTPHONE");
		emailAddress.setPhoneNumber("04567/675443");
		emailAddress.setAuditInfo(new AuditInfo("TESTER", new Date(), "TESTER",
				new Date()));
		emailAddress.setVersion(Integer.valueOf(0));

		return emailAddress;
	}
}
