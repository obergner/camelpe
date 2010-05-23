/**
 * 
 */
package com.acme.orderplacement.domain.item.promotion;

import java.util.Date;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.item.offer.Offer;
import com.acme.orderplacement.framework.domain.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Promotion <code>Promotion</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PromotionTest extends TestCase {

	/**
	 * @param name
	 */
	public PromotionTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejecstNullName() {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setName(null);
			fail("setName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejecstBlankName() {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setName("");
			fail("setName('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setStartDate(java.util.Date)}
	 * .
	 */
	public final void testSetStartDate_RejectsNullStartDate() {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setStartDate(null);
			fail("setStartDate(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setPromotionalText(java.lang.String)}
	 * .
	 */
	public final void testSetPromotionalText_RejectsNullPromotionalText() {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setPromotionalText(null);
			fail("setPromotionalText(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setPromotionalText(java.lang.String)}
	 * .
	 */
	public final void testSetPromotionalText_RejectsBlankPromotionalText() {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setPromotionalText("");
			fail("setPromotionalText('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setPromotedOffer(com.acme.orderplacement.domain.item.offer.Offer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPromotedOffer_RejectsOfferWhenAlreayPromotingAnotherOffer()
			throws CollaborationPreconditionsNotMetException {
		final Offer anotherOffer = new Offer();
		anotherOffer.setName("ANOTHER_OFFER");
		anotherOffer.setStartDate(new Date());

		final Promotion classUnderTest = new Promotion();
		classUnderTest.setPromotedOffer(anotherOffer);

		try {
			final Offer newOffer = new Offer();
			newOffer.setName("NEW_OFFER");
			newOffer.setStartDate(new Date());

			classUnderTest.setPromotedOffer(newOffer);
			fail("setPromotedOffer(" + newOffer + ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#setPromotedOffer(com.acme.orderplacement.domain.item.offer.Offer)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetPromotedOffer_RejectsNullOffer()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Promotion classUnderTest = new Promotion();
			classUnderTest.setPromotedOffer(null);
			fail("setPromotedOffer(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesEqualPromotions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Offer commonOffer = new Offer();
		commonOffer.setName("COMMON_ITEM");

		final Date commonStartDate = new Date();

		final Promotion promotionOne = new Promotion();
		promotionOne.setName("IDENTICAL_NAME");
		promotionOne.setStartDate(commonStartDate);
		promotionOne.setPromotedOffer(commonOffer);

		final Promotion promotionTwo = new Promotion();
		promotionTwo.setName("IDENTICAL_NAME");
		promotionTwo.setStartDate(commonStartDate);
		promotionTwo.setPromotedOffer(commonOffer);

		assertTrue("equals() not implemented correctly", promotionOne
				.equals(promotionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesUnequalPromotions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Offer commonOffer = new Offer();
		commonOffer.setName("COMMON_ITEM");

		final Date commonStartDate = new Date();

		final Promotion promotionOne = new Promotion();
		promotionOne.setName("NAME_1");
		promotionOne.setStartDate(commonStartDate);
		promotionOne.setPromotedOffer(commonOffer);

		final Promotion promotionTwo = new Promotion();
		promotionTwo.setName("NAME_2");
		promotionTwo.setStartDate(commonStartDate);
		promotionTwo.setPromotedOffer(commonOffer);

		assertFalse("equals() not implemented correctly", promotionOne
				.equals(promotionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.promotion.Promotion#hashCode()}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPromotions()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Offer commonOffer = new Offer();
		commonOffer.setName("COMMON_ITEM");

		final Date commonStartDate = new Date();

		final Promotion promotionOne = new Promotion();
		promotionOne.setName("IDENTICAL_NAME");
		promotionOne.setStartDate(commonStartDate);
		promotionOne.setPromotedOffer(commonOffer);

		final Promotion promotionTwo = new Promotion();
		promotionTwo.setName("IDENTICAL_NAME");
		promotionTwo.setStartDate(commonStartDate);
		promotionTwo.setPromotedOffer(commonOffer);

		assertEquals("hashCode() not implemented correctly", promotionOne
				.hashCode(), promotionTwo.hashCode());
	}

}
