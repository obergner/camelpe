/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * For the time being, we statically authenticate each caller as the user
 * &quot;admin&quot;. This is obviously a <i>temporary</i> solution.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class DummyUserInjectingAuthenticationFilter implements Filter {

	private static final String DUMMY_USER = "admin";

	private static final String DUMMY_PASSWORD = "admin";

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
			final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		final Authentication dummyAuth = new UsernamePasswordAuthenticationToken(
				DUMMY_USER, DUMMY_PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(dummyAuth);
		this.log.debug(
				"Bound dummy authentication instance [{}] to current request",
				dummyAuth);

		filterChain.doFilter(request, response);

		SecurityContextHolder.getContext().setAuthentication(null);
		this.log
				.debug(
						"Dummy authentication instance [{}] removed from current request",
						dummyAuth);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig arg0) throws ServletException {
		// Intentionally left blank
	}
}
