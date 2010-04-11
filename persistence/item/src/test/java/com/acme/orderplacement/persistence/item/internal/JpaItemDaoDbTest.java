/**
 * 
 */
package com.acme.orderplacement.persistence.item.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.persistence.item.ItemDao;
import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotPersistentException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateDeletedException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateLockedException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/persistence.support.applicationLayer.scontext",
		"classpath:/META-INF/spring/persistence.item.test.platformLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.securityLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.aspectLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.daoLayer.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		PrincipalRegistrationTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "persistence.support.platform.transactionManager", defaultRollback = true)
@Transactional
public class JpaItemDaoDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final Long EXISTING_ITEM_ID = new Long(1);

	/**
	 * 
	 */
	private static final String EXISTING_ITEM_NUMBER = "ITM-TEST-001-04711";

	/**
	 * 
	 */
	private static final String NON_EXISTING_ITEM_NUMBER = "NON_EX_ITEM_NO";

	/**
	 * 
	 */
	private static final String EXISTING_ITEM_NAME = "Test Item No. 1";

	/**
	 * 
	 */
	private static final String NON_EXISTING_ITEM_NAME = "NON_EXISTING_ITEM_NAME";

	/**
	 * 
	 */
	private static final String EXISTING_ITEM_NAME_PART = "Item No. 1";

	/**
	 * 
	 */
	private static final String NON_EXISTING_ITEM_NAME_PART = "NON_EXISTING_ITEM_NAME_PART";

	/**
	 * 
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	@Resource
	private ItemDao classUnderTest;

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
	 * {@link com.acme.orderplacement.persistence.item.internal.JpaItemDao#findByItemNumber(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByItemNumber_FindsMatchingItem() {
		final Item matchingItem = this.classUnderTest
				.findByItemNumber(EXISTING_ITEM_NUMBER);

		assertNotNull("findByItemNumber('" + EXISTING_ITEM_NUMBER
				+ "') returned <null>", matchingItem);
		assertEquals("findByItemNumber('" + EXISTING_ITEM_NUMBER
				+ "') returned wrong Item", EXISTING_ITEM_NUMBER, matchingItem
				.getItemNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.JpaItemDao#findByItemNumber(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByItemNumber_ReturnsNull() {
		final Item matchingItem = this.classUnderTest
				.findByItemNumber(NON_EXISTING_ITEM_NUMBER);

		assertNull("findByItemNumber('" + NON_EXISTING_ITEM_NUMBER
				+ "') did not return <null>", matchingItem);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.JpaItemDao#findByNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByNameLike_FindsMatchingItem() {
		final List<Item> matchingItems = this.classUnderTest
				.findByNameLike(EXISTING_ITEM_NAME_PART);

		assertNotNull("findByNameLike('" + EXISTING_ITEM_NAME_PART
				+ "') returned <null>", matchingItems);
		assertEquals("findByNameLike('" + EXISTING_ITEM_NAME_PART
				+ "') returned wrong number of matches", 1, matchingItems
				.size());
		final Item matchingItem = matchingItems.get(0);
		assertTrue("findByNameLike('" + EXISTING_ITEM_NAME_PART
				+ "') returned wrong Item", matchingItem.getName().contains(
				EXISTING_ITEM_NAME_PART));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.JpaItemDao#findByNameLike(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByNameLike_ReturnsEmptyItemList() {
		final List<Item> matchingItems = this.classUnderTest
				.findByNameLike(NON_EXISTING_ITEM_NAME_PART);

		assertNotNull("findByNameLike('" + NON_EXISTING_ITEM_NAME_PART
				+ "') returned <null>", matchingItems);
		assertEquals("findByNameLike('" + NON_EXISTING_ITEM_NAME_PART
				+ "') returned wrong number of matches", 0, matchingItems
				.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#makeTransient(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectTransientException
	 * @throws DataAccessRuntimeException
	 * @throws PersistentStateLockedException
	 * @throws PersistentStateDeletedException
	 * @throws PersistentStateConcurrentlyModifiedException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testMakeTransient_DeletesItem()
			throws PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException, PersistentStateLockedException,
			DataAccessRuntimeException, ObjectTransientException,
			NoSuchPersistentObjectException {
		final Item existingItem = this.classUnderTest.findById(
				EXISTING_ITEM_ID, false);

		final int numberOfItemsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		this.classUnderTest.makeTransient(existingItem);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		assertEquals("makeTransient('" + existingItem
				+ "') did NOT delete Item", numberOfItemsBefore - 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#findAll()}
	 * .
	 */
	@Test
	public void testFindAll() {
		final List<Item> allItems = this.classUnderTest.findAll();

		assertEquals("findAll() returned wrong number of Items", 1, allItems
				.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#findById(java.io.Serializable, boolean)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testFindById_FindsMatchingItem()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Item matchingItem = this.classUnderTest.findById(
				EXISTING_ITEM_ID, false);

		assertNotNull("findById(" + EXISTING_ITEM_ID + ") returned <null>",
				matchingItem);
		assertEquals("findById(" + EXISTING_ITEM_ID + ") returned wrong Item",
				EXISTING_ITEM_ID, matchingItem.getId());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#makePersistent(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotTransientException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testMakePersistent_SavesItem()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NoSuchPersistentObjectException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		this.classUnderTest.makePersistent(item);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		assertEquals("makePersistent('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_UpdatesItem()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Item item = createDetachedTestItem();
		final String updatedItemName = "NAME_UPDATED_BY_UNIT_TEST";
		item.setName(updatedItemName);

		this.classUnderTest.makePersistentOrUpdatePersistentState(item);
		this.classUnderTest.flush();

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT update item.name", updatedItemName,
				this.jdbcTemplate.queryForObject(
						"SELECT name FROM ITEM.ITEM WHERE ID = ?",
						new Object[] { item.getId() }, String.class));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_SavesItem()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		this.classUnderTest.makePersistentOrUpdatePersistentState(item);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = this.jdbcTemplate
				.queryForInt("SELECT count(*) FROM ITEM.ITEM");

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotPersistentException
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	@Test
	public void testEvict() throws DataAccessRuntimeException,
			ObjectNotPersistentException, NoSuchPersistentObjectException {
		final Item existingItem = createDetachedTestItem();

		this.classUnderTest.evict(existingItem);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.hibernate.AbstractHibernateDAO#flush()}
	 * .
	 */
	@Test
	public void testFlush() {
		this.classUnderTest.flush();
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Item createTransientItemToSave()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Item item = new Item();
		item.setItemNumber(NON_EXISTING_ITEM_NUMBER);
		item.setName(NON_EXISTING_ITEM_NAME);
		item.setDescription("This is a test item");

		return item;
	}

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 * @throws NoSuchPersistentObjectException
	 */
	private Item createDetachedTestItem()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException {
		final Item item = new Item();
		item.setId(EXISTING_ITEM_ID);
		item.setItemNumber(EXISTING_ITEM_NUMBER);
		item.setName(EXISTING_ITEM_NAME);
		item.setDescription("This is a test item");
		item.setAuditInfo(new AuditInfo("TESTER", new Date(), "TESTER",
				new Date()));
		item.setVersion(Integer.valueOf(0));

		return item;
	}
}
