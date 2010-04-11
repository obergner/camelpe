/**
 * 
 */
package com.acme.orderplacement.test.support.auth;

import java.io.Serializable;
import java.security.Principal;

/**
 * <p>
 * TODO: Insert short summary for TestPrincipal
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class TestPrincipal implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;

	/**
	 * @param name
	 */
	public TestPrincipal(final String name) {
		this.name = name;
	}

	/**
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return this.name;
	}
}
