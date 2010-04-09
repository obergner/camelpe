/**
 * 
 */
package com.acme.orderplacement.domain.item;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * TODO: Insert short summary for Item
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for Item
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::description"
 */
@Entity
@Table(schema = "ITEM", name = "ITEM")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_ITEM")
@NamedQueries( {
		@NamedQuery(name = Item.Queries.BY_ITEM_NUMBER, query = "from com.acme.orderplacement.domain.item.Item item where item.itemNumber = :itemNumber"),
		@NamedQuery(name = Item.Queries.BY_ITEM_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.item.Item item where item.itemNumber like :itemNumber"),
		@NamedQuery(name = Item.Queries.BY_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.Item item where item.name like :name"),
		@NamedQuery(name = Item.Queries.BY_DESCRIPTION_LIKE, query = "from com.acme.orderplacement.domain.item.Item item where item.description like :description") })
public class Item extends AbstractAuditableDomainObject<Long> implements
		Serializable {

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

		public static final String BY_ITEM_NUMBER = "item.byItemNumber";

		public static final String BY_ITEM_NUMBER_LIKE = "item.byItemNumberLike";

		public static final String BY_NAME_LIKE = "item.byNameLike";

		public static final String BY_DESCRIPTION_LIKE = "item.byDescriptionLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 4059280814044385338L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Item() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Item(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Item(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Item(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Item(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Item(final Long id, final Integer version, final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely identifying this <code>Item</code>.
	 * </p>
	 * 
	 * @uml.property name="itemNumber"
	 */
	@NotNull
	@Size(min = 5, max = 30)
	@Basic
	@Column(name = "ITEM_NUMBER", unique = true, nullable = false, length = 30)
	@org.hibernate.annotations.NaturalId(mutable = false)
	private String itemNumber;

	/**
	 * Getter of the property <tt>itemNumber</tt>
	 * 
	 * @return Returns the itemNumber.
	 * @uml.property name="itemNumber"
	 */
	public String getItemNumber() {
		return this.itemNumber;
	}

	/**
	 * Setter of the property <tt>itemNumber</tt>
	 * 
	 * @param itemNumber
	 *            The itemNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>itemNumber</code> is <code>null</code> or blank
	 * 
	 * @uml.property name="itemNumber"
	 */
	public void setItemNumber(final String itemNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(itemNumber, "itemNumber");
		this.itemNumber = itemNumber;
	}

	/**
	 * <p>
	 * This <code>Item</code>'s human-readable <em>unique</em> <i>name</i>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Size(min = 2, max = 60)
	@Basic
	@Column(name = "NAME", unique = true, nullable = false, length = 60)
	private String name;

	/**
	 * Getter of the property <tt>name</tt>
	 * 
	 * @return Returns the name.
	 * @uml.property name="name"
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * 
	 * @param name
	 *            The name to set.
	 * @throws IllegalArgumentException
	 *             If <code>name</code> is <code>null</code> or blank
	 * 
	 * @uml.property name="name"
	 */
	public void setName(final String name) throws IllegalArgumentException {
		Validate.notEmpty(name, "name");
		this.name = name;
	}

	/**
	 * <p>
	 * A human-readable <i>description</i> of this <code>Item</code>.
	 * </p>
	 * 
	 * @uml.property name="description"
	 */
	@NotNull
	@Size(min = 3, max = 2000)
	@Basic
	@Column(name = "DESCRIPTION", unique = false, nullable = false, length = 2000)
	private String description;

	/**
	 * Getter of the property <tt>description</tt>
	 * 
	 * @return Returns the description.
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter of the property <tt>description</tt>
	 * 
	 * @param description
	 *            The description to set.
	 * @uml.property name="description"
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="specifications"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "specifiedItem:com.acme.orderplacement.domain.item.ItemSpecification"
	 * @uml.association name="Specific Item - Item"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "specifiedItem")
	@org.hibernate.annotations.Sort(type = org.hibernate.annotations.SortType.COMPARATOR, comparator = ItemSpecification.ByItemSpecificationNumberComparator.class)
	@org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	private SortedSet<ItemSpecification> specifications;

	/**
	 * @return
	 */
	protected SortedSet<ItemSpecification> getSpecificationsInternal() {
		if (this.specifications == null) {
			this.specifications = new TreeSet<ItemSpecification>();
		}

		return this.specifications;
	}

	/**
	 * Getter of the property <tt>specifications</tt>
	 * 
	 * @return Returns the specifications.
	 * @uml.property name="specifications"
	 */
	public SortedSet<ItemSpecification> getSpecifications() {
		return this.specifications;
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="specifications"
	 */
	public Iterator<ItemSpecification> specificationsIterator() {
		return getSpecificationsInternal().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="specifications"
	 */
	public boolean isSpecificationsEmpty() {
		return getSpecificationsInternal().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="specifications"
	 */
	public boolean containsSpecifications(
			final ItemSpecification itemSpecification) {
		return getSpecificationsInternal().contains(itemSpecification);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="specifications"
	 */
	public boolean containsAllSpecifications(
			final Collection<ItemSpecification> specifications) {
		return getSpecificationsInternal().containsAll(specifications);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="specifications"
	 */
	public int specificationsSize() {
		return getSpecificationsInternal().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="specifications"
	 */
	public ItemSpecification[] specificationsToArray() {
		return getSpecificationsInternal().toArray(
				new ItemSpecification[this.specifications.size()]);
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
	 * @uml.property name="specifications"
	 */
	public ItemSpecification[] specificationsToArray(
			final ItemSpecification[] specifications) {
		return getSpecificationsInternal().toArray(specifications);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>itemSpecification</code> already specifies an
	 *             <code>Item</code> that is <em>not</em> this <code>Item</code>
	 * @throws IllegalArgumentException
	 *             If <code>name</code> is <code>null</code>
	 * 
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="specifications"
	 */
	public boolean addSpecification(final ItemSpecification itemSpecification)
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		Validate.notNull(itemSpecification, "itemSpecification");
		if (argumentIsAnotherItemsSpecification(itemSpecification)) {
			final String error = "Cannot accept an ItemSpecification ["
					+ itemSpecification
					+ "] as this Item's ["
					+ this
					+ "] Specification that already specifies a different Item ["
					+ itemSpecification.getSpecifiedItem() + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		if (itemSpecification.getSpecifiedItem() != this) {
			itemSpecification.setSpecifiedItem(this);
		}
		return getSpecificationsInternal().add(itemSpecification);
	}

	/**
	 * @param itemSpecification
	 * @return
	 */
	protected boolean argumentIsAnotherItemsSpecification(
			final ItemSpecification itemSpecification) {
		if (itemSpecification == null) {
			return false;
		}
		if (itemSpecification.getSpecifiedItem() == null) {
			return false;
		}

		return !itemSpecification.getSpecifiedItem().equals(this);
	}

	/**
	 * Setter of the property <tt>specifications</tt>
	 * 
	 * @param specifications
	 *            the specifications to set.
	 * @uml.property name="specifications"
	 */
	public void setSpecifications(
			final SortedSet<ItemSpecification> specifications) {
		this.specifications = specifications;
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="specifications"
	 */
	public boolean removeSpecification(final ItemSpecification itemSpecification) {
		return getSpecificationsInternal().remove(itemSpecification);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="specifications"
	 */
	public void clearSpecifications() {
		getSpecificationsInternal().clear();
	}

	/**
	 * @uml.property name="options"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="item:com.acme.orderplacement.domain.item.ItemOption"
	 * @uml.association name="Item - ItemOption"
	 */
	@ManyToMany(targetEntity = ItemOption.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(schema = "ITEM", name = "ITEM_ITEM_OPTION_ASSOC", joinColumns = { @JoinColumn(name = "ID_ITEM") }, inverseJoinColumns = { @JoinColumn(name = "ID_ITEM_OPTION") })
	@org.hibernate.annotations.ForeignKey(name = "FK_ITEM_ITEMOPTION", inverseName = "FK_ITEMOPTION_ITEM")
	private Set<ItemOption> options;

	/**
	 * @return
	 */
	public Set<ItemOption> getOptionsInternal() {
		if (this.options == null) {
			this.options = new HashSet<ItemOption>();
		}

		return this.options;
	}

	/**
	 * Getter of the property <tt>options</tt>
	 * 
	 * @return Returns the options.
	 * @uml.property name="options"
	 */
	public Set<ItemOption> getOptions() {
		return this.options;
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="options"
	 */
	public Iterator<ItemOption> optionsIterator() {
		return getOptionsInternal().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="options"
	 */
	public boolean isOptionsEmpty() {
		return getOptionsInternal().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="options"
	 */
	public boolean containsOption(final ItemOption itemOption) {
		return getOptionsInternal().contains(itemOption);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="options"
	 */
	public boolean containsAllOptions(final Collection<ItemOption> options) {
		return getOptionsInternal().containsAll(options);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="options"
	 */
	public int optionsSize() {
		return getOptionsInternal().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="options"
	 */
	public ItemOption[] optionsToArray() {
		return getOptionsInternal()
				.toArray(new ItemOption[this.options.size()]);
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
	 * @uml.property name="options"
	 */
	public ItemOption[] optionsToArray(final ItemOption[] options) {
		return getOptionsInternal().toArray(options);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws IllegalArgumentException
	 *             If <code>itemOption</code> is <code>null</code>
	 * 
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="options"
	 */
	public boolean addOption(final ItemOption itemOption) {
		Validate.notNull(itemOption, "itemOption");
		return getOptionsInternal().add(itemOption);
	}

	/**
	 * Setter of the property <tt>options</tt>
	 * 
	 * @param options
	 *            the options to set.
	 * @uml.property name="options"
	 */
	public void setOptions(final Set<ItemOption> options) {
		this.options = options;
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="options"
	 */
	public boolean removeOption(final ItemOption itemOption) {
		return getOptionsInternal().remove(itemOption);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="options"
	 */
	public void clearOptions() {
		getOptionsInternal().clear();
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
		final Item other = (Item) obj;
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
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"itemNumber", this.itemNumber).append("name", this.name)
				.append("description", this.description).toString();
	}

}
