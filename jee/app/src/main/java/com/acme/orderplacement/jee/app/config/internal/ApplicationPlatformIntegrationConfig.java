/**
 * 
 */
package com.acme.orderplacement.jee.app.config.internal;

import javax.management.MBeanServer;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;

/**
 * <p>
 * TODO: Insert short summary for ApplicationPlatformIntegrationConfig
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Configuration
public class ApplicationPlatformIntegrationConfig implements
		PlatformIntegrationConfig {

	private static final String GERONIMO_ORDERPLACEMENT_JCA_CONNECTION_FACTORY_JNDI_NAME = "jca:/com.acme.orderplacement.jee/orderplacement.jee.ear/JCAManagedConnectionFactory/jdbc/com/acme/OrderPlacementDS";

	private static final String GERONIMO_TX_SYNCHRONIZATION_JNDI_NAME = "java:/TransactionSynchronizationRegistry";

	private static final String GERONIMO_TX_MANAGER_JNDI_NAME = "java:/TransactionManager";

	private static final String GERONIMO_USER_TX_JNDI_NAME = "java:/TransactionManager";

	/**
	 * <tt>Geronimo</tt>'s <code>MBeanServer</code>'s <i>default domain</i>,
	 * used to select the appropriate <code>MBeanServer</code> from the list of
	 * available <code>MBeanServer</code>s.
	 */
	private static final String GERONIMO_DEFAULT_DOMAIN = "DefaultDomain";

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#transactionManager()
	 */
	@Bean(name = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME)
	public PlatformTransactionManager transactionManager()
			throws TransactionSystemException {
		final JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
		jtaTransactionManager
				.setTransactionManagerName(GERONIMO_TX_MANAGER_JNDI_NAME);
		jtaTransactionManager
				.setUserTransactionName(GERONIMO_USER_TX_JNDI_NAME);
		jtaTransactionManager
				.setTransactionSynchronizationRegistryName(GERONIMO_TX_SYNCHRONIZATION_JNDI_NAME);
		jtaTransactionManager.afterPropertiesSet();

		return jtaTransactionManager;
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#applicationDataSource()
	 */
	@Bean(name = PlatformIntegrationConfig.DATASOURCE_COMPONENT_NAME)
	public DataSource applicationDataSource() throws NamingException {
		final JndiObjectFactoryBean jndiLookup = new JndiObjectFactoryBean();
		jndiLookup.setExpectedType(DataSource.class);
		jndiLookup
				.setJndiName(GERONIMO_ORDERPLACEMENT_JCA_CONNECTION_FACTORY_JNDI_NAME);
		jndiLookup.setResourceRef(false);
		jndiLookup.setCache(true);
		jndiLookup.afterPropertiesSet();

		return (DataSource) jndiLookup.getObject();
	}

	/**
	 * @see com.acme.orderplacement.persistence.config.PlatformIntegrationConfig#platformMBeanServer()
	 */
	@Bean(name = PlatformIntegrationConfig.MBEAN_SERVER_COMPONENT_NAME)
	public MBeanServer platformMBeanServer() {
		final MBeanServerFactoryBean mbeanServerFactory = new MBeanServerFactoryBean();
		mbeanServerFactory.setAgentId("");
		mbeanServerFactory.setDefaultDomain(GERONIMO_DEFAULT_DOMAIN);
		mbeanServerFactory.setLocateExistingServerIfPossible(true);
		mbeanServerFactory.afterPropertiesSet();

		return mbeanServerFactory.getObject();
	}
}
