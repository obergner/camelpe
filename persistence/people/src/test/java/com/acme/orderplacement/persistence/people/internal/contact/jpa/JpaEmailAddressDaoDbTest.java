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

import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.people.contact.EmailAddressDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * Test {@link JpaEmailAddressDao}.
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
public class JpaEmailAddressDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final Long EXISTING_EMAIL_ADDRESS_ID = Long.valueOf(0L);

	/**
	 * 
	 */
	private static final Long NON_EXISTING_EMAIL_ADDRESS_ID = Long.valueOf(-1);

	/**
	 * 
	 */
	private static final String EXISTING_EMAIL_ADDRESS = "test@test.org";

	/**
	 * 
	 */
	private static final String EXISTING_EMAIL_ADDRESS_PART = "@test.o";

	/**
	 * 
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	@Resource
	private EmailAddressDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaEmailAddressDao#findByAddress(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByAddressShouldReturnOneExactMatch() {
		final EmailAddress exactMatch = this.classUnderTest
				.findByAddress(EXISTING_EMAIL_ADDRESS);

		assertNotNull("findByAddress('" + EXISTING_EMAIL_ADDRESS
				+ "') returned <null>", exactMatch);
		assertEquals("findByAddress('" + EXISTING_EMAIL_ADDRESS
				+ "') returned wrong EmailAddress", EXISTING_EMAIL_ADDRESS,
				exactMatch.getAddress());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.contact.jpa.JpaEmailAddressDao#findByAddressLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByAddressLikeShouldReturnOneInexactMatch() {
		final List<EmailAddress> inexactMatches = this.classUnderTest
				.findByAddressLike(EXISTING_EMAIL_ADDRESS_PART);

		assertNotNull("findByFirstNameLike('" + EXISTING_EMAIL_ADDRESS_PART
				+ "') returned <null>", inexactMatches);
		assertEquals("findByFirstNameLike('" + EXISTING_EMAIL_ADDRESS_PART
				+ "') returned wrong number of matches", 1, inexactMatches
				.size());
		assertTrue("findByFirstNameLike('" + EXISTING_EMAIL_ADDRESS_PART
				+ "') returned wrong EmailAddress", inexactMatches.get(0)
				.getAddress().contains(EXISTING_EMAIL_ADDRESS_PART));
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
		this.classUnderTest.evict(createDetachedTestEmailAddress());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllEmailAddresses() {
		final List<EmailAddress> allEmailAddresses = this.classUnderTest
				.findAll();

		assertNotNull("findAll() returned <null>", allEmailAddresses);
		assertEquals(
				"findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.EMAIL_ADDRESS"),
				allEmailAddresses.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test
	public final void findByIdShouldFindOneEmailAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final EmailAddress emailAddress = this.classUnderTest.findById(
				EXISTING_EMAIL_ADDRESS_ID, false);

		assertNotNull("findById(" + EXISTING_EMAIL_ADDRESS_ID
				+ ", false) returned <null>", emailAddress);
		assertEquals("findById(" + EXISTING_EMAIL_ADDRESS_ID
				+ ", false) returned wrong EmailAddress",
				EXISTING_EMAIL_ADDRESS_ID, emailAddress.getId());
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
		this.classUnderTest.findById(NON_EXISTING_EMAIL_ADDRESS_ID, false);
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
	public final void makePersistentShouldPersistTransientEmailAddress()
			throws DataAccessRuntimeException, ObjectNotTransientException {
		final EmailAddress transientEmailAddress = createTransientEmailAddressToPersist();

		final int numberOfEmailAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		this.classUnderTest.makePersistent(transientEmailAddress);
		this.classUnderTest.flush();

		final int numberOfEmailAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		assertEquals("makePersistent('" + transientEmailAddress
				+ "') did NOT persist transient EmailAddress",
				numberOfEmailAddressesBefore + 1, numberOfEmailAddressesAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientEmailAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final EmailAddress transientEmailAddress = createTransientEmailAddressToPersist();

		final int numberOfEmailAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(transientEmailAddress);
		this.classUnderTest.flush();

		final int numberOfEmailAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ transientEmailAddress
				+ "') did NOT persist transient EmailAddress",
				numberOfEmailAddressesBefore + 1, numberOfEmailAddressesAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentEmailAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final EmailAddress detachedEmailAddress = createDetachedTestEmailAddress();
		final String updatedEmailAddress = "updated@utest.org";
		detachedEmailAddress.setAddress(updatedEmailAddress);

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(detachedEmailAddress);
		this.classUnderTest.flush();

		assertEquals(
				"makePersistentOrUpdatePersistentState('"
						+ detachedEmailAddress
						+ "') did NOT update persistent EmailAddress",
				updatedEmailAddress,
				this.jdbcTemplate
						.queryForObject(
								"SELECT EMAIL_ADDRESS FROM PEOPLE.EMAIL_ADDRESS WHERE ID = ?",
								new Object[] { detachedEmailAddress.getId() },
								String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makeTransientShouldRemoveEmailAddressFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final EmailAddress emailAddress = this.classUnderTest.findById(
				EXISTING_EMAIL_ADDRESS_ID, false);

		final int numberOfEmailAddressesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		this.classUnderTest.makeTransient(emailAddress);
		this.classUnderTest.flush();

		final int numberOfEmailAddressesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMAIL_ADDRESS");

		assertEquals("makeTransient('" + emailAddress
				+ "') did NOT remove EmailAddress from database",
				numberOfEmailAddressesBefore - 1, numberOfEmailAddressesAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private EmailAddress createTransientEmailAddressToPersist() {
		final EmailAddress emailAddress = new EmailAddress();
		emailAddress.setName("TESTEMAIL");
		emailAddress.setAddress("utest@utester.org");
		emailAddress.setVersion(Integer.valueOf(0));

		return emailAddress;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private EmailAddress createDetachedTestEmailAddress()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final EmailAddress emailAddress = new EmailAddress();
		emailAddress.setId(EXISTING_EMAIL_ADDRESS_ID);
		emailAddress.setName("TESTEMAIL");
		emailAddress.setAddress("utest@utester.org");
		emailAddress.setAuditInfo(new AuditInfo("TESTER", new Date(), "TESTER",
				new Date()));
		emailAddress.setVersion(Integer.valueOf(0));

		return emailAddress;
	}
}
