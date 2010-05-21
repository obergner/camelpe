/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;

import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.domain.support.money.MonetaryAmount;

/**
 * <p>
 * Abstract bas class for all concrete <tt>Payments</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::moment"
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "ORDER", name = "PAYMENT")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_PAYMENT")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.AMOUNT_HIGHER_THAN, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where (payment.paidAmount.currency = :paidAmount.currency and payment.paidAmount.amount > :paidAmount.amount)"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.AMOUNT_LESS_THAN, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where (payment.paidAmount.currency = :paidAmount.currency and payment.paidAmount.amount < :paidAmount.amount)"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.OBTAINED_BEFORE, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where payment.obtainedOn > :date"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.OBTAINED_AFTER, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where payment.obtainedOn < :date"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.OBTAINED_BETWEEN, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where (payment.obtainedOn > :startDate and payment.obtainedOn < :endDate)"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.BOOKED_BEFORE, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where payment.bookedOn > :date"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.BOOKED_AFTER, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where payment.bookedOn < :date"),
		@org.hibernate.annotations.NamedQuery(name = Payment.Queries.BOOKED_BETWEEN, query = "from com.acme.orderplacement.domain.order.payment.Payment payment where (payment.bookedOn > :startDate and payment.bookedOn < :endDate)") })
public abstract class Payment extends AbstractAuditableDomainObject<Long>
		implements Comparable<Payment> {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -5497691415881181326L;

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>Payment</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String AMOUNT_HIGHER_THAN = "payment.amountHigherThan";

		public static final String AMOUNT_LESS_THAN = "payment.amountLessThan";

		public static final String OBTAINED_BEFORE = "payment.obtainedBefore";

		public static final String OBTAINED_AFTER = "payment.obtainedAfter";

		public static final String OBTAINED_BETWEEN = "payment.obtainedBetween";

		public static final String BOOKED_BEFORE = "payment.bookedBefore";

		public static final String BOOKED_AFTER = "payment.bookedAfter";

		public static final String BOOKED_BETWEEN = "payment.bookedBetween";
	}

	// ------------------------------------------------------------------------
	// Comparators
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A {@link Comparator <code>Comparator</code>} for comparing
	 * <code>PaymentType</code>s by their <code>obtainedOn</code> date.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public static final class ByObtainedOnComparator implements
			Comparator<Payment> {

		/**
		 * <p>
		 * This <code>Constructor</code> cannot be made <code>private</code>
		 * since the JPA/Hibernate runtime needs to instantiate this
		 * <code>Comparator</code> dynamically.
		 * </p>
		 */
		public ByObtainedOnComparator() {
			super();
		}

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final Payment payment1, final Payment payment2) {
			if ((payment1 == null) || (payment1.obtainedOn == null)) {
				return -1;
			}
			if ((payment2 == null) || (payment2.obtainedOn == null)) {
				return +1;
			}

			return payment1.obtainedOn.compareTo(payment2.obtainedOn);
		}

	}

	/**
	 * Compare two <code>PaymentType</code>s by their <code>obtainedOn</code>
	 * dates.
	 */
	public static final ByObtainedOnComparator BY_OBTAINED_ON = new ByObtainedOnComparator();

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Payment() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Payment(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Payment(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Payment(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Payment(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Payment(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * The amount paid. Must <strong>not</strong> be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="paidAmount"
	 */
	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = "amount", column = @Column(name = "PAID_AMOUNT_AMOUNT")),
			@AttributeOverride(name = "currency", column = @Column(name = "PAID_AMOUNT_CURRENCY")) })
	private MonetaryAmount paidAmount;

	/**
	 * Getter of the property <tt>paidAmount</tt>
	 * 
	 * @return Returns the paidAmount.
	 * @uml.property name="paidAmount"
	 */
	public MonetaryAmount getPaidAmount() {
		return this.paidAmount;
	}

	/**
	 * Setter of the property <tt>paidAmount</tt>
	 * 
	 * @param paidAmount
	 *            The paidAmount to set.
	 * @throws IllegalArgumentException
	 *             If <code>paidAmount</code> is <code>null</code>
	 * @uml.property name="paidAmount"
	 */
	public void setPaidAmount(final MonetaryAmount paidAmount)
			throws IllegalArgumentException {
		Validate.notNull(paidAmount, "paidAmount");
		this.paidAmount = paidAmount;
	}

	/**
	 * <p>
	 * When has this <code>Payment</code> been obtained? Must
	 * <strong>not</strong> be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="obtainedOn"
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBTAINED_ON", unique = false, nullable = false)
	private Date obtainedOn;

	/**
	 * Getter of the property <tt>obtainedOn</tt>
	 * 
	 * @return Returns the obtainedOn.
	 * @uml.property name="obtainedOn"
	 */
	public Date getObtainedOn() {
		return this.obtainedOn;
	}

	/**
	 * Setter of the property <tt>obtainedOn</tt>
	 * 
	 * @param obtainedOn
	 *            The obtainedOn to set.
	 * @throws IllegalArgumentException
	 *             If <code>obtainedOn</code> is <code>null</code>
	 * @uml.property name="obtainedOn"
	 */
	public void setObtainedOn(final Date obtainedOn)
			throws IllegalArgumentException {
		Validate.notNull(obtainedOn, "obtainedOn");
		this.obtainedOn = obtainedOn;
	}

	/**
	 * <p>
	 * When has this <code>Payment</code> been booked?
	 * </p>
	 * 
	 * @uml.property name="bookeOn"
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BOOKED_ON", unique = false, nullable = true)
	private Date bookedOn;

	/**
	 * Getter of the property <tt>bookeOn</tt>
	 * 
	 * @return Returns the bookeOn.
	 * @uml.property name="bookeOn"
	 */
	public Date getBookedOn() {
		return this.bookedOn;
	}

	/**
	 * Setter of the property <tt>bookeOn</tt>
	 * 
	 * @param bookedOn
	 *            The bookeOn to set.
	 * @throws IllegalArgumentException
	 *             If <code>bookedOn</code> is <code>null</code>
	 * @uml.property name="bookedOn"
	 */
	public void setBookedOn(final Date bookedOn)
			throws IllegalArgumentException {
		Validate.notNull(bookedOn, "bookedOn");
		this.bookedOn = bookedOn;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="paymentType"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "payment:com.acme.orderplacement.domain.order.payment.PaymentType"
	 * @uml.association name="Payment - PaymentType"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_PAYMENT_TYPE", unique = false, nullable = false)
	@ForeignKey(name = "PAYMENT_PAYMENT_TYPE_FK")
	private PaymentType paymentType;

	/**
	 * Getter of the property <tt>paymentType</tt>
	 * 
	 * @return Returns the paymentType.
	 * @uml.property name="paymentType"
	 */
	public PaymentType getPaymentType() {
		return this.paymentType;
	}

	/**
	 * Setter of the property <tt>paymentType</tt>
	 * 
	 * @param paymentType
	 *            The paymentType to set.
	 * @throws IllegalArgumentException
	 *             If <code>paymentType</code> is <code>null</code>
	 * @uml.property name="paymentType"
	 */
	public void setPaymentType(final PaymentType paymentType)
			throws IllegalArgumentException {
		Validate.notNull(paymentType, "paymentType");
		this.paymentType = paymentType;
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Payment o) {

		return BY_OBTAINED_ON.compare(this, o);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result
				+ ((this.obtainedOn == null) ? 0 : this.obtainedOn.hashCode());
		result = PRIME * result
				+ ((this.paidAmount == null) ? 0 : this.paidAmount.hashCode());
		result = PRIME
				* result
				+ ((this.paymentType == null) ? 0 : this.paymentType.hashCode());
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
		final Payment other = (Payment) obj;
		if (this.obtainedOn == null) {
			if (other.obtainedOn != null) {
				return false;
			}
		} else if (!this.obtainedOn.equals(other.obtainedOn)) {
			return false;
		}
		if (this.paidAmount == null) {
			if (other.paidAmount != null) {
				return false;
			}
		} else if (!this.paidAmount.equals(other.paidAmount)) {
			return false;
		}
		if (this.paymentType == null) {
			if (other.paymentType != null) {
				return false;
			}
		} else if (!this.paymentType.equals(other.paymentType)) {
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
				"paidAmount", this.paidAmount).append("obtainedOn",
				this.obtainedOn).append("bookedOn", this.bookedOn).append(
				"paymentType", this.paymentType).toString();
	}

}
