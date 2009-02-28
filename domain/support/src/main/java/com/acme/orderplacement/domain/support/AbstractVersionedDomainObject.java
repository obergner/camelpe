/**
 * 
 */
package com.acme.orderplacement.domain.support;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Convenience base class for {@link VersionedDomainObject
 * <code>VersionedDomainObject</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MappedSuperclass
public abstract class AbstractVersionedDomainObject<ID extends Serializable>
		extends AbstractIdentifiableDomainObject<ID> implements
		VersionedDomainObject<ID>, Serializable {

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -9105733803586970512L;

	/**
	 * <p>
	 * No-args default constructor.
	 * </p>
	 */
	public AbstractVersionedDomainObject() {
		super();
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>ID</code>.
	 * </p>
	 * 
	 * @param id
	 */
	public AbstractVersionedDomainObject(final ID id) {
		super(id);
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>version</code>.
	 * </p>
	 * 
	 * @param version
	 */
	public AbstractVersionedDomainObject(final Integer version) {
		super();
		this.version = version;
	}

	/**
	 * <p>
	 * Constructor setting both this <tt>Domain Object</tt>'s <code>ID</code>
	 * and its <code>version</code>.
	 * </p>
	 * 
	 * @param version
	 */
	public AbstractVersionedDomainObject(final ID id, final Integer version) {
		this(id);
		this.version = version;
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * This <tt>Domain Object</tt>'s <code>version</code>, to be used in
	 * <tt>Optimistic Concurrency Control</tt>.
	 */
	@Version
	@Column(name = "META_VERSION", nullable = true, unique = false, length = 8)
	private Integer version;

	/**
	 * @see com.acme.orderplacement.domain.support.VersionedDomainObject#getVersion()
	 */
	public Integer getVersion() {

		return this.version;
	}

	/**
	 * @see com.acme.orderplacement.domain.support.VersionedDomainObject#setVersion(java.lang.Integer)
	 */
	public void setVersion(final Integer newVersion) {

		this.version = newVersion;
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
				+ ((this.version == null) ? 0 : this.version.hashCode());
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
		final AbstractVersionedDomainObject<?> other = (AbstractVersionedDomainObject<?>) obj;
		if (this.version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!this.version.equals(other.version)) {
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
				"version", this.version).toString();
	}

}
