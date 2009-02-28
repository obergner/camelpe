/**
 * 
 */
package de.obergner.soa.order.domain.people.customer;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.customer.PrivateCustomer;

/**
 * <p>
 * A test for {@link PrivateCustomer <code>PrivateCustomer</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PrivateCustomerTest extends TestCase {

	/**
	 * @param name
	 */
	public PrivateCustomerTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#setCustomerNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCustomerNumber_RejectsNullCustomerNumber() {
		try {
			final PrivateCustomer classUnderTest = new PrivateCustomer();
			classUnderTest.setCustomerNumber(null);
			fail("setCustomerNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#setCustomerNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCustomerNumber_RejectsBlankCustomerNumber() {
		try {
			final PrivateCustomer classUnderTest = new PrivateCustomer();
			classUnderTest.setCustomerNumber("");
			fail("setCustomerNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#setShippingAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetShippingAddress_RejectsNullShippingAddress() {
		try {
			final PrivateCustomer classUnderTest = new PrivateCustomer();
			classUnderTest.setShippingAddress(null);
			fail("setShippingAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#setInvoiceAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetInvoiceAddress_RejectsNullInvoiceAddress() {
		try {
			final PrivateCustomer classUnderTest = new PrivateCustomer();
			classUnderTest.setInvoiceAddress(null);
			fail("setInvoiceAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#setParent(com.acme.orderplacement.domain.people.person.Person)}
	 * .
	 */
	public final void testSetParent_RejectsNullParent() {
		try {
			final PrivateCustomer classUnderTest = new PrivateCustomer();
			classUnderTest.setParent(null);
			fail("setParent(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPrivateCustomers() {
		final PrivateCustomer privateCustomerOne = new PrivateCustomer();
		privateCustomerOne.setCustomerNumber("12345");

		final PrivateCustomer privateCustomerTwo = new PrivateCustomer();
		privateCustomerTwo.setCustomerNumber("12345");

		assertTrue("equals() not implemented correctly", privateCustomerOne
				.equals(privateCustomerTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPrivateCustomers() {
		final PrivateCustomer privateCustomerOne = new PrivateCustomer();
		privateCustomerOne.setCustomerNumber("12345");

		final PrivateCustomer privateCustomerTwo = new PrivateCustomer();
		privateCustomerTwo.setCustomerNumber("123456");

		assertFalse("equals() not implemented correctly", privateCustomerOne
				.equals(privateCustomerTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.customer.PrivateCustomer#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPrivateCustomers() {
		final PrivateCustomer privateCustomerOne = new PrivateCustomer();
		privateCustomerOne.setCustomerNumber("12345");

		final PrivateCustomer privateCustomerTwo = new PrivateCustomer();
		privateCustomerTwo.setCustomerNumber("12345");

		assertEquals("hashCode() not implemented correctly", privateCustomerOne
				.hashCode(), privateCustomerTwo.hashCode());
	}

}
