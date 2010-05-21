/**
 * 
 */
package com.acme.orderplacement.persistence.company.internal.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.company.CompanyDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.SpringBasedAuthenticationProvidingTestExecutionListener;

/**
 * <p>
 * TODO: Insert short summary for JpaCompanyDaoDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaCompanyDaoDbTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/persistence.support.applicationLayer.scontext",
		"classpath:/META-INF/spring/persistence.company.test.platformLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.securityLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.aspectLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.daoLayer.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		SpringBasedAuthenticationProvidingTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "persistence.support.platform.transactionManager", defaultRollback = true)
@Transactional
public class JpaCompanyDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final Long EXISTING_COMPANY_ID = new Long(0);

	/**
	 * 
	 */
	private static final Long NON_EXISTING_COMPANY_ID = new Long(-1);

	/**
	 * 
	 */
	private static final String EXISTING_COMPANY_NUMBER = "COMP-01-98765";

	/**
	 * 
	 */
	private static final String NON_EXISTING_COMPANY_NUMBER = "NON_EX_COMP_NO";

	/**
	 * 
	 */
	private static final String EXISTING_COMPANY_NAME = "Test Company";

	/**
	 * 
	 */
	private static final String NON_EXISTING_COMPANY_NAME = "NON_EXISTING_COMPANY_NAME";

	/**
	 * 
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	@Resource
	private CompanyDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.company.internal.jpa.JpaCompanyDao#findByCompanyNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByCompanyNumberShouldFindMatchingCompany() {
		final Company matchingCompany = this.classUnderTest
				.findByCompanyNumber(EXISTING_COMPANY_NUMBER);

		assertNotNull("findByCompanyNumber('" + EXISTING_COMPANY_NUMBER
				+ "') returned <null>", matchingCompany);
		assertEquals("findByCompanyNumber('" + EXISTING_COMPANY_NUMBER
				+ "') returned wrong Company", EXISTING_COMPANY_NUMBER,
				matchingCompany.getCompanyNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.company.internal.jpa.JpaCompanyDao#findByCompanyNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByCompanyNumberShouldReturnNullForNonExistingCompanyNumber() {
		final Company matchingCompany = this.classUnderTest
				.findByCompanyNumber(NON_EXISTING_COMPANY_NUMBER);

		assertNull("findByCompanyNumber('" + NON_EXISTING_COMPANY_NUMBER
				+ "') did not return <null>", matchingCompany);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws ObjectNotPersistentException
	 */
	@Test
	public final void evictShouldThrowNoExceptions()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectNotPersistentException {
		final Company existingCompany = createDetachedTestCompany();

		this.classUnderTest.evict(existingCompany);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllCompanies() {
		final List<Company> allCompanies = this.classUnderTest.findAll();

		assertNotNull("findAll() returned <null>", allCompanies);
		assertEquals("findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM COMPANY.COMPANY"),
				allCompanies.size());
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
	public final void findByIdShouldFindOneCompany()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company company = this.classUnderTest.findById(
				EXISTING_COMPANY_ID, false);

		assertNotNull("findById(" + EXISTING_COMPANY_ID
				+ ", false) returned <null>", company);
		assertEquals("findById(" + EXISTING_COMPANY_ID
				+ ", false) returned wrong company", EXISTING_COMPANY_ID,
				company.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_COMPANY_ID, false);
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
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void makePersistentShouldPersistTransientCompany()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NoSuchPersistentObjectException {
		final Company company = createTransientCompanyToSave();

		final int numberOfCompaniesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		this.classUnderTest.makePersistent(company);
		this.classUnderTest.flush();

		final int numberOfCompaniesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		assertEquals("makePersistent('" + company
				+ "') did NOT persist transient Company",
				numberOfCompaniesBefore + 1, numberOfCompaniesAfter);
	}

	// /**
	// * Test method for
	// * {@link
	// com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistent(java.lang.Object)}
	// * .
	// *
	// * @throws ObjectNotTransientException
	// * @throws DataAccessRuntimeException
	// * @throws IllegalArgumentException
	// * @throws CollaborationPreconditionsNotMetException
	// */
	// @Test
	// public final void makePersistentShouldAlsoPersistTransientAssociations()
	// throws DataAccessRuntimeException, ObjectNotTransientException,
	// CollaborationPreconditionsNotMetException, IllegalArgumentException {
	// final Person person =
	// createTransientPersonToSaveWithTransientAccountAndHomeAddressAndAndPhoneNumbers();
	//
	// final int numberOfAccountsBefore = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.PERSON_ACCOUNT");
	// final int numberOfPostalAddressesBefore = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");
	// final int numberOfPhoneNumbersBefore = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");
	//
	// this.classUnderTest.makePersistent(person);
	// this.classUnderTest.flush();
	//
	// final int numberOfAccountsAfter = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.PERSON_ACCOUNT");
	// final int numberOfPostalAddressesAfter = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.POSTAL_ADDRESS");
	// final int numberOfPhoneNumbersAfter = this.jdbcTemplate
	// .queryForInt("SELECT count(*) FROM PEOPLE.TELEPHONE_NUMBER");
	//
	// assertEquals("makePersistent('" + person
	// + "') did NOT persist transient Account",
	// numberOfAccountsBefore + 1, numberOfAccountsAfter);
	// assertEquals("makePersistent('" + person
	// + "') did NOT persist transient PostalAddress",
	// numberOfPostalAddressesBefore + 1, numberOfPostalAddressesAfter);
	// assertEquals("makePersistent('" + person
	// + "') did NOT persist transient PhoneNumbers",
	// numberOfPhoneNumbersBefore + 3, numberOfPhoneNumbersAfter);
	// }

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientCompany()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company company = createTransientCompanyToMerge();

		final int numberOfCompaniesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		this.classUnderTest.makePersistentOrUpdatePersistentState(company);
		this.classUnderTest.flush();

		final int numberOfCompaniesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		assertEquals("makePersistentOrUpdatePersistentState('" + company
				+ "') did NOT persist transient Company",
				numberOfCompaniesBefore + 1, numberOfCompaniesAfter);
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
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentCompany()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company company = createDetachedTestCompany();
		final String updatedName = "NAME_UTEST";
		company.setName(updatedName);

		this.classUnderTest.makePersistentOrUpdatePersistentState(company);
		this.classUnderTest.flush();

		assertEquals("makePersistentOrUpdatePersistentState('" + company
				+ "') did NOT update persistent Company", updatedName,
				this.jdbcTemplate.queryForObject(
						"SELECT FIRST_NAME FROM COMPANY.COMPANY WHERE ID = ?",
						new Object[] { company.getId() }, String.class));
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
	public final void makeTransientShouldRemoveCompanyFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final Company company = this.classUnderTest.findById(
				EXISTING_COMPANY_ID, false);

		final int numberOfCompaniesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		this.classUnderTest.makeTransient(company);
		this.classUnderTest.flush();

		final int numberOfCompaniesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM COMPANY.COMPANY");

		assertEquals("makeTransient('" + company
				+ "') did NOT remove Company from database",
				numberOfCompaniesBefore - 1, numberOfCompaniesAfter);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Company createTransientCompanyToSave()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company company = new Company();
		company.setCompanyNumber(NON_EXISTING_COMPANY_NUMBER);
		company.setName(NON_EXISTING_COMPANY_NAME);

		final PostalAddress mainAddress = new PostalAddress();
		company.setMainAddress(mainAddress);
		mainAddress.setName("Main Company Address");
		mainAddress.setStreet("Main Street");
		mainAddress.setStreetNumber("12/MAIN");
		mainAddress.setPostalCode("56443");
		mainAddress.setCity("MainCity");

		final PhoneNumber mainPhone = new PhoneNumber();
		company.setMainPhoneNumber(mainPhone);
		mainPhone.setName("Main Company Phone");
		mainPhone.setType(PhoneNumber.Type.LANDLINE);
		mainPhone.setPhoneNumber("078/8976654");

		final EmailAddress mainEmail = new EmailAddress();
		company.setMainEmailAddress(mainEmail);
		mainEmail.setName("Main Company Email");
		mainEmail.setAddress("main@acme-comp.com");

		return company;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Company createDetachedTestCompany()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company existingPersistentCompany = this.classUnderTest.findById(
				EXISTING_COMPANY_ID, false);

		final Company company = new Company();
		company.setId(EXISTING_COMPANY_ID);
		company.setCompanyNumber(EXISTING_COMPANY_NUMBER);
		company.setName(EXISTING_COMPANY_NAME);

		company.setMainAddress(existingPersistentCompany.getMainAddress());
		company.setMainPhoneNumber(existingPersistentCompany
				.getMainPhoneNumber());
		company.setMainEmailAddress(existingPersistentCompany
				.getMainEmailAddress());

		company.setAuditInfo(new AuditInfo("TESTER", new Date(), "TESTER",
				new Date()));
		company.setVersion(Integer.valueOf(0));

		return company;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Company createTransientCompanyToMerge()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Company company = new Company();
		company.setCompanyNumber(NON_EXISTING_COMPANY_NUMBER);
		company.setName(NON_EXISTING_COMPANY_NAME);

		final PostalAddress mainAddress = new PostalAddress();
		company.setMainAddress(mainAddress);
		mainAddress.setName("Main Company Address");
		mainAddress.setStreet("Main Street");
		mainAddress.setStreetNumber("12/MAIN");
		mainAddress.setPostalCode("56443");
		mainAddress.setCity("MainCity");

		final PhoneNumber mainPhone = new PhoneNumber();
		company.setMainPhoneNumber(mainPhone);
		mainPhone.setName("Main Company Phone");
		mainPhone.setType(PhoneNumber.Type.LANDLINE);
		mainPhone.setPhoneNumber("078/8976654");

		final EmailAddress mainEmail = new EmailAddress();
		company.setMainEmailAddress(mainEmail);
		mainEmail.setName("Main Company Email");
		mainEmail.setAddress("main@acme-comp.com");

		return company;
	}
}
