/**
 * 
 */
package com.acme.orderplacement.test.support.auth;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.acme.orderplacement.common.support.auth.PrincipalAccess;

/**
 * <p>
 * TODO: Insert short summary for TestPrincipalAccess
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalAccess.COMPONENT_NAME)
public class TestPrincipalAccess implements PrincipalAccess {

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
	 * @see com.acme.orderplacement.common.support.auth.PrincipalAccess#currentPrincipal()
	 */
	public Principal currentPrincipal() {
		return this.delegate.currentPrincipal();
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
