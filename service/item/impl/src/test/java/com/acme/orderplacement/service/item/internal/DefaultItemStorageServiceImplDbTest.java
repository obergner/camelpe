/**
 * 
 */
package com.acme.orderplacement.service.item.internal;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.support.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.PrincipalRegistrationTestExecutionListener;

/**
 * <p>
 * TODO: Insert short summary for DefaultItemStorageImplDbTest
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for DefaultItemStorageImplDbTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@TestUser(username = "admin", password = "admin")
@ContextConfiguration(locations = { "classpath:/META-INF/spring/persistence.testsupport.testEnvironment.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		PrincipalRegistrationTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME, defaultRollback = true)
@Transactional
public class DefaultItemStorageServiceImplDbTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

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
	@Resource(name = ItemStorageService.SERVICE_NAME)
	private ItemStorageService classUnderTest;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.service.item.internal.DefaultItemStorageServiceImpl#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void registerItemShouldRefuseToRegisterNullItem()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.classUnderTest.registerItem(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.service.item.internal.DefaultItemStorageServiceImpl#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test(expected = EntityAlreadyRegisteredException.class)
	public final void registerItemShouldRefuseToRegisterItemWithDuplicateItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		this.classUnderTest
				.registerItem(createItemDtoHavingDuplicateItemNumber());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.service.item.internal.DefaultItemStorageServiceImpl#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test
	public final void registerItemShouldRegisterItemWithUniqueItemNumber()
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
