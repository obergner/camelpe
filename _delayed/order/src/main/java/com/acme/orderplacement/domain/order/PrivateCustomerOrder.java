/**
 * 
 */
package com.acme.orderplacement.domain.order;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.acme.orderplacement.domain.order.payment.Payment;
import com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * An {@link Order <code>Order</code>} issued by a {@link PrivateCustomer
 * <code>PrivateCustomer</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Table(schema = "ORDER", name = "PRIVATE_CUSTOMER_ORDER")
@PrimaryKeyJoinColumn(name = "ID_ORDER")
@org.hibernate.annotations.NamedQueries( { @org.hibernate.annotations.NamedQuery(name = PrivateCustomerOrder.Queries.BY_PRIVATE_CUSTOMER_NUMBER, query = "from com.acme.orderplacement.domain.order.PrivateCustomerOrder privateCustomerOrder where privateCustomerOrder.privateOrderer.customerNumber = :customerNumber") })
public class PrivateCustomerOrder extends Order implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PrivateCustomerOrder</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_PRIVATE_CUSTOMER_NUMBER = "privateCustomerOrder.byPrivateCustomerNumber";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 3608208514024237361L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public PrivateCustomerOrder() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public PrivateCustomerOrder(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public PrivateCustomerOrder(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public PrivateCustomerOrder(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public PrivateCustomerOrder(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public PrivateCustomerOrder(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="privateOrderer"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="privateCustomerOrder:com.acme.orderplacement.domain.people.customer.PrivateCustomer"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_PRIVATE_ORDERER", unique = false, nullable = false)
	@ForeignKey(name = "PRIV_ORDER_ORDERER_FK")
	private PrivateCustomer privateOrderer;

	/**
	 * Getter of the property <tt>privateOrderer</tt>
	 * 
	 * @return Returns the privateOrderer.
	 * @uml.property name="privateOrderer"
	 */
	public PrivateCustomer getPrivateOrderer() {
		return this.privateOrderer;
	}

	/**
	 * Setter of the property <tt>privateOrderer</tt>
	 * 
	 * @param privateOrderer
	 *            The privateOrderer to set.
	 * @throws IllegalArgumentException
	 *             If <code>privateOrderer</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>PrivateCustomerOrder</code> has already an
	 *             Orderer different from <code>privateOrderer</code>
	 * @uml.property name="privateOrderer"
	 */
	public void setPrivateOrderer(final PrivateCustomer privateOrderer)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(privateOrderer, "privateOrderer");
		if (hasAlreadyAnOrdererDifferentFrom(privateOrderer)) {
			final String error = "Cannot accept a PrivateCutomer ["
					+ privateOrderer + "] as this PrivateCustomerOrder's ["
					+ this
					+ "] Orderer while already having a different Orderer ["
					+ this.privateOrderer + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.privateOrderer = privateOrderer;
	}

	/**
	 * @param candidateOrderer
	 * @return
	 */
	protected boolean hasAlreadyAnOrdererDifferentFrom(
			final PrivateCustomer candidateOrderer) {

		return ((this.privateOrderer != null) && !this.privateOrderer
				.equals(candidateOrderer));
	}

	/**
	 * @uml.property name="obtainedPayments"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "paidOrder:com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment"
	 * @uml.association name="PrivateCustomerOrder - Obtained
	 *                  PrivateCustomerPayments"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "privatePayer")
	@Sort(type = SortType.COMPARATOR, comparator = Payment.ByObtainedOnComparator.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private SortedSet<PrivateCustomerPayment> obtainedPayments;

	/**
	 * Getter of the property <tt>obtainedPayments</tt>
	 * 
	 * @return Returns the obtainedPayments.
	 * @uml.property name="obtainedPayments"
	 */
	public SortedSet<PrivateCustomerPayment> getObtainedPayments() {
		if (this.obtainedPayments == null) {
			this.obtainedPayments = new TreeSet<PrivateCustomerPayment>();
		}
		return this.obtainedPayments;
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="obtainedPayments"
	 */
	public Iterator<PrivateCustomerPayment> obtainedPaymentsIterator() {
		return getObtainedPayments().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="obtainedPayments"
	 */
	public boolean isObtainedPaymentsEmpty() {
		return getObtainedPayments().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="obtainedPayments"
	 */
	public boolean containsObtainedPayment(
			final PrivateCustomerPayment privateCustomerPayment) {
		return getObtainedPayments().contains(privateCustomerPayment);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="obtainedPayments"
	 */
	public boolean containsAllObtainedPayments(
			final Collection<PrivateCustomerPayment> obtainedPayments) {
		return getObtainedPayments().containsAll(obtainedPayments);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="obtainedPayments"
	 */
	public int obtainedPaymentsSize() {
		return getObtainedPayments().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="obtainedPayments"
	 */
	public PrivateCustomerPayment[] obtainedPaymentsToArray() {
		return getObtainedPayments().toArray(
				new PrivateCustomerPayment[getObtainedPayments().size()]);
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
	 * @uml.property name="obtainedPayments"
	 */
	public PrivateCustomerPayment[] obtainedPaymentsToArray(
			final PrivateCustomerPayment[] obtainedPayments) {
		return getObtainedPayments().toArray(obtainedPayments);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws IllegalArgumentException
	 *             If <code>privateCustomerPayment</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>privateCustomerPayment</code> already pays for
	 *             another <code>PrivateCustomerOrder</code>
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="obtainedPayments"
	 */
	public boolean addObtainedPayment(
			final PrivateCustomerPayment privateCustomerPayment)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(privateCustomerPayment, "privateCustomerPayment");
		if (isDifferentFromOrderReferencedBy(privateCustomerPayment)) {
			final String error = "Cannot accept a Payment ["
					+ privateCustomerPayment
					+ "] as a Payment for this Order [" + this
					+ "] that already pays for another Order ["
					+ privateCustomerPayment.getPaidOrder() + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		if (privateCustomerPayment.getPaidOrder() == null) {
			privateCustomerPayment.setPaidOrder(this);
		}
		return getObtainedPayments().add(privateCustomerPayment);
	}

	/**
	 * @param privateCustomerPayment
	 * @return
	 */
	protected boolean isDifferentFromOrderReferencedBy(
			final PrivateCustomerPayment privateCustomerPayment) {

		return ((privateCustomerPayment.getPaidOrder() != null) && !(privateCustomerPayment
				.getPaidOrder().equals(this)));
	}

	/**
	 * Setter of the property <tt>obtainedPayments</tt>
	 * 
	 * @param obtainedPayments
	 *            the obtainedPayments to set.
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 * @uml.property name="obtainedPayments"
	 */
	public void setObtainedPayments(
			final SortedSet<PrivateCustomerPayment> obtainedPayments)
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		for (final PrivateCustomerPayment privateCustomerPayment : obtainedPayments) {
			addObtainedPayment(privateCustomerPayment);
		}
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="obtainedPayments"
	 */
	public boolean removeObtainedPayment(
			final PrivateCustomerPayment privateCustomerPayment) {
		return getObtainedPayments().remove(privateCustomerPayment);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="obtainedPayments"
	 */
	public void clearObtainedPayments() {
		getObtainedPayments().clear();
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
				+ ((this.privateOrderer == null) ? 0 : this.privateOrderer
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
		final PrivateCustomerOrder other = (PrivateCustomerOrder) obj;
		if (this.privateOrderer == null) {
			if (other.privateOrderer != null) {
				return false;
			}
		} else if (!this.privateOrderer.equals(other.privateOrderer)) {
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
				"privateOrderer", this.privateOrderer).toString();
	}

}
