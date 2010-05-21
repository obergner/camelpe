/**
 * 
 */
package com.acme.orderplacement.domain.order.payment;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.support.AbstractVersionedDomainObject;

/**
 * <p>
 * TODO: Insert short summary for PaymentType
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for PaymentType
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::description"
 */
@Entity
@Table(schema = "ORDER", name = "PAYMENT_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = { "NAME" }))
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_PAYMENT_TYPE")
public class PaymentType extends AbstractVersionedDomainObject<Long> implements
		Comparable<PaymentType>, Serializable {

	// ------------------------------------------------------------------------
	// Comparators
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A {@link Comparator <code>Comparator</code>} for comparing
	 * <code>PaymentType</code>s by <code>name</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public static final class ByNameComparator implements
			Comparator<PaymentType> {

		/**
		 * 
		 */
		private ByNameComparator() {
			super();
		}

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final PaymentType paymentType1,
				final PaymentType paymentType2) {
			if ((paymentType1 == null) || (paymentType1.name == null)) {
				return -1;
			}
			if ((paymentType2 == null) || (paymentType2.name == null)) {
				return +1;
			}

			return paymentType1.name.compareTo(paymentType2.name);
		}

	}

	/**
	 * Compare two <code>PaymentType</code>s by <code>name</code>.
	 */
	public static final ByNameComparator BY_NAME = new ByNameComparator();

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 1585764881528209267L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public PaymentType() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public PaymentType(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public PaymentType(final Integer version) {
		super(version);
	}

	/**
	 * @param id
	 * @param version
	 */
	public PaymentType(final Long id, final Integer version) {
		super(id, version);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>PaymentType</code>'s <em>unique</em> human-readable
	 * <i>name</i>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Length(min = 5, max = 30)
	@Basic
	@Column(name = "NAME", unique = true, nullable = false, length = 30)
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
	 *             If <code>name</code> is <code>null</code>
	 * @uml.property name="name"
	 */
	public void setName(final String name) throws IllegalArgumentException {
		Validate.notNull(name, "name");
		this.name = name;
	}

	/**
	 * <p>
	 * A human-readable description of this <code>PaymentType</code>.
	 * </p>
	 * 
	 * @uml.property name="description"
	 */
	@Length(min = 5, max = 200)
	@Basic
	@Column(name = "DESCRIPTION", unique = false, nullable = true, length = 200)
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
	 * @throws IllegalArgumentException
	 *             If <code>description</code> is <code>null</code>
	 * @uml.property name="description"
	 */
	public void setDescription(final String description)
			throws IllegalArgumentException {
		Validate.notNull(description, "description");
		this.description = description;
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
		final PaymentType other = (PaymentType) obj;
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
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"name", this.name).append("description", this.description)
				.toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final PaymentType o) {

		return BY_NAME.compare(this, o);
	}

}
