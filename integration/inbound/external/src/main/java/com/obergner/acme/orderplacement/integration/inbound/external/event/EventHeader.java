/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external.event;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for MetaDatum
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class EventHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	private final EventHeaderSpec specification;

	private final Serializable value;

	EventHeader(final EventHeaderSpec spec, final Serializable value)
			throws IllegalArgumentException {
		Validate.notNull(spec, "spec");
		Validate.notNull(value, "value");
		this.specification = spec;
		this.value = value;
	}

	/**
	 * @return the specification
	 */
	final boolean isSpecifiedBy(final EventHeaderSpec aType) {
		return this.specification.equals(aType);
	}

	/**
	 * @return the value
	 */
	final Serializable getValue() {
		return this.value;
	}

	final boolean hasDefaultValue() {
		return this.value.equals(this.specification.defaultValue());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.specification == null) ? 0 : this.specification
						.hashCode());
		result = prime * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
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
		final EventHeader other = (EventHeader) obj;
		if (this.specification == null) {
			if (other.specification != null) {
				return false;
			}
		} else if (!this.specification.equals(other.specification)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventHeader [specification=" + this.specification + ", value="
				+ this.value + "]";
	}

}
