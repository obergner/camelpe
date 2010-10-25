/**
 * 
 */
package com.acme.orderplacement.framework.wslog;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * TODO: Insert short summary for WebserviceRequestDto
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WebserviceRequestDto implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = -3441865128181678535L;

	private final String contextRoot;

	private final String serviceName;

	private final String operationName;

	private final String sourceIp;

	private final Date receivedOn;

	private final String content;

	private final Map<String, String> headers;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param contextRoot
	 * @param serviceName
	 * @param operationName
	 * @param sourceIp
	 * @param receivedOn
	 * @param content
	 * @param headers
	 * @throws IllegalArgumentException
	 */
	public WebserviceRequestDto(final String contextRoot,
			final String serviceName, final String operationName,
			final String sourceIp, final Date receivedOn, final String content,
			final Map<String, String> headers) throws IllegalArgumentException {
		Validate.notEmpty(contextRoot, "contextRoot");
		Validate.notEmpty(serviceName, "serviceName");
		Validate.notEmpty(operationName, "operationName");
		Validate.notEmpty(sourceIp, "sourceIp");
		Validate.notNull(receivedOn, "receivedOn");
		Validate.notEmpty(content, "content");
		Validate.notNull(headers, "headers");
		this.contextRoot = contextRoot;
		this.serviceName = serviceName;
		this.operationName = operationName;
		this.sourceIp = sourceIp;
		this.receivedOn = receivedOn;
		this.content = content;
		this.headers = Collections.unmodifiableMap(headers);
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the contextRoot
	 */
	public final String getContextRoot() {
		return this.contextRoot;
	}

	/**
	 * @return the serviceName
	 */
	public final String getServiceName() {
		return this.serviceName;
	}

	/**
	 * @return the operationName
	 */
	public final String getOperationName() {
		return this.operationName;
	}

	/**
	 * @return the sourceIp
	 */
	public final String getSourceIp() {
		return this.sourceIp;
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
	public final Map<String, String> getHeaders() {
		return this.headers;
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebserviceRequestDto [content=" + this.content
				+ ", contextRoot=" + this.contextRoot + ", headers="
				+ this.headers + ", operationName=" + this.operationName
				+ ", receivedOn=" + this.receivedOn + ", serviceName="
				+ this.serviceName + ", sourceIp=" + this.sourceIp + "]";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.content == null) ? 0 : this.content.hashCode());
		result = prime
				* result
				+ ((this.contextRoot == null) ? 0 : this.contextRoot.hashCode());
		result = prime * result
				+ ((this.headers == null) ? 0 : this.headers.hashCode());
		result = prime
				* result
				+ ((this.operationName == null) ? 0 : this.operationName
						.hashCode());
		result = prime * result
				+ ((this.receivedOn == null) ? 0 : this.receivedOn.hashCode());
		result = prime
				* result
				+ ((this.serviceName == null) ? 0 : this.serviceName.hashCode());
		result = prime * result
				+ ((this.sourceIp == null) ? 0 : this.sourceIp.hashCode());
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
		final WebserviceRequestDto other = (WebserviceRequestDto) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.contextRoot == null) {
			if (other.contextRoot != null) {
				return false;
			}
		} else if (!this.contextRoot.equals(other.contextRoot)) {
			return false;
		}
		if (this.headers == null) {
			if (other.headers != null) {
				return false;
			}
		} else if (!this.headers.equals(other.headers)) {
			return false;
		}
		if (this.operationName == null) {
			if (other.operationName != null) {
				return false;
			}
		} else if (!this.operationName.equals(other.operationName)) {
			return false;
		}
		if (this.receivedOn == null) {
			if (other.receivedOn != null) {
				return false;
			}
		} else if (!this.receivedOn.equals(other.receivedOn)) {
			return false;
		}
		if (this.serviceName == null) {
			if (other.serviceName != null) {
				return false;
			}
		} else if (!this.serviceName.equals(other.serviceName)) {
			return false;
		}
		if (this.sourceIp == null) {
			if (other.sourceIp != null) {
				return false;
			}
		} else if (!this.sourceIp.equals(other.sourceIp)) {
			return false;
		}
		return true;
	}

}
