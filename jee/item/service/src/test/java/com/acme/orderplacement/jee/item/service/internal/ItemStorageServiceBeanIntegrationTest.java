/**
 * 
 */
package com.acme.orderplacement.jee.item.service.internal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.orm.jpa.JpaUnitils;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;

import com.acme.orderplacement.framework.domain.meta.jpa.AuditInfoManagingEntityListener;
import com.acme.orderplacement.framework.service.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.jee.item.persistence.ItemDao;
import com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao;
import com.acme.orderplacement.service.item.dto.ItemDto;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("ItemStorageServiceBeanIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "service.item.testItemPU")
public class ItemStorageServiceBeanIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_ITEM_NUMBER = "ITM-TEST-001-04711";

	private static final String NON_EXISTING_ITEM_NUMBER = "NON_EX_ITEM_NO";

	@TestedObject
	private ItemStorageServiceBean classUnderTest;

	@InjectIntoByType
	private final ItemDao itemDao = new JpaItemDao();

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Before
	public void registerTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername("TESTER");
	}

	@Before
	public void injectEntityManager() {
		JpaUnitils.injectEntityManagerInto(this.itemDao);
	}

	@After
	public void unregisterTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername(null);
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatRegisterItemRefusesToRegisterNullItem()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.classUnderTest.registerItem(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test(expected = EntityAlreadyRegisteredException.class)
	public final void asserThatRegisterItemRefusesToRegisterItemWithDuplicateItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.classUnderTest
				.registerItem(createItemDtoHavingDuplicateItemNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test
	public final void assertThatRegisterItemRegistersItemWithUniqueItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.classUnderTest.registerItem(createItemDtoHavingUniqueItemNumber());
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private ItemDto createItemDtoHavingDuplicateItemNumber() {
		return new ItemDto(EXISTING_ITEM_NUMBER, "Duplicate Test Item",
				"Test Description", null);
	}

	private ItemDto createItemDtoHavingUniqueItemNumber() {
		return new ItemDto(NON_EXISTING_ITEM_NUMBER, "Unique Test Item",
				"Test Description", null);
	}
}
