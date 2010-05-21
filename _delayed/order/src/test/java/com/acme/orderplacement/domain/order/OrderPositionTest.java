/**
 * 
 */
package com.acme.orderplacement.domain.order;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.item.offer.Offer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link OrderPosition <code>OrderPosition</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class OrderPositionTest extends TestCase {

	/**
	 * @param name
	 */
	public OrderPositionTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#setParentOrder(com.acme.orderplacement.domain.order.Order)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetParentOrder_RejectsNullParentOrder()
			throws CollaborationPreconditionsNotMetException {
		try {
			final OrderPosition classUnderTest = new OrderPosition();
			classUnderTest.setParentOrder(null);
			fail("setParentOrder(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#setParentOrder(com.acme.orderplacement.domain.order.Order)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetParentOrder_RejectsAnOrderDifferentFromTheCurrentParentOrder()
			throws CollaborationPreconditionsNotMetException {
		final Order currentParentOrder = createConcreteOrder();
		currentParentOrder.setOrderNumber("CURRENT_ORDER");
		final OrderPosition classUnderTest = new OrderPosition();
		classUnderTest.setParentOrder(currentParentOrder);

		final Order newOrder = createConcreteOrder();
		newOrder.setOrderNumber("NEW_ORDER");
		try {
			classUnderTest.setParentOrder(newOrder);
			fail("setParentOrder(" + newOrder + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#setOrderedOffer(com.acme.orderplacement.domain.item.offer.Offer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetOrderedOffer_RejectsNullOffer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final OrderPosition classUnderTest = new OrderPosition();
			classUnderTest.setOrderedOffer(null);
			fail("setOrderedOffer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#setOrderedOffer(com.acme.orderplacement.domain.item.offer.Offer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetOrderedOffer_RejectsAnOfferDifferentFromTheCurrentOffer()
			throws CollaborationPreconditionsNotMetException {
		final Offer currentOffer = new Offer();
		currentOffer.setName("CURRENT_OFFER");
		final OrderPosition classUnderTest = new OrderPosition();
		classUnderTest.setOrderedOffer(currentOffer);

		final Offer newOffer = new Offer();
		newOffer.setName("NEW_OFFER");
		try {
			classUnderTest.setOrderedOffer(newOffer);
			fail("setOrderedOffer(" + newOffer + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#compareTo(com.acme.orderplacement.domain.order.OrderPosition)}
	 * .
	 */
	public final void testCompareTo() {
		final OrderPosition orderPositionOne = new OrderPosition();
		orderPositionOne.setPosition(1);

		final OrderPosition orderPositionTwo = new OrderPosition();
		orderPositionTwo.setPosition(2);

		assertTrue("compareTo() not implemented correctly", orderPositionOne
				.compareTo(orderPositionTwo) < 0);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEquals_RecognizesEqualOrderPositions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Order commonParentOrder = createConcreteOrder();
		commonParentOrder.setOrderNumber("COMMON_ORDER");

		final OrderPosition orderPositionOne = new OrderPosition();
		orderPositionOne.setParentOrder(commonParentOrder);

		final OrderPosition orderPositionTwo = new OrderPosition();
		orderPositionTwo.setParentOrder(commonParentOrder);

		assertTrue("equals() not implemented correctly", orderPositionOne
				.equals(orderPositionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEquals_RecognizesUnequalOrderPositions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Order parentOrderOne = createConcreteOrder();
		parentOrderOne.setOrderNumber("ORDER_1");

		final OrderPosition orderPositionOne = new OrderPosition();
		orderPositionOne.setParentOrder(parentOrderOne);

		final Order parentOrderTwo = createConcreteOrder();
		parentOrderTwo.setOrderNumber("ORDER_2");

		final OrderPosition orderPositionTwo = new OrderPosition();
		orderPositionTwo.setParentOrder(parentOrderTwo);

		assertFalse("equals() not implemented correctly", orderPositionOne
				.equals(orderPositionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.order.OrderPosition#hashCode()}.
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualOrderPositions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Order commonParentOrder = createConcreteOrder();
		commonParentOrder.setOrderNumber("COMMON_ORDER");

		final OrderPosition orderPositionOne = new OrderPosition();
		orderPositionOne.setParentOrder(commonParentOrder);

		final OrderPosition orderPositionTwo = new OrderPosition();
		orderPositionTwo.setParentOrder(commonParentOrder);

		assertEquals("hashCode() not implemented correctly", orderPositionOne
				.hashCode(), orderPositionTwo.hashCode());
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
