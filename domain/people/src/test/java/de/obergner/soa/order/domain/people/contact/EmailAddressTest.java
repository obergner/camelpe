/**
 * 
 */
package de.obergner.soa.order.domain.people.contact;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.contact.EmailAddress;

/**
 * <p>
 * A test for {@link EmailAddress <code>EmailAddress</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EmailAddressTest extends TestCase {

	/**
	 * @param name
	 */
	public EmailAddressTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.EmailAddress#setAddress(java.lang.String)}
	 * .
	 */
	public final void testSetAddress_RejectsNullAddress() {
		try {
			final EmailAddress classUnderTest = new EmailAddress();
			classUnderTest.setAddress(null);
			fail("setAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.EmailAddress#setAddress(java.lang.String)}
	 * .
	 */
	public final void testSetAddress_RejectsBlankAddress() {
		try {
			final EmailAddress classUnderTest = new EmailAddress();
			classUnderTest.setAddress("");
			fail("setAddress(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.EmailAddress#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualEmailAddresses() {
		final EmailAddress emailAddressOne = new EmailAddress();
		emailAddressOne.setAddress("IDENTICAL_ADDRESS");

		final EmailAddress emailAddressTwo = new EmailAddress();
		emailAddressTwo.setAddress("IDENTICAL_ADDRESS");

		assertTrue("equals() not implemented correctly", emailAddressOne
				.equals(emailAddressTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.EmailAddress#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalEmailAddresses() {
		final EmailAddress emailAddressOne = new EmailAddress();
		emailAddressOne.setAddress("ADDRESS1");

		final EmailAddress emailAddressTwo = new EmailAddress();
		emailAddressTwo.setAddress("ADDRESS2");

		assertFalse("equals() not implemented correctly", emailAddressOne
				.equals(emailAddressTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.contact.EmailAddress#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualEmailAddresses() {
		final EmailAddress emailAddressOne = new EmailAddress();
		emailAddressOne.setAddress("IDENTICAL_ADDRESS");

		final EmailAddress emailAddressTwo = new EmailAddress();
		emailAddressTwo.setAddress("IDENTICAL_ADDRESS");

		assertEquals("hashCode() not implemented correctly", emailAddressOne
				.hashCode(), emailAddressTwo.hashCode());
	}

}
