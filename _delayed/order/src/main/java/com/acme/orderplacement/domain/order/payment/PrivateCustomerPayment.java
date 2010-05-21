/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;

import com.acme.orderplacement.domain.order.PrivateCustomerOrder;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * A concrete {@link Payment <code>Payment</code>} issued by a
 * {@link PrivateCustomer <code>PrivateCustomer</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::moment"
 */
@Entity
@Table(schema = "ORDER", name = "PRIVATE_CUSTOMER_PAYMENT")
@PrimaryKeyJoinColumn(name = "ID_PAYMENT")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = PrivateCustomerPayment.Queries.BY_PRIVATE_CUSTOMER_NUMBER, query = "from com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment privateCustomerPayment where privateCustomerPayment.privatePayer.privateCustomerNumber = :privateCustomerNumber"),
		@org.hibernate.annotations.NamedQuery(name = PrivateCustomerPayment.Queries.BY_ORDER_NUMBER, query = "from com.acme.orderplacement.domain.order.payment.PrivateCustomerPayment privateCustomerPayment where privateCustomerPayment.paidOrder.orderNumber = :orderNumber") })
public class PrivateCustomerPayment extends Payment implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PrivateCustomerPayment</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_PRIVATE_CUSTOMER_NUMBER = "privateCustomerPayment.byPrivateCustomerNumber";

		public static final String BY_ORDER_NUMBER = "privateCustomerPayment.byOrderNumber";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 4755587190460972816L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public PrivateCustomerPayment() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public PrivateCustomerPayment(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public PrivateCustomerPayment(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public PrivateCustomerPayment(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public PrivateCustomerPayment(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public PrivateCustomerPayment(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="privatePayer"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="privateCustomerPayment:com.acme.orderplacement.domain.people.customer.PrivateCustomer"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_PRIVATE_PAYER", unique = false, nullable = false)
	@ForeignKey(name = "PRIV_PAYMENT_PAYER_FK")
	private PrivateCustomer privatePayer;

	/**
	 * Getter of the property <tt>privatePayer</tt>
	 * 
	 * @return Returns the privatePayer.
	 * @uml.property name="privatePayer"
	 */
	public PrivateCustomer getPrivatePayer() {
		return this.privatePayer;
	}

	/**
	 * Setter of the property <tt>privatePayer</tt>
	 * 
	 * @param privatePayer
	 *            The privatePayer to set.
	 * @throws IllegalArgumentException
	 *             If <code>privatePayer</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>PrivateCustomerPayment</code> has already been
	 *             issued by a <code>PrivateCustomer</code> different from
	 *             <code>privatePayer</code>
	 * @uml.property name="privatePayer"
	 */
	public void setPrivatePayer(final PrivateCustomer privatePayer)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(privatePayer, "privatePayer");
		if (belongsToAPayerDifferentFrom(privatePayer)) {
			final String error = "Cannot accept a PrivateCustomer ["
					+ privatePayer + "] as this Payment's [" + this
					+ "] Payer while this Payment already has another Payer ["
					+ this.privatePayer + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.privatePayer = privatePayer;
	}

	/**
	 * @param candidatePayer
	 * @return
	 */
	protected boolean belongsToAPayerDifferentFrom(
			final PrivateCustomer candidatePayer) {

		return ((this.privatePayer != null) && !(this.privatePayer
				.equals(candidatePayer)));
	}

	/**
	 * @uml.property name="paidOrder"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "obtainedPayments:com.acme.orderplacement.domain.order.PrivateCustomerOrder"
	 * @uml.association name="PrivateCustomerOrder - Obtained
	 *                  PrivateCustomerPayments"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_PAID_ORDER", unique = false, nullable = false)
	@ForeignKey(name = "PRIV_PAYMENT_PAID_ORDER_FK")
	private PrivateCustomerOrder paidOrder;

	/**
	 * Getter of the property <tt>paidOrder</tt>
	 * 
	 * @return Returns the paidOrder.
	 * @uml.property name="paidOrder"
	 */
	public PrivateCustomerOrder getPaidOrder() {
		return this.paidOrder;
	}

	/**
	 * Setter of the property <tt>paidOrder</tt>
	 * 
	 * @param paidOrder
	 *            The paidOrder to set.
	 * @throws IllegalArgumentException
	 *             If <code>paidOrder</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>PrivateCustomerPayment</code> already pays for
	 *             a <code>PrivateCustomerOrder</code> different from
	 *             <code>paidOrder</code>
	 * @uml.property name="paidOrder"
	 */
	public void setPaidOrder(final PrivateCustomerOrder paidOrder)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(paidOrder, "paidOrder");
		if (alreadyPaysForAnOrderDifferentFrom(paidOrder)) {
			final String error = "Cannot accept an Order [" + paidOrder
					+ "] as being paid for by this Payment [" + this
					+ "] while this Payment already pays for another Order ["
					+ this.paidOrder + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.paidOrder = paidOrder;
		if (!(paidOrder.containsObtainedPayment(this))) {
			paidOrder.addObtainedPayment(this);
		}
	}

	/**
	 * @param candidateOrder
	 * @return
	 */
	protected boolean alreadyPaysForAnOrderDifferentFrom(
			final PrivateCustomerOrder candidateOrder) {

		return ((this.paidOrder != null) && !(this.paidOrder
				.equals(candidateOrder)));
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
				+ ((this.paidOrder == null) ? 0 : this.paidOrder.hashCode());
		result = PRIME
				* result
				+ ((this.privatePayer == null) ? 0 : this.privatePayer
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
		final PrivateCustomerPayment other = (PrivateCustomerPayment) obj;
		if (this.paidOrder == null) {
			if (other.paidOrder != null) {
				return false;
			}
		} else if (!this.paidOrder.equals(other.paidOrder)) {
			return false;
		}
		if (this.privatePayer == null) {
			if (other.privatePayer != null) {
				return false;
			}
		} else if (!this.privatePayer.equals(other.privatePayer)) {
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
				"privatePayer", this.privatePayer).append("paidOrder",
				this.paidOrder).toString();
	}

}
