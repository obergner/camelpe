/**
 * 
 */
package com.acme.orderplacement.jee.framework.jmslog.internal.domain;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

/**
 * <p>
 * TODO: Insert short summary for JmsMessageExchangeError
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Embeddable
public class JmsMessageExchangeError implements Serializable {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = -1482179625848430273L;

	public static final JmsMessageExchangeError NO_ERROR = new JmsMessageExchangeError();

	@Size(max = 100)
	@Basic
	@Column(name = "ERROR_TYPE", nullable = true, length = 100)
	private final String errorType;

	/*
	 * This will not work with databases that do not support varchar type of up
	 * to 1000 characters length.
	 */
	@Size(max = 1000)
	@Basic
	@Column(name = "ERROR_MESSAGE", nullable = true, length = 1000)
	private final String errorMessage;

	@Lob
	@Column(name = "ERROR_STACKTRACE", nullable = true)
	private final String errorStackTrace;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public JmsMessageExchangeError() {
		this(null);
	}

	public JmsMessageExchangeError(final Exception error) {
		this.errorType = error != null ? error.getClass().getName() : null;
		this.errorMessage = error != null ? error.getMessage() : null;
		this.errorStackTrace = error != null ? convertStackTraceToString(error)
				: null;
	}

	private String convertStackTraceToString(final Exception error) {
		final StringWriter convertedStackTrace = new StringWriter();
		error.printStackTrace(new PrintWriter(convertedStackTrace));

		return convertedStackTrace.toString();
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the errorType
	 */
	public final String getErrorType() {
		return this.errorType;
	}

	/**
	 * @return the errorMessage
	 */
	public final String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @return the errorStackTrace
	 */
	public final String getErrorStackTrace() {
		return this.errorStackTrace;
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
				+ ((this.errorMessage == null) ? 0 : this.errorMessage
						.hashCode());
		result = prime
				* result
				+ ((this.errorStackTrace == null) ? 0 : this.errorStackTrace
						.hashCode());
		result = prime * result
				+ ((this.errorType == null) ? 0 : this.errorType.hashCode());
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
		final JmsMessageExchangeError other = (JmsMessageExchangeError) obj;
		if (this.errorMessage == null) {
			if (other.errorMessage != null) {
				return false;
			}
		} else if (!this.errorMessage.equals(other.errorMessage)) {
			return false;
		}
		if (this.errorStackTrace == null) {
			if (other.errorStackTrace != null) {
				return false;
			}
		} else if (!this.errorStackTrace.equals(other.errorStackTrace)) {
			return false;
		}
		if (this.errorType == null) {
			if (other.errorType != null) {
				return false;
			}
		} else if (!this.errorType.equals(other.errorType)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JmsMessageExchangeError [errorMessage=" + this.errorMessage
				+ ", errorStackTrace=" + this.errorStackTrace + ", errorType="
				+ this.errorType + "]";
	}

}
