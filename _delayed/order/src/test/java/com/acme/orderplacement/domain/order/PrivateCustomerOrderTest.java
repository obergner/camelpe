/**
 * 
 */
package com.acme.orderplacement.domain.order;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link PrivateCustomerOrder <code>PrivateCustomerOrder</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PrivateCustomerOrderTest extends TestCase {

	/**
	 * @param name
	 */
	public PrivateCustomerOrderTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.PrivateCustomerOrder#setPrivateOrderer(com.acme.orderplacement.domain.people.customer.PrivateCustomer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPrivateOrderer_RejectsNullOrderer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final PrivateCustomerOrder classUnderTest = new PrivateCustomerOrder();
			classUnderTest.setPrivateOrderer(null);
			fail("setPrivateOrderer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.PrivateCustomerOrder#setPrivateOrderer(com.acme.orderplacement.domain.people.customer.PrivateCustomer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPrivateOrderer_RejectsAnOrdererDifferentFromTheCurrentOrderer()
			throws CollaborationPreconditionsNotMetException {
		final PrivateCustomer currentOrderer = new PrivateCustomer();
		currentOrderer.setCustomerNumber("CURRENT_ORDERER");

		final PrivateCustomerOrder classUnderTest = new PrivateCustomerOrder();
		classUnderTest.setPrivateOrderer(currentOrderer);

		final PrivateCustomer newOrderer = new PrivateCustomer();
		newOrderer.setCustomerNumber("NEW_ORDERER");
		try {
			classUnderTest.setPrivateOrderer(newOrderer);
			fail("setPrivateOrderer(" + newOrderer
					+ ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.PrivateCustomerOrder#addObtainedPayment(com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddObtainedPayment_RejectsNullPayment()
			throws CollaborationPreconditionsNotMetException {
		try {
			final PrivateCustomerOrder classUnderTest = new PrivateCustomerOrder();
			classUnderTest.addObtainedPayment(null);
			fail("addObtainedPayment(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.PrivateCustomerOrder#addObtainedPayment(com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddObtainedPayment_RejectsAnotherOrdersPayment()
			throws CollaborationPreconditionsNotMetException {
		final PrivateCustomerOrder anotherOrder = new PrivateCustomerOrder();
		anotherOrder.setOrderNumber("ANOTHER_ORDER");

		final PrivateCustomerPayment payment = new PrivateCustomerPayment();
		payment.setPaidOrder(anotherOrder);

		final PrivateCustomerOrder classUnderTest = new PrivateCustomerOrder();
		classUnderTest.setOrderNumber("TEST_ORDER");
		try {
			classUnderTest.addObtainedPayment(payment);
			fail("addObtainedPayment(" + payment + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}
}
