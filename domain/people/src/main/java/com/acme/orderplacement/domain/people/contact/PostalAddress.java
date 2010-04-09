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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.people.person.Person;

/**
 * <p>
 * A <code>PostalAddress</code> captures the concept of a physical location
 * where its owner may be contacted in the narrow sense of an <tt>address</tt>,
 * i.e. a location known to official authorities and described in a standard
 * manner, using <tt>street</tt>, <tt>city</tt> etc.
 * </p>
 * <p>
 * Note that the relationship between a <code>PostalAddress</code> and its
 * <i>owner</i> - be it a {@link Person <code>Person</code>} or a
 * {@link PrivateCustomer <code>PrivateCustomer</code>} - is m:n by nature.
 * Several people may share the same <code>PostalAddres</code> (think of a
 * couple living in the same flat), and a person may know about more than one
 * <code>PostalAddress</code> (think of a customer having a private address and
 * a work address).
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Table(schema = "PEOPLE", name = "POSTAL_ADDRESS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STREET", "STREET_NUMBER", "POSTAL_CODE" }))
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = "postalAddress.byStreetLike", query = "from com.acme.orderplacement.domain.people.contact.PostalAddress postalAddress where postalAddress.street like :street"),
		@org.hibernate.annotations.NamedQuery(name = "postalAddress.byStreetAndCityLike", query = "from com.acme.orderplacement.domain.people.contact.PostalAddress postalAddress where (postalAddress.street like :street and postalAddress.city like :city)"),
		@org.hibernate.annotations.NamedQuery(name = "postalAddress.byStreetAndCityAndPostalCodeLike", query = "from com.acme.orderplacement.domain.people.contact.PostalAddress postalAddress where (postalAddress.street like :street and postalAddress.city like :city and postalAddress.postalCode like :postalCode)") })
public class PostalAddress extends ContactChannel implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PostalAddress</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_STREET_LIKE = "postalAddress.byStreetLike";

		public static final String BY_STREET_AND_CITY_LIKE = "postalAddress.byStreetAndCityLike";

		public static final String BY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "postalAddress.byStreetAndCityAndPostalCodeLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194891938104951284L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public PostalAddress() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * The <tt>street</tt> where this
	 * <code>PostalAddress</tt> is located. May <strong>not</strong> be <code>null</code>
	 * .
	 * </p>
	 * 
	 * @uml.property name="street"
	 */
	@NotNull
	@Size(min = 3, max = 50)
	@Basic
	@Column(name = "STREET", unique = false, nullable = false, length = 50)
	private String street;

	/**
	 * Getter of the property <tt>street</tt>
	 * 
	 * @return Returns the street.
	 * @uml.property name="street"
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * Setter of the property <tt>street</tt>
	 * 
	 * @param street
	 *            The street to set.
	 * @throws IllegalArgumentException
	 *             If <code>street</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="street"
	 */
	public void setStreet(final String street) throws IllegalArgumentException {
		Validate.notEmpty(street, "street");
		this.street = street;
	}

	/**
	 * <p>
	 * The <tt>street number</tt> belonging to this <code>PostalAddress</code>.
	 * May not be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="streetNumber"
	 */
	@NotNull
	@Size(min = 1, max = 10)
	@Basic
	@Column(name = "STREET_NUMBER", unique = false, nullable = false, length = 10)
	private String streetNumber;

	/**
	 * Getter of the property <tt>streetNumber</tt>
	 * 
	 * @return Returns the streetNumber.
	 * @uml.property name="streetNumber"
	 */
	public String getStreetNumber() {
		return this.streetNumber;
	}

	/**
	 * Setter of the property <tt>streetNumber</tt>
	 * 
	 * @param streetNumber
	 *            The streetNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>streetNumber</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="streetNumber"
	 */
	public void setStreetNumber(final String streetNumber) {
		Validate.notEmpty(streetNumber, "streetNumber");
		this.streetNumber = streetNumber;
	}

	/**
	 * <p>
	 * This <code>PostalAddress</code>'s <tt>postal code</tt>. May not be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="postalCode"
	 */
	@NotNull
	@Size(min = 3, max = 10)
	@Basic
	@Column(name = "POSTAL_CODE", unique = false, nullable = false, length = 10)
	private String postalCode;

	/**
	 * Getter of the property <tt>postalCode</tt>
	 * 
	 * @return Returns the postalCode.
	 * @uml.property name="postalCode"
	 */
	public String getPostalCode() {
		return this.postalCode;
	}

	/**
	 * Setter of the property <tt>postalCode</tt>
	 * 
	 * @param postalCode
	 *            The postalCode to set.
	 * @throws IllegalArgumentException
	 *             If <code>postalCode</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="postalCode"
	 */
	public void setPostalCode(final String postalCode) {
		Validate.notEmpty(postalCode, "postalCode");
		this.postalCode = postalCode;
	}

	/**
	 * <p>
	 * This <code>PostalAddress</code>'s <tt>city</tt>. May not be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="city"
	 */
	@NotNull
	@Size(min = 2, max = 40)
	@Basic
	@Column(name = "CITY", unique = false, nullable = false, length = 40)
	private String city;

	/**
	 * Getter of the property <tt>city</tt>
	 * 
	 * @return Returns the city.
	 * @uml.property name="city"
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Setter of the property <tt>city</tt>
	 * 
	 * @param city
	 *            The city to set.
	 * @throws IllegalArgumentException
	 *             If <code>city</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="city"
	 */
	public void setCity(final String city) {
		Validate.notEmpty(city, "city");
		this.city = city;
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
				+ ((this.city == null) ? 0 : this.city.hashCode());
		result = PRIME * result
				+ ((this.postalCode == null) ? 0 : this.postalCode.hashCode());
		result = PRIME * result
				+ ((this.street == null) ? 0 : this.street.hashCode());
		result = PRIME
				* result
				+ ((this.streetNumber == null) ? 0 : this.streetNumber
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
		final PostalAddress other = (PostalAddress) obj;
		if (this.city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!this.city.equals(other.city)) {
			return false;
		}
		if (this.postalCode == null) {
			if (other.postalCode != null) {
				return false;
			}
		} else if (!this.postalCode.equals(other.postalCode)) {
			return false;
		}
		if (this.street == null) {
			if (other.street != null) {
				return false;
			}
		} else if (!this.street.equals(other.street)) {
			return false;
		}
		if (this.streetNumber == null) {
			if (other.streetNumber != null) {
				return false;
			}
		} else if (!this.streetNumber.equals(other.streetNumber)) {
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
				"street", this.street)
				.append("streetNumber", this.streetNumber).append("postalCode",
						this.postalCode).append("city", this.city).toString();
	}

}
