/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.auth;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * <p>
 * For the time being, we statically authenticate each caller as the user
 * &quot;admin&quot;. This is obviously a <i>temporary</i> solution.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class DummyUserInjectingAuthenticationEjbInterceptor {

	private static final String DUMMY_USER = "admin";

	private static final String DUMMY_PASSWORD = "admin";

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @param invocationContext
	 * @throws Exception
	 */
	@AroundInvoke
	public Object authenticateRequest(final InvocationContext invocationContext)
			throws Exception {
		this.log.debug("Authenticating request [{}] ...", invocationContext);
		final Authentication previouslyRegisteredAuthentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (previouslyRegisteredAuthentication != null) {
			this.log
					.debug(
							"Request [{}] has already been authenticated by [{}]. Proceeding without further action.",
							invocationContext,
							previouslyRegisteredAuthentication);

			return invocationContext.proceed();
		}

		final Authentication dummyAuth = new UsernamePasswordAuthenticationToken(
				DUMMY_USER, DUMMY_PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(dummyAuth);
		this.log
				.debug(
						"Bound dummy authentication instance [{}] to current request [{}]",
						dummyAuth, invocationContext);

		final Object invocationResult = invocationContext.proceed();

		SecurityContextHolder.getContext().setAuthentication(null);
		this.log
				.debug(
						"Dummy authentication instance [{}] removed from current request [{}]",
						dummyAuth, invocationContext);

		return invocationResult;
	}
}
