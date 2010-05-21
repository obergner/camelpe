/**
 * 
 */
package com.acme.orderplacement.domain.company.people;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.company.customer.CorporateClient;

/**
 * <p>
 * A test for {@link CorporateClientRepresentative
 * <code>CorporateClientRepresentative</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CorporateClientRepresentativeTest extends TestCase {

	/**
	 * @param name
	 */
	public CorporateClientRepresentativeTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setCorporateClientRepresentativeNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCorporateClientRepresentativeNumber_RejectsNullCorporateClientRepresentativeNumber() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setCorporateClientRepresentativeNumber(null);
			fail("setCorporateClientRepresentativeNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setCorporateClientRepresentativeNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCorporateClientRepresentativeNumber_RejectsBlankCorporateClientRepresentativeNumber() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setCorporateClientRepresentativeNumber("");
			fail("setCorporateClientRepresentativeNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setPosition(com.acme.orderplacement.domain.company.people.Position)}
	 * .
	 */
	public final void testSetPosition_RejectsNullPosition() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setPosition(null);
			fail("setPosition(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setWorkAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetWorkAddress_RejectsNullWorkAddress() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setWorkAddress(null);
			fail("setWorkAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setWorkEmailAddress(com.acme.orderplacement.domain.people.contact.EmailAddress)}
	 * .
	 */
	public final void testSetWorkEmailAddress_RejectsNullWorkEmailAddress() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setWorkEmailAddress(null);
			fail("setWorkEmailAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setWorkPhoneNumber(com.acme.orderplacement.domain.people.contact.PhoneNumber)}
	 * .
	 */
	public final void testSetWorkPhoneNumber_RejectsNullWorkPhoneNumber() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setWorkPhoneNumber(null);
			fail("setWorkPhoneNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setParentPerson(com.acme.orderplacement.domain.people.person.Person)}
	 * .
	 */
	public final void testSetParentPerson_RejectsNullParentPerson() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setParentPerson(null);
			fail("setParentPerson(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#setRepresentedCorporateClient(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 */
	public final void testSetRepresentedCorporateClient_RejectsNullCorporateClient() {
		try {
			final CorporateClientRepresentative classUnderTest = new CorporateClientRepresentative();
			classUnderTest.setRepresentedCorporateClient(null);
			fail("setRepresentedCorporateClient(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualCorporateClientRepresentative() {
		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("COCL-11-111-11");

		final CorporateClientRepresentative corporateClientRepresentativeOne = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeOne
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeOne
				.setRepresentedCorporateClient(corporateClientOne);

		final CorporateClientRepresentative corporateClientRepresentativeTwo = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeTwo
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeTwo
				.setRepresentedCorporateClient(corporateClientOne);

		assertTrue("equals() not implemented correctly",
				corporateClientRepresentativeOne
						.equals(corporateClientRepresentativeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalCorporateClientRepresentative() {
		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("COCL-11-111-11");

		final CorporateClient corporateClientTwo = new CorporateClient(2L, 10);
		corporateClientOne.setCorporateClientNumber("COCL-11-111-22");

		final CorporateClientRepresentative corporateClientRepresentativeOne = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeOne
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeOne
				.setRepresentedCorporateClient(corporateClientOne);

		final CorporateClientRepresentative corporateClientRepresentativeTwo = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeTwo
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeTwo
				.setRepresentedCorporateClient(corporateClientTwo);

		assertFalse("equals() not implemented correctly",
				corporateClientRepresentativeOne
						.equals(corporateClientRepresentativeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.people.CorporateClientRepresentative#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodeForEqualCorporateClientRepresentatives() {
		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("COCL-11-111-11");

		final CorporateClientRepresentative corporateClientRepresentativeOne = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeOne
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeOne
				.setRepresentedCorporateClient(corporateClientOne);

		final CorporateClientRepresentative corporateClientRepresentativeTwo = new CorporateClientRepresentative(
				1L, 5);
		corporateClientRepresentativeTwo
				.setCorporateClientRepresentativeNumber("COCREP-22-222-22");
		corporateClientRepresentativeTwo
				.setRepresentedCorporateClient(corporateClientOne);

		assertEquals("hashCode() not implemented correctly",
				corporateClientRepresentativeOne.hashCode(),
				corporateClientRepresentativeTwo.hashCode());
	}
}
