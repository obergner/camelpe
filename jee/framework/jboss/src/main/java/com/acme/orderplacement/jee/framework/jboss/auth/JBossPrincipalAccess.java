/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.auth;

import java.security.Principal;

import org.jboss.security.SecurityAssociation;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess;

/**
 * <p>
<<<<<<< HEAD:jee/framework/jboss/src/main/java/com/acme/orderplacement/jee/framework/jboss/auth/JBossPrincipalAccess.java
 * A <a href="http://www.jboss.org">JBoss</a> specific implementation of
=======
 * A <a href="http://www.jboss.org">JBoss AS</a> specific implementation of
>>>>>>> 2783e200e540ea02246b9c601bccf4afe8e4fee0:jee/framework/jboss/src/main/java/com/acme/orderplacement/jee/framework/jboss/auth/JBossPrincipalAccess.java
 * {@link PrincipalAccess <code>PrincipalAccess</code>} that delegates to
 * JBoss's {@link org.jboss.security.SecurityAssociation
 * <code>org.jboss.security.SecurityAssociation</code>} when obtaining the
 * current <code>Principal</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JBossPrincipalAccess implements PrincipalAccess {

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		return SecurityAssociation.getCallerPrincipal();
	}

}
