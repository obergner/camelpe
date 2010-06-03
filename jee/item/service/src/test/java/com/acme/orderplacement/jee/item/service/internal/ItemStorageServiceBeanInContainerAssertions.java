/**
 * 
 */
package com.acme.orderplacement.jee.item.service.internal;

import java.security.Principal;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

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
import com.acme.orderplacement.framework.domain.meta.jpa.AuditInfoManagingEntityListener;
import com.acme.orderplacement.framework.service.exception.IllegalServiceUsageException;
import com.acme.orderplacement.framework.service.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.framework.service.meta.annotation.ServiceOperation;
import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;
import com.acme.orderplacement.jee.item.persistence.ItemDao;
import com.acme.orderplacement.jee.item.persistence.internal.JpaItemDao;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.sun.appserv.security.ProgrammaticLogin;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * <p>
 * FIXME: Due to restrictions in Arquillian-1.0.0.Alpha2 this test will
 * currently fail.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class ItemStorageServiceBeanInContainerAssertions {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_ITEM_NUMBER = "ITM-TEST-001-04711";

	private static final String NON_EXISTING_ITEM_NUMBER = "NON_EX_ITEM_NO";

	@EJB
	private ItemStorageService classUnderTest;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive itemServiceEjbModule = ShrinkWrap.create("test.jar",
				JavaArchive.class).addPackages(true,
				Validate.class.getPackage(), Item.class.getPackage(),
				ApplicationUserRole.class.getPackage(),
				IdentifiableDomainObject.class.getPackage(),
				IllegalServiceUsageException.class.getPackage(),
				ServiceOperation.class.getPackage(),
				GenericJpaDao.class.getPackage(),
				ItemStorageService.class.getPackage(),
				ItemDao.class.getPackage(),
				ItemStorageServiceBean.class.getPackage()).addManifestResource(
				"META-INF/glassfish/persistence.xml",
				ArchivePaths.create("persistence.xml")).addManifestResource(
				"META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml"))
				.addManifestResource("META-INF/glassfish/sun-ejb-jar.xml",
						ArchivePaths.create("sun-ejb-jar.xml"));

		return itemServiceEjbModule;
	}

	@Produces
	public Principal testPrincipal() {
		return new Principal() {

			@Override
			public String getName() {

				return "Tester";
			}
		};
	}

	@Before
	public void registerTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername("TESTER");
	}

	@Before
	public void login() throws Exception {
		final ProgrammaticLogin login = new ProgrammaticLogin();
		login.login("employee", "employee", "file", true);
	}

	@After
	public void unregisterTestUsername() {
		AuditInfoManagingEntityListener.registerTestUsername(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
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
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 */
	@Test(expected = EntityAlreadyRegisteredException.class)
	public final void registerItemShouldRefuseToRegisterItemWithDuplicateItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		final ItemDto itemHavingDuplicateItemNumber = createItemDtoHavingDuplicateItemNumber();

		this.classUnderTest.registerItem(itemHavingDuplicateItemNumber);
		this.classUnderTest.registerItem(itemHavingDuplicateItemNumber);
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
