/**
 * 
 */
package com.acme.orderplacement.domain.people.person;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import com.acme.orderplacement.domain.people.account.Account;
import com.acme.orderplacement.domain.people.contact.PhoneNumber;
import com.acme.orderplacement.domain.people.contact.PostalAddress;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;

/**
 * <p>
 * A <code>Person</code> represents a physically existing human being and its
 * <em>intrinsic</em> state and behavior. In this context, <em>intrinsic</em>
 * means state and behavior that is <em>not</em> bound to a certain
 * <em>role</em> a given <code>Person</code> may assume but invariant across
 * different contexts.
 * </p>
 * <p>
 * Examples for <em>intrinsic</em> properties are:<br/>
 * <br/>
 * <ul>
 * <li> <tt>Date of Birth</tt></li>
 * <li> <tt>First Name</tt></li>
 * <li> <tt>Gender</tt></li>
 * <li> <tt>Home Address</tt></li>
 * </ul>
 * </p>
 * <p>
 * Examples for <em>context-dependent</em> properties are:<br/>
 * <br/>
 * <ul>
 * <li> <tt>Shipping Address</tt> - Only makes sense if the <code>Person</code>
 * is viewed as a <code>Customer</code>.</li>
 * <li> <tt>Invoice Address</tt> - Only makes sense if the <code>Person</code> is
 * viewed as a <code>Customer</code>.</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Table(schema = "PEOPLE", name = "PERSON")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "PEOPLE.ID_SEQ_PERSON")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_LAST_NAME_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.lastName like :lastName"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_FIRST_NAME_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.firstName like :firstName"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BORN_AFTER, query = "from com.acme.orderplacement.domain.people.person.Person person where person.dateOfBirth > :dateOfBirth"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BORN_BEFORE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.dateOfBirth < :dateOfBirth"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BORN_BETWEEN, query = "from com.acme.orderplacement.domain.people.person.Person person where (person.dateOfBirth >= :firstDate and person.dateOfBirth <= :secondDate)"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_PRIVATE_RESIDENTIAL_PHONE_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.privateResidentialPhone like :privateResidentialPhone"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_PRIVATE_MOBILE_PHONE_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.privateMobilePhone like :privateMobilePhone"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_PRIVATE_FAX_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.privateFax like :privateFax"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_USERNAME_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.account.username like :username"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_STREET_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where person.homeAddress.street like :street"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_STREET_AND_CITY_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where (person.homeAddress.street like :street and person.homeAddress.city like :city)"),
		@org.hibernate.annotations.NamedQuery(name = Person.Queries.BY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE, query = "from com.acme.orderplacement.domain.people.person.Person person where (person.homeAddress.street like :street and person.homeAddress.city like :city and person.homeAddress.postalCode like :postalCode)") })
public class Person extends AbstractAuditableDomainObject<Long> implements
		Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>Person</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_LAST_NAME_LIKE = "person.byLastNameLike";

		public static final String BY_FIRST_NAME_LIKE = "person.byFirstNameLike";

		public static final String BORN_AFTER = "person.bornAfter";

		public static final String BORN_BEFORE = "person.bornBefore";

		public static final String BORN_BETWEEN = "person.bornBetween";

		public static final String BY_PRIVATE_RESIDENTIAL_PHONE_LIKE = "person.byPrivateResidentialPhoneLike";

		public static final String BY_PRIVATE_FAX_LIKE = "person.byPrivateFaxLike";

		public static final String BY_PRIVATE_MOBILE_PHONE_LIKE = "person.byPrivateMobilePhoneLike";

		public static final String BY_USERNAME_LIKE = "person.byUsernameLike";

		public static final String BY_STREET_LIKE = "person.byStreetLike";

		public static final String BY_STREET_AND_CITY_LIKE = "person.byStreetAndCityLike";

		public static final String BY_STREET_AND_CITY_AND_POSTAL_CODE_LIKE = "person.byStreetAndCityAndPostalCodeLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = -6714310996384737256L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public Person() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * This <code>Person</code>'s <tt>First Name</tt>. May neither be
	 * <code>null</code> nor empty.
	 * 
	 * @uml.property name="firstName"
	 */
	@NotNull
	@Length(min = 2, max = 40)
	@Basic
	@Column(name = "FIRST_NAME", unique = false, nullable = false, length = 40)
	private String firstName;

	/**
	 * Getter of the property <tt>firstName</tt>
	 * 
	 * @return Returns the firstName.
	 * @uml.property name="firstName"
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Setter of the property <tt>firstName</tt>
	 * 
	 * @param firstName
	 *            The firstName to set.
	 * @throws IllegalArgumentException
	 *             If <code>firstName</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="firstName"
	 */
	public void setFirstName(final String firstName)
			throws IllegalArgumentException {
		Validate.notEmpty(firstName, "firstName");
		this.firstName = firstName;
	}

	/**
	 * This <code>Person</code>'s <tt>Middle Names</tt>. May be
	 * <code>null</code>. May be <code>null</code>.
	 * 
	 * @uml.property name="middleNames"
	 */
	@Length(min = 1, max = 60)
	@Basic
	@Column(name = "MIDDLE_NAMES", unique = false, nullable = true, length = 60)
	private String middleNames;

	/**
	 * Getter of the property <tt>middleNames</tt>
	 * 
	 * @return Returns the middleNames.
	 * @uml.property name="middleNames"
	 */
	public String getMiddleNames() {
		return this.middleNames;
	}

	/**
	 * Setter of the property <tt>middleNames</tt>
	 * 
	 * @param middleNames
	 *            The middleNames to set.
	 * @uml.property name="middleNames"
	 */
	public void setMiddleNames(final String middleNames) {
		this.middleNames = middleNames;
	}

	/**
	 * This <code>Person</code>'s <tt>Last Name</tt>. May neither be
	 * <code>null</code> nor empty.
	 * 
	 * @uml.property name="lastName"
	 */
	@NotNull
	@Length(min = 2, max = 40)
	@Basic
	@Column(name = "LAST_NAME", unique = false, nullable = false, length = 40)
	private String lastName;

	/**
	 * Getter of the property <tt>lastName</tt>
	 * 
	 * @return Returns the lastName.
	 * @uml.property name="lastName"
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Setter of the property <tt>lastName</tt>
	 * 
	 * @param lastName
	 *            The lastName to set.
	 * @throws IllegalArgumentException
	 *             If <code>lastName</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="lastName"
	 */
	public void setLastName(final String lastName)
			throws IllegalArgumentException {
		Validate.notEmpty(lastName, "lastName");
		this.lastName = lastName;
	}

	/**
	 * <p>
	 * Build and return this <code>Person</code>'s <tt>Full Name</tt>, i.e.
	 * <code>firstName (middleNames ) lastName</code>.
	 * </p>
	 * 
	 * @return
	 */
	public String getFullName() {
		final StringBuilder fullName = new StringBuilder(this.firstName);
		fullName.append(' ');
		if (this.middleNames != null) {
			fullName.append(this.middleNames).append(' ');
		}
		fullName.append(this.lastName);

		return fullName.toString();
	}

	/**
	 * When this <code>Person</code> was born. May <strong>not</strong> be
	 * <code>null</code>.
	 * 
	 * @uml.property name="dateOfBirth"
	 */
	@NotNull
	@Past
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_BIRTH", unique = false, nullable = false)
	private Date dateOfBirth;

	/**
	 * Getter of the property <tt>dateOfBirth</tt>
	 * 
	 * @return Returns the dateOfBirth.
	 * @uml.property name="dateOfBirth"
	 */
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	/**
	 * Setter of the property <tt>dateOfBirth</tt>
	 * 
	 * @param dateOfBirth
	 *            The dateOfBirth to set.
	 * @throws IllegalArgumentException
	 *             If <code>dateOfBirth</code> is <code>null</code>
	 * 
	 * @uml.property name="dateOfBirth"
	 */
	public void setDateOfBirth(final Date dateOfBirth)
			throws IllegalArgumentException {
		Validate.notNull(dateOfBirth, "dateOfBirth");
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * This <code>Person</code>'s <tt>Salutation</tt>, i.e. &quot;Mr.&quot; or
	 * &quot;Mrs.&quot;. May <strong>not</strong> be <code>null</code>.
	 * 
	 * @uml.property name="salutation"
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "SALUTATION", unique = false, nullable = false, length = 10)
	private Salutation salutation;

	/**
	 * Getter of the property <tt>salutation</tt>
	 * 
	 * @return Returns the salutation.
	 * @uml.property name="salutation"
	 */
	public Salutation getSalutation() {
		return this.salutation;
	}

	/**
	 * Setter of the property <tt>salutation</tt>
	 * 
	 * @param salutation
	 *            The salutation to set.
	 * @throws IllegalArgumentException
	 *             If <code>salutation</code> is <code>null</code>
	 * 
	 * @uml.property name="salutation"
	 */
	public void setSalutation(final Salutation salutation)
			throws IllegalArgumentException {
		Validate.notNull(salutation, "salutation");
		this.salutation = salutation;
	}

	/**
	 * This <code>Person</code>'s <tt>Title</tt> (i.e. &quot;Dr.&quot;), if any.
	 * May be <code>null</code>.
	 * 
	 * @uml.property name="title"
	 */
	@Length(min = 1, max = 30)
	@Basic
	@Column(name = "TITLE", unique = false, nullable = true, length = 30)
	private String title;

	/**
	 * Getter of the property <tt>title</tt>
	 * 
	 * @return Returns the title.
	 * @uml.property name="title"
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter of the property <tt>title</tt>
	 * 
	 * @param title
	 *            The title to set.
	 * @uml.property name="title"
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * This <code>Person</code>'s {@link Gender <code>Gender</code>} , i.e.
	 * <tt>male</tt> or <tt>female</tt>. May <strong>not</strong> be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="gender"
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "GENDER", unique = false, nullable = false, length = 10)
	private Gender gender;

	/**
	 * Getter of the property <tt>gender</tt>
	 * 
	 * @return Returns the gender.
	 * @uml.property name="gender"
	 */
	public Gender getGender() {
		return this.gender;
	}

	/**
	 * Setter of the property <tt>gender</tt>
	 * 
	 * @param gender
	 *            The gender to set.
	 * @throws IllegalArgumentException
	 *             If <code>gender</code> is <code>null</code>
	 * 
	 * @uml.property name="gender"
	 */
	public void setGender(final Gender gender) throws IllegalArgumentException {
		Validate.notNull(gender, "gender");
		this.gender = gender;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="account"
	 * @uml.associationEnd inverse=
	 *                     "accountOwner:com.acme.orderplacement.domain.people.account.Account"
	 * @uml.association name="Account - AccountOwner"
	 */
	@OneToOne(mappedBy = "accountOwner", cascade = CascadeType.ALL)
	private Account account;

	/**
	 * Getter of the property <tt>account</tt>
	 * 
	 * @return Returns the account.
	 * @uml.property name="account"
	 */
	public Account getAccount() {
		return this.account;
	}

	/**
	 * Setter of the property <tt>account</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>account</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>account</code> is owned by another
	 *             <code>Person</code>
	 * 
	 * @param account
	 *            The account to set.
	 * 
	 * @uml.property name="account"
	 */
	public void setAccount(final Account account)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		try {
			Validate.notNull(account, "account");
			if (account.hasAccountOwner()
					&& !account.getAccountOwner().equals(this)) {
				final String error = "Cannot accept an Account ['"
						+ account
						+ "'] as this Person's account which is owned by another Person ['"
						+ account.getAccountOwner() + "']";
				throw new CollaborationPreconditionsNotMetException(error);
			}
			this.account = account;
			if ((!account.hasAccountOwner())
					|| (account.getAccountOwner() != this)) {
				account.setAccountOwner(this);
			}
		} catch (final IllegalDomainObjectStateException e) {
			throw new NestableRuntimeException(e);
		}
	}

	/**
	 * @uml.property name="homeAddress"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "person:com.acme.orderplacement.domain.people.contact.PostalAddress"
	 * @uml.association name="Person - HomeAddress"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_HOME_ADDRESS", unique = false, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_PERSON_HOMEADDR")
	private PostalAddress homeAddress;

	/**
	 * Getter of the property <tt>homeAddress</tt>
	 * 
	 * @return Returns the homeAddress.
	 * @uml.property name="homeAddress"
	 */
	public PostalAddress getHomeAddress() {
		return this.homeAddress;
	}

	/**
	 * Setter of the property <tt>homeAddress</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>homeAddress</code> is <code>null</code>
	 * 
	 * @param homeAddress
	 *            The homeAddress to set.
	 * @uml.property name="homeAddress"
	 */
	public void setHomeAddress(final PostalAddress homeAddress)
			throws IllegalArgumentException {
		Validate.notNull(homeAddress, "homeAddress");
		this.homeAddress = homeAddress;
	}

	/**
	 * Remove property <code>homeAddress</code>.
	 */
	public void removeHomeAddress() {

		this.homeAddress = null;
	}

	/**
	 * @uml.property name="privateResidentialPhone"
	 * @uml.associationEnd multiplicity="(1 1)" inverse=
	 *                     "person:com.acme.orderplacement.domain.people.contact.PhoneNumber"
	 * @uml.association name="Person - PrivateResidentialPhone"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_PRIVATE_RESIDENTIAL_PHONE", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_PERSON_PRVRESPHONE")
	private PhoneNumber privateResidentialPhone;

	/**
	 * Getter of the property <tt>privateResidentialPhone</tt>
	 * 
	 * @return Returns the privateResidentialPhone.
	 * @uml.property name="privateResidentialPhone"
	 */
	public PhoneNumber getPrivateResidentialPhone() {
		return this.privateResidentialPhone;
	}

	/**
	 * Setter of the property <tt>privateResidentialPhone</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>privateResidentialPhone</code> is <code>null</code>
	 * 
	 * @param privateResidentialPhone
	 *            The privateResidentialPhone to set.
	 * @uml.property name="privateResidentialPhone"
	 */
	public void setPrivateResidentialPhone(
			final PhoneNumber privateResidentialPhone)
			throws IllegalArgumentException {
		Validate.notNull(privateResidentialPhone, "privateResidentialPhone");
		this.privateResidentialPhone = privateResidentialPhone;
	}

	/**
	 * Remove property <code>privateResidentialPhone</code>.
	 */
	public void removePrivateResidentialPhone() {

		this.privateResidentialPhone = null;
	}

	/**
	 * @uml.property name="privateMobilePhone"
	 * @uml.associationEnd inverse=
	 *                     "person:com.acme.orderplacement.domain.people.contact.PhoneNumber"
	 * @uml.association name="Person - PrivateMobilePhone"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_PRIVATE_MOBILE_PHONE", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_PERSON_PRVMOBPHONE")
	private PhoneNumber privateMobilePhone;

	/**
	 * Getter of the property <tt>privateMobilePhone</tt>
	 * 
	 * @return Returns the privateMobilePhone.
	 * @uml.property name="privateMobilePhone"
	 */
	public PhoneNumber getPrivateMobilePhone() {
		return this.privateMobilePhone;
	}

	/**
	 * Setter of the property <tt>privateMobilePhone</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>privateMobilePhone</code> is <code>null</code>
	 * 
	 * @param privateMobilePhone
	 *            The privateMobilePhone to set.
	 * @uml.property name="privateMobilePhone"
	 */
	public void setPrivateMobilePhone(final PhoneNumber privateMobilePhone)
			throws IllegalArgumentException {
		Validate.notNull(privateMobilePhone, "privateMobilePhone");
		this.privateMobilePhone = privateMobilePhone;
	}

	/**
	 * Remove property <code>privateMobilePhone</code>.
	 */
	public void removePrivateMobilePhone() {

		this.privateMobilePhone = null;
	}

	/**
	 * @uml.property name="privateFax"
	 * @uml.associationEnd inverse=
	 *                     "person:com.acme.orderplacement.domain.people.contact.PhoneNumber"
	 * @uml.association name="Person - PrivateFax"
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_PRIVATE_FAX", unique = false, nullable = true)
	@org.hibernate.annotations.ForeignKey(name = "FK_PERSON_PRVFAX")
	private PhoneNumber privateFax;

	/**
	 * Getter of the property <tt>privateFax</tt>
	 * 
	 * @return Returns the privateFax.
	 * @uml.property name="privateFax"
	 */
	public PhoneNumber getPrivateFax() {
		return this.privateFax;
	}

	/**
	 * Setter of the property <tt>privateFax</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>privateFax</code> is <code>null</code>
	 * 
	 * @param privateFax
	 *            The privateFax to set.
	 * @uml.property name="privateFax"
	 */
	public void setPrivateFax(final PhoneNumber privateFax)
			throws IllegalArgumentException {
		Validate.notNull(privateFax, "privateFax");
		this.privateFax = privateFax;
	}

	/**
	 * Remove property <code>privateFax</code>.
	 */
	public void removePrivateFax() {

		this.privateFax = null;
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
				+ ((this.dateOfBirth == null) ? 0 : this.dateOfBirth.hashCode());
		result = PRIME * result
				+ ((this.firstName == null) ? 0 : this.firstName.hashCode());
		result = PRIME * result
				+ ((this.gender == null) ? 0 : this.gender.hashCode());
		result = PRIME * result
				+ ((this.lastName == null) ? 0 : this.lastName.hashCode());
		result = PRIME * result
				+ ((this.salutation == null) ? 0 : this.salutation.hashCode());
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
		final Person other = (Person) obj;
		if (this.dateOfBirth == null) {
			if (other.dateOfBirth != null) {
				return false;
			}
		} else if (!this.dateOfBirth.equals(other.dateOfBirth)) {
			return false;
		}
		if (this.firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!this.firstName.equals(other.firstName)) {
			return false;
		}
		if (this.gender == null) {
			if (other.gender != null) {
				return false;
			}
		} else if (!this.gender.equals(other.gender)) {
			return false;
		}
		if (this.lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!this.lastName.equals(other.lastName)) {
			return false;
		}
		if (this.salutation == null) {
			if (other.salutation != null) {
				return false;
			}
		} else if (!this.salutation.equals(other.salutation)) {
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
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("firstName", this.firstName)
				.append("middleNames", this.middleNames)
				.append("lastName", this.lastName)
				.append("dateOfBirth", this.dateOfBirth)
				.append("salutation", this.salutation)
				.append("title", this.title)
				.append("gender", this.gender)
				.append(
						"username",
						(this.account != null ? this.account.getUsername()
								: null))
				.append("homeAddress", this.homeAddress)
				.append("privateResidentialPhone", this.privateResidentialPhone)
				.append("privateMobilePhone", this.privateMobilePhone).append(
						"privateFax", this.privateFax).toString();
	}

}
