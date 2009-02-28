/**
 * 
 */
package com.acme.orderplacement.domain.support.meta;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * A class to be used as a <a href="http://www.hibernate.org">Hibernate</a>
 * <code>Component</code> holding meta data about a persistent entity.
 * </p>
 * <p>
 * This class defines the following fields:<br/>
 * <ul>
 * <li> <code>createdBy</code> Who created this entity?</li>
 * <li> <code>createdOn</code> When was this entity created?</li>
 * <li> <code>lastUpdatedBy</code> Who made the last change to this entity's
 * persistent state?</li>
 * <li> <code>lastUpdatedOn</code> When was this entity last changed?</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 *         TODO: Guard against setting null values, dates in the past etc.
 */
@Embeddable
public class AuditInfo implements Serializable {

	/**
	 * Class version
	 */
	private static final long serialVersionUID = -4945036678422489768L;

	/**
	 * <p>
	 * {@link Comparator<T> <code>Comparator<T></code>} for sorting
	 * {@link AuditInfo <code>AuditInfo</code>} instances ascending by their
	 * {@link AuditInfo#getCreatedOn() <code>createdOn</code>} property.
	 * </p>
	 */
	public static final Comparator<AuditInfo> SORT_BY_CREATE_DATE_ASC = new Comparator<AuditInfo>() {

		public int compare(final AuditInfo o1, final AuditInfo o2) {
			if ((o1 == null) || (o1.createdOn == null)) {
				return -1;
			}
			if ((o2 == null) || (o2.createdOn == null)) {
				return +1;
			}
			return o1.createdOn.compareTo(o2.createdOn);
		}

	};

	/**
	 * <p>
	 * {@link Comparator<T> <code>Comparator<T></code>} for sorting
	 * {@link AuditInfo <code>AuditInfo</code>} instances ascending by their
	 * {@link AuditInfo#getLastUpdatedOn()() <code>lastUpdatedOn</code>}
	 * property.
	 * </p>
	 */
	public static final Comparator<AuditInfo> SORT_BY_LAST_UPDATE_DATE_ASC = new Comparator<AuditInfo>() {

		public int compare(final AuditInfo o1, final AuditInfo o2) {
			if ((o1 == null) || (o1.lastUpdatedOn == null)) {
				return -1;
			}
			if ((o2 == null) || (o2.lastUpdatedOn == null)) {
				return +1;
			}
			return o1.lastUpdatedOn.compareTo(o2.lastUpdatedOn);
		}

	};

	/**
	 * Who created this entity?
	 */
	@Basic
	@Column(name = "AUDIT_CREATED_BY", unique = false, nullable = false, length = 15)
	private String createdBy;

	/**
	 * When was this entity created?
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_CREATED_ON", unique = false, nullable = false)
	private Date createdOn;

	/**
	 * Who made the last change to this entity's persistent state?
	 */
	@Basic
	@Column(name = "AUDIT_LAST_UPDATED_BY", unique = false, nullable = false, length = 15)
	private String lastUpdatedBy;

	/**
	 * When was this entity last changed?
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_LAST_UPDATED_ON", unique = false, nullable = false)
	private Date lastUpdatedOn;

	/**
	 * 
	 */
	public AuditInfo() {
		// Intentionally left blank
	}

	/**
	 * @param createdBy
	 * @param createdOn
	 * @param lastUpdatedBy
	 * @param lastUpdatedOn
	 */
	public AuditInfo(final String createdBy, final Date createdOn,
			final String lastUpdatedBy, final Date lastUpdatedOn) {
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * Look up and return the <code>createdBy</code>.
	 * 
	 * @return the createdBy
	 * @uml.property name="createdBy"
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 * Set the <code>createdBy</code> to the supplied <code>createdBy</code>.
	 * 
	 * @param createdBy
	 *            the createdBy to set
	 * @uml.property name="createdBy"
	 */
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Look up and return the <code>createdOn</code>.
	 * 
	 * @return the createdOn
	 * @uml.property name="createdOn"
	 */
	public Date getCreatedOn() {
		return this.createdOn;
	}

	/**
	 * Set the <code>createdOn</code> to the supplied <code>createdOn</code>.
	 * 
	 * @param createdOn
	 *            the createdOn to set
	 * @uml.property name="createdOn"
	 */
	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Look up and return the <code>lastUpdatedBy</code>.
	 * 
	 * @return the lastUpdatedBy
	 * @uml.property name="lastUpdatedBy"
	 */
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	/**
	 * Set the <code>lastUpdatedBy</code> to the supplied
	 * <code>lastUpdatedBy</code>.
	 * 
	 * @param lastUpdatedBy
	 *            the lastUpdatedBy to set
	 * @uml.property name="lastUpdatedBy"
	 */
	public void setLastUpdatedBy(final String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * Look up and return the <code>lastUpdatedOn</code>.
	 * 
	 * @return the lastUpdatedOn
	 * @uml.property name="lastUpdatedOn"
	 */
	public Date getLastUpdatedOn() {
		return this.lastUpdatedOn;
	}

	/**
	 * Set the <code>lastUpdatedOn</code> to the supplied
	 * <code>lastUpdatedOn</code>.
	 * 
	 * @param lastUpdatedOn
	 *            the lastUpdatedOn to set
	 * @uml.property name="lastUpdatedOn"
	 */
	public void setLastUpdatedOn(final Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * @return
	 */
	public boolean isNew() {
		return (this.createdBy == null) && (this.createdOn == null)
				&& (this.lastUpdatedBy == null) && (this.lastUpdatedOn == null);
	}

	/**
	 * @return
	 */
	public boolean isComplete() {
		return (this.createdBy != null) && (this.createdOn != null)
				&& (this.lastUpdatedBy != null) && (this.lastUpdatedOn != null);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((this.createdBy == null) ? 0 : this.createdBy.hashCode());
		result = PRIME * result
				+ ((this.createdOn == null) ? 0 : this.createdOn.hashCode());
		result = PRIME
				* result
				+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy
						.hashCode());
		result = PRIME
				* result
				+ ((this.lastUpdatedOn == null) ? 0 : this.lastUpdatedOn
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
		final AuditInfo other = (AuditInfo) obj;
		if (this.createdBy == null) {
			if (other.createdBy != null) {
				return false;
			}
		} else if (!this.createdBy.equals(other.createdBy)) {
			return false;
		}
		if (this.createdOn == null) {
			if (other.createdOn != null) {
				return false;
			}
		} else if (!this.createdOn.equals(other.createdOn)) {
			return false;
		}
		if (this.lastUpdatedBy == null) {
			if (other.lastUpdatedBy != null) {
				return false;
			}
		} else if (!this.lastUpdatedBy.equals(other.lastUpdatedBy)) {
			return false;
		}
		if (this.lastUpdatedOn == null) {
			if (other.lastUpdatedOn != null) {
				return false;
			}
		} else if (!this.lastUpdatedOn.equals(other.lastUpdatedOn)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("createdBy", this.createdBy)
				.append("createdOn", this.createdOn).append("lastUpdatedBy",
						this.lastUpdatedBy).append("lastUpdatedOn",
						this.lastUpdatedOn).toString();
	}

}
