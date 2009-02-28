/**
 * 
 */
package com.acme.orderplacement.domain.support.meta;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.support.AbstractVersionedDomainObject;
import com.acme.orderplacement.domain.support.meta.jpa.AuditInfoManagingEntityListener;

/**
 * <p>
 * Convenience base class for {@link AbstractAuditableDomainObject
 * <code>AbstractAuditableDomainObject</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MappedSuperclass
@EntityListeners( { AuditInfoManagingEntityListener.class })
public abstract class AbstractAuditableDomainObject<ID extends Serializable>
		extends AbstractVersionedDomainObject<ID> implements
		AuditableDomainObject<ID>, Serializable {

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -2208517110986403189L;

	/**
	 * <p>
	 * Empty no-args default constructor.
	 * </p>
	 */
	public AbstractAuditableDomainObject() {
		super();
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>ID</code>.
	 * </p>
	 * 
	 * @param id
	 */
	public AbstractAuditableDomainObject(final ID id) {
		super(id);
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>version</code>.
	 * </p>
	 * 
	 * @param version
	 */
	public AbstractAuditableDomainObject(final Integer version) {
		super(version);
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>auditInfo</code>.
	 * </p>
	 * 
	 * @param auditInfo
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>auditInfo</code> is <code>null</code>
	 */
	public AbstractAuditableDomainObject(final AuditInfo auditInfo)
			throws IllegalArgumentException {
		super();
		setAuditInfo(auditInfo);
	}

	/**
	 * <p>
	 * Constructor setting both this <tt>Domain Object</tt>'s <code>ID</code>
	 * and its <code>version</code>.
	 * </p>
	 * 
	 * @param id
	 * @param version
	 */
	public AbstractAuditableDomainObject(final ID id, final Integer version) {
		super(id, version);
	}

	/**
	 * <p>
	 * Constructor setting this <tt>Domain Object</tt>'s <code>ID</code> as well
	 * as its <code>version</code> and <code>auditInfo</code>.
	 * </p>
	 * 
	 * @param id
	 * @param version
	 * @param auditInfo
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>auditInfo</code> is <code>null</code>
	 */
	public AbstractAuditableDomainObject(final ID id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version);
		setAuditInfo(auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * This <tt>Domain Object</tt>'s {@link AuditInfo <code>AuditInfo</code>}
	 * instance.
	 */
	@Embedded
	private AuditInfo auditInfo;

	/**
	 * @see com.acme.orderplacement.domain.support.meta.AuditableDomainObject#getAuditInfo()
	 */
	public AuditInfo getAuditInfo() {
		if (this.auditInfo == null) {
			this.auditInfo = new AuditInfo();
		}

		return this.auditInfo;
	}

	/**
	 * @see com.acme.orderplacement.domain.support.meta.AuditableDomainObject#setAuditInfo(com.acme.orderplacement.domain.support.meta.AuditInfo)
	 */
	public void setAuditInfo(final AuditInfo newAuditInfo)
			throws IllegalArgumentException {
		Validate.notNull(newAuditInfo, "newAuditInfo");
		this.auditInfo = newAuditInfo;
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
				+ ((this.auditInfo == null) ? 0 : this.auditInfo.hashCode());
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
		final AbstractAuditableDomainObject<?> other = (AbstractAuditableDomainObject<?>) obj;
		if (this.auditInfo == null) {
			if (other.auditInfo != null) {
				return false;
			}
		} else if (!this.auditInfo.equals(other.auditInfo)) {
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
				"auditInfo", this.auditInfo).toString();
	}

}
