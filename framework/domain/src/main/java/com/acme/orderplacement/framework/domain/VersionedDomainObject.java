/**
 * 
 */
package com.acme.orderplacement.framework.domain;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>
 * Interface to be implemented by {@link IdentifiableDomainObject
 * <code>IdentifiableDomainObject</code>}s exhibiting a property that tells
 * clients which
 * <tt>version</t> they are dealing with. This is especially useful when dealing with <i>Optimistic Locking</tt>
 * when persisting <tt>Domain Object</tt>s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface VersionedDomainObject<ID extends Serializable> extends
		IdentifiableDomainObject<ID> {

	/**
	 * <p>
	 * {@link Comparator<T> <code>Comparator<T></code>} for sorting
	 * {@link VersionedDomainObject<T> <code>VersionedDomainObject<T></code>}
	 * instances by their <code>version</code> property.
	 * </p>
	 */
	Comparator<VersionedDomainObject<? extends Serializable>> SORT_BY_VERSION = new Comparator<VersionedDomainObject<? extends Serializable>>() {

		/**
		 * @param o1
		 * @param o2
		 * @return
		 */
		public int compare(
				final VersionedDomainObject<? extends Serializable> o1,
				final VersionedDomainObject<? extends Serializable> o2) {
			if ((o1 == null) || (o1.getVersion() == null)) {
				return -1;
			}
			if ((o2 == null) || (o2.getVersion() == null)) {
				return +1;
			}
			return o1.getVersion().compareTo(o2.getVersion());
		}

	};

	/**
	 * <p>
	 * Return this <tt>Domain Object</tt>'s <i>version</i>.
	 * </p>
	 * 
	 * @return This <tt>Domain Object</tt>'s <i>version</i>
	 */
	Integer getVersion();

	/**
	 * <p>
	 * Set this <tt>Domain Object</tt>'s <i>version</i> to the supplied
	 * <code>newVersion</code>.
	 * </p>
	 * 
	 * @param newVersion
	 *            This <tt>Domain Object</tt>'s new <i>version</i>
	 */
	void setVersion(Integer newVersion);

}
