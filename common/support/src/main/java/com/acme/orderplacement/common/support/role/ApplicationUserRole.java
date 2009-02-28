/**
 * 
 */
package com.acme.orderplacement.common.support.role;

/**
 * <p>
 * Defines <code>String</code> constants for all <tt>Roles</tt> users may assume
 * in the context of our <strong>SOA Order Application</strong>. These roles
 * will be used by our security subsystem to restrict access to secured
 * resources.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
public class ApplicationUserRole {

	// ------------------------------------------------------------------------
	// Role names
	// ------------------------------------------------------------------------

	/**
	 * Anonymous, unauthenticated user.
	 */
	public static final String ROLE_GUEST = "ROLE_GUEST";

	/**
	 * Authenticated external user.
	 */
	public static final String ROLE_EXTERNAL_USER = "ROLE_EXTERNAL_USER";

	/**
	 * A regular employee of our company.
	 */
	public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";

	/**
	 * Application administrator.
	 */
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	/**
	 * Accountant.
	 */
	public static final String ROLE_ACCOUNTANT = "ROLE_ACCOUNTANT";

	/**
	 * Array of all roles known to out application.
	 */
	public static final String[] ALL_ROLES = new String[] { ROLE_GUEST,
			ROLE_EXTERNAL_USER, ROLE_EMPLOYEE, ROLE_ADMIN, ROLE_ACCOUNTANT };

	// ------------------------------------------------------------------------
	// Hidden ctor
	// ------------------------------------------------------------------------

	/**
	 * No need to instantiate.
	 */
	private ApplicationUserRole() {
		// Intentionally left blank
	}

}
