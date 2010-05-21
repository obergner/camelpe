/**
 * 
 */
package com.acme.orderplacement.domain.people.contact;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Represents a telephone number to personally reach a <code>Person</code>. A
 * <code>PhoneNumber</code> may be either <tt>mobile</tt> or <tt>landline</tt>,
 * depending on its {@link #type <code>type</code>} property.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Table(schema = "PEOPLE", name = "TELEPHONE_NUMBER", uniqueConstraints = @UniqueConstraint(columnNames = { "PHONE_NUMBER" }))
@NamedQueries( {
		@NamedQuery(name = PhoneNumber.Queries.BY_NUMBER, query = "from com.acme.orderplacement.domain.people.contact.PhoneNumber phoneNumber where phoneNumber.phoneNumber = :number"),
		@NamedQuery(name = PhoneNumber.Queries.BY_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.people.contact.PhoneNumber phoneNumber where phoneNumber.phoneNumber like :number") })
public class PhoneNumber extends ContactChannel implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PhoneNumber</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_NUMBER = "phoneNumber.byNumber";

		public static final String BY_NUMBER_LIKE = "phoneNumber.byNumberLike";
	}

	// ------------------------------------------------------------------------
	// Nested enum
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <code>PhoneNumber</code>'s <em>type</em>, i.e. <tt>landline</tt> or
	 * <tt>mobile</tt>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public enum Type {

		LANDLINE, MOBILE
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344591384172302622L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public PhoneNumber() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>PhoneNumber</code>'s {@link Type <code>Type</code>}, i.e.
	 * whether it is a mobile or a landline number.
	 * </p>
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PHONE_TYPE", unique = false, nullable = false, length = 20)
	private Type type = Type.LANDLINE;

	/**
	 * @param type
	 *            the type to set
	 * @throws IllegalArgumentException
	 *             If <code>type</code> is <code>null</code>
	 */
	public void setType(final Type type) throws IllegalArgumentException {
		Validate.notNull(type, "type");
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * <p>
	 * The <tt>phone number</tt> proper.
	 * </p>
	 * 
	 * @uml.property name="phoneNumber"
	 */
	@NotNull
	@Size(min = 3, max = 30)
	@Basic
	@Column(name = "PHONE_NUMBER", unique = true, nullable = false, length = 30)
	private String phoneNumber;

	/**
	 * Getter of the property <tt>phoneNumber</tt>
	 * 
	 * @return Returns the phoneNumber.
	 * @uml.property name="phoneNumber"
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * Setter of the property <tt>phoneNumber</tt>
	 * 
	 * @param phoneNumber
	 *            The phoneNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>phoneNumber</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="phoneNumber"
	 */
	public void setPhoneNumber(final String phoneNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(phoneNumber, "phoneNumber");
		this.phoneNumber = phoneNumber;
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
				+ ((this.phoneNumber == null) ? 0 : this.phoneNumber.hashCode());
		result = PRIME * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
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
		final PhoneNumber other = (PhoneNumber) obj;
		if (this.phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!this.phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
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
				"type", this.type).append("phoneNumber", this.phoneNumber)
				.toString();
	}

}
