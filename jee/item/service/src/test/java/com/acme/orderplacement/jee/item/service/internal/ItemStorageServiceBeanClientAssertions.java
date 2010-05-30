/**
 * 
 */
package com.acme.orderplacement.jee.item.service.internal;

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
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class ItemStorageServiceBeanClientAssertions {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_ITEM_NUMBER = "ITM-TEST-001-04711";

	private static final String NON_EXISTING_ITEM_NUMBER = "NON_EX_ITEM_NO";

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
				IllegalServiceUsageException.class.getPackage(),
				ServiceOperation.class.getPackage(),
				GenericJpaDao.class.getPackage(),
				ItemStorageService.class.getPackage(),
				ItemDao.class.getPackage(),
				ItemStorageServiceBean.class.getPackage()).addManifestResource(
				"META-INF/persistence.xml",
				ArchivePaths.create("persistence.xml")).addManifestResource(
				"META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml"))
				.addManifestResource("META-INF/sun-ejb-jar.xml",
						ArchivePaths.create("sun-ejb-jar.xml"));
		System.out.println(deployment.toString(true));

		return deployment;
	}

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

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 * @throws NamingException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void registerItemShouldRefuseToRegisterNullItem()
			throws EntityAlreadyRegisteredException, IllegalArgumentException,
			NamingException {
		lookupService().registerItem(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 * @throws NamingException
	 */
	@Test(expected = EntityAlreadyRegisteredException.class)
	public final void registerItemShouldRefuseToRegisterItemWithDuplicateItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException,
			NamingException {
		final ItemStorageService classUnderTest = lookupService();
		final ItemDto itemHavingDuplicateItemNumber = createItemDtoHavingDuplicateItemNumber();

		classUnderTest.registerItem(itemHavingDuplicateItemNumber);
		classUnderTest.registerItem(itemHavingDuplicateItemNumber);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.item.service.internal.ItemStorageServiceBean#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws EntityAlreadyRegisteredException
	 * @throws NamingException
	 */
	@Test
	public final void registerItemShouldRegisterItemWithUniqueItemNumber()
			throws EntityAlreadyRegisteredException, IllegalArgumentException,
			NamingException {
		lookupService().registerItem(createItemDtoHavingUniqueItemNumber());
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

	private ItemStorageService lookupService() throws NamingException {
		final InitialContext ic = new InitialContext();

		return new ItemStorageServiceDelegate((ItemStorageService) ic
				.lookup("java:global/test/ItemStorageServiceBean"));
	}

	private static class ItemStorageServiceDelegate implements
			ItemStorageService {

		private final ItemStorageService delegate;

		/**
		 * @param delegate
		 */
		ItemStorageServiceDelegate(final ItemStorageService delegate) {
			this.delegate = delegate;
		}

		@Override
		public void registerItem(final ItemDto newItemToRegister)
				throws EntityAlreadyRegisteredException,
				IllegalArgumentException {
			try {
				this.delegate.registerItem(newItemToRegister);
			} catch (final javax.ejb.EJBException e) {
				final Exception cause = e.getCausedByException();
				if (cause instanceof IllegalArgumentException) {
					throw (IllegalArgumentException) cause;
				}
				if (cause instanceof EntityAlreadyRegisteredException) {
					throw (EntityAlreadyRegisteredException) cause;
				}

				throw e;
			}

		}

	}
}
