/**
 * 
 */
package com.acme.orderplacement.framework.wslog.internal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

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
@Table(schema = "LOG", name = "WEBSERVICE_RESPONSE")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "LOG.ID_SEQ_WEBSERVICE_RESPONSE")
public class WebserviceResponse implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 2004480924089566734L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	@Column(name = "ID", nullable = false)
	private Long id;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENT_ON", nullable = false)
	private Date sentOn;

	@NotNull
	@Lob
	@Column(name = "CONTENT", nullable = false)
	private String content;

	// -------------------------------------------------------------------------
	// Relationships
	// -------------------------------------------------------------------------

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_WEBSERVICE_REQUEST")
	@ForeignKey(name = "FK_WSRESPONSE_WSREQUEST")
	private WebserviceRequest request;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public WebserviceResponse() {
		// Intentionally left blank
	}

	/**
	 * @param sentOn
	 * @param content
	 * @param request
	 * @throws IllegalArgumentException
	 */
	public WebserviceResponse(final Date sentOn, final String content,
			final WebserviceRequest request) throws IllegalArgumentException {
		Validate.notNull(sentOn, "sentOn");
		Validate.notEmpty(content, "content");
		Validate.notNull(request, "request");
		this.sentOn = sentOn;
		this.content = content;
		this.request = request;
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
	 * @return the sentOn
	 */
	public final Date getSentOn() {
		return this.sentOn;
	}

	/**
	 * @param sentOn
	 *            the sentOn to set
	 */
	public final void setSentOn(final Date sentOn) {
		this.sentOn = sentOn;
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
	 * @return the request
	 */
	public final WebserviceRequest getRequest() {
		return this.request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public final void setRequest(final WebserviceRequest request) {
		this.request = request;
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
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.request == null) ? 0 : this.request.hashCode());
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
		final WebserviceResponse other = (WebserviceResponse) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.request == null) {
			if (other.request != null) {
				return false;
			}
		} else if (!this.request.equals(other.request)) {
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
		return "WebserviceResponse [content=" + this.content + ", id="
				+ this.id + ", request=" + this.request + ", sentOn="
				+ this.sentOn + "]";
	}

}
