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
class EventHeaders implements Serializable, Iterable<EventHeader> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	private final Set<EventHeader> headers;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	EventHeaders(final Set<EventHeader> headers) {
		this.headers = headers != null ? ImmutableSet.copyOf(headers)
				: Collections.<EventHeader> emptySet();
	}

	// -------------------------------------------------------------------------
	// java.lang.Iterable
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<EventHeader> iterator() {
		return this.headers.iterator();
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	EventHeader specifiedBy(final EventHeaderSpec specification) {
		for (final EventHeader candidate : this) {
			if (candidate.isSpecifiedBy(specification)) {
				return candidate;
			}
		}

		return null;
	}
}
