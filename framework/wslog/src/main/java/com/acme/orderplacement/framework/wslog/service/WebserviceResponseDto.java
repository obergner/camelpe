/**
 * 
 */
package com.acme.orderplacement.framework.wslog.service;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for WebserviceResponseDto
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WebserviceResponseDto implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 8978785186472160606L;

	private final Long referencedRequestId;

	private final Date sentOn;

	private final String content;

	private final boolean failed;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param referencedRequestId
	 * @param sentOn
	 * @param content
	 * @param failed
	 * @throws IllegalArgumentException
	 */
	public WebserviceResponseDto(final Long referencedRequestId,
			final Date sentOn, final String content, final boolean failed)
			throws IllegalArgumentException {
		Validate.notNull(referencedRequestId, "referencedRequestId");
		Validate.notNull(sentOn, "sentOn");
		Validate.notEmpty(content, "content");
		this.referencedRequestId = referencedRequestId;
		this.sentOn = sentOn;
		this.content = content;
		this.failed = failed;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the referencedRequestId
	 */
	public final Long getReferencedRequestId() {
		return this.referencedRequestId;
	}

	/**
	 * @return the sentOn
	 */
	public final Date getSentOn() {
		return this.sentOn;
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return this.content;
	}

	/**
	 * @return the failed
	 */
	public final boolean isFailed() {
		return this.failed;
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
		result = prime * result + (this.failed ? 1231 : 1237);
		result = prime
				* result
				+ ((this.referencedRequestId == null) ? 0
						: this.referencedRequestId.hashCode());
		result = prime * result
				+ ((this.sentOn == null) ? 0 : this.sentOn.hashCode());
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
		final WebserviceResponseDto other = (WebserviceResponseDto) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.failed != other.failed) {
			return false;
		}
		if (this.referencedRequestId == null) {
			if (other.referencedRequestId != null) {
				return false;
			}
		} else if (!this.referencedRequestId.equals(other.referencedRequestId)) {
			return false;
		}
		if (this.sentOn == null) {
			if (other.sentOn != null) {
				return false;
			}
		} else if (!this.sentOn.equals(other.sentOn)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebserviceResponseDto [failed=" + this.failed
				+ ", referencedRequestId=" + this.referencedRequestId
				+ ", sentOn=" + this.sentOn + "]";
	}

}
