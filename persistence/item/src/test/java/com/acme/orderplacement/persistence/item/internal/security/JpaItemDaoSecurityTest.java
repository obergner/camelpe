/**
 * 
 */
package com.acme.orderplacement.persistence.item.internal.security;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.acme.orderplacement.persistence.item.ItemDao;
import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.annotation.spring.SpringBasedAuthenticationProvidingTestExecutionListener;

/**
 * <p>
 * An integration test for verifying that (a) all security attributes are set
 * correctly on {@link ItemDao} methods and (b) the security subsystem is
 * properly activated.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/persistence.support.applicationLayer.scontext",
		"classpath:/META-INF/spring/persistence.item.test.platformLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.securityLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.aspectLayer.scontext",
		"classpath:/META-INF/spring/persistence.support.daoLayer.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		SpringBasedAuthenticationProvidingTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaItemDaoSecurityTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	@Resource
	private ItemDao classUnderTest;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.hibernate.domain.ContactDAOImpl#findByCityLike(java.lang.String)}
	 * .
	 */
	@TestUser(username = "guest", password = "guest")
	@Test
	public final void testFindByItemNumber_AcceptsGuest() {
		this.classUnderTest.findByItemNumber("SECURITY_TEST");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.hibernate.domain.ContactDAOImpl#findByCityLike(java.lang.String)}
	 * .
	 */
	@TestUser(username = "external", password = "external")
	@Test(expected = AccessDeniedException.class)
	public final void testFindByItemNumber_RejectsExternalUser() {
		this.classUnderTest.findByItemNumber("SECURITY_TEST");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.hibernate.domain.ContactDAOImpl#findByCityLike(java.lang.String)}
	 * .
	 */
	@TestUser(username = "guest", password = "guest")
	@Test
	public final void testFindByNameLike_AcceptsGuest() {
		this.classUnderTest.findByNameLike("SECURITY_TEST");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.item.internal.hibernate.domain.ContactDAOImpl#findByCityLike(java.lang.String)}
	 * .
	 */
	@TestUser(username = "external", password = "external")
	@Test(expected = AccessDeniedException.class)
	public final void testFindByNameLike_RejectsExternalUser() {
		this.classUnderTest.findByNameLike("SECURITY_TEST");
	}
}
