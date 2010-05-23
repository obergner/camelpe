/**
 * 
 */
package com.acme.orderplacement.framework.domain.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Represents an amount of money in a specific {@link Currency
 * <code>Currency</code>}.
 * </p>
 * <p>
 * FIXME: This class should <strong>really</strong> be
 * <strong>immutable</strong> but cannot since it will be persisted by
 * <em>Hibernate</em>. Try to think of a solution.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Embeddable
public class MonetaryAmount implements Serializable {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -312284228676197024L;

	/**
	 * <p>
	 * The amount represented by this instance.
	 * </p>
	 */
	@Basic
	@Column(name = "MA_AMOUNT", unique = false, nullable = false)
	private BigDecimal amount;

	/**
	 * <p>
	 * The currency represented by this instance.
	 * </p>
	 */
	@Basic
	@Column(name = "MA_CURRENCY", unique = false, nullable = false)
	private Currency currency;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Empty noa-args default constructor. Needed by Hibernate.
	 * </p>
	 */
	public MonetaryAmount() {
		// Intentionally left blank
	}

	/**
	 * <p>
	 * Full constructor.
	 * </p>
	 * 
	 * @param amount
	 * @param currency
	 * @throws IllegalArgumentException
	 *             If any of the arguments is <code>null</code>
	 */
	public MonetaryAmount(final BigDecimal amount, final Currency currency)
			throws IllegalArgumentException {
		super();
		Validate.notNull(amount, "amount");
		Validate.notNull(currency, "currency");
		this.amount = amount;
		this.currency = currency;
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 * @throws IllegalArgumentException
	 *             If <code>amount</code> is <code>null</code>
	 */
	public void setAmount(final BigDecimal amount)
			throws IllegalArgumentException {
		Validate.notNull(amount, "amount");
		this.amount = amount;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return this.currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 * @throws IllegalArgumentException
	 *             If <code>currency</code> is <code>null</code>
	 */
	public void setCurrency(final Currency currency)
			throws IllegalArgumentException {
		Validate.notNull(currency, "currency");
		this.currency = currency;
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
		int result = 1;
		result = PRIME * result
				+ ((this.amount == null) ? 0 : this.amount.hashCode());
		result = PRIME * result
				+ ((this.currency == null) ? 0 : this.currency.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MonetaryAmount other = (MonetaryAmount) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!this.currency.equals(other.currency)) {
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
				"amount", this.amount).append("currency", this.currency)
				.toString();
	}

}
