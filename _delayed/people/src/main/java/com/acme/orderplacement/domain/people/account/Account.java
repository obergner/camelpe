/**
 * 
 */
package com.acme.orderplacement.domain.people.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;

import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;

/**
 * <p>
 * An <code>Account</code> holds credentials uniquely identifying a
 * {@link Person <code>Person</code>} and metadata for these credentials, i.e.
 * when do these credentials expire etc.
 * </p>
 * <p>
 * The relationship between {@link Person <code>Person</code>} and
 * <code>Account</code> is 1:1, so that there is exactly one
 * <code>Account</code> per <code>Person</code> and each <code>Account</code>
 * belongs to exactly one <code>Person</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Table(schema = "PEOPLE", name = "PERSON_ACCOUNT")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "PEOPLE.ID_SEQ_ACCOUNT")
@NamedQueries( {
		@NamedQuery(name = Account.Queries.BY_USERNAME, query = "from com.acme.orderplacement.domain.people.account.Account account where account.username = :username"),
		@NamedQuery(name = Account.Queries.BY_USERNAME_LIKE, query = "from com.acme.orderplacement.domain.people.account.Account account where account.username like '%:username%'") })
public class Account extends AbstractAuditableDomainObject<Long> implements
		Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>Account</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_USERNAME = "account.byUsername";

		public static final String BY_USERNAME_LIKE = "account.byUsernameLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Class version used in serialization.
	 */
	private static final long serialVersionUID = 8852835276868459947L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.
	 */
	public Account() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * The <tt>username</tt> of the <code>Person</code> who owns this
	 * <code>Account</tt>. May neither be <code>null</code> nor empty.
	 * </p>
	 * 
	 * @uml.property name="username"
	 */
	@NotNull
	@Size(min = 8, max = 20)
	@Basic
	@Column(name = "USERNAME", unique = true, nullable = false, length = 20)
	private String username;

	/**
	 * Getter of the property <tt>username</tt>
	 * 
	 * @return Returns the username.
	 * @uml.property name="username"
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Setter of the property <tt>username</tt>
	 * 
	 * @param username
	 *            The username to set.
	 * @throws IllegalArgumentException
	 *             If <code>username</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="username"
	 */
	public void setUsername(final String username)
			throws IllegalArgumentException {
		Validate.notEmpty(username, "username");
		this.username = username;
	}

	/**
	 * <p>
	 * The cleartext <code>password</code> of the <code>Person</code> who owns
	 * this account. May neither be <code>null</code> nor empty.
	 * </p>
	 * 
	 * @uml.property name="password"
	 */
	@NotNull
	@Size(min = 8, max = 30)
	@Basic
	@Column(name = "PASSWORD", unique = false, nullable = false, length = 30)
	private String password;

	/**
	 * Getter of the property <tt>password</tt>
	 * 
	 * @return Returns the password.
	 * @uml.property name="password"
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter of the property <tt>password</tt>
	 * 
	 * @param password
	 *            The password to set.
	 * @throws IllegalArgumentException
	 *             If <code>password</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="password"
	 */
	public void setPassword(final String password)
			throws IllegalArgumentException {
		Validate.notEmpty(password, "password");
		this.password = password;
	}

	/**
	 * <p>
	 * When has this <code>Acount</code>'s <tt>password</tt> last been changed?
	 * May not be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="passwordLastChangedOn"
	 */
	@NotNull
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PASSWORD_LAST_CHANGED_ON", unique = false, nullable = false)
	private Date passwordLastChangedOn;

	/**
	 * Getter of the property <tt>passwordLastChangedOn</tt>
	 * 
	 * @return Returns the passwordLastChangedOn.
	 * @uml.property name="passwordLastChangedOn"
	 */
	public Date getPasswordLastChangedOn() {
		return this.passwordLastChangedOn;
	}

	/**
	 * Setter of the property <tt>passwordLastChangedOn</tt>
	 * 
	 * @param passwordLastChangedOn
	 *            The passwordLastChangedOn to set.
	 * @throws IllegalArgumentException
	 *             If <code>passwordLastChangedOn</code> is <code>null</code>
	 * 
	 * @uml.property name="passwordLastChangedOn"
	 */
	public void setPasswordLastChangedOn(final Date passwordLastChangedOn)
			throws IllegalArgumentException {
		Validate.notNull(passwordLastChangedOn, "passwordLastChangedOn");
		this.passwordLastChangedOn = passwordLastChangedOn;
	}

	/**
	 * <p>
	 * When does this <code>Account</code> expire? A value of <code>null</code>
	 * means that it will never expire.
	 * </p>
	 * 
	 * @uml.property name="accountExpiresOn"
	 */
	@Future
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ACCOUNT_EXPIRES_ON", unique = false, nullable = true)
	private Date accountExpiresOn;

	/**
	 * Getter of the property <tt>accountExpiresOn</tt>
	 * 
	 * @return Returns the accountExpiresOn.
	 * @uml.property name="accountExpiresOn"
	 */
	public Date getAccountExpiresOn() {
		return this.accountExpiresOn;
	}

	/**
	 * Setter of the property <tt>accountExpiresOn</tt>
	 * 
	 * @param accountExpiresOn
	 *            The accountExpiresOn to set.
	 * @uml.property name="accountExpiresOn"
	 */
	public void setAccountExpiresOn(final Date accountExpiresOn) {
		this.accountExpiresOn = accountExpiresOn;
	}

	/**
	 * <p>
	 * The secret question to be asked this <code>Account</code>'s owner if
	 * he/she forgets his/her password. May be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="secretQuestion"
	 */
	@Size(min = 3, max = 200)
	@Basic
	@Column(name = "SECRET_QUESTION", unique = false, nullable = true, length = 200)
	private String secretQuestion;

	/**
	 * Getter of the property <tt>secretQuestion</tt>
	 * 
	 * @return Returns the secretQuestion.
	 * @uml.property name="secretQuestion"
	 */
	public String getSecretQuestion() {
		return this.secretQuestion;
	}

	/**
	 * Setter of the property <tt>secretQuestion</tt>
	 * 
	 * @param secretQuestion
	 *            The secretQuestion to set.
	 * @uml.property name="secretQuestion"
	 */
	public void setSecretQuestion(final String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	/**
	 * <p>
	 * The answer to the <code>secretQuestion</code>. May be <code>null</code>
	 * if {@link #secretQuestion} is <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="secretAnswer"
	 */
	@Size(min = 1, max = 100)
	@Basic
	@Column(name = "SECRET_ANSWER", unique = false, nullable = true, length = 100)
	private String secretAnswer;

	/**
	 * Getter of the property <tt>secretAnswer</tt>
	 * 
	 * @return Returns the secretAnswer.
	 * @uml.property name="secretAnswer"
	 */
	public String getSecretAnswer() {
		return this.secretAnswer;
	}

	/**
	 * Setter of the property <tt>secretAnswer</tt>
	 * 
	 * @param secretAnswer
	 *            The secretAnswer to set.
	 * @throws IllegalStateException
	 *             If <code>secretAnswer</code> is <code>null</code> while
	 *             {@link #secretQuestion <code>secretQuestion</code>} is
	 *             <strong>not</strong> <code>null</code>.
	 * 
	 * @uml.property name="secretAnswer"
	 */
	public void setSecretAnswer(final String secretAnswer)
			throws IllegalStateException {
		if (StringUtils.isBlank(secretAnswer) && (getSecretQuestion() != null)) {
			throw new IllegalStateException(
					"Cannot accept an empty 'Secret Answer' to a non-empty 'Secret Question'");
		}
		this.secretAnswer = secretAnswer;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="accountOwner"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="account:com.acme.orderplacement.domain.people.person.Person"
	 * @uml.association name="Account - AccountOwner"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_PERSON", referencedColumnName = "ID", nullable = false, unique = true)
	@ForeignKey(name = "FK_ACCOUNT_PERSON")
	private Person accountOwner;

	/**
	 * Getter of the property <tt>accountOwner</tt>
	 * 
	 * @throws IllegalDomainObjectStateException
	 *             If {@link #accountOwner <code>accountOwner</code>} is
	 *             <code>null</code>. An <code>Account</code> cannot live
	 *             without its parent {@link Person <code>Person</code>}.
	 * 
	 * @return Returns the accountOwner.
	 * @uml.property name="accountOwner"
	 */
	public Person getAccountOwner() throws IllegalDomainObjectStateException {
		if (this.accountOwner == null) {
			throw new IllegalDomainObjectStateException(
					"An Account MUST have an associated parent Person [accountOwner = <null>]");
		}

		return this.accountOwner;
	}

	/**
	 * Setter of the property <tt>accountOwner</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>accountOwner</code> is <code>null</code>
	 * @throws CollaborationPreconditionsNotMetException
	 *             If <code>accountOwner</code> already owns an
	 *             <code>Account</code> that is not this <code>Account</code>
	 * @param accountOwner
	 *            The accountOwner to set.
	 * @uml.property name="accountOwner"
	 */
	public void setAccountOwner(final Person accountOwner)
			throws IllegalArgumentException,
			CollaborationPreconditionsNotMetException {
		Validate.notNull(accountOwner, "accountOwner");
		if ((accountOwner.getAccount() != null)
				&& !accountOwner.getAccount().equals(this)) {
			final String error = "Cannot accept a Person ['"
					+ accountOwner
					+ "'] as this Account's owner who already owns a different Account ['"
					+ accountOwner.getAccount() + "']";
			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.accountOwner = accountOwner;
		if (accountOwner.getAccount() != this) {
			accountOwner.setAccount(this);
		}
	}

	/**
	 * <p>
	 * Test whether this <code>Account</code> has an {@link Person
	 * <code>AccountOwner</code>} as it should.
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasAccountOwner() {

		return this.accountOwner != null;
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
				+ ((this.password == null) ? 0 : this.password.hashCode());
		result = PRIME * result
				+ ((this.username == null) ? 0 : this.username.hashCode());
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
		final Account other = (Account) obj;
		if (this.password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!this.password.equals(other.password)) {
			return false;
		}
		if (this.username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!this.username.equals(other.username)) {
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
				"username", this.username).append("password", this.password)
				.append("passwordLastChangedOn", this.passwordLastChangedOn)
				.append("accountExpiresOn", this.accountExpiresOn).append(
						"secretQuestion", this.secretQuestion).append(
						"secretAnswer", this.secretAnswer).append(
						"accountOwner",
						(this.accountOwner != null ? this.accountOwner
								.getFullName() : null)).toString();
	}

}
