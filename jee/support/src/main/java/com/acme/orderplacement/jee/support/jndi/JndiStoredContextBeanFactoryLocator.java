/**
 * 
 */
package com.acme.orderplacement.jee.support.jndi;

import javax.naming.NamingException;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextBeanFactoryReference;
import org.springframework.jndi.JndiLocatorSupport;

/**
 * <p>
 * TODO: Insert short summary for JndiStoredContextBeanFactoryLocator
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JndiStoredContextBeanFactoryLocator extends JndiLocatorSupport
		implements BeanFactoryLocator {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.beans.factory.access.BeanFactoryLocator#useBeanFactory(java.lang.String)
	 */
	public BeanFactoryReference useBeanFactory(final String factoryKey)
			throws BeansException {
		try {
			Validate.notEmpty(factoryKey, "factoryKey");
			if (this.logger.isTraceEnabled()) {
				this.logger
						.trace("Attempting to look up ApplicationContext in JNDI using JNDI name ["
								+ factoryKey + "] ...");
			}
			final ApplicationContext appCtxInJndi = (ApplicationContext) lookup(
					factoryKey, ApplicationContext.class);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Retrieved AplicationContext [ID = "
						+ appCtxInJndi.getId() + "|displayName = "
						+ appCtxInJndi.getDisplayName()
						+ "] from JNDI using JNDI name [" + factoryKey + "]");
			}

			return new ContextBeanFactoryReference(appCtxInJndi);
		} catch (final NamingException e) {
			final String error = "Failed to look up ApplicationContext in JNDI using JNDI name ["
					+ factoryKey + "]";
			this.logger.error(error, e);

			throw new BootstrapException(error, e);
		}
	}
}
