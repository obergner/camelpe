/**
 * 
 */
package com.acme.orderplacement.domain.order;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.order.payment.CorporateClientPayment;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link CorporateClientOrder <code>CorporateClientOrder</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CorporateClientOrderTest extends TestCase {

	/**
	 * @param name
	 */
	public CorporateClientOrderTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.CorporateClientOrder#setCorporateOrderer(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 */
	public final void testSetCorporateOrderer_RejectsNullOrderer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final CorporateClientOrder classUnderTest = new CorporateClientOrder();
			classUnderTest.setCorporateOrderer(null);
			fail("setCorporateOrderer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.CorporateClientOrder#setCorporateOrderer(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 */
	public final void testSetCorporateOrderer_RejectsAnOrdererDifferentFromTheCurrentOrderer()
			throws CollaborationPreconditionsNotMetException {
		final CorporateClient currentOrderer = new CorporateClient();
		currentOrderer.setCorporateClientNumber("CURRENT_ORDERER");

		final CorporateClientOrder classUnderTest = new CorporateClientOrder();
		classUnderTest.setCorporateOrderer(currentOrderer);

		final CorporateClient newOrderer = new CorporateClient();
		newOrderer.setCorporateClientNumber("NEW_ORDERER");
		try {
			classUnderTest.setCorporateOrderer(newOrderer);
			fail("setCorporateOrderer(" + newOrderer
					+ ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.CorporateClientOrder#setContactPerson(com.acme.orderplacement.domain.company.people.CorporateClientRepresentative)}
	 * .
	 */
	public final void testSetContactPerson_RejectsNullContactPerson() {
		try {
			final CorporateClientOrder classUnderTest = new CorporateClientOrder();
			classUnderTest.setContactPerson(null);
			fail("setContactPerson(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.CorporateClientOrder#addObtainedPayment(com.acme.orderplacement.domain.order.payment.CorporateClientPayment)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddObtainedPayment_RejectsNullPayment()
			throws CollaborationPreconditionsNotMetException {
		try {
			final CorporateClientOrder classUnderTest = new CorporateClientOrder();
			classUnderTest.addObtainedPayment(null);
			fail("addObtainedPayment(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.CorporateClientOrder#addObtainedPayment(com.acme.orderplacement.domain.order.payment.CorporateClientPayment)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddObtainedPayment_RejectsAnotherOrdersPayment()
			throws CollaborationPreconditionsNotMetException {
		final CorporateClientOrder anotherOrder = new CorporateClientOrder();
		anotherOrder.setOrderNumber("ANOTHER_ORDER");

		final CorporateClientPayment payment = new CorporateClientPayment();
		payment.setPaidOrder(anotherOrder);

		final CorporateClientOrder classUnderTest = new CorporateClientOrder();
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
