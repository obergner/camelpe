/**
 * 
 */
package com.acme.orderplacement.log.ws.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.ForeignKey;

/**
 * <p>
 * TODO: Insert short summary for WebserviceRequest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "LOG", name = "WEBSERVICE_REQUEST")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "LOG.ID_SEQ_WEBSERVICE_REQUEST")
public class WebserviceRequest implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = -2028889462301348840L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	@Column(name = "ID", nullable = false)
	private Long id;

	@NotNull
	@Size(min = 7, max = 15)
	@Pattern(regexp = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")
	@Basic
	@Column(name = "SOURCE_IP", nullable = false, length = 15)
	private String sourceIp;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVED_ON", nullable = false)
	private Date receivedOn;

	@NotNull
	@Lob
	@Column(name = "CONTENT", nullable = false)
	private String content;

	// -------------------------------------------------------------------------
	// Relationships
	// -------------------------------------------------------------------------

	@ManyToOne
	@JoinColumn(name = "ID_WEBSERVICE_OPERATION", unique = false, nullable = false)
	@ForeignKey(name = "FK_WSREQUEST_WSOPERATION")
	private WebserviceOperation webserviceOperation;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_WEBSERVICE_REQUEST", nullable = false)
	@ForeignKey(name = "FK_WSHEADER_WSREQUEST")
	private Set<WebserviceHeader> headers;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	public WebserviceRequest() {
		// Intentionally left blank
	}

	public WebserviceRequest(final String sourceIp, final Date receivedOn,
			final String content,
			final WebserviceOperation webserviceOperation,
			final Map<String, String> headers) throws IllegalArgumentException {
		Validate.notEmpty(sourceIp, "sourceIp");
		Validate.notNull(receivedOn, "receivedOn");
		Validate.notEmpty(content, "content");
		Validate.notNull(webserviceOperation, "webserviceOperation");
		this.sourceIp = sourceIp;
		this.receivedOn = receivedOn;
		this.content = content;
		this.webserviceOperation = webserviceOperation;
		setHeaders(headers);
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the id
	 */
	public final Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the sourceIp
	 */
	public final String getSourceIp() {
		return this.sourceIp;
	}

	/**
	 * @param sourceIp
	 *            the sourceIp to set
	 */
	public final void setSourceIp(final String sourceIp) {
		this.sourceIp = sourceIp;
	}

	/**
	 * @return the receivedOn
	 */
	public final Date getReceivedOn() {
		return this.receivedOn;
	}

	/**
	 * @param receivedOn
	 *            the receivedOn to set
	 */
	public final void setReceivedOn(final Date receivedOn) {
		this.receivedOn = receivedOn;
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public final void setContent(final String content) {
		this.content = content;
	}

	/**
	 * @return
	 */
	public final WebserviceOperation getWebserviceOperation() {
		return this.webserviceOperation;
	}

	/**
	 * @param webserviceOperation
	 */
	public final void setWebserviceOperation(
			final WebserviceOperation webserviceOperation) {
		this.webserviceOperation = webserviceOperation;
	}

	/**
	 * @return the headers
	 */
	public final Set<WebserviceHeader> getHeaders() {
		return this.headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public final void setHeaders(final Set<WebserviceHeader> headers) {
		this.headers = headers;
	}

	/**
	 * @param headers
	 */
	public final void setHeaders(final Map<String, String> headers) {
		this.headers = new HashSet<WebserviceHeader>();
		for (final Map.Entry<String, String> header : headers.entrySet()) {
			this.headers.add(new WebserviceHeader(header.getKey(), header
					.getValue()));
		}
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
				+ ((this.headers == null) ? 0 : this.headers.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.receivedOn == null) ? 0 : this.receivedOn.hashCode());
		result = prime * result
				+ ((this.sourceIp == null) ? 0 : this.sourceIp.hashCode());
		result = prime
				* result
				+ ((this.webserviceOperation == null) ? 0
						: this.webserviceOperation.hashCode());
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
		final WebserviceRequest other = (WebserviceRequest) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.headers == null) {
			if (other.headers != null) {
				return false;
			}
		} else if (!this.headers.equals(other.headers)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.receivedOn == null) {
			if (other.receivedOn != null) {
				return false;
			}
		} else if (!this.receivedOn.equals(other.receivedOn)) {
			return false;
		}
		if (this.sourceIp == null) {
			if (other.sourceIp != null) {
				return false;
			}
		} else if (!this.sourceIp.equals(other.sourceIp)) {
			return false;
		}
		if (this.webserviceOperation == null) {
			if (other.webserviceOperation != null) {
				return false;
			}
		} else if (!this.webserviceOperation.equals(other.webserviceOperation)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebserviceRequest [content=" + this.content + ", headers="
				+ this.headers + ", id=" + this.id + ", receivedOn="
				+ this.receivedOn + ", sourceIp=" + this.sourceIp
				+ ", webserviceOperation=" + this.webserviceOperation + "]";
	}

}
