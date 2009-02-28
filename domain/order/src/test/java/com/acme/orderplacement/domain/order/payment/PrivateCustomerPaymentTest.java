/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.order.PrivateCustomerOrder;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link PrivateCustomerPayment <code>PrivateCustomerPayment</code>}
 * .
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PrivateCustomerPaymentTest extends TestCase {

	/**
	 * @param name
	 */
	public PrivateCustomerPaymentTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment#setPrivatePayer(com.acme.orderplacement.domain.people.customer.PrivateCustomer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPrivatePayer_RejectsNullPayer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final PrivateCustomerPayment classUnderTest = new PrivateCustomerPayment();
			classUnderTest.setPrivatePayer(null);
			fail("setPrivatePayer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment#setPrivatePayer(com.acme.orderplacement.domain.people.customer.PrivateCustomer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPrivatePayer_RejectsPayerDifferentFromCurrentPayer()
			throws CollaborationPreconditionsNotMetException {
		final PrivateCustomer currentPayer = new PrivateCustomer();
		currentPayer.setCustomerNumber("CURRENT_PAYER");

		final PrivateCustomerPayment classUnderTest = new PrivateCustomerPayment();
		classUnderTest.setPrivatePayer(currentPayer);

		final PrivateCustomer newPayer = new PrivateCustomer();
		newPayer.setCustomerNumber("NEW_PAYER");
		try {
			classUnderTest.setPrivatePayer(newPayer);
			fail("setPrivatePayer(" + newPayer + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment#setPaidOrder(com.acme.orderplacement.domain.order.PrivateCustomerOrder)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPaidOrder_RejectsNullOrder()
			throws CollaborationPreconditionsNotMetException {
		try {
			final PrivateCustomerPayment classUnderTest = new PrivateCustomerPayment();
			classUnderTest.setPaidOrder(null);
			fail("setPaidOrder(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment#setPaidOrder(com.acme.orderplacement.domain.order.PrivateCustomerOrder)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPaidOrder_RejectsOrderDifferentFromCurrentOrder()
			throws CollaborationPreconditionsNotMetException {
		final PrivateCustomerOrder currentOrder = new PrivateCustomerOrder();
		currentOrder.setOrderNumber("CURRENT_ORDER");

		final PrivateCustomerPayment classUnderTest = new PrivateCustomerPayment();
		classUnderTest.setPaidOrder(currentOrder);

		final PrivateCustomerOrder newOrder = new PrivateCustomerOrder();
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
