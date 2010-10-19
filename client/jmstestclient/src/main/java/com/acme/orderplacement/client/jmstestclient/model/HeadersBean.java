/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient.model;

/**
 * <p>
 * TODO: Insert short summary for HeadersBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class HeadersBean {

	private final String eventId;

	public HeadersBean(final String eventId) {
		this.eventId = eventId;
	}

	public final String getEventId() {
		return this.eventId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.eventId == null) ? 0 : this.eventId.hashCode());
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
		final HeadersBean other = (HeadersBean) obj;
		if (this.eventId == null) {
			if (other.eventId != null) {
				return false;
			}
		} else if (!this.eventId.equals(other.eventId)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HeadersBean [eventId=" + this.eventId + "]";
	}
}
