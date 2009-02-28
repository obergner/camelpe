/**
 * 
 */
package com.acme.orderplacement.domain.item.offer;

import java.util.Date;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.item.Item;

/**
 * <p>
 * A test for {@link Offer <code>Offer</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class OfferTest extends TestCase {

	/**
	 * @param name
	 */
	public OfferTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsNullName() {
		try {
			final Offer classUnderTest = new Offer();
			classUnderTest.setName(null);
			fail("setName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsBlankName() {
		try {
			final Offer classUnderTest = new Offer();
			classUnderTest.setName("");
			fail("setName('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#setStartDate(java.util.Date)}
	 * .
	 */
	public final void testSetStartDate_RejectsNullStartDate() {
		try {
			final Offer classUnderTest = new Offer();
			classUnderTest.setStartDate(null);
			fail("setStartDate(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#setPrice(com.acme.orderplacement.domain.item.offer.MonetaryAmount)}
	 * .
	 */
	public final void testSetPrice_RejectsNullPrice() {
		try {
			final Offer classUnderTest = new Offer();
			classUnderTest.setPrice(null);
			fail("setPrice(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#setOfferedItem(com.acme.orderplacement.domain.item.Item)}
	 * .
	 */
	public final void testSetOfferedItem_RejectsNullOfferedItem() {
		try {
			final Offer classUnderTest = new Offer();
			classUnderTest.setOfferedItem(null);
			fail("setOfferedItem(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesEqualOffers() {
		final Item commonItem = new Item();
		commonItem.setName("COMMON_ITEM");
		commonItem.setItemNumber("COMMON_ITEM_NO");

		final Date commonStartDate = new Date();

		final Offer offerOne = new Offer();
		offerOne.setName("IDENTICAL_NAME");
		offerOne.setStartDate(commonStartDate);
		offerOne.setOfferedItem(commonItem);

		final Offer offerTwo = new Offer();
		offerTwo.setName("IDENTICAL_NAME");
		offerTwo.setStartDate(commonStartDate);
		offerTwo.setOfferedItem(commonItem);

		assertTrue("equals() not implemented correctly", offerOne
				.equals(offerTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesUnequalOffers() {
		final Item commonItem = new Item();
		commonItem.setName("COMMON_ITEM");
		commonItem.setItemNumber("COMMON_ITEM_NO");

		final Date commonStartDate = new Date();

		final Offer offerOne = new Offer();
		offerOne.setName("NAME_1");
		offerOne.setStartDate(commonStartDate);
		offerOne.setOfferedItem(commonItem);

		final Offer offerTwo = new Offer();
		offerTwo.setName("NAME_2");
		offerTwo.setStartDate(commonStartDate);
		offerTwo.setOfferedItem(commonItem);

		assertFalse("equals() not implemented correctly", offerOne
				.equals(offerTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.offer.Offer#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualOffers() {
		final Item commonItem = new Item();
		commonItem.setName("COMMON_ITEM");
		commonItem.setItemNumber("COMMON_ITEM_NO");

		final Date commonStartDate = new Date();

		final Offer offerOne = new Offer();
		offerOne.setName("IDENTICAL_NAME");
		offerOne.setStartDate(commonStartDate);
		offerOne.setOfferedItem(commonItem);

		final Offer offerTwo = new Offer();
		offerTwo.setName("IDENTICAL_NAME");
		offerTwo.setStartDate(commonStartDate);
		offerTwo.setOfferedItem(commonItem);

		assertEquals("hashCode() not implemented correctly", offerOne
				.hashCode(), offerTwo.hashCode());
	}

}
