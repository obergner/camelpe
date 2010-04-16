/**
 * 
 */
package com.acme.orderplacement.domain.support.meta.jpa;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.common.support.auth.PrincipalAccess;
import com.acme.orderplacement.domain.support.meta.AuditInfo;
import com.acme.orderplacement.domain.support.meta.AuditableDomainObject;

/**
 * <p>
 * A <tt>JPA</tt> <code>EntityListener</code> for initially setting and updating
 * an {@link AuditableDomainObject <code>AuditableDomainObject</code>}'s
 * {@link AuditInfo <code>AuditInfo</code>} component, using information
 * obtained from the {@link SecurityContext <code>SecurityContext</code>}
 * associated with the current thread.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class AuditInfoManagingEntityListener {

	// ------------------------------------------------------------------------
	// Static fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static PrincipalAccess principalAccess;

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Our faithful logger.
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	// ------------------------------------------------------------------------
	// Static dependencies
	// ------------------------------------------------------------------------

	public static void setPrincipalAccess(final PrincipalAccess principalAccess) {
		AuditInfoManagingEntityListener.principalAccess = principalAccess;
	}

	// ------------------------------------------------------------------------
	// Public API
	// ------------------------------------------------------------------------

	/**
	 * @param <T>
	 * @param auditableEntity
	 */
	@PrePersist
	public <T extends AuditableDomainObject<? extends Serializable>> void fillAuditInfoBeforePersisting(
			final T auditableEntity) throws IllegalStateException {
		ensureCorrectlyConfigured();
		final AuditInfo auditInfo = auditableEntity.getAuditInfo();
		if (!auditInfo.isNew()) {
			final String error = "An entity to be peristed for the first time should have an empty AuditInfo component. "
					+ "However, this entity's ["
					+ auditableEntity
					+ "] AuditInfo [" + auditInfo + "] is not empty.";
			this.log.error(error);

			throw new IllegalStateException(error);
		}

		final String currentUser = currentUser();
		final Date currentDate = new Date();
		auditInfo.setCreatedBy(currentUser);
		auditInfo.setCreatedOn(currentDate);
		auditInfo.setLastUpdatedBy(currentUser);
		auditInfo.setLastUpdatedOn(currentDate);
		this.log.debug(
				"The entity [{}] is about to be persisted for the first time. "
						+ "Its AuditInfo has been updated to [{}].",
				auditableEntity, auditInfo);
	}

	/**
	 * @param <T>
	 * @param auditableEntity
	 */
	@PreUpdate
	public <T extends AuditableDomainObject<? extends Serializable>> void fillAuditInfoBeforeUpdating(
			final T auditableEntity) throws IllegalStateException {
		final AuditInfo auditInfo = auditableEntity.getAuditInfo();
		if (!auditInfo.isComplete()) {
			final String error = "An entity to be updated should have a completely filled AuditInfo component. "
					+ "However, this entity's ["
					+ auditableEntity
					+ "] AuditInfo [" + auditInfo + "] is not complete.";
			this.log.error(error);

			throw new IllegalStateException(error);
		}

		final String currentUser = currentUser();
		final Date currentDate = new Date();
		auditInfo.setLastUpdatedBy(currentUser);
		auditInfo.setLastUpdatedOn(currentDate);
		this.log.debug("The entity [{}] is about to be updated. "
				+ "Its AuditInfo has been updated to [{}].", auditableEntity,
				auditInfo);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @return
	 * @throws IllegalStateException
	 */
	private String currentUser() throws IllegalStateException {
		final Principal currentPrincipal = principalAccess.currentPrincipal();
		if (currentPrincipal == null) {
			final String error = "There is no Principal associated with the current thread: "
					+ "Unable to determine the current user.";
			this.log.error(error);

			throw new IllegalStateException(error);
		}
		this.log.debug("Obtained Principal [{}] from the current thread.",
				currentPrincipal);

		final String username = currentPrincipal.getName();
		if (username == null) {
			final String error = "Unable to determine the current user: No username found in the current Principal.";
			this.log.error(error);

			throw new IllegalArgumentException(error);
		}
		this.log.debug(
				"Extracted current user [{}] from the current Principal.",
				username);

		return username;
	}

	/**
	 * @throws IllegalStateException
	 */
	private void ensureCorrectlyConfigured() throws IllegalStateException {
		if (principalAccess == null) {
			final String error = "No ["
					+ PrincipalAccess.class.getName()
					+ "] has been set. Cannot access the Principal associated with each current thread.";
			this.log.error(error);

			throw new IllegalStateException(error);
		}
	}
}
