/**
 * 
 */
package com.acme.orderplacement.framework.domain.meta;

import java.io.Serializable;

import com.acme.orderplacement.framework.domain.VersionedDomainObject;

/**
 * <p>
 * Interface to be implemented by <tt>Domain Object</tt>s that know about
 * associated {@link AuditInfo <code>AuditInfo</code>}, i.e. when they have been
 * last modified, by whom etc.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface AuditableDomainObject<ID extends Serializable> extends
		VersionedDomainObject<ID> {

	/**
	 * <p>
	 * Return this <tt>Domain Object</tt>'s {@link AuditInfo
	 * <code>MetaData</code>}. If none is associated with this
	 * <tt>Domain Object</tt> at the time of calling, a new empty
	 * {@link AuditInfo <code>AuditInfo</code>} instance is created, associated
	 * with this <tt>Domain Object</tt> and returned. Thus, this method will
	 * <strong>never</strong> return <code>null</code>.
	 * </p>
	 * 
	 * @return This <tt>Domain Object</tt>'s {@link AuditInfo
	 *         <code>MetaData</code>}
	 */
	AuditInfo getAuditInfo();

	/**
	 * <p>
	 * Set this <tt>Domain Object</tt>'s {@link AuditInfo <code>MetaData</code>}
	 * to <code>newMetaData</code>.
	 * </p>
	 * 
	 * @param newAuditInfo
	 *            This <tt>Domain Object</tt>'s new {@link AuditInfo
	 *            <code>MetaData</code>}
	 * @throws IllegalArgumentException
	 *             If <code>newMetaData</code> is <code>null</code>
	 */
	void setAuditInfo(AuditInfo newAuditInfo) throws IllegalArgumentException;
}
