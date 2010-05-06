/**
 * 
 */
package com.acme.orderplacement.log.ws.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p>
 * TODO: Insert short summary for WebserviceMethod
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "ITEM", name = "ITEM")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_ITEM")
@NamedQueries( { @NamedQuery(name = WebserviceMethod.Queries.BY_NAME, query = "from com.acme.orderplacement.log.ws.domain.WebserviceMethod wsm where wsm.name = :name") })
public class WebserviceMethod implements Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>WebserviceMethod</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_NAME = "log.ws.webserviceMethod.byName";
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private Integer id;

	private String name;

	private boolean logged;

	private boolean idempotent;

	private Date since;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	public WebserviceMethod() {
		// Intentionally left blank
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
		final WebserviceMethod other = (WebserviceMethod) obj;
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
		return "WebserviceMethod [id=" + this.id + ", idempotent="
				+ this.idempotent + ", logged=" + this.logged + ", name="
				+ this.name + ", since=" + this.since + "]";
	}
}
