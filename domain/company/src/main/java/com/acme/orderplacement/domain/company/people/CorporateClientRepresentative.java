/**
 * 
 */
package com.acme.orderplacement.domain.company.people;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * A <code>CorporateClientRepresentative</code> is a natural {@link Person
 * <code>Person</code>} <i>acting</i> as a <tt>Representative</tt> of an
 * {@link CorporateClient <code>CorporateClient</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::role"
 */
@Entity
@Table(schema = "COMPANY", name = "CORPORATE_CLIENT_REPRESENTATIVE")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ID_SEQ_CORP_CLIENT_REP")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_CORPORATE_CLIENT_REPRESENTATIVE_NUMBER, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.corporateClientRepresentativeNumber = :corporateClientRepresentativeNumber"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_CORPORATE_CLIENT_REPRESENTATIVE_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.corporateClientRepresentativeNumber like '%:corporateClientRepresentativeNumber%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_LAST_NAME_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.parentPerson.lastName like '%:lastName%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_FIRST_NAME_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.parentPerson.firstName like '%:firstName%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_WORK_STREET_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.workAddress.street like '%:street%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_WORK_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where (corporateClientRepresentative.workAddress.street like '%:street%' and corporateClientRepresentative.workAddress.city like '%:city%')"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_WORK_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where (corporateClientRepresentative.workAddress.street like '%:street%' and corporateClientRepresentative.workAddress.city like '%:city%' and corporateClientRepresentative.workAddress.postalCode like '%:postalCode%')"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_WORK_EMAIL_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.workEmailAddress.address like '%:emailAddress%'"),
		@org.hibernate.annotations.NamedQuery(name = CorporateClientRepresentative.Queries.BY_WORK_PHONE_LIKE, query = "from com.acme.orderplacement.domain.company.people.CorporateClientRepresentative corporateClientRepresentative where corporateClientRepresentative.workPhoneNumber.phoneNumber like '%:phoneNumber%'") })
public class CorporateClientRepresentative extends
		AbstractAuditableDomainObject<Long> implements Serializable {

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

		public static final String BY_CORPORATE_CLIENT_REPRESENTATIVE_NUMBER = "corporateClientRepresentative.byCorporateClientRepresentativeNumber";

		public static final String BY_CORPORATE_CLIENT_REPRESENTATIVE_NUMBER_LIKE = "corporateClientRepresentative.byCorporateClientRepresentativeNumberLike";

		public static final String BY_LAST_NAME_LIKE = "corporateClientRepresentative.byLastNameLike";

		public static final String BY_FIRST_NAME_LIKE = "corporateClientRepresentative.byFirstNameLike";

		public static final String BY_WORK_STREET_LIKE = "corporateClientRepresentative.byWorkStreetLike";

		public static final String BY_WORK_STREET_AND_CITY_LIKE = "corporateClientRepresentative.byWorkStreetAndCityLike";

		public static final String BY_WORK_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "corporateClientRepresentative.byWorkStreetAndCityAndPostalCodeLike";

		public static final String BY_WORK_EMAIL_LIKE = "corporateClientRepresentative.byWorkEmailLike";

		public static final String BY_WORK_PHONE_LIKE = "corporateClientRepresentative.byWorkPhoneLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = 5623309056785355326L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public CorporateClientRepresentative() {
	}

	/**
	 * @param id
	 */
	public CorporateClientRepresentative(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public CorporateClientRepresentative(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientRepresentative(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public CorporateClientRepresentative(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public CorporateClientRepresentative(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A <tt>Business Key</tt> uniquely defining this
	 * <code>CompanyRepresentative</tt>. May <strong>not</strong> be <code>null</code>
	 * .
	 * </p>
	 * 
	 * @uml.property name="corporateClientRepresentativeNumber"
	 */
	@NotNull
	@Length(min = 5, max = 30)
	@Basic
	@Column(name = "CORPORATE_CLIENT_REP_NUMBER", unique = true, nullable = false, length = 30)
	@org.hibernate.annotations.NaturalId(mutable = false)
	private String corporateClientRepresentativeNumber;

	/**
	 * Getter of the property <tt>companyRepresentativeNumber</tt>
	 * 
	 * @return Returns the companyRepresentativeNumber.
	 * @uml.property name="corporateClientRepresentativeNumber"
	 */
	public String getCorporateClientRepresentativeNumber() {
		return this.corporateClientRepresentativeNumber;
	}

	/**
	 * Setter of the property <tt>companyRepresentativeNumber</tt>
	 * 
	 * @param companyRepresentativeNumber
	 *            The companyRepresentativeNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>corporateClientRepresentativeNumber</code> is
	 *             <code>null</code> or blank
	 * @uml.property name="corporateClientRepresentativeNumber"
	 */
	public void setCorporateClientRepresentativeNumber(
			final String corporateClientRepresentativeNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(corporateClientRepresentativeNumber,
				"corporateClientRepresentativeNumber");
		this.corporateClientRepresentativeNumber = corporateClientRepresentativeNumber;
	}

	/**
	 * <p>
	 * This <code>CorporateClientRepresentative</code>'s {@link Position
	 * <code>Position</code>} in his/her {@link Company <code>Company</code>} .
	 * </p>
	 * 
	 * @uml.property name="position"
	 */
	@NotNull
	@Length(min = 2, max = 20)
	@Enumerated(EnumType.STRING)
	@Column(name = "POSITION", unique = false, nullable = false, length = 20)
	private Position position;

	/**
	 * Getter of the property <tt>position</tt>
	 * 
	 * @return Returns the position.
	 * @uml.property name="position"
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * Setter of the property <tt>position</tt>
	 * 
	 * @param position
	 *            The position to set.
	 * @throws IllegalArgumentException
	 *             If <code>position</code> is <code>null</code>
	 * @uml.property name="position"
	 */
	public void setPosition(final Position position)
			throws IllegalArgumentException {
		Validate.notNull(position, "position");
		this.position = position;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="workAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="companyRepresentative:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_WORK_ADDRESS", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_REPWORKADDR")
	private PostalAddress workAddress;

	/**
	 * Getter of the property <tt>workAddress</tt>
	 * 
	 * @return Returns the workAddress.
	 * @uml.property name="workAddress"
	 */
	public PostalAddress getWorkAddress() {
		return this.workAddress;
	}

	/**
	 * Setter of the property <tt>workAddress</tt>
	 * 
	 * @param workAddress
	 *            The workAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>workAddress</code> is <code>null</code>
	 * @uml.property name="workAddress"
	 */
	public void setWorkAddress(final PostalAddress workAddress)
			throws IllegalArgumentException {
		Validate.notNull(workAddress, "workAddress");
		this.workAddress = workAddress;
	}

	/**
	 * @uml.property name="workEmailAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="companyRepresentative:com.acme.orderplacement.domain.people.contact.EmailAddress"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_WORK_EMAIL_ADDRESS", unique = false, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_REPWORKEMAIL")
	private EmailAddress workEmailAddress;

	/**
	 * Getter of the property <tt>workEmailAddress</tt>
	 * 
	 * @return Returns the workEmailAddress.
	 * @uml.property name="workEmailAddress"
	 */
	public EmailAddress getWorkEmailAddress() {
		return this.workEmailAddress;
	}

	/**
	 * Setter of the property <tt>workEmailAddress</tt>
	 * 
	 * @param workEmailAddress
	 *            The workEmailAddress to set.
	 * @throws IllegalArgumentException
	 *             If <code>workEmailAddress</code> is <code>null</code>
	 * @uml.property name="workEmailAddress"
	 */
	public void setWorkEmailAddress(final EmailAddress workEmailAddress)
			throws IllegalArgumentException {
		Validate.notNull(workEmailAddress, "workEmailAddress");
		this.workEmailAddress = workEmailAddress;
	}

	/**
	 * @uml.property name="workPhoneNumber"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="corporateClientRepresentative:com.acme.orderplacement.domain.people.contact.PhoneNumber"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_WORK_PHONE_NUMBER", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_REPWORKPHONE")
	private PhoneNumber workPhoneNumber;

	/**
	 * Getter of the property <tt>workPhoneNumber</tt>
	 * 
	 * @return Returns the workPhoneNumber.
	 * @uml.property name="workPhoneNumber"
	 */
	public PhoneNumber getWorkPhoneNumber() {
		return this.workPhoneNumber;
	}

	/**
	 * Setter of the property <tt>workPhoneNumber</tt>
	 * 
	 * @param workPhoneNumber
	 *            The workPhoneNumber to set.
	 * @throws IllegalArgumentException
	 *             If <code>workPhoneNumber</code> is <code>null</code>
	 * @uml.property name="workPhoneNumber"
	 */
	public void setWorkPhoneNumber(final PhoneNumber workPhoneNumber)
			throws IllegalArgumentException {
		Validate.notNull(workPhoneNumber, "workPhoneNumber");
		this.workPhoneNumber = workPhoneNumber;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="parentPerson"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "companyRepresentative:com.acme.orderplacement.domain.people.person.Person"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_PARENT_PERSON", /* referencedColumnName = "ID", */unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_REPPARENTPERSON")
	private Person parentPerson;

	/**
	 * Getter of the property <tt>parentPerson</tt>
	 * 
	 * @return Returns the parentPerson.
	 * @uml.property name="parentPerson"
	 */
	public Person getParentPerson() {
		return this.parentPerson;
	}

	/**
	 * Setter of the property <tt>parentPerson</tt>
	 * 
	 * TODO: Find a way to verify that parentPerson is not already acting as
	 * another CorporateClient's Representative
	 * 
	 * @param parentPerson
	 *            The parentPerson to set.
	 * @throws IllegalArgumentException
	 *             If <code>parentPerson</code> is <code>null</code>
	 * @uml.property name="parentPerson"
	 */
	public void setParentPerson(final Person parentPerson)
			throws IllegalArgumentException {
		Validate.notNull(parentPerson, "parentPerson");
		this.parentPerson = parentPerson;
	}

	/**
	 * @uml.property name="representedCorporateClient"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="representatives:com.acme.orderplacement.domain.company.customer.CorporateClient"
	 * @uml.association name="CorporateClient - Representatives"
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID_CORPORATE_CLIENT", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_CORPCLIENT_REPCORPCLIENT")
	private CorporateClient representedCorporateClient;

	/**
	 * Getter of the property <tt>representedCorporateClient</tt>
	 * 
	 * @return Returns the representedCorporateClient.
	 * @uml.property name="representedCorporateClient"
	 */
	public CorporateClient getRepresentedCorporateClient() {
		return this.representedCorporateClient;
	}

	/**
	 * Setter of the property <tt>representedCorporateClient</tt>
	 * 
	 * @param representedCorporateClient
	 *            The representedCorporateClient to set.
	 * @throws IllegalArgumentException
	 *             If <code>representedCorporateClient</code> is
	 *             <code>null</code>
	 * @uml.property name="representedCorporateClient"
	 */
	public void setRepresentedCorporateClient(
			final CorporateClient representedCorporateClient)
			throws IllegalArgumentException {
		Validate.notNull(representedCorporateClient,
				"representedCorporateClient");
		this.representedCorporateClient = representedCorporateClient;
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
				+ ((this.corporateClientRepresentativeNumber == null) ? 0
						: this.corporateClientRepresentativeNumber.hashCode());
		result = PRIME * result
				+ ((this.position == null) ? 0 : this.position.hashCode());
		result = PRIME
				* result
				+ ((this.representedCorporateClient == null) ? 0
						: this.representedCorporateClient.hashCode());
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
		final CorporateClientRepresentative other = (CorporateClientRepresentative) obj;
		if (this.corporateClientRepresentativeNumber == null) {
			if (other.corporateClientRepresentativeNumber != null) {
				return false;
			}
		} else if (!this.corporateClientRepresentativeNumber
				.equals(other.corporateClientRepresentativeNumber)) {
			return false;
		}
		if (this.position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!this.position.equals(other.position)) {
			return false;
		}
		if (this.representedCorporateClient == null) {
			if (other.representedCorporateClient != null) {
				return false;
			}
		} else if (!this.representedCorporateClient
				.equals(other.representedCorporateClient)) {
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
				"workAddress", this.workAddress).append("workEmailAddress",
				this.workEmailAddress).append(
				"corporateClientRepresentativeNumber",
				this.corporateClientRepresentativeNumber).append(
				"workPhoneNumber", this.workPhoneNumber).append("position",
				this.position).toString();
	}

}
