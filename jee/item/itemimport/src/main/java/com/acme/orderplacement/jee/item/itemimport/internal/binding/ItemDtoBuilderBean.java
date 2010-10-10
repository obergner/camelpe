/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal.binding;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;

/**
 * <p>
 * TODO: Insert short summary for ItemDtoBuilderBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemDtoBuilderBean {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private String itemNumber;

	private String name;

	private String description;

	private Set<ItemSpecificationDtoBuilderBean> specifications;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public ItemDto build() {
		final SortedSet<ItemSpecificationDto> specifications = new TreeSet<ItemSpecificationDto>();
		for (final ItemSpecificationDtoBuilderBean specificationBuilder : getSpecifications()) {
			specifications.add(specificationBuilder.build());
		}

		return new ItemDto(getItemNumber(), getName(), getDescription(),
				specifications);
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the itemNumber
	 */
	public final String getItemNumber() {
		return this.itemNumber;
	}

	/**
	 * @param itemNumber
	 *            the itemNumber to set
	 */
	public final void setItemNumber(final String itemNumber) {
		this.itemNumber = itemNumber;
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

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the specifications
	 */
	public final Set<ItemSpecificationDtoBuilderBean> getSpecifications() {
		return this.specifications;
	}

	/**
	 * @param specifications
	 *            the specifications to set
	 */
	public final void setSpecifications(
			final Set<ItemSpecificationDtoBuilderBean> specifications) {
		this.specifications = specifications;
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
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result
				+ ((this.itemNumber == null) ? 0 : this.itemNumber.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime
				* result
				+ ((this.specifications == null) ? 0 : this.specifications
						.hashCode());
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
		final ItemDtoBuilderBean other = (ItemDtoBuilderBean) obj;
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
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
		if (this.specifications == null) {
			if (other.specifications != null) {
				return false;
			}
		} else if (!this.specifications.equals(other.specifications)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemDtoBuilderBean [description=" + this.description
				+ ", itemNumber=" + this.itemNumber + ", name=" + this.name
				+ ", specifications=" + this.specifications + "]";
	}
}
