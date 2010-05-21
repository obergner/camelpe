/**
 * 
 */
package com.acme.orderplacement.domain.order;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * Abstract base class for all <tt>Orders</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "ORDER", name = "ORDER", uniqueConstraints = @UniqueConstraint(columnNames = { "ORDER_NUMBER" }))
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_ORDER")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.BY_ORDER_NUMBER, query = "from com.acme.orderplacement.domain.order.Order order where order.orderNumber = :orderNumber"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.BY_ORDER_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.order.Order order where order.orderNumber like '%:orderNumber%'"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.ORDERED_BEFORE, query = "from com.acme.orderplacement.domain.order.Order order where order.orderedOn > :date"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.ORDERED_AFTER, query = "from com.acme.orderplacement.domain.order.Order order where order.orderedOn < :date"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.ORDERED_BETWEEN, query = "from com.acme.orderplacement.domain.order.Order order where (order.orderedOn > :startDate and order.orderedOn < :endDate)"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.FULFILLED_BEFORE, query = "from com.acme.orderplacement.domain.order.Order order where order.fulfilledOn > :date"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.FULFILLED_AFTER, query = "from com.acme.orderplacement.domain.order.Order order where order.fulfilledOn < :date"),
		@org.hibernate.annotations.NamedQuery(name = Order.Queries.FULFILLED_BETWEEN, query = "from com.acme.orderplacement.domain.order.Order order where (order.fulfilledOn > :startDate and order.fulfilledOn < :endDate)") })
public abstract class Order extends AbstractAuditableDomainObject<Long> {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>Order</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_ORDER_NUMBER = "order.byOrderNumber";

		public static final String BY_ORDER_NUMBER_LIKE = "order.byOrderNumberLike";

		public static final String ORDERED_BEFORE = "order.orderedBefore";

		public static final String ORDERED_AFTER = "order.orderedAfter";

		public static final String ORDERED_BETWEEN = "order.orderedBetween";

		public static final String FULFILLED_BEFORE = "order.fulfilledBefore";

		public static final String FULFILLED_AFTER = "order.fulfilledAfter";

		public static final String FULFILLED_BETWEEN = "order.fulfilledBetween";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -3149797167737635232L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Order() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Order(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Order(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Order(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Order(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Order(final Long id, final Integer version, final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * <tt>Business Key</tt> uniquely identifying this <code>Order</code>. Must
	 * <strong>not</strong> be <code>null</code>.
	 * </p>
	 */
	@NotNull
	@Length(min = 5, max = 30)
	@Basic
	@Column(name = "ORDER_NUMBER", unique = true, nullable = false, length = 30)
	private String orderNumber;

	/**
	 * @param orderNumber
	 *            the orderNumber to set
	 * @throws IllegalArgumentException
	 *             If <code>orderNumber</code> is <code>null</code> or blank
	 */
	public void setOrderNumber(final String orderNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(orderNumber, "orderNumber");
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return this.orderNumber;
	}

	/**
	 * <p>
	 * When was this <code>Order</code> placed? Must <strong>not</strong> be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="orderedOn"
	 */
	@NotNull
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDERED_ON", unique = false, nullable = false)
	private Date orderedOn;

	/**
	 * Getter of the property <tt>orderedOn</tt>
	 * 
	 * @return Returns the orderedOn.
	 * @uml.property name="orderedOn"
	 */
	public Date getOrderedOn() {
		return this.orderedOn;
	}

	/**
	 * Setter of the property <tt>orderedOn</tt>
	 * 
	 * @param orderedOn
	 *            The orderedOn to set.
	 * @throws IllegalArgumentException
	 *             If <code>orderedOn</code> is <code>null</code>
	 * @uml.property name="orderedOn"
	 */
	public void setOrderedOn(final Date orderedOn)
			throws IllegalArgumentException {
		Validate.notNull(orderedOn, "orderedOn");
		this.orderedOn = orderedOn;
	}

	/**
	 * <p>
	 * When was this <code>Order</code> fulfilled, e.g. all associated
	 * <code>OrderPosition</code>s have been delivered.
	 * </p>
	 * 
	 * @uml.property name="fulfilledOn"
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FULFILLED_ON", unique = false, nullable = true)
	private Date fulfilledOn;

	/**
	 * Getter of the property <tt>fulfilledOn</tt>
	 * 
	 * @return Returns the fulfilledOn.
	 * @uml.property name="fulfilledOn"
	 */
	public Date getFulfilledOn() {
		return this.fulfilledOn;
	}

	/**
	 * Setter of the property <tt>fulfilledOn</tt>
	 * 
	 * @param fulfilledOn
	 *            The fulfilledOn to set.
	 * @throws IllegalArgumentException
	 *             If <code>orderedOn</code> is <code>null</code>
	 * @uml.property name="fulfilledOn"
	 */
	public void setFulfilledOn(final Date fulfilledOn)
			throws IllegalArgumentException {
		Validate.notNull(fulfilledOn, "fulfilledOn");
		this.fulfilledOn = fulfilledOn;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="shipToAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "order:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_SHIPPING_ADDRESS", unique = false, nullable = false)
	private PostalAddress shipToAddress;

	/**
	 * Getter of the property <tt>shipToAddress</tt>
	 * 
	 * @return Returns the shipToAddress.
	 * @uml.property name="shipToAddress"
	 */
	public PostalAddress getShipToAddress() {
		return this.shipToAddress;
	}

	/**
	 * Setter of the property <tt>shipToAddress</tt>
	 * 
	 * @param shipToAddress
	 *            The shipToAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>shipToAddress</code> is <code>null</code>
	 * @uml.property name="shipToAddress"
	 */
	public void setShipToAddress(final PostalAddress shipToAddress)
			throws IllegalArgumentException {
		Validate.notNull(shipToAddress, "shipToAddress");
		this.shipToAddress = shipToAddress;
	}

	/**
	 * @uml.property name="billToAddress"
	 * @uml.associationEnd inverse=
	 *                     "order:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_BILLING_ADDRESS", unique = false, nullable = true)
	private PostalAddress billToAddress;

	/**
	 * Getter of the property <tt>billToAddress</tt>
	 * 
	 * @return Returns the billToAddress.
	 * @uml.property name="billToAddress"
	 */
	public PostalAddress getBillToAddress() {
		if (this.billToAddress == null) {
			return getShipToAddress();
		}
		return this.billToAddress;
	}

	/**
	 * Setter of the property <tt>billToAddress</tt>
	 * 
	 * @param billToAddress
	 *            The billToAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>billToAddress</code> is <code>null</code>
	 * @uml.property name="billToAddress"
	 */
	public void setBillToAddress(final PostalAddress billToAddress)
			throws IllegalArgumentException {
		Validate.notNull(billToAddress, "billToAddress");
		this.billToAddress = billToAddress;
	}

	/**
	 * @uml.property name="orderPositions"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite"
	 *                     inverse=
	 *                     "parentOrder:com.acme.orderplacement.domain.order.OrderPosition"
	 * @uml.association name="Order - OrderPositions"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentOrder")
	@Sort(type = SortType.COMPARATOR, comparator = OrderPosition.ByPositionComparator.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private SortedSet<OrderPosition> orderPositions;

	/**
	 * Getter of the property <tt>orderPositions</tt>
	 * 
	 * @return Returns the orderPositions.
	 * @uml.property name="orderPositions"
	 */
	public SortedSet<OrderPosition> getOrderPositions() {
		if (this.orderPositions == null) {
			this.orderPositions = new TreeSet<OrderPosition>();
		}
		return this.orderPositions;
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="orderPositions"
	 */
	public Iterator<OrderPosition> orderPositionsIterator() {
		return getOrderPositions().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="orderPositions"
	 */
	public boolean isOrderPositionsEmpty() {
		return getOrderPositions().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="orderPositions"
	 */
	public boolean containsOrderPosition(final OrderPosition orderPosition) {
		return getOrderPositions().contains(orderPosition);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="orderPositions"
	 */
	public boolean containsAllOrderPositions(
			final Collection<OrderPosition> orderPositions) {
		return getOrderPositions().containsAll(orderPositions);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="orderPositions"
	 */
	public int orderPositionsSize() {
		return getOrderPositions().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="orderPositions"
	 */
	public OrderPosition[] orderPositionsToArray() {
		return getOrderPositions().toArray(
				new OrderPosition[getOrderPositions().size()]);
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
	 * @uml.property name="orderPositions"
	 */
	public OrderPosition[] orderPositionsToArray(
			final OrderPosition[] orderPositions) {
		return getOrderPositions().toArray(orderPositions);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>orderPosition</code> already belongs to an
	 *             <code>Order</code> different from this <code>Order</code>
	 * @throws IllegalArgumentException
	 *             If <code>orderPosition</code> is <code>null</code>
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="orderPositions"
	 */
	public boolean addOrderPosition(final OrderPosition orderPosition)
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		Validate.notNull(orderPosition, "orderPosition");
		if (isDifferentFromTheOrderReferencedBy(orderPosition)) {
			final String error = "Cannot accept an OrderPosition ["
					+ orderPosition
					+ "] as this Order's ["
					+ this
					+ "] OrderPosition that already belongs to a different Order ["
					+ orderPosition.getParentOrder() + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		if (orderPosition.getParentOrder() == null) {
			orderPosition.setParentOrder(this);
		}
		return getOrderPositions().add(orderPosition);
	}

	/**
	 * @param orderPosition
	 * @return
	 */
	private boolean isDifferentFromTheOrderReferencedBy(
			final OrderPosition orderPosition) {
		return (orderPosition.getParentOrder() != null)
				&& !orderPosition.getParentOrder().equals(this);
	}

	/**
	 * Setter of the property <tt>orderPositions</tt>
	 * 
	 * @param orderPositions
	 *            the orderPositions to set.
	 * @throws CollaborationPreconditionsNotMetException
	 * @uml.property name="orderPositions"
	 */
	public void setOrderPositions(final SortedSet<OrderPosition> orderPositions)
			throws CollaborationPreconditionsNotMetException {
		for (final OrderPosition orderPosition : orderPositions) {
			addOrderPosition(orderPosition);
		}
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="orderPositions"
	 */
	public boolean removeOrderPosition(final OrderPosition orderPosition) {
		return getOrderPositions().remove(orderPosition);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="orderPositions"
	 */
	public void clearOrderPositions() {
		getOrderPositions().clear();
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
				+ ((this.orderNumber == null) ? 0 : this.orderNumber.hashCode());
		result = PRIME * result
				+ ((this.orderedOn == null) ? 0 : this.orderedOn.hashCode());
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
		final Order other = (Order) obj;
		if (this.orderNumber == null) {
			if (other.orderNumber != null) {
				return false;
			}

		} else if (!this.orderNumber.equals(other.orderNumber)) {
			return false;
		}
		if (this.orderedOn == null) {
			if (other.orderedOn != null) {
				return false;
			}

		} else if (!this.orderedOn.equals(other.orderedOn)) {
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
				"orderNumber", this.orderNumber).append("orderedOn",
				this.orderedOn).append("fulfilledOn", this.fulfilledOn).append(
				"shipToAddress", this.shipToAddress).append("billToAddress",
				this.billToAddress).toString();
	}

}
