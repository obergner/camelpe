/**
 * 
 */
package com.acme.orderplacement.jee.ejb.internal.spring;

import com.acme.orderplacement.jee.support.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor;
import com.acme.orderplacement.jee.support.jndi.JndiNames;

/**
 * <p>
 * TODO: Insert short summary for
 * JndiEnabledServiceLayerSpringBeanAutowiringInterceptor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class JndiEnabledServiceLayerSpringBeanAutowiringInterceptor
		extends AbstractJndiEnabledSpringBeanAutowiringInterceptor {

	private static final String ITEM_MODULE_CONTEXT_RESOURCE_LOCATION = "classpath:META-INF/spring/item.serviceContext.scontext";

	/**
	 * @see com.acme.orderplacement.jee.support.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor#getParentApplicationContextJndiName()
	 */
	@Override
	protected String getParentApplicationContextJndiName() {

		return JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT;
	}

	/**
	 * @see com.acme.orderplacement.jee.support.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor#getApplicationContextResourceLocation()
	 */
	@Override
	protected String getApplicationContextResourceLocation() {

		return ITEM_MODULE_CONTEXT_RESOURCE_LOCATION;
	}
}
