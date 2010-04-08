/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.internal.jaas;

import java.io.IOException;

import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.geronimo.security.ContextManager;
import org.apache.geronimo.security.realm.providers.GeronimoPasswordCredential;
import org.apache.geronimo.security.realm.providers.GeronimoUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * TODO: Insert short summary for
 * SpringSecurityContextPropagatingJaasAuthenticationFilter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SpringSecurityContextPropagatingJaasAuthenticationFilter implements
		Filter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// Intentionally left blank
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		this.log
				.info("About to propagate Spring Security provided Authentication to JAAS context ...");
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (authentication == null) {
			this.log
					.warn("No Authentication found in Spring Security context. JAAS will not be able to authenticat the current request.");

			chain.doFilter(request, response);

			return;
		}

		final Subject jaasSubject = transformIntoJAASSubject(authentication);
		ContextManager.registerSubject(jaasSubject);
		ContextManager.setCallers(jaasSubject, jaasSubject);
		this.log
				.info(
						"Propagated Spring Security provided Authentication [{}] to JAAS as Subject [{}]",
						authentication, jaasSubject);

		chain.doFilter(request, response);

		ContextManager.unregisterSubject(jaasSubject);
		this.log.info("Unregistered Subject [{}] from JAAS", jaasSubject);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig filterConfig) throws ServletException {
		// Intentionally left blank
	}

	private Subject transformIntoJAASSubject(final Authentication authentication) {
		final String principal = (String) authentication.getPrincipal();
		final String credentials = (String) authentication.getCredentials();

		final Subject jaasSubject = new Subject();
		jaasSubject.getPrincipals().add(new GeronimoUserPrincipal(principal));
		jaasSubject.getPrivateCredentials().add(
				new GeronimoPasswordCredential(principal, credentials
						.toCharArray()));

		return jaasSubject;
	}
}
