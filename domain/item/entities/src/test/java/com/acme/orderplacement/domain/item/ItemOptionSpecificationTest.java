/**
 * 
 */
package com.acme.orderplacement.domain.item;

import junit.framework.TestCase;

import com.acme.orderplacement.framework.domain.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link ItemOptionSpecification
 * <code>ItemOptionSpecification</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemOptionSpecificationTest extends TestCase {

	/**
	 * @param name
	 */
	public ItemOptionSpecificationTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#setSpecifiedItemOption(com.acme.orderplacement.domain.item.ItemOption)}
	 * .
	 */
	public final void testSetSpecifiedItemOption_RejectsNullItemOption() {
		try {
			final ItemOptionSpecification classUnderTest = new ItemOptionSpecification();
			classUnderTest.setSpecifiedItemOption(null);
			fail("setSpecifiedItemOption(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#setItemSpecification(com.acme.orderplacement.domain.item.ItemSpecification)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetItemSpecification_RejectsNullItemSpecification()
			throws CollaborationPreconditionsNotMetException {
		try {
			final ItemOptionSpecification classUnderTest = new ItemOptionSpecification();
			classUnderTest.setItemSpecification(null);
			fail("setItemSpecification(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#setItemSpecification(com.acme.orderplacement.domain.item.ItemSpecification)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetItemSpecification_RejectsItemSpecificationDifferentFromTheOneALreadySet()
			throws CollaborationPreconditionsNotMetException {
		// (1)
		final Item alreadySetItem = new Item();
		alreadySetItem.setName("ALREADY_SET_ITEM");
		alreadySetItem.setItemNumber("ALREADY_SET_ITEM_NO");

		final ItemOption alreadySetItemOption = new ItemOption();
		alreadySetItemOption.setName("ALREADY_SET_ITEM_OPTION_NAME");
		alreadySetItemOption.setType("ALREADY_SET_ITEM_OPTION_TYPE");
		alreadySetItem.addOption(alreadySetItemOption);

		final ItemSpecification itemSpecificationAlreadySet = new ItemSpecification();
		itemSpecificationAlreadySet.setSpecifiedItem(alreadySetItem);

		final ItemOptionSpecification classUnderTest = new ItemOptionSpecification();
		classUnderTest.setSpecifiedItemOption(alreadySetItemOption);
		classUnderTest.setItemSpecification(itemSpecificationAlreadySet);

		// (2)
		final Item newItem = new Item();
		newItem.setName("NEW_ITEM");
		newItem.setItemNumber("NEW_ITEM_NO");

		final ItemOption newItemOption = new ItemOption();
		newItemOption.setName("NEW_ITEM_OPTION_NAME");
		newItemOption.setType("NEW_ITEM_OPTION_TYPE");
		newItem.addOption(alreadySetItemOption);
		classUnderTest.setSpecifiedItemOption(newItemOption);

		final ItemSpecification newItemSpecification = new ItemSpecification();
		newItemSpecification.setSpecifiedItem(newItem);
		try {
			classUnderTest.setItemSpecification(newItemSpecification);
			fail("setItemSpecification(<null>) did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesEqualItemOptionSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final ItemSpecification commonItemSpecification = new ItemSpecification();

		final ItemOption commonItemOption = new ItemOption();

		final ItemOptionSpecification itemOptionSpecificationOne = new ItemOptionSpecification();
		itemOptionSpecificationOne.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationOne
				.setItemSpecification(commonItemSpecification);
		itemOptionSpecificationOne.setSpecifiedItemOption(commonItemOption);

		final ItemOptionSpecification itemOptionSpecificationTwo = new ItemOptionSpecification();
		itemOptionSpecificationTwo.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationTwo
				.setItemSpecification(commonItemSpecification);
		itemOptionSpecificationTwo.setSpecifiedItemOption(commonItemOption);

		assertTrue("equals() not implemented correctly",
				itemOptionSpecificationOne.equals(itemOptionSpecificationTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testEqualsObject_RecognizesUnequalItemOptionSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final ItemSpecification commonItemSpecification = new ItemSpecification();

		final ItemOptionSpecification itemOptionSpecificationOne = new ItemOptionSpecification();
		itemOptionSpecificationOne.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationOne
				.setItemSpecification(commonItemSpecification);
		final ItemOption itemOptionOne = new ItemOption();
		itemOptionOne.setName("ITEM_OPTION_1");
		itemOptionSpecificationOne.setSpecifiedItemOption(itemOptionOne);

		final ItemOptionSpecification itemOptionSpecificationTwo = new ItemOptionSpecification();
		itemOptionSpecificationTwo.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationTwo
				.setItemSpecification(commonItemSpecification);
		final ItemOption itemOptionTwo = new ItemOption();
		itemOptionTwo.setName("ITEM_OPTION_2");
		itemOptionSpecificationTwo.setSpecifiedItemOption(itemOptionTwo);

		assertFalse("equals() not implemented correctly",
				itemOptionSpecificationOne.equals(itemOptionSpecificationTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOptionSpecification#hashCode()}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualItemOptionSpecifications()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final ItemSpecification commonItemSpecification = new ItemSpecification();

		final ItemOption commonItemOption = new ItemOption();

		final ItemOptionSpecification itemOptionSpecificationOne = new ItemOptionSpecification();
		itemOptionSpecificationOne.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationOne
				.setItemSpecification(commonItemSpecification);
		itemOptionSpecificationOne.setSpecifiedItemOption(commonItemOption);

		final ItemOptionSpecification itemOptionSpecificationTwo = new ItemOptionSpecification();
		itemOptionSpecificationTwo.setValue("IDENTICAL_VALUE");
		itemOptionSpecificationTwo
				.setItemSpecification(commonItemSpecification);
		itemOptionSpecificationTwo.setSpecifiedItemOption(commonItemOption);

		assertEquals("hashCode() not implemented correctly",
				itemOptionSpecificationOne.hashCode(),
				itemOptionSpecificationTwo.hashCode());
	}

}
