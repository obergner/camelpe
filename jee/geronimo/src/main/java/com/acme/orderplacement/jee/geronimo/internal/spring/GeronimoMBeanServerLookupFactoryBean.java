/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.internal.spring;

import java.util.ArrayList;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * A <tt>Spring</tt> {@link FactoryBean <code>FactoryBean</code>} for locating
 * <tt>Geronimo</tt>'s {@link MBeanServer <code>MBeanServer</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class GeronimoMBeanServerLookupFactoryBean implements
		FactoryBean<MBeanServer> {

	/**
	 * <tt>Geronimo</tt>'s <code>MBeanServer</code>'s <i>default domain</i>,
	 * used to select the appropriate <code>MBeanServer</code> from the list of
	 * available <code>MBeanServer</code>s.
	 * 
	 * TODO: Since version 2.2 (or 2.1.5?) Geronimo uses the standard platform
	 * mbean server. Consequently, this code isn't necessary anymore. Get rid of
	 * it.
	 */
	private static final String GERONIMO_DEFAULT_DOMAIN = "DefaultDomain";

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public MBeanServer getObject() throws Exception {
		this.log
				.info(
						"Trying to obtain Geronimo's MBeanServer using DefaultDomain [{}] as selection criterion ...",
						GERONIMO_DEFAULT_DOMAIN);

		final ArrayList<MBeanServer> allMBeanServers = MBeanServerFactory
				.findMBeanServer(null);
		this.log.debug("Found [{}] MBeanServers", Integer
				.valueOf(allMBeanServers.size()));

		MBeanServer geronimoMBeanServer = null;
		for (final MBeanServer server : allMBeanServers) {
			this.log
					.trace(
							"Inspecting MBeanServer [DefaultDomain = {} | server = {}]",
							server.getDefaultDomain(), server);
			if (server.getDefaultDomain().equalsIgnoreCase(
					GERONIMO_DEFAULT_DOMAIN)) {
				geronimoMBeanServer = server;
				break;
			}
		}

		if (geronimoMBeanServer == null) {
			this.log
					.error(
							"Could not find Geronimo MBeanServer using DefaultDomain [{}] as selection criterion",
							GERONIMO_DEFAULT_DOMAIN);

			throw new IllegalStateException(
					"Could not find Geronimo MBeanServer using DefaultDomain ["
							+ GERONIMO_DEFAULT_DOMAIN
							+ "] as selection criterion");
		}
		this.log
				.info(
						"Found Geronimo MBeanServer [DefaultDomain = {} | server = {}]",
						geronimoMBeanServer.getDefaultDomain(),
						geronimoMBeanServer);

		return geronimoMBeanServer;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<MBeanServer> getObjectType() {

		return MBeanServer.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {

		return true;
	}
}
