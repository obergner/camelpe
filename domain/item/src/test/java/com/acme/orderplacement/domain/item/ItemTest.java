/**
 * 
 */
package com.acme.orderplacement.domain.item;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Item <code>Item</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemTest extends TestCase {

	/**
	 * @param name
	 */
	public ItemTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#setItemNumber(java.lang.String)}
	 * .
	 */
	public final void testSetItemNumber_RejectsNullItemNumber() {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.setItemNumber(null);
			fail("setItemNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#setItemNumber(java.lang.String)}
	 * .
	 */
	public final void testSetItemNumber_RejectsBlankItemNumber() {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.setItemNumber("");
			fail("setItemNumber('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsNullName() {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.setName(null);
			fail("setName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsBlankName() {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.setName("");
			fail("setName('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#addSpecification(com.acme.orderplacement.domain.item.ItemSpecification)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddSpecification_RejectsNullItemSpecification()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.addSpecification(null);
			fail("addSpecification(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#addSpecification(com.acme.orderplacement.domain.item.ItemSpecification)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testAddSpecification_RejectsAnotherItemsSpecification()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Item anotherItem = new Item();
		anotherItem.setItemNumber("anotherItemsNumber");
		final ItemSpecification anotherItemsSpecification = new ItemSpecification();
		anotherItem.addSpecification(anotherItemsSpecification);
		try {
			final Item classUnderTest = new Item();
			classUnderTest.addSpecification(anotherItemsSpecification);
			fail("addSpecification(" + anotherItemsSpecification
					+ ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#addOption(com.acme.orderplacement.domain.item.ItemOption)}
	 * .
	 */
	public final void testAddOption_RejectsNullItemOption()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Item classUnderTest = new Item();
			classUnderTest.addOption(null);
			fail("addOption(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesEqualItems() {
		final Item itemOne = new Item();
		itemOne.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemOne.setName("IDENTICAL_NAME");

		final Item itemTwo = new Item();
		itemTwo.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemTwo.setName("IDENTICAL_NAME");

		assertTrue("equals() not implemented correctly", itemOne
				.equals(itemTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesUnequalItems() {
		final Item itemOne = new Item();
		itemOne.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemOne.setName("NAME1");

		final Item itemTwo = new Item();
		itemTwo.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemTwo.setName("NAME2");

		assertFalse("equals() not implemented correctly", itemOne
				.equals(itemTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.Item#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualItems() {
		final Item itemOne = new Item();
		itemOne.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemOne.setName("IDENTICAL_NAME");

		final Item itemTwo = new Item();
		itemTwo.setItemNumber("IDENTICAL_ITEM_NUMBER");
		itemTwo.setName("IDENTICAL_NAME");

		assertEquals("hashCode() not implemented correctly",
				itemOne.hashCode(), itemTwo.hashCode());
	}

}
