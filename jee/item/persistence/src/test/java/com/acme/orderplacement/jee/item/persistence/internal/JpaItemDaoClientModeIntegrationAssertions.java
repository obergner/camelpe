/**
 * 
 */
package com.acme.orderplacement.jee.item.persistence.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.framework.common.role.ApplicationUserRole;
import com.acme.orderplacement.framework.domain.IdentifiableDomainObject;
import com.acme.orderplacement.framework.domain.meta.AuditInfo;
import com.acme.orderplacement.framework.domain.meta.jpa.AuditInfoManagingEntityListener;
import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;
import com.acme.orderplacement.jee.framework.persistence.exception.DataAccessRuntimeException;
import com.acme.orderplacement.jee.framework.persistence.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotPersistentException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectNotTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.ObjectTransientException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateDeletedException;
import com.acme.orderplacement.jee.framework.persistence.exception.PersistentStateLockedException;
import com.acme.orderplacement.jee.item.persistence.ItemDao;
import com.sun.appserv.security.ProgrammaticLogin;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class JpaItemDaoClientModeIntegrationAssertions {

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

	private ItemDao cachedItemDao;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive deployment = ShrinkWrap.create("test.jar",
				JavaArchive.class).addPackages(true,
				Validate.class.getPackage(), Item.class.getPackage(),
				ApplicationUserRole.class.getPackage(),
				IdentifiableDomainObject.class.getPackage(),
				GenericJpaDao.class.getPackage(), ItemDao.class.getPackage(),
				JpaItemDao.class.getPackage()).addManifestResource(
				"META-INF/glassfish/persistence.xml",
				ArchivePaths.create("persistence.xml")).addManifestResource(
				"META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml"))
				.addManifestResource("META-INF/glassfish/sun-ejb-jar.xml",
						ArchivePaths.create("sun-ejb-jar.xml"));

		return deployment;
	}

	/**
	 * Unfortunately, JPA mananged objects are neither JEE resources nor CDI
	 * beans and thus cannot be a target of CDI injection. Therefore, for the
	 * time being we need to register our test user manually.
	 */
	@Before
	public void registerTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername("TESTER");
	}

	@Before
	public void login() throws Exception {
		final ProgrammaticLogin login = new ProgrammaticLogin();
		login.login("admin", "admin", "file", true);
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
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#evict(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotPersistentException
	 * @throws DataAccessRuntimeException
	 * @throws NamingException
	 */
	@Test
	public void testEvict() throws DataAccessRuntimeException,
			ObjectNotPersistentException, NamingException {
		final Item existingItem = createDetachedTestItem();

		lookupItemDao().evict(existingItem);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#flush()}
	 * .
	 * 
	 * @throws NamingException
	 * @throws DataAccessRuntimeException
	 * @throws PersistentStateDeletedException
	 * @throws PersistentStateConcurrentlyModifiedException
	 * @throws PersistentStateLockedException
	 */
	@Test
	public void testFlush() throws PersistentStateLockedException,
			PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException, DataAccessRuntimeException,
			NamingException {
		lookupItemDao().flush();
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByItemNumber(java.lang.String)}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public void testFindByItemNumber_FindsMatchingItem() throws NamingException {
		final Item matchingItem = lookupItemDao().findByItemNumber(
				EXISTING_ITEM_NUMBER);

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
	 * 
	 * @throws NamingException
	 */
	@Test
	public void testFindByItemNumber_ReturnsNull() throws NamingException {
		final Item matchingItem = lookupItemDao().findByItemNumber(
				NON_EXISTING_ITEM_NUMBER);

		assertNull("findByItemNumber('" + NON_EXISTING_ITEM_NUMBER
				+ "') did not return <null>", matchingItem);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao#findByNameLike(java.lang.String)}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public void testFindByNameLike_FindsMatchingItem() throws NamingException {
		final List<Item> matchingItems = lookupItemDao().findByNameLike(
				EXISTING_ITEM_NAME_PART);

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
	 * 
	 * @throws NamingException
	 */
	@Test
	public void testFindByNameLike_ReturnsEmptyItemList()
			throws NamingException {
		final List<Item> matchingItems = lookupItemDao().findByNameLike(
				NON_EXISTING_ITEM_NAME_PART);

		assertNotNull("findByNameLike('" + NON_EXISTING_ITEM_NAME_PART
				+ "') returned <null>", matchingItems);
		assertEquals("findByNameLike('" + NON_EXISTING_ITEM_NAME_PART
				+ "') returned wrong number of matches", 0, matchingItems
				.size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#findAll()}
	 * .
	 * 
	 * @throws NamingException
	 * @throws DataAccessRuntimeException
	 */
	@Test
	public void testFindAll() throws DataAccessRuntimeException,
			NamingException {
		final List<Item> allItems = lookupItemDao().findAll();

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
	 * @throws NamingException
	 */
	@Test
	public void testFindById_FindsMatchingItem()
			throws NoSuchPersistentObjectException, DataAccessRuntimeException,
			NamingException {
		final Item matchingItem = lookupItemDao().findById(EXISTING_ITEM_ID,
				false);

		assertNotNull("findById(" + EXISTING_ITEM_ID + ") returned <null>",
				matchingItem);
		assertEquals("findById(" + EXISTING_ITEM_ID + ") returned wrong Item",
				EXISTING_ITEM_ID, matchingItem.getId());
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
	 * @throws NamingException
	 */
	@Test
	public void testMakeTransient_DeletesItem()
			throws PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException, PersistentStateLockedException,
			DataAccessRuntimeException, ObjectTransientException,
			NoSuchPersistentObjectException, NamingException {
		final Item existingItem = lookupItemDao().findById(EXISTING_ITEM_ID,
				false);

		final int numberOfItemsBefore = lookupItemDao().findAll().size();

		lookupItemDao().makeTransient(existingItem);
		lookupItemDao().flush();

		final int numberOfItemsAfter = lookupItemDao().findAll().size();

		assertEquals("makeTransient('" + existingItem
				+ "') did NOT delete Item", numberOfItemsBefore - 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistent(java.lang.Object)}
	 * .
	 * 
	 * @throws ObjectNotTransientException
	 * @throws DataAccessRuntimeException
	 * @throws NamingException
	 */
	@Test
	public void testMakePersistent_SavesItem()
			throws DataAccessRuntimeException, ObjectNotTransientException,
			NamingException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = lookupItemDao().findAll().size();

		lookupItemDao().makePersistent(item);
		lookupItemDao().flush();

		final int numberOfItemsAfter = lookupItemDao().findAll().size();

		assertEquals("makePersistent('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * FIXME: Make this test pass.
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NamingException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_UpdatesItem()
			throws DataAccessRuntimeException, NamingException {
		final Item item = createDetachedTestItem();
		final String updatedItemName = "NAME_UPDATED_BY_UNIT_TEST";
		item.setName(updatedItemName);

		lookupItemDao().makePersistentOrUpdatePersistentState(item);
		lookupItemDao().flush();

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT update item.name", 1, lookupItemDao()
				.findByNameLike(updatedItemName).size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.persistence.hibernate.AbstractHibernateDAO#makePersistentOrUpdatePersistentState(java.lang.Object)}
	 * .
	 * 
	 * FIXME: Make this test pass.
	 * 
	 * @throws DataAccessRuntimeException
	 * @throws NamingException
	 */
	@Test
	public void testMakePersistentOrUpdatePersistentState_SavesItem()
			throws DataAccessRuntimeException, NamingException {
		final Item item = createTransientItemToSave();

		final int numberOfItemsBefore = lookupItemDao().findAll().size();

		lookupItemDao().makePersistentOrUpdatePersistentState(item);
		lookupItemDao().flush();

		final int numberOfItemsAfter = lookupItemDao().findAll().size();

		assertEquals("makePersistentOrUpdatePersistentState('" + item
				+ "') did NOT persist transient Item", numberOfItemsBefore + 1,
				numberOfItemsAfter);
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

	private ItemDao lookupItemDao() throws NamingException {
		if (this.cachedItemDao == null) {
			final InitialContext ic = new InitialContext();

			this.cachedItemDao = new EjbExceptionUnwrappingItemDaoDelegate(
					(ItemDao) ic
							.lookup("java:global/test/TransactionInitiatingItemDaoProxyBean"));
		}

		return this.cachedItemDao;
	}

	private static class EjbExceptionUnwrappingItemDaoDelegate implements
			ItemDao {

		private final ItemDao delegate;

		/**
		 * @param delegate
		 */
		EjbExceptionUnwrappingItemDaoDelegate(final ItemDao delegate) {
			this.delegate = delegate;
		}

		/**
		 * @param persistentObject
		 * @throws DataAccessRuntimeException
		 * @throws ObjectNotPersistentException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#evict(java.lang.Object)
		 */
		public void evict(final Item persistentObject)
				throws DataAccessRuntimeException, ObjectNotPersistentException {
			this.delegate.evict(persistentObject);
		}

		/**
		 * @return
		 * @throws DataAccessRuntimeException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findAll()
		 */
		public List<Item> findAll() throws DataAccessRuntimeException {
			return this.delegate.findAll();
		}

		/**
		 * @param id
		 * @param lock
		 * @return
		 * @throws NoSuchPersistentObjectException
		 * @throws DataAccessRuntimeException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findById(java.io.Serializable,
		 *      boolean)
		 */
		public Item findById(final Long id, final boolean lock)
				throws NoSuchPersistentObjectException,
				DataAccessRuntimeException {
			return this.delegate.findById(id, lock);
		}

		/**
		 * @param itemNumber
		 * @return
		 * @see com.acme.orderplacement.jee.item.persistence.ItemDao#findByItemNumber(java.lang.String)
		 */
		public Item findByItemNumber(final String itemNumber) {
			return this.delegate.findByItemNumber(itemNumber);
		}

		/**
		 * @param itemName
		 * @return
		 * @see com.acme.orderplacement.jee.item.persistence.ItemDao#findByNameLike(java.lang.String)
		 */
		public List<Item> findByNameLike(final String itemName) {
			return this.delegate.findByNameLike(itemName);
		}

		/**
		 * @throws DataAccessRuntimeException
		 * @throws PersistentStateLockedException
		 * @throws PersistentStateConcurrentlyModifiedException
		 * @throws PersistentStateDeletedException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#flush()
		 */
		public void flush() throws DataAccessRuntimeException,
				PersistentStateLockedException,
				PersistentStateConcurrentlyModifiedException,
				PersistentStateDeletedException {
			this.delegate.flush();
		}

		/**
		 * @param transientObject
		 * @return
		 * @throws DataAccessRuntimeException
		 * @throws ObjectNotTransientException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
		 */
		public Item makePersistent(final Item transientObject)
				throws DataAccessRuntimeException, ObjectNotTransientException {
			return this.delegate.makePersistent(transientObject);
		}

		/**
		 * @param object
		 * @return
		 * @throws DataAccessRuntimeException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
		 */
		public Item makePersistentOrUpdatePersistentState(final Item object)
				throws DataAccessRuntimeException {
			return this.delegate.makePersistentOrUpdatePersistentState(object);
		}

		/**
		 * @param persistentOrDetachedObject
		 * @throws DataAccessRuntimeException
		 * @throws ObjectTransientException
		 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
		 */
		public void makeTransient(final Item persistentOrDetachedObject)
				throws DataAccessRuntimeException, ObjectTransientException {
			this.delegate.makeTransient(persistentOrDetachedObject);
		}
	}
}
