/**
 * 
 */
package com.acme.orderplacement.domain.item;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;

import com.acme.orderplacement.domain.support.AbstractVersionedDomainObject;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * TODO: Insert short summary for ItemOptionSpecification
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ItemOptionSpecification
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::description"
 */
@Entity
@Table(schema = "ITEM", name = "ITEM_OPTION_SPECIFICATION")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_ITEM_OPTION_SPEC")
@NamedQueries( {
		@NamedQuery(name = ItemOptionSpecification.Queries.BY_VALUE, query = "from com.acme.orderplacement.domain.item.ItemOptionSpecification itemOptionSpecification where itemOptionSpecification.value = :value"),
		@NamedQuery(name = ItemOptionSpecification.Queries.BY_VALUE_LIKE, query = "from com.acme.orderplacement.domain.item.ItemOptionSpecification itemOptionSpecification where itemOptionSpecification.value like '%:value%'"),
		@NamedQuery(name = ItemOptionSpecification.Queries.BY_SPECIFIED_ITEM_OPTION_ID, query = "from com.acme.orderplacement.domain.item.ItemOptionSpecification itemOptionSpecification where itemOptionSpecification.specifiedItemOption.id = :itemOptionId"),
		@NamedQuery(name = ItemOptionSpecification.Queries.BY_SPECIFIED_ITEM_OPTION_NAME, query = "from com.acme.orderplacement.domain.item.ItemOptionSpecification itemOptionSpecification where itemOptionSpecification.specifiedItemOption.name = :itemOptionName") })
public class ItemOptionSpecification extends
		AbstractVersionedDomainObject<Long> implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PrivateCustomer</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_VALUE = "itemOptionSpecification.byValue";

		public static final String BY_VALUE_LIKE = "itemOptionSpecification.byValueLike";

		public static final String BY_SPECIFIED_ITEM_OPTION_ID = "itemOptionSpecification.bySpecifiedItemOptionId";

		public static final String BY_SPECIFIED_ITEM_OPTION_NAME = "itemOptionSpecification.bySpecifiedItemOptionName";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -4995393427120662407L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public ItemOptionSpecification() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public ItemOptionSpecification(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public ItemOptionSpecification(final Integer version) {
		super(version);
	}

	/**
	 * @param id
	 * @param version
	 */
	public ItemOptionSpecification(final Long id, final Integer version) {
		super(id, version);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>ItemOptionSpecification</code>'s <i>value</i>.
	 * </p>
	 * 
	 * @uml.property name="value"
	 */
	@NotNull
	@Size(min = 0, max = 200)
	@Basic
	@Column(name = "NAME", unique = false, nullable = true, length = 200)
	private String value;

	/**
	 * Getter of the property <tt>value</tt>
	 * 
	 * @return Returns the value.
	 * @uml.property name="value"
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter of the property <tt>value</tt>
	 * 
	 * @param value
	 *            The value to set.
	 * @uml.property name="value"
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="specifiedItemOption"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "itemOptionSpecification:com.acme.orderplacement.domain.item.ItemOption"
	 * @uml.association name="ItemOption - ItemOptionSpecification"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ITEM_OPTION", unique = false, nullable = false)
	@ForeignKey(name = "FK_ITEMOPTIONSPEC_ITEMOPTION")
	private ItemOption specifiedItemOption;

	/**
	 * Getter of the property <tt>specifiedItemOption</tt>
	 * 
	 * @return Returns the specifiedItemOption.
	 * @uml.property name="specifiedItemOption"
	 */
	public ItemOption getSpecifiedItemOption() {
		return this.specifiedItemOption;
	}

	/**
	 * Setter of the property <tt>specifiedItemOption</tt>
	 * 
	 * @param specifiedItemOption
	 *            The specifiedItemOption to set.
	 * @throws IllegalArgumentException
	 *             If <code>specifiedItemOption</code> is <code>null</code>
	 * 
	 * @uml.property name="specifiedItemOption"
	 */
	public void setSpecifiedItemOption(final ItemOption specifiedItemOption)
			throws IllegalArgumentException {
		Validate.notNull(specifiedItemOption, "specifiedItemOption");
		this.specifiedItemOption = specifiedItemOption;
	}

	/**
	 * @uml.property name="itemSpecification"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "itemOptionSpecifications:com.acme.orderplacement.domain.item.ItemSpecification"
	 * @uml.association name="ItemSpecification - ItemOptionSpecifications"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_ITEM_SPECIFICATION", unique = false, nullable = false)
	@ForeignKey(name = "FK_ITEMOPTIONSPEC_ITEMSPEC")
	private ItemSpecification itemSpecification;

	/**
	 * Getter of the property <tt>itemSpecification</tt>
	 * 
	 * @return Returns the itemSpecification.
	 * @uml.property name="itemSpecification"
	 */
	public ItemSpecification getItemSpecification() {
		return this.itemSpecification;
	}

	/**
	 * Setter of the property <tt>itemSpecification</tt>
	 * 
	 * @param itemSpecification
	 *            The itemSpecification to set.
	 * @throws IllegalArgumentException
	 *             If <code>itemSpecification</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>ItemOptinSpecification</code> is already
	 *             associated with an <code>ItemSpecification</code> different
	 *             from the supplied <code>itemSpecification</code>
	 * 
	 * @uml.property name="itemSpecification"
	 */
	public void setItemSpecification(final ItemSpecification itemSpecification)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(itemSpecification, "itemSpecification");
		if (isAssociatedWithAnItemSpecificationDifferentFrom(itemSpecification)) {
			final String error = "Cannot acept an ItemSpecification ["
					+ itemSpecification
					+ "] as this ItemOptionSpecification's ["
					+ this
					+ "] parent ItemSpecification while already having a different parent ItemSpecification ["
					+ this.itemSpecification + "]";

			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.itemSpecification = itemSpecification;
		if (!itemSpecification.containsItemOptionSpecification(this)) {
			itemSpecification.addItemOptionSpecification(this);
		}
	}

	/**
	 * @param itemSpecification
	 * @return
	 */
	protected boolean isAssociatedWithAnItemSpecificationDifferentFrom(
			final ItemSpecification itemSpecification) {
		if (this.itemSpecification == null) {
			return false;
		}
		if (itemSpecification == null) {

			return true;
		}

		return !this.itemSpecification.equals(itemSpecification);
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
		result = PRIME
				* result
				+ ((this.itemSpecification == null) ? 0
						: this.itemSpecification.hashCode());
		result = PRIME
				* result
				+ ((this.specifiedItemOption == null) ? 0
						: this.specifiedItemOption.hashCode());
		result = PRIME * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
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
		final ItemOptionSpecification other = (ItemOptionSpecification) obj;
		if (this.itemSpecification == null) {
			if (other.itemSpecification != null) {
				return false;
			}
		} else if (!this.itemSpecification.equals(other.itemSpecification)) {
			return false;
		}
		if (this.specifiedItemOption == null) {
			if (other.specifiedItemOption != null) {
				return false;
			}
		} else if (!this.specifiedItemOption.equals(other.specifiedItemOption)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"value", this.value).append("specifiedItemOption",
				this.specifiedItemOption).toString();
	}

}
