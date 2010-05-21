/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.employee.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.people.employee.Department;
import com.acme.orderplacement.domain.people.employee.Employee;
import com.acme.orderplacement.domain.people.person.Gender;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.people.person.Salutation;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;
import com.acme.orderplacement.persistence.people.employee.EmployeeDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

;

/**
 * <p>
 * TODO: Insert short summary for JpaEmployeeDaoDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaEmployeeDaoDbTest
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
public class JpaEmployeeDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final Long EXISTING_EMPLOYEE_ID = Long.valueOf(0L);

	private static final Long NON_EXISTING_EMPLOYEE_ID = Long.valueOf(-1);

	private static final String EXISTING_EMPLOYEE_NUMBER = "EMP-HR-0001";

	private static final String NON_EXISTING_EMPLOYEE_NUMBER = "NON_EXISTING_EMPLOYEE_NUMBER";

	private static final String EXISTING_EMPLOYEE_NUMBER_PART = "HR-000";

	private static final Long UNEMPLOYED_PERSON_ID = Long.valueOf(1L);

	private static final Long UNUSED_EMAIL_ADDRESS_ID = Long.valueOf(4L);

	private JdbcTemplate jdbcTemplate;

	/**
	 * <p>
	 * Our {@link javax.persistence.EntityManager <em>JPA EntityManager</em>}.
	 * </p>
	 */
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@Resource
	private EmployeeDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.people.internal.employee.jpa.JpaEmployeeDao#findByDepartment(com.acme.orderplacement.domain.people.employee.Department)}
	 * .
	 */
	@Test
	public final void findByDepartmentShouldReturnAllDepartmentMembers() {
		final List<Employee> departmentMembers = this.classUnderTest
				.findByDepartment(Department.ACCOUNTING);

		assertNotNull(
				"findByDepartment(Department.ACCOUNTING) returned <null>",
				departmentMembers);
		assertEquals(
				"findByDepartment(Department.ACCOUNTING) returned wrong matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.EMPLOYEE WHERE DEPARTMENT = '"
								+ Department.ACCOUNTING.toString() + "'"),
				departmentMembers.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.employee.jpa.JpaEmployeeDao#findByEmployeeNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByEmployeeNumberShouldReturnOneExactMatch() {
		final Employee exactMatch = this.classUnderTest
				.findByEmployeeNumber(EXISTING_EMPLOYEE_NUMBER);

		assertNotNull("findByEmployeeNumber(" + EXISTING_EMPLOYEE_NUMBER
				+ ") returned <null>", exactMatch);
		assertEquals("findByEmployeeNumber(" + EXISTING_EMPLOYEE_NUMBER
				+ ") returned wrong Employee", exactMatch.getEmployeeNumber(),
				EXISTING_EMPLOYEE_NUMBER);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.employee.jpa.JpaEmployeeDao#findByEmployeeNumber(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByEmployeeNumberShouldReturnNoMatch() {
		final Employee unexpectedMatch = this.classUnderTest
				.findByEmployeeNumber(NON_EXISTING_EMPLOYEE_NUMBER);

		assertNull("findByEmployeeNumber(" + NON_EXISTING_EMPLOYEE_NUMBER
				+ ") did NOT return <null>", unexpectedMatch);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.people.internal.employee.jpa.JpaEmployeeDao#findByEmployeeNumberLike(java.lang.String)}
	 * .
	 */
	@Test
	public final void findByEmployeeNumberLikeShouldReturnAllInexactMatches() {
		final List<Employee> allInexactMatches = this.classUnderTest
				.findByEmployeeNumberLike(EXISTING_EMPLOYEE_NUMBER_PART);

		assertNotNull("findByEmployeeNumberLike("
				+ EXISTING_EMPLOYEE_NUMBER_PART + ") returned <null>",
				allInexactMatches);
		assertEquals(
				"findByEmployeeNumberLike(" + EXISTING_EMPLOYEE_NUMBER_PART
						+ ") returned wrong matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.EMPLOYEE WHERE EMPLOYEE_NUMBER LIKE '%"
								+ EXISTING_EMPLOYEE_NUMBER_PART + "%'"),
				allInexactMatches.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws RolePreconditionsNotMetException
	 * @throws IllegalDomainObjectStateException
	 */
	@Test
	public final void evictShouldNotThrowAnyExceptions()
			throws DataAccessRuntimeException, ObjectNotPersistentException,
			NoSuchPersistentObjectException, RolePreconditionsNotMetException,
			IllegalArgumentException, IllegalDomainObjectStateException {
		this.classUnderTest.evict(createDetachedTestEmployee());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findAll()}
	 * .
	 */
	@Test
	public final void findAllShouldReturnAllEmployees() {
		final List<Employee> allEmployeees = this.classUnderTest.findAll();

		assertNotNull("findAll() returned <null>", allEmployeees);
		assertEquals("findAll() returned wrong number of matches",
				this.jdbcTemplate
						.queryForInt("SELECT COUNT(*) FROM PEOPLE.EMPLOYEE"),
				allEmployeees.size());
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
	public final void findByIdShouldReturnOneEmployee()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Employee employee = this.classUnderTest.findById(
				EXISTING_EMPLOYEE_ID, false);

		assertNotNull("findById(" + EXISTING_EMPLOYEE_ID
				+ ", false) returned <null>", employee);
		assertEquals("findById(" + EXISTING_EMPLOYEE_ID
				+ ", false) returned wrong Employee", EXISTING_EMPLOYEE_ID,
				employee.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#findById(java.io.Serializable, boolean)}
	 * .
	 */
	@Test(expected = NoSuchPersistentObjectException.class)
	public final void findByIdShouldThrowNoSuchPersistentObjectException()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		this.classUnderTest.findById(NON_EXISTING_EMPLOYEE_ID, false);
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
	public final void makePersistentShouldPersistTransientEmployee()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			CollaborationPreconditionsNotMetException, IllegalArgumentException {
		final Employee transientEmployee = createTransientEmployeeToPersist();

		final int numberOfEmployeesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		this.classUnderTest.makePersistent(transientEmployee);
		this.classUnderTest.flush();

		final int numberOfEmployeesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		assertEquals("makePersistent('" + transientEmployee
				+ "') did NOT persist transient Employee",
				numberOfEmployeesBefore + 1, numberOfEmployeesAfter);
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
	public final void makePersistentOrUpdatePersistentStateShouldPersistTransientEmployee()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			IllegalArgumentException {
		final Employee transientEmployee = createTransientEmployeeToMerge();

		final int numberOfEmployeesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(transientEmployee);
		this.classUnderTest.flush();

		final int numberOfEmployeesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		assertEquals("makePersistentOrUpdatePersistentState('"
				+ transientEmployee + "') did NOT persist transient Employee",
				numberOfEmployeesBefore + 1, numberOfEmployeesAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws ObjectNotTransientException
	 * @throws NoSuchPersistentObjectException
	 * @throws IllegalArgumentException
	 * @throws IllegalDomainObjectStateException
	 */
	@Test
	public final void makePersistentOrUpdatePersistentStateShouldUpdatePersistentEmployee()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NoSuchPersistentObjectException, IllegalArgumentException,
			IllegalDomainObjectStateException {
		final Employee detachedEmployee = createDetachedTestEmployee();
		final Department updatedDepartment = Department.ITEM;
		detachedEmployee.setDepartment(updatedDepartment);

		this.classUnderTest
				.makePersistentOrUpdatePersistentState(detachedEmployee);
		this.classUnderTest.flush();

		assertEquals(
				"makePersistentOrUpdatePersistentState('" + detachedEmployee
						+ "') did NOT update persistent Employee",
				updatedDepartment.toString(),
				this.jdbcTemplate
						.queryForObject(
								"SELECT DEPARTMENT FROM PEOPLE.EMPLOYEE WHERE ID = ?",
								new Object[] { detachedEmployee.getId() },
								String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao#makeTransient(java.lang.Object)}
	 * .
	 */
	@Test
	public final void makeTransientShouldRemoveEmployeeFromDatabase()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			ObjectTransientException {
		final Employee employee = this.classUnderTest.findById(
				EXISTING_EMPLOYEE_ID, false);

		final int numberOfEmployeesBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		this.classUnderTest.makeTransient(employee);
		this.classUnderTest.flush();

		final int numberOfEmployeesAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM PEOPLE.EMPLOYEE");

		assertEquals("makeTransient('" + employee
				+ "') did NOT remove Employee from database",
				numberOfEmployeesBefore - 1, numberOfEmployeesAfter);
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
	private Employee createTransientEmployeeToPersist()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Person roleOwner = new Person();

		roleOwner.setFirstName("UTEST");
		roleOwner.setLastName("UTESTER");
		roleOwner.setSalutation(Salutation.MISS);
		roleOwner.setGender(Gender.FEMALE);
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

		final Employee employee = new Employee();
		employee.setParent(roleOwner);
		employee.setDepartment(Department.HR);
		employee.setEmployeeNumber("EMP-009-897665");

		employee.setVersion(Integer.valueOf(0));

		final EmailAddress workEmail = new EmailAddress();
		employee.setWorkEmailAddress(workEmail);
		workEmail.setName("Test Work Email");
		workEmail.setAddress("tester@acme.com");

		return employee;
	}

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Employee createTransientEmployeeToMerge()
			throws IllegalArgumentException {
		final Person persistentRoleOwner = this.entityManager.find(
				Person.class, UNEMPLOYED_PERSON_ID);

		final EmailAddress persistentWorkEmail = this.entityManager.find(
				EmailAddress.class, UNUSED_EMAIL_ADDRESS_ID);

		final Employee employee = new Employee();
		employee.setParent(persistentRoleOwner);
		employee.setWorkEmailAddress(persistentWorkEmail);
		employee.setDepartment(Department.CRM);
		employee.setEmployeeNumber("EMP-CRM-1007");

		employee.setVersion(Integer.valueOf(0));

		return employee;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 * @throws IllegalArgumentException
	 * @throws IllegalDomainObjectStateException
	 */
	private Employee createDetachedTestEmployee()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			IllegalArgumentException, IllegalDomainObjectStateException {
		final Employee correspondingPersistentEmployee = this.classUnderTest
				.findById(EXISTING_EMPLOYEE_ID, false);

		final Employee employee = new Employee();
		employee.setId(correspondingPersistentEmployee.getId());
		employee.setDepartment(correspondingPersistentEmployee.getDepartment());
		employee.setEmployeeNumber(correspondingPersistentEmployee
				.getEmployeeNumber());
		employee.setAuditInfo(correspondingPersistentEmployee.getAuditInfo());
		employee.setVersion(correspondingPersistentEmployee.getVersion());

		employee.setParent(correspondingPersistentEmployee.getParent());

		employee.setWorkEmailAddress(correspondingPersistentEmployee
				.getWorkEmailAddress());

		return employee;
	}
}
