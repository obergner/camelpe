/**
 * 
 */
package com.acme.orderplacement.domain.item;

import junit.framework.TestCase;

import com.acme.orderplacement.framework.domain.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link ItemSpecification <code>ItemSpecification</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemSpecificationTest extends TestCase {

	/**
	 * @param name
	 */
	public ItemSpecificationTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#setItemSpecificationNumber(java.lang.String)}
	 * .
	 */
	public final void testSetItemSpecificationNumber_RejectsNullItemSpecificationNumber() {
		try {
			final ItemSpecification classUnderTest = new ItemSpecification();
			classUnderTest.setItemSpecificationNumber(null);
			fail("setItemSpecificationNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#setItemSpecificationNumber(java.lang.String)}
	 * .
	 */
	public final void testSetItemSpecificationNumber_RejectsBlankItemSpecificationNumber() {
		try {
			final ItemSpecification classUnderTest = new ItemSpecification();
			classUnderTest.setItemSpecificationNumber("");
			fail("setItemSpecificationNumber('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#setSpecifiedItem(com.acme.orderplacement.domain.item.Item)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetSpecifiedItem_RejectsNullItem()
			throws CollaborationPreconditionsNotMetException {
		try {
			final ItemSpecification classUnderTest = new ItemSpecification();
			classUnderTest.setSpecifiedItem(null);
			fail("setSpecifiedItem(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#setSpecifiedItem(com.acme.orderplacement.domain.item.Item)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetSpecifiedItem_RejectsAnItemDifferentFromTheAlreadySpecifiedItem()
			throws CollaborationPreconditionsNotMetException {
		final Item specifiedItem = new Item();
		specifiedItem.setItemNumber("ITEM_NUMBER");
		specifiedItem.setName("ITEM_NAME");

		final ItemSpecification classUnderTest = new ItemSpecification();
		classUnderTest.setSpecifiedItem(specifiedItem);

		final Item anotherItem = new Item();
		anotherItem.setItemNumber("ANOTHER_ITEM_NUMBER");
		anotherItem.setName("ANOTHER_ITEM_NAME");
		try {
			classUnderTest.setSpecifiedItem(anotherItem);
			fail("setSpecifiedItem(" + anotherItem
					+ ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#addItemOptionSpecification(com.acme.orderplacement.domain.item.ItemOptionSpecification)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddItemOptionSpecification_RejectsNullItemOptionSpecification()
			throws CollaborationPreconditionsNotMetException {
		try {
			final ItemSpecification classUnderTest = new ItemSpecification();
			classUnderTest.addItemOptionSpecification(null);
			fail("addItemOptionSpecification(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesEqualItemSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Item commonItem = new Item();
		commonItem.setItemNumber("COMMON_ITEM_NO");
		commonItem.setName("COMMON_ITEM_NAME");

		final ItemSpecification itemSpecificationOne = new ItemSpecification();
		itemSpecificationOne
				.setItemSpecificationNumber("IDENTICAL_ITEM_SPEC_NUMBER");
		itemSpecificationOne.setSpecifiedItem(commonItem);

		final ItemSpecification itemSpecificationTwo = new ItemSpecification();
		itemSpecificationTwo
				.setItemSpecificationNumber("IDENTICAL_ITEM_SPEC_NUMBER");
		itemSpecificationTwo.setSpecifiedItem(commonItem);

		assertTrue("equals() not implemented correctly", itemSpecificationOne
				.equals(itemSpecificationTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesUnequalItemSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Item commonItem = new Item();
		commonItem.setItemNumber("COMMON_ITEM_NO");
		commonItem.setName("COMMON_ITEM_NAME");

		final ItemSpecification itemSpecificationOne = new ItemSpecification();
		itemSpecificationOne.setItemSpecificationNumber("ITEM_SPEC_NUMBER_1");
		itemSpecificationOne.setSpecifiedItem(commonItem);

		final ItemSpecification itemSpecificationTwo = new ItemSpecification();
		itemSpecificationTwo.setItemSpecificationNumber("ITEM_SPEC_NUMBER_2");
		itemSpecificationTwo.setSpecifiedItem(commonItem);

		assertFalse("equals() not implemented correctly", itemSpecificationOne
				.equals(itemSpecificationTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemSpecification#hashCode()}.
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualItemSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Item commonItem = new Item();
		commonItem.setItemNumber("COMMON_ITEM_NO");
		commonItem.setName("COMMON_ITEM_NAME");

		final ItemSpecification itemSpecificationOne = new ItemSpecification();
		itemSpecificationOne
				.setItemSpecificationNumber("IDENTICAL_ITEM_SPEC_NUMBER");
		itemSpecificationOne.setSpecifiedItem(commonItem);

		final ItemSpecification itemSpecificationTwo = new ItemSpecification();
		itemSpecificationTwo
				.setItemSpecificationNumber("IDENTICAL_ITEM_SPEC_NUMBER");
		itemSpecificationTwo.setSpecifiedItem(commonItem);

		assertEquals("hashCode() not implemented correctly",
				itemSpecificationOne.hashCode(), itemSpecificationTwo
						.hashCode());
	}

}
