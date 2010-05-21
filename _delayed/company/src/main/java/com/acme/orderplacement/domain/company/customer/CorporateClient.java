/**
 * 
 */
package com.acme.orderplacement.domain.company.customer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.domain.company.people.CorporateClientRepresentative;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * A <code>CorporateClient</code> represents a {@link Company
 * <code>Company</code>} acting as a <tt>Customer</tt> of <strong>SOA Order
 * Inc.</strong>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::role"
 */
@Entity
@Table(schema = "COMPANY", name = "CORPORATE_CLIENT")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_CORPORATE_CLIENT")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_CORPORATE_CLIENT_NUMBER, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where corporateClient.corporateClientNumber = :corporateClientNumber"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_CORPORATE_CLIENT_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where corporateClient.corporateClientNumber like '%:corporateClientNumber%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_COMPANY_NAME_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where corporateClient.parent.name like '%:companyName%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_DELIVERY_STREET_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where corporateClient.deliveryAddress.street like '%:street%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_DELIVERY_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where (corporateClient.deliveryAddress.street like '%:street%' and corporateClient.deliveryAddress.city like '%:city%')"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_DELIVERY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where (corporateClient.deliveryAddress.street like '%:street%' and corporateClient.deliveryAddress.city like '%:city%' and corporateClient.deliveryAddress.postalCode like '%:postalCode%')"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_INVOICE_STREET_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where corporateClient.invoiceAddress.street like '%:street%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_INVOICE_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where (corporateClient.invoiceAddress.street like '%:street%' and corporateClient.invoiceAddress.city like '%:city%')"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClient.Queries.BY_INVOICE_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.company.customer.CorporateClient corporateClient where (corporateClient.invoiceAddress.street like '%:street%' and corporateClient.invoiceAddress.city like '%:city%' and corporateClient.invoiceAddress.postalCode like '%:postalCode%')") })
public class CorporateClient extends AbstractAuditableDomainObject<Long>
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

		public static final String BY_CORPORATE_CLIENT_NUMBER = "corporateClient.byCorporateClientNumber";

		public static final String BY_CORPORATE_CLIENT_NUMBER_LIKE = "corporateClient.byCorporateClientNumberLike";

		public static final String BY_COMPANY_NAME_LIKE = "corporateClient.byCompanyNameLike";

		public static final String BY_DELIVERY_STREET_LIKE = "corporateClient.byDeliveryStreetLike";

		public static final String BY_DELIVERY_STREET_AND_CITY_LIKE = "corporateClient.byDeliveryStreetAndCityLike";

		public static final String BY_DELIVERY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "corporateClient.byDeliveryStreetAndCityAndPostalCodeLike";

		public static final String BY_INVOICE_STREET_LIKE = "corporateClient.byInvoiceStreetLike";

		public static final String BY_INVOICE_STREET_AND_CITY_LIKE = "corporateClient.byInvoiceStreetAndCityLike";

		public static final String BY_INVOICE_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "corporateClient.byInvoiceStreetAndCityAndPostalCodeLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = -7603959147655215668L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public CorporateClient() {
	}

	/**
	 * @param id
	 */
	public CorporateClient(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public CorporateClient(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClient(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public CorporateClient(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClient(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely identifying this
	 * <code>CorporateClient</tt>.
	 * </p>
	 * 
	 * @uml.property name="corporateClientNumber"
	 */
	@NotNull
	@Length(min = 5, max = 30)
	@Basic
	@Column(name = "CORPORATE_CLIENT_NUMBER", unique = true, nullable = false, length = 30)
	@org.hibernate.annotations.NaturalId(mutable = false)
	private String corporateClientNumber;

	/**
	 * Getter of the property <tt>corporateClientNumber</tt>
	 * 
	 * @return Returns the corporateClientNumber.
	 * @uml.property name="corporateClientNumber"
	 */
	public String getCorporateClientNumber() {
		return this.corporateClientNumber;
	}

	/**
	 * Setter of the property <tt>corporateClientNumber</tt>
	 * 
	 * @param corporateClientNumber
	 *            The corporateClientNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>corporateClientNumber</code> is <code>null</code> or
	 *             blank
	 * @uml.property name="corporateClientNumber"
	 */
	public void setCorporateClientNumber(final String corporateClientNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(corporateClientNumber, "corporateClientNumber");
		this.corporateClientNumber = corporateClientNumber;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="deliveryAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "corporateClient:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_DELIVERY_ADDRESS", /* referencedColumnName = "ID", */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_DELIVERYADDR")
	private PostalAddress deliveryAddress;

	/**
	 * Getter of the property <tt>deliveryAddress</tt>
	 * 
	 * @return Returns the deliveryAddress.
	 * @uml.property name="deliveryAddress"
	 */
	public PostalAddress getDeliveryAddress() {
		if (this.deliveryAddress == null) {
			return getParent().getMainAddress();
		}
		return this.deliveryAddress;
	}

	/**
	 * Setter of the property <tt>deliveryAddress</tt>
	 * 
	 * @param deliveryAddress
	 *            The deliveryAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>deliveryAddress</code> is <code>null</code>
	 * @uml.property name="deliveryAddress"
	 */
	public void setDeliveryAddress(final PostalAddress deliveryAddress) {
		Validate.notNull(deliveryAddress, "deliveryAddress");
		this.deliveryAddress = deliveryAddress;
	}

	/**
	 * @uml.property name="invoiceAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "corporateClient:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_INVOICE_ADDRESS", /* referencedColumnName = "ID", */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_INVOICEADDR")
	private PostalAddress invoiceAddress;

	/**
	 * Getter of the property <tt>invoiceAddress</tt>
	 * 
	 * @return Returns the invoiceAddress.
	 * @uml.property name="invoiceAddress"
	 */
	public PostalAddress getInvoiceAddress() {
		if (this.invoiceAddress == null) {
			return getDeliveryAddress();
		}
		return this.invoiceAddress;
	}

	/**
	 * Setter of the property <tt>invoiceAddress</tt>
	 * 
	 * @param invoiceAddress
	 *            The invoiceAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>invoiceAddress</code> is <code>null</code>
	 * @uml.property name="invoiceAddress"
	 */
	public void setInvoiceAddress(final PostalAddress invoiceAddress) {
		Validate.notNull(invoiceAddress, "invoiceAddress");
		this.invoiceAddress = invoiceAddress;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="parent"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "corporateClient:com.acme.orderplacement.domain.company.Company"
	 * @uml.association name="Corporate Client - Parent Company"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID", unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_COMPANY")
	private Company parent;

	/**
	 * Getter of the property <tt>parent</tt>
	 * 
	 * @return Returns the parent.
	 * @uml.property name="parent"
	 */
	public Company getParent() {
		return this.parent;
	}

	/**
	 * Setter of the property <tt>parent</tt>
	 * 
	 * @param parent
	 *            The parent to set.
	 * @throws RolePreconditionsNotMetException
	 *             If this <code>CorporateClient</code> is already acting as
	 *             another {@link Company <code>Company</code>}'s
	 *             <code>CorporateClient</code>
	 * @throws IllegalArgumentException
	 *             If <code>parent</code> is <code>null</code>
	 * @uml.property name="parent"
	 */
	public void setParent(final Company parent)
			throws RolePreconditionsNotMetException, IllegalArgumentException {
		Validate.notNull(parent, "parent");
		if ((this.parent != null) && !this.parent.equals(parent)) {
			final String error = "Cannot act as Company's ['"
					+ parent.getName()
					+ "'] CorporateClient while already acting as Company's ['"
					+ this.parent.getName() + "'] CorporateClient.";
			throw new RolePreconditionsNotMetException(error);
		}
		if (this.parent != parent) {
			this.parent = parent;
		}
		if (parent.getCorporateClient() != this) {
			parent.setCorporateClient(this);
		}
	}

	/**
	 * @uml.property name="representatives"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse="representedCorporateClient:com.acme.orderplacement.domain.company.people.CorporateClientRepresentative"
	 * @uml.association name="CorporateClient - Representatives"
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "representedCorporateClient")
	@org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	private Set<CorporateClientRepresentative> representatives;

	/**
	 * @return
	 */
	protected Set<CorporateClientRepresentative> getRepresentativesInternal() {
		if (this.representatives == null) {
			this.representatives = new HashSet<CorporateClientRepresentative>();
		}

		return this.representatives;
	}

	/**
	 * Getter of the property <tt>representatives</tt>
	 * 
	 * @return Returns the representatives.
	 * @uml.property name="representatives"
	 */
	public Set<CorporateClientRepresentative> getRepresentatives() {
		return Collections.unmodifiableSet(getRepresentativesInternal());
	}

	/**
	 * Returns an iterator over the elements in this collection.
	 * 
	 * @return an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property name="representatives"
	 */
	public Iterator<CorporateClientRepresentative> representativesIterator() {
		return getRepresentativesInternal().iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property name="representatives"
	 */
	public boolean isRepresentativesEmpty() {
		return getRepresentativesInternal().isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element.
	 * 
	 * @param element
	 *            whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property name="representatives"
	 */
	public boolean containsRepresentatives(
			final CorporateClientRepresentative corporateClientRepresentative) {
		return getRepresentativesInternal().contains(
				corporateClientRepresentative);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in
	 * the specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property name="representatives"
	 */
	public boolean containsAllRepresentatives(
			final Collection<CorporateClientRepresentative> representatives) {
		return getRepresentativesInternal().containsAll(representatives);
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property name="representatives"
	 */
	public int representativesSize() {
		return getRepresentativesInternal().size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * 
	 * @return an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property name="representatives"
	 */
	public CorporateClientRepresentative[] representativesToArray() {
		return getRepresentativesInternal().toArray(
				new CorporateClientRepresentative[this.representatives.size()]);
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
	 * @uml.property name="representatives"
	 */
	public CorporateClientRepresentative[] representativesToArray(
			final CorporateClientRepresentative[] representatives) {
		return getRepresentativesInternal().toArray(representatives);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @throws RolePreconditionsNotMetException
	 *             If <code>corporateClientRepresentative</code> is already
	 *             acting as another <code>CorporateClient<code>'s
	 *             <tt>Representative</tt>
	 * @throws IllegalArgumentException
	 *             If <code>corporateClientRepresentative</code> is
	 *             <code>null</code>
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="representatives"
	 */
	public boolean addRepresentative(
			final CorporateClientRepresentative corporateClientRepresentative)
			throws RolePreconditionsNotMetException, IllegalArgumentException {
		Validate.notNull(corporateClientRepresentative,
				"corporateClientRepresentative");
		if ((corporateClientRepresentative.getRepresentedCorporateClient() != null)
				&& !corporateClientRepresentative
						.getRepresentedCorporateClient().equals(this)) {
			final String error = "Cannot accept an CorporateClientRepresentative as this CorporateClient's representative who is already acting as another CorporateClient's ['"
					+ corporateClientRepresentative
							.getRepresentedCorporateClient()
							.getCorporateClientNumber() + "'] representative.";
			throw new RolePreconditionsNotMetException(error);
		}
		if (corporateClientRepresentative.getRepresentedCorporateClient() != this) {
			corporateClientRepresentative.setRepresentedCorporateClient(this);
		}
		return getRepresentativesInternal().add(corporateClientRepresentative);
	}

	/**
	 * Setter of the property <tt>representatives</tt>
	 * 
	 * @param representatives
	 *            the representatives to set.
	 * @uml.property name="representatives"
	 */
	public void setRepresentatives(
			final Set<CorporateClientRepresentative> representatives) {
		this.representatives = representatives;
	}

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param element
	 *            to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property name="representatives"
	 */
	public boolean removeRepresentative(
			final CorporateClientRepresentative corporateClientRepresentative) {
		return getRepresentativesInternal().remove(
				corporateClientRepresentative);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * 
	 * @see java.util.Collection#clear()
	 * @uml.property name="representatives"
	 */
	public void clearRepresentatives() {
		getRepresentativesInternal().clear();
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
				+ ((this.corporateClientNumber == null) ? 0
						: this.corporateClientNumber.hashCode());
		result = PRIME * result
				+ ((this.parent == null) ? 0 : this.parent.hashCode());
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
		final CorporateClient other = (CorporateClient) obj;
		if (this.corporateClientNumber == null) {
			if (other.corporateClientNumber != null) {
				return false;
			}
		} else if (!this.corporateClientNumber
				.equals(other.corporateClientNumber)) {
			return false;
		}
		if (this.parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!this.parent.equals(other.parent)) {
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
				"corporateClientNumber", this.corporateClientNumber).toString();
	}

}
