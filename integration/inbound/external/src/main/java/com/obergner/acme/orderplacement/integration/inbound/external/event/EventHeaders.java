/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * <p>
 * TODO: Insert short summary for MetaData
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EventHeaders implements Serializable,
		Iterable<EventHeader<? extends Serializable>> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Set<EventHeader<? extends Serializable>> metaData;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	EventHeaders(final Set<EventHeader<? extends Serializable>> metaData) {
		this.metaData = metaData != null ? ImmutableSet.copyOf(metaData)
				: Collections.<EventHeader<? extends Serializable>> emptySet();
	}

	// -------------------------------------------------------------------------
	// java.lang.Iterable
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<EventHeader<? extends Serializable>> iterator() {
		return this.metaData.iterator();
	}

}
