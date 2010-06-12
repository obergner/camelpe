/**
 * 
 */
package com.acme.orderplacement.framework.common.auth;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * <p>
 * Test [@link CachingPrincipalAccessFactory}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CachingPrincipalAccessFactoryTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.common.auth.CachingPrincipalAccessFactory#getPrincipalAccess()}
	 * .
	 */
	@Test
	public final void assertThatGetPrincipalAccessReturnsANotNullPrincipalAccess() {
		final PrincipalAccess principalAccess = PrincipalAccess.FACTORY
				.getPrincipalAccess();

		assertNotNull(
				"PrincipalAccess.FACTORY.getPrincipalAccess() should have returned an implementation of "
						+ PrincipalAccess.class.getName()
						+ ", yet it returned null.", principalAccess);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.common.auth.CachingPrincipalAccessFactory#getPrincipalAccess()}
	 * .
	 */
	@Test
	public final void assertThatGetPrincipalAccessReturnsSamplePrincipalAccess() {
		final PrincipalAccess principalAccess = PrincipalAccess.FACTORY
				.getPrincipalAccess();

		assertTrue(
				"PrincipalAccess.FACTORY.getPrincipalAccess() should have returned an instance of "
						+ SamplePrincipalAccess.class.getName()
						+ ", yet it didn't.", SamplePrincipalAccess.class
						.isAssignableFrom(principalAccess.getClass()));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.common.auth.CachingPrincipalAccessFactory#getPrincipalAccess()}
	 * .
	 */
	@Test
	public final void assertThatGetPrincipalAccessAlwaysReturnsTheSameCachedInstance() {
		final PrincipalAccess principalAccess1 = PrincipalAccess.FACTORY
				.getPrincipalAccess();
		final PrincipalAccess principalAccess2 = PrincipalAccess.FACTORY
				.getPrincipalAccess();

		assertSame(
				"PrincipalAccess.FACTORY.getPrincipalAccess() should always return the same cached instance of "
						+ SamplePrincipalAccess.class.getName()
						+ ", yet it didn't.", principalAccess1,
				principalAccess2);
	}
}
