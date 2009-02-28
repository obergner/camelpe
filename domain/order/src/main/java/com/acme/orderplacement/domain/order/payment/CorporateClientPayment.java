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

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.order.CorporateClientOrder;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * A concrete {@link Payment <code>Payment</code>} issued by a
 * {@link CorporateClient <code>CorporateClient</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::moment"
 */
@Entity
@Table(schema = "ORDER", name = "CORPORATE_CLIENT_PAYMENT")
@PrimaryKeyJoinColumn(name = "ID_PAYMENT")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = CorporateClientPayment.Queries.BY_CORPORATE_CLIENT_NUMBER, query = "from com.acme.orderplacement.domain.order.payment.CorporateClientPayment corporateClientPayment where corporateClientPayment.corporatePayer.corporateClientNumber = :corporateClientNumber"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientPayment.Queries.BY_ORDER_NUMBER, query = "from com.acme.orderplacement.domain.order.payment.CorporateClientPayment corporateClientPayment where corporateClientPayment.paidOrder.orderNumber = :orderNumber") })
public class CorporateClientPayment extends Payment implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>CorporateClientPayment</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_CORPORATE_CLIENT_NUMBER = "corporateClientPayment.byCorporateClientNumber";

		public static final String BY_ORDER_NUMBER = "corporateClientPayment.byOrderNumber";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = -6851107639668895377L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public CorporateClientPayment() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public CorporateClientPayment(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public CorporateClientPayment(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientPayment(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public CorporateClientPayment(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientPayment(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="corporatePayer"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="corporateClientPayment:com.acme.orderplacement.domain.company.customer.CorporateClient"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_CORPORATE_PAYER", unique = false, nullable = false)
	@ForeignKey(name = "CORP_PAYMENT_PAYER_FK")
	private CorporateClient corporatePayer;

	/**
	 * Getter of the property <tt>corporatePayer</tt>
	 * 
	 * @return Returns the corporatePayer.
	 * @uml.property name="corporatePayer"
	 */
	public CorporateClient getCorporatePayer() {
		return this.corporatePayer;
	}

	/**
	 * Setter of the property <tt>corporatePayer</tt>
	 * 
	 * @param corporatePayer
	 *            The corporatePayer to set.
	 * @throws IllegalArgumentException
	 *             If <code>corporatePayer</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If this <code>CorporateClientPayment</code> has already been
	 *             issued by a <code>CorporateClient</code> different from
	 *             <code>corporatePayer</code>
	 * @uml.property name="corporatePayer"
	 */
	public void setCorporatePayer(final CorporateClient corporatePayer)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(corporatePayer, "corporatePayer");
		if (belongsToAPayerDifferentFrom(corporatePayer)) {
			final String error = "Cannot accept a CorporateClient ["
					+ corporatePayer + "] as this Payment's [" + this
					+ "] Payer while this Payment already has another Payer ["
					+ this.corporatePayer + "]";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.corporatePayer = corporatePayer;
	}

	/**
	 * @param candidatePayer
	 * @return
	 */
	protected boolean belongsToAPayerDifferentFrom(
			final CorporateClient candidatePayer) {

		return ((this.corporatePayer != null) && !(this.corporatePayer
				.equals(candidatePayer)));
	}

	/**
	 * @uml.property name="paidOrder"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "obtainedPayments:com.acme.orderplacement.domain.order.CorporateClientOrder"
	 * @uml.association name="CorporateClientOrder - Obtained
	 *                  CorporateClientPayments"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_PAID_ORDER", unique = false, nullable = false)
	@ForeignKey(name = "CORP_PAYMENT_PAID_ORDER_FK")
	private CorporateClientOrder paidOrder;

	/**
	 * Getter of the property <tt>paidOrder</tt>
	 * 
	 * @return Returns the paidOrder.
	 * @uml.property name="paidOrder"
	 */
	public CorporateClientOrder getPaidOrder() {
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
	 *             If this <code>CorporateClientPayment</code> already pays for
	 *             a <code>CorporateClientOrder</code> different from
	 *             <code>paidOrder</code>
	 * @uml.property name="paidOrder"
	 */
	public void setPaidOrder(final CorporateClientOrder paidOrder)
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
			final CorporateClientOrder candidateOrder) {

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
		result = PRIME
				* result
				+ ((this.corporatePayer == null) ? 0 : this.corporatePayer
						.hashCode());
		result = PRIME * result
				+ ((this.paidOrder == null) ? 0 : this.paidOrder.hashCode());
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
		final CorporateClientPayment other = (CorporateClientPayment) obj;
		if (this.corporatePayer == null) {
			if (other.corporatePayer != null) {
				return false;
			}
		} else if (!this.corporatePayer.equals(other.corporatePayer)) {
			return false;
		}
		if (this.paidOrder == null) {
			if (other.paidOrder != null) {
				return false;
			}
		} else if (!this.paidOrder.equals(other.paidOrder)) {
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
				"corporatePayer", this.corporatePayer).append("paidOrder",
				this.paidOrder).toString();
	}

}
