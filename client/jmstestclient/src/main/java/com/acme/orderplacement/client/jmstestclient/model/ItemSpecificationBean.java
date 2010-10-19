/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient.model;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * TODO: Insert short summary for ItemSpecificationBean
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ItemSpecificationBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
public class ItemSpecificationBean implements Comparable<ItemSpecificationBean> {

	// ------------------------------------------------------------------------
	// Comparators
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Compare two <code>ItemSpecificationBean</code>s by their
	 * <code>itemSpecificationNumber</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public static final class ByItemSpecificationNumberComparator implements
			Comparator<ItemSpecificationBean> {

		/**
		 * 
		 */
		public ByItemSpecificationNumberComparator() {
			super();
		}

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final ItemSpecificationBean o1,
				final ItemSpecificationBean o2) {
			if ((o1 == null) || (o1.getItemSpecificationNumber() == null)) {
				return -1;
			}
			if ((o2 == null) || (o2.getItemSpecificationNumber() == null)) {
				return +1;
			}
			return o1.getItemSpecificationNumber().compareTo(
					o2.getItemSpecificationNumber());
		}

	}

	/**
	 * <p>
	 * Compare two <code>ItemSpecificationBean</code>s by their
	 * <code>itemSpecificationNumber</code>.
	 * </p>
	 */
	public static final Comparator<ItemSpecificationBean> BY_ITEM_SPECIFICATION_NUMBER = new ByItemSpecificationNumberComparator();

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private final String itemSpecificationNumber;

	private final String name;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	public ItemSpecificationBean(final String itemSpecificationNumber,
			final String name) throws IllegalArgumentException {
		Validate.notEmpty(itemSpecificationNumber, "itemSpecificationNumber");
		Validate.notEmpty(name, "name");
		this.itemSpecificationNumber = itemSpecificationNumber;
		this.name = name;
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	public final String getItemSpecificationNumber() {
		return this.itemSpecificationNumber;
	}

	public final String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.itemSpecificationNumber == null) ? 0
						: this.itemSpecificationNumber.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ItemSpecificationBean other = (ItemSpecificationBean) obj;
		if (this.itemSpecificationNumber == null) {
			if (other.itemSpecificationNumber != null) {
				return false;
			}
		} else if (!this.itemSpecificationNumber
				.equals(other.itemSpecificationNumber)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("itemSpecificationNumber",
				this.itemSpecificationNumber).append("name", this.name)
				.toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final ItemSpecificationBean o) {

		return BY_ITEM_SPECIFICATION_NUMBER.compare(this, o);
	}
}
