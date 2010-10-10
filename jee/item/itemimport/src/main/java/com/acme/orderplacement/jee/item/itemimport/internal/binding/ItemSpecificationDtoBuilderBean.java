/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal.binding;

import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;

/**
 * <p>
 * TODO: Insert short summary for ItemSpecificationDtoBuilderBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemSpecificationDtoBuilderBean {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private String itemSpecificationNumber;

	private String name;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public ItemSpecificationDto build() {
		return new ItemSpecificationDto(getItemSpecificationNumber(), getName());
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the itemSpecificationNumber
	 */
	public final String getItemSpecificationNumber() {
		return this.itemSpecificationNumber;
	}

	/**
	 * @param itemSpecificationNumber
	 *            the itemSpecificationNumber to set
	 */
	public final void setItemSpecificationNumber(
			final String itemSpecificationNumber) {
		this.itemSpecificationNumber = itemSpecificationNumber;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

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
		final ItemSpecificationDtoBuilderBean other = (ItemSpecificationDtoBuilderBean) obj;
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
		return "ItemSpecificationDtoBuilderBean [itemSpecificationNumber="
				+ this.itemSpecificationNumber + ", name=" + this.name + "]";
	}
}
