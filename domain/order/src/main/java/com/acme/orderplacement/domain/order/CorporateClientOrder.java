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

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.company.people.CorporateClientRepresentative;
import com.acme.orderplacement.domain.order.payment.CorporateClientPayment;
import com.acme.orderplacement.domain.order.payment.Payment;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * An {@link Order <code>Order</code>} issued by a {@link CorporateClient
 * <code>CorporateClient</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Table(schema = "ORDER", name = "CORPORATE_CLIENT_ORDER")
@PrimaryKeyJoinColumn(name = "ID_ORDER")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = CorporateClientOrder.Queries.BY_CORPORATE_CLIENT_NUMBER, query = "from com.acme.orderplacement.domain.order.CorporateClientOrder corporateClientOrder where corporateClientOrder.corporateOrderer.corporateClientNumber = :corporateClientNumber"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientOrder.Queries.BY_CONTACT_PERSON_NUMBER, query = "from com.acme.orderplacement.domain.order.CorporateClientOrder corporateClientOrder where corporateClientOrder.contactPerson.corporateClientRepresentativeNumber = :corporateClientRepresentativeNumber") })
public class CorporateClientOrder extends Order implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>CorporateClientOrder</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_CORPORATE_CLIENT_NUMBER = "corporateClientOrder.byCorporateClientNumber";

		public static final String BY_CONTACT_PERSON_NUMBER = "corporateClientOrder.byContactPersonNumber";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = -7462263921906083845L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public CorporateClientOrder() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public CorporateClientOrder(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public CorporateClientOrder(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientOrder(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public CorporateClientOrder(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientOrder(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="corporateOrderer"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="corporateClientOrder:com.acme.orderplacement.domain.company.customer.CorporateClient"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_CORPORATE_ORDERER", unique = false, nullable = false)
	@ForeignKey(name = "CORP_ORDER_ORDERER_FK")
	private CorporateClient corporateOrderer;

	/**
	 * Getter of the property <tt>corporateOrderer</tt>
	 * 
	 * @return Returns the corporateOrderer.
	 * @uml.property name="corporateOrderer"
	 */
	public CorporateClient getCorporateOrderer() {
		return this.corporateOrderer;
	}

	/**
	 * Setter of the property <tt>corporateOrderer</tt>
	 * 
	 * @param corporateOrderer
	 *            The corporateOrderer to set.
	 * @throws IllegalArgumentException
	 *             If <code>corporateOrderer</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>CorporateClientOrder</code> has already an
	 *             Orderer different from <code>corporateOrderer</code>
	 * @uml.property name="corporateOrderer"
	 */
	public void setCorporateOrderer(final CorporateClient corporateOrderer)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(corporateOrderer, "corporateOrderer");
		if (hasAlreadyAnOrdererDifferentFrom(corporateOrderer)) {
			final String error = "Cannot accept a CorporateClient ["
					+ corporateOrderer + "] as this CorporateClientOrder's ["
					+ this
					+ "] Orderer while already having a different Orderer ["
					+ this.corporateOrderer + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.corporateOrderer = corporateOrderer;
	}

	/**
	 * @param candidateOrderer
	 * @return
	 */
	protected boolean hasAlreadyAnOrdererDifferentFrom(
			final CorporateClient candidateOrderer) {

		return ((this.corporateOrderer != null) && !this.corporateOrderer
				.equals(candidateOrderer));
	}

	/**
	 * @uml.property name="contactPerson"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="corporateClientOrder:com.acme.orderplacement.domain.company.people.CorporateClientRepresentative"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_CONTACT_PERSON", unique = false, nullable = false)
	@ForeignKey(name = "CORP_ORDER_CONTACT_PERSON_FK")
	private CorporateClientRepresentative contactPerson;

	/**
	 * Getter of the property <tt>contactPerson</tt>
	 * 
	 * @return Returns the contactPerson.
	 * @uml.property name="contactPerson"
	 */
	public CorporateClientRepresentative getContactPerson() {
		return this.contactPerson;
	}

	/**
	 * Setter of the property <tt>contactPerson</tt>
	 * 
	 * @param contactPerson
	 *            The contactPerson to set.
	 * @throws IllegalArgumentException
	 *             If <code>contactPerson</code> is <code>null</code>
	 * @uml.property name="contactPerson"
	 */
	public void setContactPerson(
			final CorporateClientRepresentative contactPerson)
			throws IllegalArgumentException {
		Validate.notNull(contactPerson, "contactPerson");
		this.contactPerson = contactPerson;
	}

	/**
	 * @uml.property name="obtainedPayments"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "paidOrder:com.acme.orderplacement.domain.order.payment.CorporateClientPayment"
	 * @uml.association name="CorporateClientOrder - Obtained
	 *                  CorporateClientPayments"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "corporatePayer")
	@Sort(type = SortType.COMPARATOR, comparator = Payment.ByObtainedOnComparator.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private SortedSet<CorporateClientPayment> obtainedPayments;

	/**
	 * Getter of the property <tt>obtainedPayments</tt>
	 * 
	 * @return Returns the obtainedPayments.
	 * @uml.property name="obtainedPayments"
	 */
	public SortedSet<CorporateClientPayment> getObtainedPayments() {
		if (this.obtainedPayments == null) {
			this.obtainedPayments = new TreeSet<CorporateClientPayment>();
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
	public Iterator<CorporateClientPayment> obtainedPaymentsIterator() {
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
			final CorporateClientPayment corporateClientPayment) {
		return getObtainedPayments().contains(corporateClientPayment);
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
			final Collection<CorporateClientPayment> obtainedPayments) {
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
	public CorporateClientPayment[] obtainedPaymentsToArray() {
		return getObtainedPayments().toArray(
				new CorporateClientPayment[getObtainedPayments().size()]);
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
	public CorporateClientPayment[] obtainedPaymentsToArray(
			final CorporateClientPayment[] obtainedPayments) {
		return getObtainedPayments().toArray(obtainedPayments);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws IllegalArgumentException
	 *             If <code>corporateClientPayment</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>corporateClientPayment</code> already pays for
	 *             another <code>CorporateClientOrder</code>
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="obtainedPayments"
	 */
	public boolean addObtainedPayment(
			final CorporateClientPayment corporateClientPayment)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(corporateClientPayment, "corporateClientPayment");
		if (isDifferentFromOrderReferencedBy(corporateClientPayment)) {
			final String error = "Cannot accept a Payment ["
					+ corporateClientPayment
					+ "] as a Payment for this Order [" + this
					+ "] that already pays for another Order ["
					+ corporateClientPayment.getPaidOrder() + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		if (corporateClientPayment.getPaidOrder() == null) {
			corporateClientPayment.setPaidOrder(this);
		}
		return getObtainedPayments().add(corporateClientPayment);
	}

	/**
	 * @param candidatePayment
	 * @return
	 */
	protected boolean isDifferentFromOrderReferencedBy(
			final CorporateClientPayment candidatePayment) {

		return ((candidatePayment.getPaidOrder() != null) && !(candidatePayment
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
			final SortedSet<CorporateClientPayment> obtainedPayments)
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		for (final CorporateClientPayment corporateClientPayment : obtainedPayments) {
			addObtainedPayment(corporateClientPayment);
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
			final CorporateClientPayment corporateClientPayment) {
		return getObtainedPayments().remove(corporateClientPayment);
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
				+ ((this.corporateOrderer == null) ? 0 : this.corporateOrderer
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
		final CorporateClientOrder other = (CorporateClientOrder) obj;
		if (this.corporateOrderer == null) {
			if (other.corporateOrderer != null) {
				return false;
			}
		} else if (!this.corporateOrderer.equals(other.corporateOrderer)) {
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
				"corporateOrderer", this.corporateOrderer).append(
				"contactPerson", this.contactPerson).toString();
	}

}
