/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient.model;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ItemBean {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private final String itemNumber;

	private final String name;

	private final String description;

	private final SortedSet<ItemSpecificationBean> specifications;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	public ItemBean(final String itemNumber, final String name,
			final String description,
			final SortedSet<ItemSpecificationBean> specifications)
			throws IllegalArgumentException {
		Validate.notEmpty(itemNumber, "itemNumber");
		Validate.notEmpty(name, "name");
		Validate.notEmpty(description, "description");
		this.itemNumber = itemNumber;
		this.name = name;
		this.description = description;
		this.specifications = specifications != null ? specifications
				: new TreeSet<ItemSpecificationBean>();
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	public String getItemNumber() {
		return this.itemNumber;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public SortedSet<ItemSpecificationBean> getSpecifications() {
		return Collections.unmodifiableSortedSet(this.specifications);
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result
				+ ((this.itemNumber == null) ? 0 : this.itemNumber.hashCode());
		result = PRIME * result
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ItemBean other = (ItemBean) obj;
		if (this.itemNumber == null) {
			if (other.itemNumber != null) {
				return false;
			}
		} else if (!this.itemNumber.equals(other.itemNumber)) {
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
		return new ToStringBuilder(this).append("itemNumber", this.itemNumber)
				.append("name", this.name).append("description",
						this.description).toString();
	}
}
