/**
 * 
 */
package com.acme.orderplacement.domain.people.contact;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;

/**
 * <p>
 * Abstract base class for all types representing a <tt>contact channel</tt> to
 * be used for getting in contact with a {@link Person <code>Person</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(schema = "PEOPLE", name = "CONTACT_CHANNEL")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "PEOPLE.ID_SEQ_CONTACT_CHANNEL")
@NamedQueries( {
		@NamedQuery(name = "contactChannel.byName", query = "from com.acme.orderplacement.domain.people.contact.ContactChannel contactChannel where contactChannel.name = :name"),
		@NamedQuery(name = "contactChannel.byNameLike", query = "from com.acme.orderplacement.domain.people.contact.ContactChannel contactChannel where contactChannel.name like '%:name%'") })
public abstract class ContactChannel extends
		AbstractAuditableDomainObject<Long> implements Serializable {

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 8417855910857569727L;

	/**
	 * Noa-args default constructor. Shouldn't be here, but is needed by i.e. <a
	 * href="http://www.hibernate.org">Hibernate</a>.s
	 */
	protected ContactChannel() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A human readable <tt>name</tt> for this
	 * <code>ContactChannel</tt>, i.e. &quot;Work Email&quot;. The <code>name</code>
	 * may <strong>not</strong> be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Size(min = 2, max = 30)
	@Basic
	@Column(name = "NAME", unique = false, nullable = false, length = 30)
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
	 *             if <code>name</code> is <code>null</code> or empty
	 * 
	 * @uml.property name="name"
	 */
	public void setName(final String name) throws IllegalArgumentException {
		Validate.notEmpty(name, "name");
		this.name = name;
	}

	/**
	 * <p>
	 * An optional comment further describing this <code>ContactChannel</code>.
	 * May be <code>null</code>.
	 * </p>
	 */
	@Size(min = 2, max = 200)
	@Basic
	@Column(name = "COMMENT", unique = false, nullable = true, length = 200)
	private String comment;

	/**
	 * @param newComment
	 *            the comment to set
	 */
	public void setComment(final String newComment) {
		this.comment = newComment;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
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
				+ ((this.comment == null) ? 0 : this.comment.hashCode());
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
		final ContactChannel other = (ContactChannel) obj;
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
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
				"name", this.name).append("comment", this.comment).toString();
	}

}
