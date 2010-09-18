/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchange
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "LOG", name = "JMS_MESSAGE")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "LOG.ID_SEQ_JMS_MESSAGE")
@NamedQueries( { @NamedQuery(name = JmsMessageExchange.Queries.BY_GUID, query = "from com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageExchange jms where jms.guid = :guid") })
public class JmsMessageExchange implements Serializable {

	// -------------------------------------------------------------------------
	// Processing state
	// -------------------------------------------------------------------------

	public enum ProcessingState {
		IN_PROGRESS, SUCCESSFUL, FAILED;
	}

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>WebserviceOperation</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_GUID = "log.jms.jmsMessageExchange.byGuid";
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 2389437473958815032L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	@Column(name = "ID", nullable = false)
	private Long id;

	@NotNull
	@Size(min = 7, max = 50)
	@Basic
	@NaturalId(mutable = false)
	@Column(name = "GUID", nullable = false, length = 50, unique = true, updatable = false)
	private String guid;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PROCESSING_STATE", nullable = false, length = 15)
	private JmsMessageExchange.ProcessingState processingState;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVED_ON", nullable = false)
	private Date receivedOn;

	@NotNull
	@Lob
	@Column(name = "CONTENT", nullable = false)
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMPLETED_ON", nullable = true)
	private Date completedOn;

	@Embedded
	private JmsMessageExchangeError error = JmsMessageExchangeError.NO_ERROR;

	// -------------------------------------------------------------------------
	// Relationships
	// -------------------------------------------------------------------------

	@ManyToOne
	@JoinColumn(name = "ID_JMSMESSAGETYPE", unique = false, nullable = false)
	@ForeignKey(name = "FK_JMSMESSAGE_JMSMESSAGETYPE")
	private JmsMessageType messageType;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_JMS_MESSAGE", nullable = false)
	@ForeignKey(name = "FK_JMSHEADER_JMSMESSAGE")
	private Set<JmsMessageHeader> headers;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	public JmsMessageExchange() {
		// Intentionally left blank
	}

	public JmsMessageExchange(final String guid, final Date receivedOn,
			final String content, final JmsMessageType messageType,
			final Map<String, String> headers) throws IllegalArgumentException {
		Validate.notEmpty(guid, "guid");
		Validate.notNull(receivedOn, "receivedOn");
		Validate.notEmpty(content, "content");
		this.guid = guid;
		this.receivedOn = receivedOn;
		this.content = content;
		this.messageType = messageType != null ? messageType
				: JmsMessageType.UNKNOWN;
		this.processingState = JmsMessageExchange.ProcessingState.IN_PROGRESS;
		setHeaders(headers);
		setError(JmsMessageExchangeError.NO_ERROR);
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
	 * @return the guid
	 */
	public final String getGuid() {
		return this.guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public final void setGuid(final String guid) {
		this.guid = guid;
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
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(final JmsMessageType messageType) {
		this.messageType = messageType != null ? messageType
				: JmsMessageType.UNKNOWN;
	}

	/**
	 * @return the messageType
	 */
	public JmsMessageType getMessageType() {
		return this.messageType;
	}

	/**
	 * @param processingState
	 *            the processingState to set
	 */
	public void setProcessingState(
			final JmsMessageExchange.ProcessingState processingState) {
		this.processingState = processingState;
	}

	/**
	 * @return the processingState
	 */
	public JmsMessageExchange.ProcessingState getProcessingState() {
		return this.processingState;
	}

	/**
	 * @return the headers
	 */
	public final Set<JmsMessageHeader> getHeaders() {
		return this.headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public final void setHeaders(final Set<JmsMessageHeader> headers) {
		this.headers = headers;
	}

	/**
	 * @param headers
	 */
	public final void setHeaders(final Map<String, String> headers) {
		this.headers = new HashSet<JmsMessageHeader>();
		for (final Map.Entry<String, String> header : headers.entrySet()) {
			this.headers.add(new JmsMessageHeader(header.getKey(), header
					.getValue()));
		}
	}

	/**
	 * @return the completedOn
	 */
	public final Date getCompletedOn() {
		return this.completedOn;
	}

	/**
	 * @param completedOn
	 *            the completedOn to set
	 */
	public final void setCompletedOn(final Date completedOn) {
		this.completedOn = completedOn;
	}

	/**
	 * @return the error
	 */
	public final JmsMessageExchangeError getError() {
		return this.error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public final void setError(final JmsMessageExchangeError error) {
		this.error = error != null ? error : JmsMessageExchangeError.NO_ERROR;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public final void setError(final Exception error) {
		this.error = error != null ? new JmsMessageExchangeError(error)
				: JmsMessageExchangeError.NO_ERROR;
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
		result = prime
				* result
				+ ((this.completedOn == null) ? 0 : this.completedOn.hashCode());
		result = prime * result
				+ ((this.content == null) ? 0 : this.content.hashCode());
		result = prime * result
				+ ((this.error == null) ? 0 : this.error.hashCode());
		result = prime * result
				+ ((this.guid == null) ? 0 : this.guid.hashCode());
		result = prime * result
				+ ((this.headers == null) ? 0 : this.headers.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime
				* result
				+ ((this.messageType == null) ? 0 : this.messageType.hashCode());
		result = prime
				* result
				+ ((this.processingState == null) ? 0 : this.processingState
						.hashCode());
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
		final JmsMessageExchange other = (JmsMessageExchange) obj;
		if (this.completedOn == null) {
			if (other.completedOn != null) {
				return false;
			}
		} else if (!this.completedOn.equals(other.completedOn)) {
			return false;
		}
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.error == null) {
			if (other.error != null) {
				return false;
			}
		} else if (!this.error.equals(other.error)) {
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
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.messageType == null) {
			if (other.messageType != null) {
				return false;
			}
		} else if (!this.messageType.equals(other.messageType)) {
			return false;
		}
		if (this.processingState == null) {
			if (other.processingState != null) {
				return false;
			}
		} else if (!this.processingState.equals(other.processingState)) {
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
		return "JmsMessageExchange [completedOn=" + this.completedOn
				+ ", content=" + this.content + ", error=" + this.error
				+ ", guid=" + this.guid + ", headers=" + this.headers + ", id="
				+ this.id + ", messageType=" + this.messageType
				+ ", processingState=" + this.processingState + ", receivedOn="
				+ this.receivedOn + "]";
	}

}
