/**
 * 
 */
package com.acme.orderplacement.domain.testsupport.auth;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.common.support.auth.PrincipalAccess;
import com.acme.orderplacement.domain.support.meta.jpa.AuditInfoManagingEntityListener;
import com.acme.orderplacement.test.support.auth.PrincipalHolder;

/**
 * <p>
 * TODO: Insert short summary for PrincipalAccessInjector
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalAccessInjector.COMPONENT_NAME)
public class PrincipalAccessInjector {

	public static final String COMPONENT_NAME = "persistence.testsupport.PrincipalAccessInjector";

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource(name = PrincipalHolder.COMPONENT_NAME)
	private PrincipalAccess principalAccess;

	/**
	 * @param principalAccess
	 *            the principalAccess to set
	 */
	public final void setPrincipalAccess(final PrincipalAccess principalAccess) {
		this.principalAccess = principalAccess;
	}

	// -------------------------------------------------------------------------
	// Sanity check
	// -------------------------------------------------------------------------

	@PostConstruct
	public void ensureCorrectlyInitialized() throws IllegalStateException {
		if (this.principalAccess == null) {
			throw new IllegalStateException("Unsatisfied dependency: ["
					+ PrincipalAccess.class.getName() + "]");
		}
	}

	// -------------------------------------------------------------------------
	// Injector
	// -------------------------------------------------------------------------

	@PostConstruct
	public void injectPrincipalAccessIntoAuditInfoManagingEntityListener() {
		AuditInfoManagingEntityListener
				.setPrincipalAccess(this.principalAccess);
		this.log.debug("PrincipalAccess injected into [{}]",
				AuditInfoManagingEntityListener.class.getName());
	}
}
