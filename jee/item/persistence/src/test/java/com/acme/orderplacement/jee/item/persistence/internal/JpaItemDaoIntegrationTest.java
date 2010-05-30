/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.engine.SessionImplementor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.framework.domain.meta.AuditInfo;
import com.acme.orderplacement.framework.domain.meta.jpa.AuditInfoManagingEntityListener;
import com.acme.orderplacement.jee.framework.persistence.exception.DataAccessRuntimeException;
import com.acme.orderplacement.jee.framework.persistence.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotPersistentException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateDeletedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateLockedException;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("JpaItemDaoIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "persistence.item.testItemPU")
public class JpaItemDaoIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final Long EXISTING_ITEM_ID = new Long(1);

	private static final String EXISTING_ITEM_NUMBER = "ITM-TEST-001-04711";

	private static final String NON_EXISTING_ITEM_NUMBER = "NON_EX_ITEM_NO";

	private static final String EXISTING_ITEM_NAME = "Test Item No. 1";

	private static final String NON_EXISTING_ITEM_NAME = "NON_EXISTING_ITEM_NAME";

	private static final String EXISTING_ITEM_NAME_PART = "Item No. 1";

	private static final String NON_EXISTING_ITEM_NAME_PART = "NON_EXISTING_ITEM_NAME_PART";

	@TestedObject
	private JpaItemDao classUnderTest;

	@InjectIntoByType
	@PersistenceContext
	private EntityManager entityManager;

	private final QueryRunner queryRunner = new QueryRunner();

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Before
	public void registerTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername("TESTER");
	}

	@After
	public void unregisterTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername(null);
	}

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByItemNumber(java.lang.String)}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByItemNumber(java.lang.String)}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByNameLike(java.lang.String)}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByNameLike(java.lang.String)}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makeTransient(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectTransientException
	 * @throws DataAccessRuntimeException
	 * @throws PersistentStateLockedException
	 * @throws PersistentStateDeletedException
	 * @throws PersistentStateConcurrentlyModifiedException
	 * @throws NoSuchPersistentObjectException
	 * @throws SQLException
	 */
	@Test
	public void testMakeTransient_DeletesItem()
			throws PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException, PersistentStateLockedException,
			DataAccessRuntimeException, ObjectTransientException,
			NoSuchPersistentObjectException, SQLException {
		final Item existingItem = this.classUnderTest.findById(
				EXISTING_ITEM_ID, false);

		final int numberOfItemsBefore = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		this.classUnderTest.makeTransient(existingItem);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		assertEquals("makeTransient('" + existingItem
				+ "') did NOT delete Item", numberOfItemsBefore - 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#findAll()}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#findById(java.io.Serializable, boolean)}
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
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistent(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotTransientException
	 * @throws DataAccessRuntimeException
	 * @throws SQLException
	 */
	@Test
	public void testMakePersistent_SavesItem()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			SQLException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		this.classUnderTest.makePersistent(item);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		assertEquals("makePersistent('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws SQLException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_UpdatesItem()
			throws DataAccessRuntimeException, SQLException {
		final Item item = createDetachedTestItem();
		final String updatedItemName = "NAME_UPDATED_BY_UNIT_TEST";
		item.setName(updatedItemName);

		this.classUnderTest.makePersistentOrUpdatePersistentState(item);
		this.classUnderTest.flush();

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT update item.name", updatedItemName,
				this.queryRunner.query(getCurrentSqlConnection(),
						"SELECT name FROM ITEM.ITEM WHERE ID = ?",
						new ScalarHandler(1), item.getId()));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws SQLException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_SavesItem()
			throws DataAccessRuntimeException, SQLException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		this.classUnderTest.makePersistentOrUpdatePersistentState(item);
		this.classUnderTest.flush();

		final int numberOfItemsAfter = (Integer) this.queryRunner.query(
				getCurrentSqlConnection(), "SELECT count(*) FROM ITEM.ITEM",
				new ScalarHandler(1));

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotPersistentException
	 * @throws DataAccessRuntimeException
	 */
	@Test
	public void testEvict() throws DataAccessRuntimeException,
			ObjectNotPersistentException {
		final Item existingItem = createDetachedTestItem();

		this.classUnderTest.evict(existingItem);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#flush()}
	 * .
	 */
	@Test
	public void testFlush() {
		this.classUnderTest.flush();
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	private Item createTransientItemToSave() throws DataAccessRuntimeException {
		final Item item = new Item();
		item.setItemNumber(NON_EXISTING_ITEM_NUMBER);
		item.setName(NON_EXISTING_ITEM_NAME);
		item.setDescription("This is a test item");

		return item;
	}

	private Item createDetachedTestItem() throws DataAccessRuntimeException {
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

	private Connection getCurrentSqlConnection() {
		return this.entityManager.unwrap(SessionImplementor.class).connection();
	}
}
