/**
 * 
 */
package com.acme.orderplacement.domain.order;

import java.util.Date;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Order}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class OrderTest extends TestCase {

	/**
	 * @param name
	 */
	public OrderTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#setOrderedOn(java.util.Date)}
	 * .
	 */
	public final void testSetOrderedOn_RejectsNullDate() {
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.setOrderedOn(null);
			fail("setOrderedOn(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#setFulfilledOn(java.util.Date)}
	 * .
	 */
	public final void testSetFulfilledOn_RejectsNullDate() {
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.setFulfilledOn(null);
			fail("setFulfilledOn(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#setShipToAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetShipToAddress_RejectsNullAddress() {
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.setShipToAddress(null);
			fail("setShipToAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#setBillToAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetBillToAddress_RejectsNullAddress() {
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.setBillToAddress(null);
			fail("setBillToAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#addOrderPosition(com.acme.orderplacement.domain.order.OrderPosition)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddOrderPosition_RejectsNullOrderPosition()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.addOrderPosition(null);
			fail("addOrderPosition(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#addOrderPosition(com.acme.orderplacement.domain.order.OrderPosition)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddOrderPosition_RejectsOrderPositionBelongingToAnotherOrder()
			throws CollaborationPreconditionsNotMetException {
		final Order anotherOrder = createConcreteOrder();
		anotherOrder.setOrderNumber("ORD-ANOTHER");
		final OrderPosition orderPos = new OrderPosition();
		orderPos.setParentOrder(anotherOrder);
		try {
			final Order classUnderTest = createConcreteOrder();
			classUnderTest.addOrderPosition(orderPos);
			fail("addOrderPosition(" + orderPos + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesEqualOrders() {
		final String commonOrderNumber = "ORD-TEST-001";
		final Date commonOrderedOnDate = new Date();

		final Order orderOne = createConcreteOrder();
		orderOne.setOrderedOn(commonOrderedOnDate);
		orderOne.setOrderNumber(commonOrderNumber);

		final Order orderTwo = createConcreteOrder();
		orderTwo.setOrderedOn(commonOrderedOnDate);
		orderTwo.setOrderNumber(commonOrderNumber);

		assertTrue("equals() not implemented correctly", orderOne
				.equals(orderTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesUnequalOrders()
			throws CollaborationPreconditionsNotMetException {
		final Date commonOrderedOnDate = new Date();

		final Order orderOne = createConcreteOrder();
		orderOne.setOrderedOn(commonOrderedOnDate);
		orderOne.setOrderNumber("ORD-TEST-001");

		final Order orderTwo = createConcreteOrder();
		orderTwo.setOrderedOn(commonOrderedOnDate);
		orderTwo.setOrderNumber("ORD-TEST-002");

		assertFalse("equals() not implemented correctly", orderOne
				.equals(orderTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.Order#hashCode()}.
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualOrders()
			throws CollaborationPreconditionsNotMetException {
		final String commonOrderNumber = "ORD-TEST-001";
		final Date commonOrderedOnDate = new Date();

		final Order orderOne = createConcreteOrder();
		orderOne.setOrderedOn(commonOrderedOnDate);
		orderOne.setOrderNumber(commonOrderNumber);

		final Order orderTwo = createConcreteOrder();
		orderTwo.setOrderedOn(commonOrderedOnDate);
		orderTwo.setOrderNumber(commonOrderNumber);

		assertEquals("hashCode() not implemented correctly", orderOne
				.hashCode(), orderTwo.hashCode());
	}

	/**
	 * @return
	 */
	@SuppressWarnings("serial")
	private Order createConcreteOrder() {

		return new Order() {
		};
	}
}
