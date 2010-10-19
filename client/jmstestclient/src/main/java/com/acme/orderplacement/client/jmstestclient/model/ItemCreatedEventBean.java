/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient.model;

/**
 * <p>
 * TODO: Insert short summary for ItemCreatedEventBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemCreatedEventBean {

	public static final RandomItemCreatedEventBeanBuilder BUILDER = new RandomItemCreatedEventBeanBuilder();

	private final HeadersBean headers;

	private final ItemBean createdItem;

	public ItemCreatedEventBean(final HeadersBean headers,
			final ItemBean createdItem) {
		this.headers = headers;
		this.createdItem = createdItem;
	}

	public final HeadersBean getHeaders() {
		return this.headers;
	}

	public final ItemBean getCreatedItem() {
		return this.createdItem;
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
				+ ((this.createdItem == null) ? 0 : this.createdItem.hashCode());
		result = prime * result
				+ ((this.headers == null) ? 0 : this.headers.hashCode());
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
		final ItemCreatedEventBean other = (ItemCreatedEventBean) obj;
		if (this.createdItem == null) {
			if (other.createdItem != null) {
				return false;
			}
		} else if (!this.createdItem.equals(other.createdItem)) {
			return false;
		}
		if (this.headers == null) {
			if (other.headers != null) {
				return false;
			}
		} else if (!this.headers.equals(other.headers)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemCreatedEventBean [createdItem=" + this.createdItem
				+ ", headers=" + this.headers + "]";
	}
}
