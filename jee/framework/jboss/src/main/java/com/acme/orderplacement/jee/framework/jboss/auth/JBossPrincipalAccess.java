/**
 * 
 */
package com.acme.orderplacement.jee.framework.jboss.auth;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.SubjectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.common.auth.PrincipalAccess;

/**
 * <p>
 * A <a href="http://www.jboss.org">JBoss AS</a> specific implementation of
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

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see com.acme.orderplacement.framework.common.auth.PrincipalAccess#currentPrincipal()
	 */
	@Override
	public Principal currentPrincipal() {
		this.log.trace("Trying to determine authenticated user ...");

		final SecurityContext securityContext = SecurityContextAssociation
				.getSecurityContext();
		if (securityContext == null) {
			throw new IllegalStateException(
					"No SecurityContext bound to current request");
		}

		final SubjectInfo subjectInfo = securityContext.getSubjectInfo();
		if (subjectInfo == null) {
			throw new IllegalStateException(
					"No SubjectInfo found in current SecurityContext");
		}

		final Subject subject = subjectInfo.getAuthenticatedSubject();
		if (subject == null) {
			throw new IllegalStateException(
					"No authenticated Subject found in current SubjectInfo");
		}

		final Set<Principal> principals = subject.getPrincipals();
		Principal userPrincipal = null;
		for (final Principal principal : principals) {
			if (principal.getClass() == SimplePrincipal.class) {
				userPrincipal = principal;
				break;
			}
		}
		if (userPrincipal == null) {
			throw new IllegalStateException(
					"No User Principal found in set of Principals associated with current Subject");
		}

		this.log.trace("Authenticated user is [{}]", userPrincipal);

		return userPrincipal;
	}

}
