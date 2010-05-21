/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.order.CorporateClientOrder;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link CorporateClientPayment <code>CorporateClientPayment</code>}
 * .
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CorporateClientPaymentTest extends TestCase {

	/**
	 * @param name
	 */
	public CorporateClientPaymentTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.CorporateClientPayment#setCorporatePayer(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 */
	public final void testSetCorporatePayer_RejectsNullPayer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final CorporateClientPayment classUnderTest = new CorporateClientPayment();
			classUnderTest.setCorporatePayer(null);
			fail("setCorporatePayer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.CorporateClientPayment#setCorporatePayer(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 */
	public final void testSetCorporatePayer_RejectsPayerDifferentFromCurrentPayer()
			throws CollaborationPreconditionsNotMetException {
		final CorporateClient currentPayer = new CorporateClient();
		currentPayer.setCorporateClientNumber("CURRENT_PAYER");

		final CorporateClientPayment classUnderTest = new CorporateClientPayment();
		classUnderTest.setCorporatePayer(currentPayer);

		final CorporateClient newPayer = new CorporateClient();
		newPayer.setCorporateClientNumber("NEW_PAYER");
		try {
			classUnderTest.setCorporatePayer(newPayer);
			fail("setCorporatePayer(" + newPayer + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.CorporateClientPayment#setPaidOrder(com.acme.orderplacement.domain.order.CorporateClientOrder)}
	 * .
	 */
	public final void testSetPaidOrder_RejectsNullOrder()
			throws CollaborationPreconditionsNotMetException {
		try {
			final CorporateClientPayment classUnderTest = new CorporateClientPayment();
			classUnderTest.setPaidOrder(null);
			fail("setPaidOrder(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.CorporateClientPayment#setPaidOrder(com.acme.orderplacement.domain.order.CorporateClientOrder)}
	 * .
	 */
	public final void testSetPaidOrder_RejectsOrderDifferentFromCurrentOrder()
			throws CollaborationPreconditionsNotMetException {
		final CorporateClientOrder currentOrder = new CorporateClientOrder();
		currentOrder.setOrderNumber("CURRENT_ORDER");

		final CorporateClientPayment classUnderTest = new CorporateClientPayment();
		classUnderTest.setPaidOrder(currentOrder);

		final CorporateClientOrder newOrder = new CorporateClientOrder();
		newOrder.setOrderNumber("NEW_ORDER");
		try {
			classUnderTest.setPaidOrder(newOrder);
			fail("setPaidOrder(" + newOrder + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

}
