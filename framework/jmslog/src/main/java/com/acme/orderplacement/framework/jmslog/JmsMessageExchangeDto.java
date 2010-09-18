/**
 * 
 */
package com.acme.orderplacement.framework.jmslog;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchangeDto
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JmsMessageExchangeDto implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = -3441865128181678535L;

	private final String messageType;

	private final String guid;

	private final Date receivedOn;

	private final String content;

	private final Map<String, Object> headers;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public JmsMessageExchangeDto(final String messageType, final String guid,
			final Date receivedOn, final String content,
			final Map<String, Object> headers) throws IllegalArgumentException {
		Validate.notEmpty(messageType, "messageType");
		Validate.notEmpty(guid, "guid");
		Validate.notNull(receivedOn, "receivedOn");
		Validate.notEmpty(content, "content");
		Validate.notNull(headers, "headers");
		this.messageType = messageType;
		this.guid = guid;
		this.receivedOn = receivedOn;
		this.content = content;
		this.headers = Collections.unmodifiableMap(headers);
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the messageType
	 */
	public final String getMessageType() {
		return this.messageType;
	}

	/**
	 * @return the guid
	 */
	public final String getGuid() {
		return this.guid;
	}

	/**
	 * @return the receivedOn
	 */
	public final Date getReceivedOn() {
		return this.receivedOn;
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return this.content;
	}

	/**
	 * @return the headers
	 */
	public final Map<String, Object> getHeaders() {
		return this.headers;
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.content == null) ? 0 : this.content.hashCode());
		result = prime * result
				+ ((this.guid == null) ? 0 : this.guid.hashCode());
		result = prime * result
				+ ((this.headers == null) ? 0 : this.headers.hashCode());
		result = prime
				* result
				+ ((this.messageType == null) ? 0 : this.messageType.hashCode());
		result = prime * result
				+ ((this.receivedOn == null) ? 0 : this.receivedOn.hashCode());
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
		final JmsMessageExchangeDto other = (JmsMessageExchangeDto) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.guid == null) {
			if (other.guid != null) {
				return false;
			}
		} else if (!this.guid.equals(other.guid)) {
			return false;
		}
		if (this.headers == null) {
			if (other.headers != null) {
				return false;
			}
		} else if (!this.headers.equals(other.headers)) {
			return false;
		}
		if (this.messageType == null) {
			if (other.messageType != null) {
				return false;
			}
		} else if (!this.messageType.equals(other.messageType)) {
			return false;
		}
		if (this.receivedOn == null) {
			if (other.receivedOn != null) {
				return false;
			}
		} else if (!this.receivedOn.equals(other.receivedOn)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JmsMessageExchangeDto [content=" + this.content + ", guid="
				+ this.guid + ", headers=" + this.headers + ", messageType="
				+ this.messageType + ", receivedOn=" + this.receivedOn + "]";
	}
}
