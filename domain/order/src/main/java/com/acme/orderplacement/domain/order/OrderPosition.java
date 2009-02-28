/**
 * 
 */
package com.acme.orderplacement.domain.order;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.item.offer.Offer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * TODO: Insert short summary for OrderPosition
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for OrderPosition
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Table(schema = "ORDER", name = "ORDER_POSITION", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ID_ORDER", "POSITION" }))
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_ORDER_POSITION")
public class OrderPosition extends AbstractAuditableDomainObject<Long>
		implements Comparable<OrderPosition>, Serializable {

	// ------------------------------------------------------------------------
	// Comparators
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A {@link Comparator <code>Comparator</code>} for comparing two
	 * <code>OrderPosition</code>s based on their <code>position</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public static final class ByPositionComparator implements
			Comparator<OrderPosition> {

		/**
		 * <p>
		 * This <code>Constructor</code> cannot be made <code>private</code>
		 * since the JPA/Hibernate runtime needs to instantiate this
		 * <code>Comparator</code> dynamically.
		 * </p>
		 */
		public ByPositionComparator() {
			super();
		}

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final OrderPosition orderPosition1,
				final OrderPosition orderPosition2) {
			if (orderPosition1 == null) {
				return -1;
			}
			if (orderPosition2 == null) {
				return +1;
			}

			return (orderPosition1.position - orderPosition2.position);
		}

	}

	/**
	 * Compare two <code>OrderPosition</code>s by their <code>position</code>.
	 */
	public static final ByPositionComparator BY_POSITION = new ByPositionComparator();

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 7239077458562472547L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public OrderPosition() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public OrderPosition(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public OrderPosition(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public OrderPosition(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public OrderPosition(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public OrderPosition(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>OrderPosition</code>'s <tt>position</tt> within the
	 * <code>SortedSet</code> of <code>OrderPosition</code>s held by its parent
	 * <code>Order</code>. Has to be unique among all <code>OrderPosition</code>
	 * s of a given <code>Order</code>.
	 * </p>
	 * 
	 * @uml.property name="position"
	 */
	@NotNull
	@Basic
	@Column(name = "POSITION", unique = false, nullable = false)
	private int position;

	/**
	 * Getter of the property <tt>position</tt>
	 * 
	 * @return Returns the position.
	 * @uml.property name="position"
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Setter of the property <tt>position</tt>
	 * 
	 * @param position
	 *            The position to set.
	 * @uml.property name="position"
	 */
	public void setPosition(final int position) {
		this.position = position;
	}

	/**
	 * <p>
	 * The number of <code>Offer</code>s ordered by the customer.
	 * </p>
	 * 
	 * @uml.property name="numberOfOrderedOffers"
	 */
	@NotNull
	@Basic
	@Column(name = "COUNT_ORDERED_OFFERS", unique = false, nullable = false)
	private int numberOfOrderedOffers;

	/**
	 * Getter of the property <tt>numberOfOrderedOffers</tt>
	 * 
	 * @return Returns the numberOfOrderedOffers.
	 * @uml.property name="numberOfOrderedOffers"
	 */
	public int getNumberOfOrderedOffers() {
		return this.numberOfOrderedOffers;
	}

	/**
	 * Setter of the property <tt>numberOfOrderedOffers</tt>
	 * 
	 * @param numberOfOrderedOffers
	 *            The numberOfOrderedOffers to set.
	 * @uml.property name="numberOfOrderedOffers"
	 */
	public void setNumberOfOrderedOffers(final int numberOfOrderedOffers) {
		this.numberOfOrderedOffers = numberOfOrderedOffers;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="parentOrder"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="orderPositions:com.acme.orderplacement.domain.order.Order"
	 * @uml.association name="Order - OrderPositions"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ORDER", unique = false, nullable = false)
	@ForeignKey(name = "ORDER_DETAIL_ORDER_FK")
	private Order parentOrder;

	/**
	 * Getter of the property <tt>parentOrder</tt>
	 * 
	 * @return Returns the parentOrder.
	 * @uml.property name="parentOrder"
	 */
	public Order getParentOrder() {
		return this.parentOrder;
	}

	/**
	 * Setter of the property <tt>parentOrder</tt>
	 * 
	 * @param parentOrder
	 *            The parentOrder to set.
	 * @throws IllegalArgumentException
	 *             If <code>parentOrder</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>OrderPosition</code> already has a parent
	 *             <code>Order</code> different from <code>parentOrder</code>
	 * @uml.property name="parentOrder"
	 */
	public void setParentOrder(final Order parentOrder)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(parentOrder, "parentOrder");
		if (hasAlreadyAParentOrderDifferentFrom(parentOrder)) {
			final String error = "Cannot accept an Order ["
					+ parentOrder
					+ "] as this OrderPosition's ["
					+ this
					+ "] parent Order while already having a different parent Order ["
					+ this.parentOrder + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.parentOrder = parentOrder;
		if (!parentOrder.containsOrderPosition(this)) {
			parentOrder.addOrderPosition(this);
		}
	}

	/**
	 * @param order
	 * @return
	 */
	protected boolean hasAlreadyAParentOrderDifferentFrom(final Order order) {

		return ((this.parentOrder != null) && !this.parentOrder.equals(order));
	}

	/**
	 * @uml.property name="orderedOffer"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="orderPosition:com.acme.orderplacement.domain.item.offer.Offer"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_OFFER", unique = false, nullable = false)
	@ForeignKey(name = "ORDER_DETAIL_OFFER_FK")
	private Offer orderedOffer;

	/**
	 * Getter of the property <tt>orderedOffer</tt>
	 * 
	 * @return Returns the orderedOffer.
	 * @uml.property name="orderedOffer"
	 */
	public Offer getOrderedOffer() {
		return this.orderedOffer;
	}

	/**
	 * Setter of the property <tt>orderedOffer</tt>
	 * 
	 * @param offerToOrder
	 *            The orderedOffer to set.
	 * @throws IllegalArgumentException
	 *             If <code>orderedOffer</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>OrderPosition</code> already orders an
	 *             <code>Offer</code> different from <code>orderedOffer</code>
	 * @uml.property name="orderedOffer"
	 */
	public void setOrderedOffer(final Offer offerToOrder)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(offerToOrder, "orderedOffer");
		if (alreadyOrdersAnOfferDifferentFrom(offerToOrder)) {
			final String error = "Cannot order an Offer [" + offerToOrder
					+ "] while already ordering a different Offer ["
					+ this.orderedOffer + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.orderedOffer = offerToOrder;
	}

	/**
	 * @param candidateOffer
	 * @return
	 */
	protected boolean alreadyOrdersAnOfferDifferentFrom(
			final Offer candidateOffer) {

		return ((this.orderedOffer != null) && !(this.orderedOffer
				.equals(candidateOffer)));
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final OrderPosition o) {

		return BY_POSITION.compare(this, o);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME
				* result
				+ ((this.parentOrder == null) ? 0 : this.parentOrder.hashCode());
		result = PRIME * result + this.position;
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
		final OrderPosition other = (OrderPosition) obj;
		if (this.parentOrder == null) {
			if (other.parentOrder != null) {
				return false;
			}
		} else if (!this.parentOrder.equals(other.parentOrder)) {
			return false;
		}
		if (this.position != other.position) {
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
				"position", this.position).append("numberOfOrderedOffers",
				this.numberOfOrderedOffers).append("parentOrder",
				this.parentOrder).append("orderedOffer", this.orderedOffer)
				.toString();
	}

}
