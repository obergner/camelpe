/**
 * 
 */
package com.acme.orderplacement.persistence.support.jta.hibernate;

import java.util.Properties;

import javax.management.ObjectName;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.hibernate.HibernateException;
import org.hibernate.transaction.TransactionManagerLookup;

/**
 * <p>
 * TODO: Insert short summary for Geronimo21TransactionManagerLookup
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class Geronimo21TransactionManagerLookup implements
		TransactionManagerLookup {

	/**
	 * 
	 */
	public static final String TRANSACTION_MANAGER_GBEAN_NAME = "geronimo.server:J2EEApplication=null,J2EEModule=geronimo/j2ee-server/2.0.1/car,J2EEServer=geronimo,j2eeType=TransactionManager,name=TransactionManager";

	/**
	 * 
	 */
	public static final String USER_TRANSACTION_JNDI_NAME = "java:comp/UserTransaction";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.transaction.TransactionManagerLookup#getTransactionIdentifier
	 * (javax.transaction.Transaction)
	 */
	public Object getTransactionIdentifier(final Transaction transaction) {

		return transaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.transaction.TransactionManagerLookup#getTransactionManager
	 * (java.util.Properties)
	 */
	public TransactionManager getTransactionManager(final Properties props)
			throws HibernateException {
		try {
			final Class<?> kernelClass = Class
					.forName(" org.apache.geronimo.kernel.Kernel");
			final Class<?> kernelRegistryClass = Class
					.forName(" org.apache.geronimo.kernel.KernelRegistry");
			final Class<?> proxyManagerClass = Class
					.forName(" org.apache.geronimo.kernel.proxy.ProxyManager");
			final ObjectName TransactionManagerName = new ObjectName(
					TRANSACTION_MANAGER_GBEAN_NAME);
			final Object kernel = kernelRegistryClass.getMethod(
					"getSingleKernel", new Class[] {}).invoke(null,
					new Object[] {});
			final Object proxyManager = kernelClass.getMethod(
					"getProxyManager", new Class[] {}).invoke(kernel,
					new Object[] {});
			final Class<?>[] clzArray = { ObjectName.class, Class.class };
			final Object[] objArray = { TransactionManagerName,
					TransactionManager.class };

			return (TransactionManager) proxyManagerClass.getMethod(
					"createProxy", clzArray).invoke(proxyManager, objArray);
		} catch (final Exception e) {
			throw new HibernateException(
					"Geronimo Transaction Manager Lookup Failed", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.transaction.TransactionManagerLookup#getUserTransactionName
	 * ()
	 */
	public String getUserTransactionName() {
		return USER_TRANSACTION_JNDI_NAME;
	}
}
