/**
 * 
 */
package com.acme.orderplacement.framework.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Convenience base class for {@link IdentifiableDomainObject
 * <code>IdentifiableDomainObject</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MappedSuperclass
public abstract class AbstractIdentifiableDomainObject<ID extends Serializable>
		implements IdentifiableDomainObject<ID>, Serializable {

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 4507592741477678789L;

	/**
	 * No-args default constructor.
	 */
	public AbstractIdentifiableDomainObject() {
		// Intentionally left blank
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>ID</code>.
	 * </p>
	 * 
	 * @param id
	 */
	public AbstractIdentifiableDomainObject(final ID id) {
		super();
		this.id = id;
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * This <tt>Domain Object</tt>'s <code>ID</code>.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	private ID id;

	/**
	 * @see com.acme.orderplacement.framework.domain.IdentifiableDomainObject#getId()
	 */
	public ID getId() {

		return this.id;
	}

	/**
	 * @see com.acme.orderplacement.framework.domain.IdentifiableDomainObject#setId(java.io.Serializable)
	 */
	public void setId(final ID newId) {

		this.id = newId;
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
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		final AbstractIdentifiableDomainObject<?> other = (AbstractIdentifiableDomainObject<?>) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * Serializable
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).toString();
	}
}
