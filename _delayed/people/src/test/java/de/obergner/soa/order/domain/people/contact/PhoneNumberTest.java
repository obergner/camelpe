/**
 * 
 */
package de.obergner.soa.order.domain.people.contact;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.contact.PhoneNumber;

/**
 * <p>
 * A test for {@link PhoneNumber <code>PhoneNumber</code>}.
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PhoneNumberTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PhoneNumberTest extends TestCase {

	/**
	 * @param name
	 */
	public PhoneNumberTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#setType(com.acme.orderplacement.domain.people.contact.PhoneNumber.Type)}
	 * .
	 */
	public final void testSetType_RejectsNullType() {
		try {
			final PhoneNumber classUnderTest = new PhoneNumber();
			classUnderTest.setType(null);
			fail("setType(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#setPhoneNumber(java.lang.String)}
	 * .
	 */
	public final void testSetPhoneNumber_RejectsNullPhoneNumber() {
		try {
			final PhoneNumber classUnderTest = new PhoneNumber();
			classUnderTest.setPhoneNumber(null);
			fail("setPhoneNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#setPhoneNumber(java.lang.String)}
	 * .
	 */
	public final void testSetPhoneNumber_RejectsBlankPhoneNumber() {
		try {
			final PhoneNumber classUnderTest = new PhoneNumber();
			classUnderTest.setPhoneNumber("");
			fail("setPhoneNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPhoneNumbers() {
		final PhoneNumber phoneNumberOne = new PhoneNumber();
		phoneNumberOne.setPhoneNumber("12345");

		final PhoneNumber phoneNumberTwo = new PhoneNumber();
		phoneNumberTwo.setPhoneNumber("12345");

		assertTrue("equals() not implemented correctly", phoneNumberOne
				.equals(phoneNumberTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPhoneNumbers() {
		final PhoneNumber phoneNumberOne = new PhoneNumber();
		phoneNumberOne.setPhoneNumber("12345");

		final PhoneNumber phoneNumberTwo = new PhoneNumber();
		phoneNumberTwo.setPhoneNumber("23456");

		assertFalse("equals() not implemented correctly", phoneNumberOne
				.equals(phoneNumberTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.PhoneNumber#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPhoneNumbers() {
		final PhoneNumber phoneNumberOne = new PhoneNumber();
		phoneNumberOne.setPhoneNumber("12345");

		final PhoneNumber phoneNumberTwo = new PhoneNumber();
		phoneNumberTwo.setPhoneNumber("12345");

		assertEquals("hashCode() not implemented correctly", phoneNumberOne
				.hashCode(), phoneNumberTwo.hashCode());
	}

}
