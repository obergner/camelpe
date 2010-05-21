/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jndi;

import java.util.Properties;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * <p>
 * TODO: Insert short summary for
 * AbstractJndiEnabledSpringBeanAutowiringInterceptor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public abstract class AbstractJndiEnabledSpringBeanAutowiringInterceptor extends
		SpringBeanAutowiringInterceptor {

	/**
	 * @see org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor#
	 *      getBeanFactoryLocator(java.lang.Object)
	 */
	@Override
	protected final BeanFactoryLocator getBeanFactoryLocator(final Object target) {
		final JndiStoredParentContextAwareBeanFactoryLocator jndiStoredParentContextAwareBeanFactoryLocator = JndiStoredParentContextAwareBeanFactoryLocator
				.getInstance(getParentApplicationContextJndiName());
		jndiStoredParentContextAwareBeanFactoryLocator
				.setJndiEnvironment(getJndiEnvironment());

		return jndiStoredParentContextAwareBeanFactoryLocator;
	}

	/**
	 * @see org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor#
	 *      getBeanFactoryLocatorKey(java.lang.Object)
	 */
	@Override
	protected final String getBeanFactoryLocatorKey(final Object target) {

		return getApplicationContextResourceLocation();
	}

	/**
	 * @return
	 */
	protected abstract String getParentApplicationContextJndiName();

	/**
	 * @return
	 */
	protected abstract String getApplicationContextResourceLocation();

	/**
	 * @return
	 */
	protected Properties getJndiEnvironment() {
		return null;
	}
}
