/**
 * 
 */
package com.acme.orderplacement.domain.people.contact;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * <p>
 * Represents an email address.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "PEOPLE", name = "EMAIL_ADDRESS", uniqueConstraints = @UniqueConstraint(columnNames = { "EMAIL_ADDRESS" }))
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = EmailAddress.Queries.BY_ADDRESS, query = "from com.acme.orderplacement.domain.people.contact.EmailAddress emailAddress where emailAddress.address = :address"),
		@org.hibernate.annotations.NamedQuery(name = EmailAddress.Queries.BY_ADDRESS_LIKE, query = "from com.acme.orderplacement.domain.people.contact.EmailAddress emailAddress where emailAddress.address like :address") })
public class EmailAddress extends ContactChannel implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>EmailAddress</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_ADDRESS = "emailAddress.byAddress";

		public static final String BY_ADDRESS_LIKE = "emailAddress.byAddressLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = -211910856986167228L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public EmailAddress() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * The <tt>email address</tt> proper. Must <strong>not</strong> be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="address"
	 */
	@NotNull
	@Email
	@Length(min = 1, max = 70)
	@Basic
	@Column(name = "EMAIL_ADDRESS", unique = true, nullable = false, length = 70)
	private String address;

	/**
	 * Getter of the property <tt>address</tt>
	 * 
	 * @return Returns the address.
	 * @uml.property name="address"
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Setter of the property <tt>address</tt>
	 * 
	 * @param address
	 *            The address to set.
	 * @throws IllegalArgumentException
	 *             If <code>address</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="address"
	 */
	public void setAddress(final String address)
			throws IllegalArgumentException {
		Validate.notEmpty(address, "address");
		this.address = address;
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
				+ ((this.address == null) ? 0 : this.address.hashCode());
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
		final EmailAddress other = (EmailAddress) obj;
		if (this.address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!this.address.equals(other.address)) {
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
				"address", this.address).toString();
	}

}
