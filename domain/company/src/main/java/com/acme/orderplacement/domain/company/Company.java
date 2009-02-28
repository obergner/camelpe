/**
 * 
 */
package com.acme.orderplacement.domain.company;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * Models a <tt>Company</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Table(schema = "COMPANY", name = "COMPANY")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "COMPANY.ID_SEQ_COMPANY")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_COMPANY_NUMBER, query = "from com.acme.orderplacement.domain.company.Company company where company.companyNumber = :companyNumber"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_COMPANY_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where company.companyNumber like '%:companyNumber%'"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_NAME_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where company.name like '%:name%'"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_MAIN_STREET_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where company.mainAddress.street like '%:street%'"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_MAIN_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where (company.mainAddress.street like '%:street%' and company.mainAddress.city like '%:city%')"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_MAIN_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where (company.mainAddress.street like '%:street%' and company.mainAddress.city like '%:city%' and company.mainAddress.postalCode like '%:postalCode%')"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_MAIN_EMAIL_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where company.mainEmailAddress.address like '%:emailAddress%'"),
		@org.hibernate.annotations.NamedQuery(name = Company.Queries.BY_MAIN_PHONE_LIKE, query = "from com.acme.orderplacement.domain.company.Company company where company.mainPhoneNumber.phoneNumber like '%:phoneNumber%'") })
public class Company extends AbstractAuditableDomainObject<Long> implements
		Serializable {

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

		public static final String BY_COMPANY_NUMBER = "company.byCompanyNumber";

		public static final String BY_COMPANY_NUMBER_LIKE = "company.byCompanyNumberLike";

		public static final String BY_NAME_LIKE = "company.byNameLike";

		public static final String BY_MAIN_STREET_LIKE = "company.byMainStreetLike";

		public static final String BY_MAIN_STREET_AND_CITY_LIKE = "company.byMainStreetAndCityLike";

		public static final String BY_MAIN_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "company.byMainStreetAndCityAndPostalCodeLike";

		public static final String BY_MAIN_EMAIL_LIKE = "company.byMainEmailLike";

		public static final String BY_MAIN_PHONE_LIKE = "company.byInvoiceStreetAndCityLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = 8382183377556468643L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Company() {
	}

	/**
	 * @param id
	 */
	public Company(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Company(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Company(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Company(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Company(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>Company</code>'s official <tt>name</tt>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Length(min = 2, max = 60)
	@Basic
	@Column(name = "NAME", unique = true, nullable = false, length = 60)
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
	 *             If <code>name</code> is <code>null</code> or blank
	 * @uml.property name="name"
	 */
	public void setName(final String name) throws IllegalArgumentException {
		Validate.notEmpty(name, "name");
		this.name = name;
	}

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely identifying this
	 * <code>Company</tt>. May <strong>not</strong> be <code>null</strong>.
	 * </p>
	 * 
	 * @uml.property name="companyNumber"
	 */
	@NotNull
	@Length(min = 5, max = 30)
	@Basic
	@Column(name = "COMPANY_NUMBER", unique = true, nullable = false, length = 30)
	@org.hibernate.annotations.NaturalId(mutable = false)
	private String companyNumber;

	/**
	 * Getter of the property <tt>companyNumber</tt>
	 * 
	 * @return Returns the companyNumber.
	 * @uml.property name="companyNumber"
	 */
	public String getCompanyNumber() {
		return this.companyNumber;
	}

	/**
	 * Setter of the property <tt>companyNumber</tt>
	 * 
	 * @param companyNumber
	 *            The companyNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>companyNumber</code> is <code>null</code> or blank
	 * @uml.property name="companyNumber"
	 */
	public void setCompanyNumber(final String companyNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(companyNumber, "companyNumber");
		this.companyNumber = companyNumber;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="mainAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "company:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MAIN_POSTAL_ADDRESS", /*
												 * referencedColumnName = "ID",
												 */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_COMPANY_MAINADDR")
	private PostalAddress mainAddress;

	/**
	 * Getter of the property <tt>mainAddress</tt>
	 * 
	 * @return Returns the mainAddress.
	 * @uml.property name="mainAddress"
	 */
	public PostalAddress getMainAddress() {
		return this.mainAddress;
	}

	/**
	 * Setter of the property <tt>mainAddress</tt>
	 * 
	 * @param mainAddress
	 *            The mainAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>mainAddress</code> is <code>null</code>
	 * @uml.property name="mainAddress"
	 */
	public void setMainAddress(final PostalAddress mainAddress) {
		Validate.notNull(mainAddress, "mainAddress");
		this.mainAddress = mainAddress;
	}

	/**
	 * @uml.property name="mainPhoneNumber"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "company:com.acme.orderplacement.domain.people.contact.PhoneNumber"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MAIN_PHONE_NUMBER", /* referencedColumnName = "ID", */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_COMPANY_MAINPHONE")
	private PhoneNumber mainPhoneNumber;

	/**
	 * Getter of the property <tt>mainPhoneNumber</tt>
	 * 
	 * @return Returns the mainPhoneNumber.
	 * @uml.property name="mainPhoneNumber"
	 */
	public PhoneNumber getMainPhoneNumber() {
		return this.mainPhoneNumber;
	}

	/**
	 * Setter of the property <tt>mainPhoneNumber</tt>
	 * 
	 * @param mainPhoneNumber
	 *            The mainPhoneNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>mainPhoneNumber</code> is <code>null</code>
	 * @uml.property name="mainPhoneNumber"
	 */
	public void setMainPhoneNumber(final PhoneNumber mainPhoneNumber) {
		Validate.notNull(mainPhoneNumber, "mainPhoneNumber");
		this.mainPhoneNumber = mainPhoneNumber;
	}

	/**
	 * @uml.property name="mainEmailAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "company:com.acme.orderplacement.domain.people.contact.EmailAddress"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MAIN_EMAIL_ADDRESS", /* referencedColumnName = "ID", */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_COMPANY_MAINEMAIL")
	private EmailAddress mainEmailAddress;

	/**
	 * Getter of the property <tt>mainEmailAddress</tt>
	 * 
	 * @return Returns the mainEmailAddress.
	 * @uml.property name="mainEmailAddress"
	 */
	public EmailAddress getMainEmailAddress() {
		return this.mainEmailAddress;
	}

	/**
	 * Setter of the property <tt>mainEmailAddress</tt>
	 * 
	 * @param mainEmailAddress
	 *            The mainEmailAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>mainEmailAddress</code> is <code>null</code>
	 * @uml.property name="mainEmailAddress"
	 */
	public void setMainEmailAddress(final EmailAddress mainEmailAddress) {
		Validate.notNull(mainEmailAddress, "mainEmailAddress");
		this.mainEmailAddress = mainEmailAddress;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="corporateClient"
	 * @uml.associationEnd inverse=
	 *                     "parent:com.acme.orderplacement.domain.company.customer.CorporateClient"
	 * @uml.association name="Corporate Client - Parent Company"
	 */
	@OneToOne(mappedBy = "parent", cascade = CascadeType.ALL)
	private CorporateClient corporateClient;

	/**
	 * Getter of the property <tt>corporateClient</tt>
	 * 
	 * @return Returns the corporateClient.
	 * @uml.property name="corporateClient"
	 */
	public CorporateClient getCorporateClient() {
		return this.corporateClient;
	}

	/**
	 * Setter of the property <tt>corporateClient</tt>
	 * 
	 * @param corporateClient
	 *            The corporateClient to set.
	 * @throws RolePreconditionsNotMetException
	 *             If <code>corporateClient</code> is already acting as another
	 *             <code>Company</code>'s {@link CorporateClient
	 *             <code>CorporateClient</code>}
	 * @throws IllegalArgumentException
	 *             If <code>corporateClient</code> is <code>null</code>
	 * @uml.property name="corporateClient"
	 */
	public void setCorporateClient(final CorporateClient corporateClient)
			throws RolePreconditionsNotMetException {
		Validate.notNull(corporateClient, "corporateClient");
		if ((corporateClient.getParent() != null)
				&& !this.equals(corporateClient.getParent())) {
			final String error = "Cannot accept a CorporateClient as this Company's CorporateClient who is already acting as another Company's ['"
					+ corporateClient.getParent().getName()
					+ "'] CorporateClient.";
			throw new RolePreconditionsNotMetException(error);
		}
		if (this.corporateClient != corporateClient) {
			this.corporateClient = corporateClient;
		}
		if (corporateClient.getParent() != this) {
			corporateClient.setParent(this);
		}
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
				+ ((this.companyNumber == null) ? 0 : this.companyNumber
						.hashCode());
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
		final Company other = (Company) obj;
		if (this.companyNumber == null) {
			if (other.companyNumber != null) {
				return false;
			}
		} else if (!this.companyNumber.equals(other.companyNumber)) {
			return false;
		}
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"name", this.name).append("companyNumber", this.companyNumber)
				.append("mainPhoneNumber", this.mainPhoneNumber).append(
						"mainEmailAddress", this.mainEmailAddress).append(
						"corporateClient", this.corporateClient).toString();
	}

}
