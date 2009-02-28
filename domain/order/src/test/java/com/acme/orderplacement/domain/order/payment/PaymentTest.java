/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.support.money.MonetaryAmount;

/**
 * <p>
 * A test for {@link Payment <code>Payment</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PaymentTest extends TestCase {

	/**
	 * @param name
	 */
	public PaymentTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#setPaidAmount(com.acme.orderplacement.domain.common.money.MonetaryAmount)}
	 * .
	 */
	public final void testSetPaidAmount_RejectsNullAmount() {
		try {
			final Payment classUnderTest = createConcretePayment();
			classUnderTest.setPaidAmount(null);
			fail("setPaidAmount(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#setObtainedOn(java.util.Date)}
	 * .
	 */
	public final void testSetObtainedOn_RejectsNullDate() {
		try {
			final Payment classUnderTest = createConcretePayment();
			classUnderTest.setObtainedOn(null);
			fail("setObtainedOn(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#setBookedOn(java.util.Date)}
	 * .
	 */
	public final void testSetBookedOn_RejectsNullDate() {
		try {
			final Payment classUnderTest = createConcretePayment();
			classUnderTest.setBookedOn(null);
			fail("setBookedOn(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#setPaymentType(com.acme.orderplacement.domain.order.payment.PaymentType)}
	 * .
	 */
	public final void testSetPaymentType_RejectsNullPaymentType() {
		try {
			final Payment classUnderTest = createConcretePayment();
			classUnderTest.setPaymentType(null);
			fail("setPaymentType(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#compareTo(com.acme.orderplacement.domain.order.payment.Payment)}
	 * .
	 */
	public final void testCompareTo() {
		final MonetaryAmount lesserAmount = new MonetaryAmount(new BigDecimal(
				"1.0"), Currency.getInstance("EUR"));
		final Payment lesserPayment = createConcretePayment();
		lesserPayment.setPaidAmount(lesserAmount);

		final MonetaryAmount higherAmount = new MonetaryAmount(new BigDecimal(
				"2.0"), Currency.getInstance("EUR"));
		final Payment higherPayment = createConcretePayment();
		higherPayment.setPaidAmount(higherAmount);

		assertTrue("compareTo() not implemented correctly", lesserPayment
				.compareTo(higherPayment) < 0);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPayments() {
		final PaymentType commonPaymentType = new PaymentType();
		commonPaymentType.setName("COMMON_PAYMENT_TYPE");

		final MonetaryAmount commonAmount = new MonetaryAmount(new BigDecimal(
				"1.0"), Currency.getInstance("EUR"));

		final Date commonObtainedOnDate = new Date();

		final Payment paymentOne = createConcretePayment();
		paymentOne.setPaymentType(commonPaymentType);
		paymentOne.setPaidAmount(commonAmount);
		paymentOne.setObtainedOn(commonObtainedOnDate);

		final Payment paymentTwo = createConcretePayment();
		paymentTwo.setPaymentType(commonPaymentType);
		paymentTwo.setPaidAmount(commonAmount);
		paymentTwo.setObtainedOn(commonObtainedOnDate);

		assertTrue("equals() not implemented correctly", paymentOne
				.equals(paymentTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPayments() {
		final PaymentType paymentTypeOne = new PaymentType();
		paymentTypeOne.setName("PAYMENT_TYPE_1");

		final MonetaryAmount amountOne = new MonetaryAmount(new BigDecimal(
				"1.0"), Currency.getInstance("EUR"));

		final Date obtainedOnDateOne = new Date();

		final Payment paymentOne = createConcretePayment();
		paymentOne.setPaymentType(paymentTypeOne);
		paymentOne.setPaidAmount(amountOne);
		paymentOne.setObtainedOn(obtainedOnDateOne);

		final PaymentType paymentTypeTwo = new PaymentType();
		paymentTypeTwo.setName("PAYMENT_TYPE_2");

		final MonetaryAmount amountTwo = new MonetaryAmount(new BigDecimal(
				"2.0"), Currency.getInstance("EUR"));

		final Date obtainedOnDateTwo = new Date();

		final Payment paymentTwo = createConcretePayment();
		paymentTwo.setPaymentType(paymentTypeTwo);
		paymentTwo.setPaidAmount(amountTwo);
		paymentTwo.setObtainedOn(obtainedOnDateTwo);

		assertFalse("equals() not implemented correctly", paymentOne
				.equals(paymentTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.Payment#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPayments() {
		final PaymentType commonPaymentType = new PaymentType();
		commonPaymentType.setName("COMMON_PAYMENT_TYPE");

		final MonetaryAmount commonAmount = new MonetaryAmount(new BigDecimal(
				"1.0"), Currency.getInstance("EUR"));

		final Date commonObtainedOnDate = new Date();

		final Payment paymentOne = createConcretePayment();
		paymentOne.setPaymentType(commonPaymentType);
		paymentOne.setPaidAmount(commonAmount);
		paymentOne.setObtainedOn(commonObtainedOnDate);

		final Payment paymentTwo = createConcretePayment();
		paymentTwo.setPaymentType(commonPaymentType);
		paymentTwo.setPaidAmount(commonAmount);
		paymentTwo.setObtainedOn(commonObtainedOnDate);

		assertEquals("hashCode() not implemented correctly", paymentOne
				.hashCode(), paymentTwo.hashCode());
	}

	@SuppressWarnings("serial")
	private final Payment createConcretePayment() {

		return new Payment() {
		};
	}
}
