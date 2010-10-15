/**
 * 
 */
package com.obergner.acme.orderplacement.integration.inbound.external;

/**
 * <p>
 * TODO: Insert short summary for ExternalEventSourceSystems
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class ExternalEventSourceSystems {

	private static final String EVENT_SOURCE_SYSTEM_URN_SCHEME = "urn:event-source:";

	public static final String JMS_TEST_CLIENT = EVENT_SOURCE_SYSTEM_URN_SCHEME
			+ "external.jms.testClient";

	private ExternalEventSourceSystems() {
		// Noop
	}

}
