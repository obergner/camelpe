/**
 * 
 */
package de.obergner.soa.order.domain.people.contact;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.contact.PostalAddress;

/**
 * <p>
 * A test for {@link PostalAddress <code>PostalAddress</code>}.
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PostalAddressTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PostalAddressTest extends TestCase {

	/**
	 * @param name
	 */
	public PostalAddressTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setStreet(java.lang.String)}
	 * .
	 */
	public final void testSetStreet_RejectsNullStreet() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setStreet(null);
			fail("setStreet(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setStreet(java.lang.String)}
	 * .
	 */
	public final void testSetStreet_RejectsBlankStreet() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setStreet("");
			fail("setStreet(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setStreetNumber(java.lang.String)}
	 * .
	 */
	public final void testSetStreetNumber_RejectsNullStreetNumber() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setStreetNumber(null);
			fail("setStreetNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setStreetNumber(java.lang.String)}
	 * .
	 */
	public final void testSetStreetNumber_RejectsBlankStreetNumber() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setStreetNumber("");
			fail("setStreetNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setPostalCode(java.lang.String)}
	 * .
	 */
	public final void testSetPostalCode_RejectsNullPostalCode() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setPostalCode(null);
			fail("setPostalCode(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setPostalCode(java.lang.String)}
	 * .
	 */
	public final void testSetPostalCode_RejectsBlankPostalCode() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setPostalCode("");
			fail("setPostalCode(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setCity(java.lang.String)}
	 * .
	 */
	public final void testSetCity_RejectsNullCity() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setCity(null);
			fail("setCity(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#setCity(java.lang.String)}
	 * .
	 */
	public final void testSetCity_RejectsBlankCity() {
		try {
			final PostalAddress classUnderTest = new PostalAddress();
			classUnderTest.setCity("");
			fail("setCity(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPostalAddresses() {
		final PostalAddress postalAddressOne = new PostalAddress();
		postalAddressOne.setStreet("IDENTICAL_STREET");
		postalAddressOne.setStreetNumber("111");
		postalAddressOne.setPostalCode("12345");
		postalAddressOne.setCity("IDENTICAL_CITY");

		final PostalAddress postalAddressTwo = new PostalAddress();
		postalAddressTwo.setStreet("IDENTICAL_STREET");
		postalAddressTwo.setStreetNumber("111");
		postalAddressTwo.setPostalCode("12345");
		postalAddressTwo.setCity("IDENTICAL_CITY");

		assertTrue("equals() not implemented correctly", postalAddressOne
				.equals(postalAddressTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPostalAddresses() {
		final PostalAddress postalAddressOne = new PostalAddress();
		postalAddressOne.setStreet("IDENTICAL_STREET");
		postalAddressOne.setStreetNumber("111");
		postalAddressOne.setPostalCode("12345");
		postalAddressOne.setCity("IDENTICAL_CITY");

		final PostalAddress postalAddressTwo = new PostalAddress();
		postalAddressTwo.setStreet("IDENTICAL_STREET");
		postalAddressTwo.setStreetNumber("111");
		postalAddressTwo.setPostalCode("12345");
		postalAddressTwo.setCity("CITY1");

		assertFalse("equals() not implemented correctly", postalAddressOne
				.equals(postalAddressTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PostalAddress#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPostalAddresses() {
		final PostalAddress postalAddressOne = new PostalAddress();
		postalAddressOne.setStreet("IDENTICAL_STREET");
		postalAddressOne.setStreetNumber("111");
		postalAddressOne.setPostalCode("12345");
		postalAddressOne.setCity("IDENTICAL_CITY");

		final PostalAddress postalAddressTwo = new PostalAddress();
		postalAddressTwo.setStreet("IDENTICAL_STREET");
		postalAddressTwo.setStreetNumber("111");
		postalAddressTwo.setPostalCode("12345");
		postalAddressTwo.setCity("IDENTICAL_CITY");

		assertEquals("hashCode() not implemented correctly", postalAddressOne
				.hashCode(), postalAddressTwo.hashCode());
	}
}
