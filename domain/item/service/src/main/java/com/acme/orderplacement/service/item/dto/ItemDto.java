/**
 * 
 */
package com.acme.orderplacement.service.item.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * TODO: Insert short summary for ItemDto
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ItemDto
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
public class ItemDto implements Serializable {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final long serialVersionUID = 2184132448491586150L;

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely identifying this <code>ItemDto</code>.
	 * </p>
	 * 
	 */
	private final String itemNumber;

	/**
	 * <p>
	 * This <code>ItemDto</code>'s human-readable <em>unique</em> <i>name</i>.
	 * </p>
	 */
	private final String name;

	/**
	 * <p>
	 * A human-readable <i>description</i> of this <code>ItemDto</code>.
	 * </p>
	 */
	private final String description;

	/**
	 * 
	 */
	private final SortedSet<ItemSpecificationDto> specifications;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * @param itemNumber
	 * @param name
	 * @param description
	 * @param specifications
	 * @throws IllegalArgumentException
	 */
	public ItemDto(final String itemNumber, final String name,
			final String description,
			final SortedSet<ItemSpecificationDto> specifications)
			throws IllegalArgumentException {
		Validate.notEmpty(itemNumber, "itemNumber");
		Validate.notEmpty(name, "name");
		Validate.notEmpty(description, "description");
		this.itemNumber = itemNumber;
		this.name = name;
		this.description = description;
		this.specifications = specifications != null ? specifications
				: new TreeSet<ItemSpecificationDto>();
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * Getter of the property <tt>itemNumber</tt>
	 * 
	 * @return Returns the itemNumber.
	 */
	public String getItemNumber() {
		return this.itemNumber;
	}

	/**
	 * Getter of the property <tt>name</tt>
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Getter of the property <tt>description</tt>
	 * 
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Getter of the property <tt>specifications</tt>
	 * 
	 * @return Returns the specifications.
	 */
	public SortedSet<ItemSpecificationDto> getSpecifications() {
		return Collections.unmodifiableSortedSet(this.specifications);
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="specifications"
	 */
	public ItemSpecificationDto[] specificationsToArray() {
		return getSpecifications().toArray(
				new ItemSpecificationDto[this.specifications.size()]);
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
		final ItemDto other = (ItemDto) obj;
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
