/**
 * 
 */
package com.acme.orderplacement.domain.item;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

import com.acme.orderplacement.framework.domain.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.framework.domain.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.framework.domain.meta.AuditInfo;

/**
 * <p>
 * TODO: Insert short summary for ItemSpecification
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ItemSpecification
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::thing"
 */
@Entity
@Table(schema = "ITEM", name = "ITEM_SPECIFICATION")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_ITEM_SPEC")
@NamedQueries( {
		@NamedQuery(name = ItemSpecification.Queries.BY_ITEM_SPECIFICATION_NUMBER, query = "from com.acme.orderplacement.domain.item.ItemSpecification itemSpecification where itemSpecification.itemSpecificationNumber = :itemSpecificationNumber"),
		@NamedQuery(name = ItemSpecification.Queries.BY_ITEM_SPECIFICATION_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.item.ItemSpecification itemSpecification where itemSpecification.itemSpecificationNumber like '%:itemSpecificationNumber%'"),
		@NamedQuery(name = ItemSpecification.Queries.BY_SPECIFIED_ITEM_ID, query = "from com.acme.orderplacement.domain.item.ItemSpecification itemSpecification where itemSpecification.specifiedItem.id = :itemId"),
		@NamedQuery(name = ItemSpecification.Queries.BY_SPECIFIED_ITEM_NAME, query = "from com.acme.orderplacement.domain.item.ItemSpecification itemSpecification where itemSpecification.specifiedItem.name = :itemName") })
public class ItemSpecification extends AbstractAuditableDomainObject<Long>
		implements Serializable, Comparable<ItemSpecification> {

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

		public static final String BY_ITEM_SPECIFICATION_NUMBER = "itemSpecification.byItemSpecificationNumber";

		public static final String BY_ITEM_SPECIFICATION_NUMBER_LIKE = "itemSpecification.byItemSpecificationNumberLike";

		public static final String BY_SPECIFIED_ITEM_ID = "itemSpecification.bySpecifiedItemId";

		public static final String BY_SPECIFIED_ITEM_NAME = "itemSpecification.bySpecifiedItemName";
	}

	// ------------------------------------------------------------------------
	// Comparators
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Compare two <code>ItemSpecification</code>s by their
	 * <code>itemSpecificationNumber</code>.
	 * </p>
	 * <p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public static final class ByItemSpecificationNumberComparator implements
			Comparator<ItemSpecification> {

		/**
		 * 
		 */
		public ByItemSpecificationNumberComparator() {
			super();
		}

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final ItemSpecification o1,
				final ItemSpecification o2) {
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
	 * Compare two <code>ItemSpecification</code>s by their
	 * <code>itemSpecificationNumber</code>.
	 * </p>
	 */
	public static final Comparator<ItemSpecification> BY_ITEM_SPECIFICATION_NUMBER = new ByItemSpecificationNumberComparator();

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860964542990333291L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public ItemSpecification() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public ItemSpecification(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public ItemSpecification(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public ItemSpecification(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public ItemSpecification(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public ItemSpecification(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely identifying this
	 * <code>ItemSpecification</code>. This attribute may <strong>not</strong>
	 * be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="itemSpecificationNumber"
	 */
	@NotNull
	@Size(min = 5, max = 60)
	@Basic
	@Column(name = "ITEM_SPECIFICATION_NUMBER", unique = true, nullable = false, length = 60)
	@NaturalId(mutable = false)
	private String itemSpecificationNumber;

	/**
	 * Getter of the property <tt>itemSpecificationNumber</tt>
	 * 
	 * @return Returns the itemSpecificationNumber.
	 * @uml.property name="itemSpecificationNumber"
	 */
	public String getItemSpecificationNumber() {
		return this.itemSpecificationNumber;
	}

	/**
	 * Setter of the property <tt>itemSpecificationNumber</tt>
	 * 
	 * @param itemSpecificationNumber
	 *            The itemSpecificationNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>itemSpecificationNumber</code> is <code>null</code>
	 *             or blank
	 * 
	 * @uml.property name="itemSpecificationNumber"
	 */
	public void setItemSpecificationNumber(final String itemSpecificationNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(itemSpecificationNumber, "itemSpecificationNumber");
		this.itemSpecificationNumber = itemSpecificationNumber;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="specifiedItem"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="specifications:com.acme.orderplacement.domain.item.Item"
	 * @uml.association name="Specific Item - Item"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ITEM", unique = false, nullable = false)
	@ForeignKey(name = "FK_ITEMSPEC_ITEM")
	private Item specifiedItem;

	/**
	 * Getter of the property <tt>specifiedItem</tt>
	 * 
	 * @return Returns the specifiedItem.
	 * @uml.property name="specifiedItem"
	 */
	public Item getSpecifiedItem() {
		return this.specifiedItem;
	}

	/**
	 * Setter of the property <tt>specifiedItem</tt>
	 * 
	 * @param specifiedItem
	 *            The specifiedItem to set.
	 * @throws IllegalArgumentException
	 *             If <code>specifiedItem</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>ItemSpecification</code> already specifies an
	 *             <code>Item</code> that is <em>not</em>
	 *             <code>specifiedItem</code>
	 * 
	 * @uml.property name="specifiedItem"
	 */
	public void setSpecifiedItem(final Item specifiedItem)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(specifiedItem, "specifiedItem");
		if (specifiesAnItemDifferentFrom(specifiedItem)) {
			final String error = "Cannot accept an Item ["
					+ specifiedItem
					+ "] as the Item specified by this Specification ["
					+ this
					+ "] when this Specification already specifies another Item ["
					+ getSpecifiedItem() + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.specifiedItem = specifiedItem;
		if (!specifiedItem.containsSpecifications(this)) {
			specifiedItem.addSpecification(this);
		}
	}

	/**
	 * @param item
	 * @return
	 */
	protected boolean specifiesAnItemDifferentFrom(final Item item) {
		if (this.specifiedItem == null) {
			return false;
		}
		if (item == null) {
			return true;
		}

		return !item.equals(this.specifiedItem);
	}

	/**
	 * @uml.property name="itemOptionSpecifications"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "itemSpecification:com.acme.orderplacement.domain.item.ItemOptionSpecification"
	 * @uml.association name="ItemSpecification - ItemOptionSpecifications"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "itemSpecification")
	// @OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ItemOptionSpecification> itemOptionSpecifications;

	/**
	 * @return
	 */
	public Set<ItemOptionSpecification> getItemOptionSpecificationsInternal() {
		if (this.itemOptionSpecifications == null) {
			this.itemOptionSpecifications = new HashSet<ItemOptionSpecification>();
		}

		return this.itemOptionSpecifications;
	}

	/**
	 * Getter of the property <tt>itemOptionSpecifications</tt>
	 * 
	 * @return Returns the itemOptionSpecifications.
	 * @uml.property name="itemOptionSpecifications"
	 */
	public Set<ItemOptionSpecification> getItemOptionSpecifications() {
		return this.itemOptionSpecifications;
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="itemOptionSpecifications"
	 */
	public Iterator<ItemOptionSpecification> itemOptionSpecificationsIterator() {
		return getItemOptionSpecificationsInternal().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="itemOptionSpecifications"
	 */
	public boolean isItemOptionSpecificationsEmpty() {
		return getItemOptionSpecificationsInternal().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="itemOptionSpecifications"
	 */
	public boolean containsItemOptionSpecification(
			final ItemOptionSpecification itemOptionSpecification) {
		return getItemOptionSpecificationsInternal().contains(
				itemOptionSpecification);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="itemOptionSpecifications"
	 */
	public boolean containsAllItemOptionSpecifications(
			final Collection<ItemOptionSpecification> itemOptionSpecifications) {
		return getItemOptionSpecificationsInternal().containsAll(
				itemOptionSpecifications);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="itemOptionSpecifications"
	 */
	public int itemOptionSpecificationsSize() {
		return getItemOptionSpecificationsInternal().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="itemOptionSpecifications"
	 */
	public ItemOptionSpecification[] itemOptionSpecificationsToArray() {
		return getItemOptionSpecificationsInternal().toArray(
				new ItemOptionSpecification[this.itemOptionSpecifications
						.size()]);
	}

	/**
	 * Returns an array containing all of the elements in this collection; the
	 * runtime type of the returned array is that of the specified array.
	 * 
	 * @param a
	 *            the array into which the elements of this collection are to be
	 *            stored.
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray(Object[])
	 * @uml.property name="itemOptionSpecifications"
	 */
	public ItemOptionSpecification[] itemOptionSpecificationsToArray(
			final ItemOptionSpecification[] itemOptionSpecifications) {
		return getItemOptionSpecificationsInternal().toArray(
				itemOptionSpecifications);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws IllegalArgumentException
	 *             If <code>itemOptionSpecification</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>itemOptionSpecification</code> references an
	 *             <code>ItemOption</code> that is <em>not</em> known to this
	 *             <code>ItemSpecification</code>'s parent <code>Item</code>
	 * 
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="itemOptionSpecifications"
	 */
	public boolean addItemOptionSpecification(
			final ItemOptionSpecification itemOptionSpecification)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(itemOptionSpecification, "itemOptionSpecification");
		if (!isAllowableTargetFor(itemOptionSpecification)) {
			final String error = "Cannot accept an ItemOptionSpecification ["
					+ itemOptionSpecification
					+ "] for this ItemOption' ["
					+ this
					+ "] while said ItemOptionSpecification specifies an ItemOption ["
					+ itemOptionSpecification.getSpecifiedItemOption()
					+ "] that is not associated with this ItemSpecification's specified Item ["
					+ getSpecifiedItem() + "]";

			throw new CollaborationPreconditionsNotMetException(error);
		}
		if (itemOptionSpecification.getItemSpecification() != this) {
			itemOptionSpecification.setItemSpecification(this);
		}
		return getItemOptionSpecificationsInternal().add(
				itemOptionSpecification);
	}

	/**
	 * @param itemOptionSpecification
	 * @return
	 */
	protected boolean isAllowableTargetFor(
			final ItemOptionSpecification itemOptionSpecification) {
		if (itemOptionSpecification == null) {
			return false;
		}
		if (getSpecifiedItem() == null) {
			/*
			 * This means we are still in the process of setting up this object.
			 * Be lenient and allow the supplied ItemOptionSpecification.
			 * 
			 * Note that this is inherently unsafe.
			 * 
			 * TODO: Is there a better way?
			 */
			return true;
		}
		if (!getSpecifiedItem().containsOption(
				itemOptionSpecification.getSpecifiedItemOption())) {
			return false;
		}

		return true;
	}

	/**
	 * Setter of the property <tt>itemOptionSpecifications</tt>
	 * 
	 * @param itemOptionSpecifications
	 *            the itemOptionSpecifications to set.
	 * @uml.property name="itemOptionSpecifications"
	 */
	public void setItemOptionSpecifications(
			final Set<ItemOptionSpecification> itemOptionSpecifications) {
		this.itemOptionSpecifications = itemOptionSpecifications;
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="itemOptionSpecifications"
	 */
	public boolean removeItemOptionSpecification(
			final ItemOptionSpecification itemOptionSpecification) {
		return getItemOptionSpecificationsInternal().remove(
				itemOptionSpecification);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="itemOptionSpecifications"
	 */
	public void clearItemOptionSpecifications() {
		getItemOptionSpecificationsInternal().clear();
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
				+ ((this.itemSpecificationNumber == null) ? 0
						: this.itemSpecificationNumber.hashCode());
		result = PRIME
				* result
				+ ((this.specifiedItem == null) ? 0 : this.specifiedItem
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ItemSpecification other = (ItemSpecification) obj;
		if (this.itemSpecificationNumber == null) {
			if (other.itemSpecificationNumber != null) {
				return false;
			}
		} else if (!this.itemSpecificationNumber
				.equals(other.itemSpecificationNumber)) {
			return false;
		}
		if (this.specifiedItem == null) {
			if (other.specifiedItem != null) {
				return false;
			}
		} else if (!this.specifiedItem.equals(other.specifiedItem)) {
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
				"itemSpecificationNumber", this.itemSpecificationNumber)
				.append("itemOptionSpecifications",
						getItemOptionSpecificationsInternal()).toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final ItemSpecification o) {

		return BY_ITEM_SPECIFICATION_NUMBER.compare(this, o);
	}

}
