/**
 * 
 */
package com.acme.orderplacement.domain.support;

import java.io.Serializable;

/**
 * <p>
 * Interface to be implemented by <tt>domain objects</tt> that exhibit a
 * {@link Serializable <code>Serializable</code>} property uniquely identifying
 * instances.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface IdentifiableDomainObject<ID extends Serializable> extends
		Serializable {

	/**
	 * <p>
	 * {@link Comparator<T> <code>Comparator<T></code>} for sorting
	 * {@link IdentifiableDomainObject<T>
	 * <code>IdentifiableDomainObject<T></code>} instances by their
	 * <code>ID</code> property.
	 * </p>
	 */
	// Comparator<IdentifiableDomainObject<? extends Comparable<Serializable>>>
	// SORT_BY_ID = new Comparator<IdentifiableDomainObject<? extends
	// Comparable<Serializable>>>() {
	//
	// /**
	// * @param o1
	// * @param o2
	// * @return
	// */
	// public int compare(
	// IdentifiableDomainObject<? extends Comparable<Serializable>> o1,
	// IdentifiableDomainObject<? extends Comparable<Serializable>> o2) {
	// if ((o1 == null) || (o1.getId() == null)) {
	// return -1;
	// }
	// if ((o2 == null) || (o2.getId() == null)) {
	// return +1;
	// }
	// return o1.getId().compareTo(o2.getId());
	// }
	//
	// };
	/**
	 * <p>
	 * Return this <tt>Domain Object</tt>'s unique <tt>identifier</tt>.
	 * </p>
	 * 
	 * @return This <tt>Domain Object</tt>'s unique <tt>identifier</tt>
	 */
	ID getId();

	/**
	 * <p>
	 * Set this <tt>Domain Object</tt>'s unique <tt>identifier</tt> to
	 * <code>newId</code>.
	 * </p>
	 * 
	 * @param newId
	 *            This <tt>Domain Object</tt> new <code>ID</code>
	 */
	void setId(ID newId);
}
