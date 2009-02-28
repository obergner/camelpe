/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import junit.framework.TestCase;

/**
 * <p>
 * A test for {@link PaymentType <code>PaymentType</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PaymentTypeTest extends TestCase {

	/**
	 * @param name
	 */
	public PaymentTypeTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsNullName() {
		try {
			final PaymentType classUnderTest = new PaymentType();
			classUnderTest.setName(null);
			fail("setName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#setDescription(java.lang.String)}
	 * .
	 */
	public final void testSetDescription_RejectsNullDescription() {
		try {
			final PaymentType classUnderTest = new PaymentType();
			classUnderTest.setDescription(null);
			fail("setDescription(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#compareTo(com.acme.orderplacement.domain.order.payment.PaymentType)}
	 * .
	 */
	public final void testCompareTo() {
		final String lesserPaymentTypeName = "A";
		final PaymentType lesserPaymentType = new PaymentType();
		lesserPaymentType.setName(lesserPaymentTypeName);

		final String higherPaymentTypeName = "B";
		final PaymentType higherPaymentType = new PaymentType();
		higherPaymentType.setName(higherPaymentTypeName);

		assertTrue("compareTo() not implemented correctly", lesserPaymentType
				.compareTo(higherPaymentType) < 0);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPaymentTypes() {
		final PaymentType paymentTypeOne = new PaymentType();
		paymentTypeOne.setName("COMMON");

		final PaymentType paymentTypeTwo = new PaymentType();
		paymentTypeTwo.setName("COMMON");

		assertTrue("equals() not implemented correctly", paymentTypeOne
				.equals(paymentTypeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPaymentTypes() {
		final PaymentType paymentTypeOne = new PaymentType();
		paymentTypeOne.setName("PAYMENT_TYPE_1");

		final PaymentType paymentTypeTwo = new PaymentType();
		paymentTypeTwo.setName("PAYMENT_TYPE_2");

		assertFalse("equals() not implemented correctly", paymentTypeOne
				.equals(paymentTypeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PaymentType#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPaymentTypes() {
		final PaymentType paymentTypeOne = new PaymentType();
		paymentTypeOne.setName("COMMON");

		final PaymentType paymentTypeTwo = new PaymentType();
		paymentTypeTwo.setName("COMMON");

		assertEquals("hashCode() not implemented correctly", paymentTypeOne
				.hashCode(), paymentTypeTwo.hashCode());
	}

}
