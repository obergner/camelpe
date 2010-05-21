/**
 * 
 */
package com.acme.orderplacement.jee.framework.geronimo.internal.auth;

import java.security.Principal;

import javax.security.auth.Subject;

import org.apache.geronimo.security.ContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.common.support.auth.PrincipalAccess;

/**
 * <p>
 * TODO: Insert short summary for GeronimoPrincipalAccess
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(PrincipalAccess.COMPONENT_NAME)
public class GeronimoPrincipalAccess implements PrincipalAccess {

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see com.acme.orderplacement.common.support.auth.PrincipalAccess#currentPrincipal()
	 */
	public Principal currentPrincipal() {
		this.log.debug("Trying to access Geronimo's current Principal ...");
		final Subject currentCaller = ContextManager.getCurrentCaller();
		if (currentCaller == null) {
			this.log.warn("No current caller Subject could be obtained");

			return null;
		}

		final Principal currentPrincipal = ContextManager
				.getCurrentPrincipal(currentCaller);
		if (currentPrincipal == null) {
			this.log
					.warn(
							"No current caller Principal could be obtained from currentCaller subject [{}]",
							currentCaller);

			return null;
		}
		this.log.debug("Obtained Geronimo's current Principal [{}]",
				currentPrincipal);

		return currentPrincipal;
	}

}
