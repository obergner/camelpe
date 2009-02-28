/**
 * 
 */
package com.acme.orderplacement.domain.item;

import junit.framework.TestCase;

/**
 * <p>
 * A test for {@link ItemOption <code>ItemOption</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemOptionTest extends TestCase {

	/**
	 * @param name
	 */
	public ItemOptionTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsNullName() {
		try {
			final ItemOption classUnderTest = new ItemOption();
			classUnderTest.setName(null);
			fail("setName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#setName(java.lang.String)}
	 * .
	 */
	public final void testSetName_RejectsBlankName() {
		try {
			final ItemOption classUnderTest = new ItemOption();
			classUnderTest.setName("");
			fail("setName('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#setType(java.lang.String)}
	 * .
	 */
	public final void testSetType_RejectsNullType() {
		try {
			final ItemOption classUnderTest = new ItemOption();
			classUnderTest.setType(null);
			fail("setType(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#setType(java.lang.String)}
	 * .
	 */
	public final void testSetType_RejectsBlankType() {
		try {
			final ItemOption classUnderTest = new ItemOption();
			classUnderTest.setType("");
			fail("setType('') did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesEqualItemOptions() {
		final ItemOption itemOptionOne = new ItemOption();
		itemOptionOne.setName("IDENTICAL_NAME");
		itemOptionOne.setType("IDENTICAL_TYPE");

		final ItemOption itemOptionTwo = new ItemOption();
		itemOptionTwo.setName("IDENTICAL_NAME");
		itemOptionTwo.setType("IDENTICAL_TYPE");

		assertTrue("equals() not implemented correctly", itemOptionOne
				.equals(itemOptionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#equals(java.lang.Object)}
	 * .
	 */
	public final void testEqualsObject_RecognizesUnequalItemOptions() {
		final ItemOption itemOptionOne = new ItemOption();
		itemOptionOne.setName("NAME_1");
		itemOptionOne.setType("TYPE_1");

		final ItemOption itemOptionTwo = new ItemOption();
		itemOptionTwo.setName("NAME_2");
		itemOptionTwo.setType("TYPE_2");

		assertFalse("equals() not implemented correctly", itemOptionOne
				.equals(itemOptionTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.item.ItemOption#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualItemOptions() {
		final ItemOption itemOptionOne = new ItemOption();
		itemOptionOne.setName("IDENTICAL_NAME");
		itemOptionOne.setType("IDENTICAL_TYPE");

		final ItemOption itemOptionTwo = new ItemOption();
		itemOptionTwo.setName("IDENTICAL_NAME");
		itemOptionTwo.setType("IDENTICAL_TYPE");

		assertEquals("hashCode() not implemented correctly", itemOptionOne
				.hashCode(), itemOptionTwo.hashCode());
	}

}
