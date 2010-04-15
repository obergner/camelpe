/**
 * 
 */
package com.acme.orderplacement.test.support.auth;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO: Insert short summary for TestPrincipalRegistration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalRegistration.COMPONENT_NAME)
public class TestPrincipalRegistration implements PrincipalRegistration {

	@Resource(name = PrincipalHolder.COMPONENT_NAME)
	private PrincipalHolder delegate;

	/**
	 * @param delegate
	 *            the delegate to set
	 */
	public final void setDelegate(final PrincipalHolder delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see com.acme.orderplacement.test.support.auth.PrincipalRegistration#
	 *      registerCurrentPrincipal (java . security . Principal )
	 */
	public void registerCurrentPrincipal(final Principal principal) {
		this.delegate.registerCurrentPrincipal(principal);
	}

	/**
	 * @see com.acme.orderplacement.test.support.auth.PrincipalRegistration#
	 *      unregisterCurrentPrincipal ()
	 */
	public void unregisterCurrentPrincipal() {
		this.delegate.unregisterCurrentPrincipal();
	}

	/**
	 * @throws IllegalStateException
	 */
	@PostConstruct
	public void ensureCorrectlyConfigured() throws IllegalStateException {
		if (this.delegate == null) {
			throw new IllegalStateException("No ["
					+ PrincipalHolder.class.getName() + "] has been injected");
		}
	}
}
