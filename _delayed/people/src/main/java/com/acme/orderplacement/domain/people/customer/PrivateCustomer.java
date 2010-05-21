/**
 * 
 */
package com.acme.orderplacement.domain.people.customer;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;

/**
 * <p>
 * A <code>PrivateCustomer</code> is a <tt>role</tt> a {@link Person
 * <code>Person</code>} enacts when interacting with the <strong>SOA Order
 * Application</strong>. It represents that portion of its parent
 * <code>Person</code>'s state and behaviour needed and sufficient to
 * successfully conduct business.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::role"
 */
@Entity
@Table(schema = "PEOPLE", name = "PRIVATE_CUSTOMER")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "PEOPLE.ID_SEQ_PRIVATE_CUSTOMER")
@NamedQueries( {
		@NamedQuery(name = PrivateCustomer.Queries.BY_CUSTOMER_NUMBER, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where privateCustomer.customerNumber = :customerNumber"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_CUSTOMER_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where privateCustomer.customerNumber like :customerNumber"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_SHIPPING_STREET_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where privateCustomer.shippingAddress.street like :street"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_SHIPPING_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where (privateCustomer.shippingAddress.street like :street and privateCustomer.shippingAddress.city like :city)"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_SHIPPING_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where (privateCustomer.shippingAddress.street like :street and privateCustomer.shippingAddress.city like :city and privateCustomer.shippingAddress.postalCode like :postalCode)"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_INVOICE_STREET_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where privateCustomer.invoiceAddress.street like :street"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_INVOICE_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where (privateCustomer.invoiceAddress.street like :street and privateCustomer.invoiceAddress.city like :city)"),
		@NamedQuery(name = PrivateCustomer.Queries.BY_INVOICE_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.people.customer.PrivateCustomer privateCustomer where (privateCustomer.invoiceAddress.street like :street and privateCustomer.invoiceAddress.city like :city and privateCustomer.invoiceAddress.postalCode like :postalCode)") })
public class PrivateCustomer extends AbstractAuditableDomainObject<Long>
		implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PrivateCustomer</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_CUSTOMER_NUMBER = "privateCustomer.byCustomerNumber";

		public static final String BY_CUSTOMER_NUMBER_LIKE = "privateCustomer.byCustomerNumberLike";

		public static final String BY_SHIPPING_STREET_LIKE = "privateCustomer.byShippingStreetLike";

		public static final String BY_SHIPPING_STREET_AND_CITY_LIKE = "privateCustomer.byShippingStreetAndCityLike";

		public static final String BY_SHIPPING_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "privateCustomer.byShippingStreetAndCityAndPostalCodeLike";

		public static final String BY_INVOICE_STREET_LIKE = "privateCustomer.byInvoiceStreetLike";

		public static final String BY_INVOICE_STREET_AND_CITY_LIKE = "privateCustomer.byInvoiceStreetAndCityLike";

		public static final String BY_INVOICE_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "privateCustomer.byInvoiceStreetAndCityAndPostalCodeLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = 7033862582635270891L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public PrivateCustomer() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <tt>business key</tt> uniquely identifying this
	 * <code>PrivateCustomer</tt>. May <strong>not</strong> be <code>null</code>
	 * .
	 * </p>
	 * 
	 * @uml.property name="customerNumber"
	 */
	@NotNull
	@Size(min = 5, max = 30)
	@Basic
	@Column(name = "CUSTOMER_NUMBER", unique = true, nullable = false, length = 30)
	@NaturalId(mutable = false)
	private String customerNumber;

	/**
	 * Getter of the property <tt>customerNumber</tt>
	 * 
	 * @return Returns the customerNumber.
	 * @uml.property name="customerNumber"
	 */
	public String getCustomerNumber() {
		return this.customerNumber;
	}

	/**
	 * Setter of the property <tt>customerNumber</tt>
	 * 
	 * @param customerNumber
	 *            The customerNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>customerNumber</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="customerNumber"
	 */
	public void setCustomerNumber(final String customerNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(customerNumber, "customerNumber");
		this.customerNumber = customerNumber;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * The {@link PostalAddress <code>Address</code>} goods ordered by this
	 * <code>PrivateCustomer</code> will be shipped to.
	 * </p>
	 * <p>
	 * Each <code>PrivateCustomer</code> may define an explicit
	 * <code>shippingAddress</code>. If this is not set, it will default to the
	 * parent {@link Person <code>Person</code>}'s
	 * {@link Person#getHomeAddress() <code>homeAddress</code>}.
	 * </p>
	 * 
	 * @uml.property name="shippingAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "privateCustomer:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 * @uml.association name="PrivateCustomer - ShippingAddress"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_SHIPPING_ADDRESS", unique = false, nullable = true)
	@ForeignKey(name = "FK_PRIVATECUST_SHIPPINGADDR", inverseName = "FK_SHIPPINGADDR_PRIVATECUST")
	private PostalAddress shippingAddress;

	/**
	 * Getter of the property <tt>shippingAddress</tt>
	 * 
	 * @return Returns the shippingAddress.
	 * @uml.property name="shippingAddress"
	 */
	public PostalAddress getShippingAddress() {
		if (this.shippingAddress != null) {
			return this.shippingAddress;
		}

		return this.parent != null ? this.parent.getHomeAddress() : null;
	}

	/**
	 * Setter of the property <tt>shippingAddress</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>shippingAddress</code> is <code>null</code>
	 * 
	 * @param shippingAddress
	 *            The shippingAddress to set.
	 * @uml.property name="shippingAddress"
	 */
	public void setShippingAddress(final PostalAddress shippingAddress)
			throws IllegalArgumentException {
		Validate.notNull(shippingAddress, "shippingAddress");
		this.shippingAddress = shippingAddress;
	}

	/**
	 * Remove property <code>shippingAddress</code>.
	 */
	public void removeShippingAddress() {

		this.shippingAddress = null;
	}

	/**
	 * <p>
	 * The {@link PostalAddress <code>Address</code>} the invoice for goods
	 * ordered by this <code>PrivateCustomer</code> will be sent to.
	 * </p>
	 * <p>
	 * Each <code>PrivateCustomer</code> may define an explicit
	 * <code>invoiceAddress</code>. If this is not set, it will default to the
	 * {@link #getShippingAddress() <code>shippingAddress</code>}.
	 * </p>
	 * 
	 * @uml.property name="invoiceAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "privateCustomer:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 * @uml.association name="PrivateCustomer - InvoiceAddress"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_INVOICE_ADDRESS", unique = false, nullable = true)
	@ForeignKey(name = "FK_PRIVATECUST_INVOICEADDR", inverseName = "FK_INVOICEADDR_PRIVATECUST")
	private PostalAddress invoiceAddress = null;

	/**
	 * Getter of the property <tt>invoiceAddress</tt>
	 * 
	 * @return Returns the invoiceAddress.
	 * @uml.property name="invoiceAddress"
	 */
	public PostalAddress getInvoiceAddress() {
		if (this.invoiceAddress != null) {
			return this.invoiceAddress;
		}

		return getShippingAddress();
	}

	/**
	 * Setter of the property <tt>invoiceAddress</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>invoiceAddress</code> is <code>null</code>
	 * 
	 * @param invoiceAddress
	 *            The invoiceAddress to set.
	 * @uml.property name="invoiceAddress"
	 */
	public void setInvoiceAddress(final PostalAddress invoiceAddress)
			throws IllegalArgumentException {
		Validate.notNull(invoiceAddress, "invoiceAddress");
		this.invoiceAddress = invoiceAddress;
	}

	/**
	 * Remove property <code>invoiceAddress</code>.
	 */
	public void removeInvoiceAddress() {

		this.invoiceAddress = null;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>PrivateCustomer</code>'s parent {@link Person
	 * <code>Person</code>}, i.e. the real human being that <i>acts</i> as a
	 * <code>PrivateCustomer</code> when interacting with the <strong>SOA Order
	 * Application</strong>.
	 * </p>
	 * <p>
	 * The relationship between a <code>Person</code> and its associated
	 * <code>PrivateCustomer</code> <tt>role</tt> is bidirectional. This quality
	 * has to be carefully managed.
	 * </p>
	 * 
	 * @uml.property name="parent"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "privateCustomer:com.acme.orderplacement.domain.people.person.Person"
	 * @uml.association name="Customer - Parent"
	 */
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_PERSON", referencedColumnName = "ID", unique = true, nullable = false)
	@ForeignKey(name = "FK_PRIVATECUST_PERSON")
	private Person parent;

	/**
	 * Getter of the property <tt>parent</tt>
	 * 
	 * @throws IllegalDomainObjectStateException
	 *             If {@link #parent <code>parent</code>} is <code>null</code>.
	 *             A <code>PrivateCustomer</code> cannot live without its parent
	 *             {@link Person <code>Person</code>}.
	 * 
	 * @return Returns the parent.
	 * @uml.property name="parent"
	 * 
	 */
	public Person getParent() throws IllegalDomainObjectStateException {
		if (this.parent == null) {
			throw new IllegalDomainObjectStateException(
					"A PrivateCustomer MUST have an associated parent Person [parent = <null>]");
		}

		return this.parent;
	}

	/**
	 * <p>
	 * Check if this <code>PrivateCustomer</code> really has a
	 * <code>parent</code> as it should.
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasParent() {

		return this.parent != null;
	}

	/**
	 * Setter of the property <tt>parent</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>parent</code> is <code>null</code>
	 * 
	 * @param parent
	 *            The parent to set.
	 * @uml.property name="parent"
	 */
	public void setParent(final Person parent) throws IllegalArgumentException {
		Validate.notNull(parent, "parent");
		this.parent = parent;
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
		result = PRIME
				* result
				+ ((this.customerNumber == null) ? 0 : this.customerNumber
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PrivateCustomer other = (PrivateCustomer) obj;
		if (this.customerNumber == null) {
			if (other.customerNumber != null) {
				return false;
			}
		} else if (!this.customerNumber.equals(other.customerNumber)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"customerNumber", this.customerNumber).append(
				"shippingAddress", this.shippingAddress).append(
				"invoiceAddress", this.invoiceAddress).append("parent",
				this.parent).toString();
	}
}
