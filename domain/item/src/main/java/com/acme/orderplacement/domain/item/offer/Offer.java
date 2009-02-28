/**
 * 
 */
package com.acme.orderplacement.domain.item.offer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Future;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.domain.support.money.MonetaryAmount;

/**
 * <p>
 * TODO: Insert short summary for Offer
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for Offer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Table(schema = "ITEM", name = "OFFER")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_OFFER")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.BY_NAME, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.name = :name"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.BY_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.name like '%:name%'"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.OFFERED_AFTER, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.endDate > :date"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.OFFERED_BEFORE, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.startDate < :date"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.OFFERED_BETWEEN, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where (offer.endDate > :startDate and offer.startDate < :endDate)"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.BY_OFFERED_ITEM_ID, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.offeredItem.id = :itemId"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.BY_OFFERED_ITEM_NAME, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.offeredItem.name = :itemName"),
		@org.hibernate.annotations.NamedQuery(name = Offer.Queries.BY_OFFERED_ITEM_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.offer.Offer offer where offer.offeredItem.name like '%:itemName%'") })
public class Offer extends AbstractAuditableDomainObject<Long> implements
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

		public static final String BY_NAME = "offer.byName";

		public static final String BY_NAME_LIKE = "offer.byNameLike";

		public static final String OFFERED_AFTER = "offer.offeredAfter";

		public static final String OFFERED_BEFORE = "offer.offeredBefore";

		public static final String OFFERED_BETWEEN = "offer.offeredBetween";

		public static final String BY_OFFERED_ITEM_ID = "offer.byOfferedItemId";

		public static final String BY_OFFERED_ITEM_NAME = "offer.byOfferedItemName";

		public static final String BY_OFFERED_ITEM_NAME_LIKE = "offer.byOfferedItemNameLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -186415302735612322L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Offer() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Offer(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Offer(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Offer(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Offer(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Offer(final Long id, final Integer version, final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A unique human-readable <i>name</i> identifying this <code>Offer</code>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Length(min = 2, max = 60)
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
	 * The <code>Date</code> on which this <code>Offer</code> starts/started.
	 * </p>
	 * 
	 * @uml.property name="startDate"
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", unique = false, nullable = false)
	private Date startDate;

	/**
	 * Getter of the property <tt>startDate</tt>
	 * 
	 * @return Returns the startDate.
	 * @uml.property name="startDate"
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Setter of the property <tt>startDate</tt>
	 * 
	 * @param startDate
	 *            The startDate to set.
	 * @throws IllegalArgumentException
	 *             If <code>startDate</code> is <code>null</code>
	 * 
	 * @uml.property name="startDate"
	 */
	public void setStartDate(final Date startDate)
			throws IllegalArgumentException {
		Validate.notNull(startDate, "startDate");
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * The <code>Date</code> on which this <code>Offer</code> ends/ended.
	 * </p>
	 * 
	 * @uml.property name="endDate"
	 */
	@Future
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", unique = false, nullable = true)
	private Date endDate;

	/**
	 * Getter of the property <tt>endDate</tt>
	 * 
	 * @return Returns the endDate.
	 * @uml.property name="endDate"
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Setter of the property <tt>endDate</tt>
	 * 
	 * @param endDate
	 *            The endDate to set.
	 * @uml.property name="endDate"
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @uml.property name="price"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "offer:com.acme.orderplacement.domain.item.offer.MonetaryAmount"
	 * @uml.association name="Offer - Price"
	 */
	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = "amount", column = @Column(name = "PRICE_AMOUNT")),
			@AttributeOverride(name = "currency", column = @Column(name = "PRICE_CURRENCY")) })
	private MonetaryAmount price;

	/**
	 * Getter of the property <tt>price</tt>
	 * 
	 * @return Returns the price.
	 * @uml.property name="price"
	 */
	public MonetaryAmount getPrice() {
		return this.price;
	}

	/**
	 * Setter of the property <tt>price</tt>
	 * 
	 * @param price
	 *            The price to set.
	 * @throws IllegalArgumentException
	 *             If <code>price</code> is <code>null</code>
	 * 
	 * @uml.property name="price"
	 */
	public void setPrice(final MonetaryAmount price)
			throws IllegalArgumentException {
		Validate.notNull(price, "price");
		this.price = price;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="offeredItem"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="offer:com.acme.orderplacement.domain.item.Item"
	 * @uml.association name="Offer - Offered Item"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_ITEM", unique = false, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_OFFER_ITEM")
	private Item offeredItem;

	/**
	 * Getter of the property <tt>offeredItem</tt>
	 * 
	 * @return Returns the offeredItem.
	 * @uml.property name="offeredItem"
	 */
	public Item getOfferedItem() {
		return this.offeredItem;
	}

	/**
	 * Setter of the property <tt>offeredItem</tt>
	 * 
	 * @param offeredItem
	 *            The offeredItem to set.
	 * @throws IllegalArgumentException
	 *             If <code>offeredItem</code> is <code>null</code>
	 * 
	 * @uml.property name="offeredItem"
	 */
	public void setOfferedItem(final Item offeredItem)
			throws IllegalArgumentException {
		Validate.notNull(offeredItem, "offeredItem");
		this.offeredItem = offeredItem;
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
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME
				* result
				+ ((this.offeredItem == null) ? 0 : this.offeredItem.hashCode());
		result = PRIME * result
				+ ((this.price == null) ? 0 : this.price.hashCode());
		result = PRIME * result
				+ ((this.startDate == null) ? 0 : this.startDate.hashCode());
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
		final Offer other = (Offer) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.offeredItem == null) {
			if (other.offeredItem != null) {
				return false;
			}
		} else if (!this.offeredItem.equals(other.offeredItem)) {
			return false;
		}
		if (this.price == null) {
			if (other.price != null) {
				return false;
			}
		} else if (!this.price.equals(other.price)) {
			return false;
		}
		if (this.startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!this.startDate.equals(other.startDate)) {
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
				"startDate", this.startDate).append("endDate", this.endDate)
				.append("name", this.name).append("price", this.price).append(
						"offeredItem", this.offeredItem).toString();
	}

}
