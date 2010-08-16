/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageType
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "LOG", name = "JMS_MESSAGE_TYPE")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "LOG.ID_SEQ_JMS_MESSAGE_TYPE")
@NamedQueries( { @NamedQuery(name = JmsMessageType.Queries.BY_NAME, query = "from com.acme.orderplacement.jee.framework.jmslog.internal.domain.JmsMessageType jmt where jmt.name = :name") })
public class JmsMessageType implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>JmsMessageType</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_NAME = "log.jms.messageType.byName";
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 3195727247538329650L;

	public static final JmsMessageType UNKNOWN;

	static {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		UNKNOWN = new JmsMessageType(Integer.valueOf(1), "UNKNOWN", true,
				false, cal.getTime());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	@Column(name = "ID", nullable = false)
	private Integer id;

	@NotNull
	@Size(min = 10, max = 200)
	@Basic
	@Column(name = "NAME", nullable = false, length = 200)
	private String name;

	@Basic
	@Column(name = "LOGGED", nullable = false)
	private boolean logged;

	@Basic
	@Column(name = "IDEMPOTENT", nullable = false)
	private boolean idempotent;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "SINCE", nullable = false)
	private Date since;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public JmsMessageType() {
		// Intentionally left blank
	}

	public JmsMessageType(final Integer id, final String name,
			final boolean logged, final boolean idempotent, final Date since) {
		this.id = id;
		this.name = name;
		this.logged = logged;
		this.idempotent = idempotent;
		this.since = since;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the id
	 */
	public final Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the logged
	 */
	public final boolean isLogged() {
		return this.logged;
	}

	/**
	 * @param logged
	 *            the logged to set
	 */
	public final void setLogged(final boolean logged) {
		this.logged = logged;
	}

	/**
	 * @return the idempotent
	 */
	public final boolean isIdempotent() {
		return this.idempotent;
	}

	/**
	 * @param idempotent
	 *            the idempotent to set
	 */
	public final void setIdempotent(final boolean idempotent) {
		this.idempotent = idempotent;
	}

	/**
	 * @return the since
	 */
	public final Date getSince() {
		return this.since;
	}

	/**
	 * @param since
	 *            the since to set
	 */
	public final void setSince(final Date since) {
		this.since = since;
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
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + (this.idempotent ? 1231 : 1237);
		result = prime * result + (this.logged ? 1231 : 1237);
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result
				+ ((this.since == null) ? 0 : this.since.hashCode());
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
		final JmsMessageType other = (JmsMessageType) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.idempotent != other.idempotent) {
			return false;
		}
		if (this.logged != other.logged) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.since == null) {
			if (other.since != null) {
				return false;
			}
		} else if (!this.since.equals(other.since)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JmsMessageType [id=" + this.id + ", idempotent="
				+ this.idempotent + ", logged=" + this.logged + ", name="
				+ this.name + ", since=" + this.since + "]";
	}
}
