/**
 * 
 */
package com.acme.orderplacement.jee.item.mdb.internal.spring;

import com.acme.orderplacement.jee.framework.common.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor;
import com.acme.orderplacement.jee.framework.common.jndi.JndiNames;

/**
 * <p>
 * By default Spring's
 * {@link org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor
 * <code>SpringBeanAutowiringInterceptor</code>} loads <strong>all</strong>
 * <code>ApplicationContext</code>s defined in bean defintion files matching
 * <code>classpath*:beanRefContext.xml</code>. This, however, is undesirable in
 * our situation since we want <strong>separate</strong>
 * <code>ApplicationContext</code>s for our <tt>Service Layer</tt> and our
 * <tt>Integration Layer</tt>. We therefore create a custom subclass that loads
 * a <strong>specific</strong> <code>ApplicationContext</code> by id.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class JndiEnabledIntegrationLayerSpringBeanAutowiringInterceptor
		extends AbstractJndiEnabledSpringBeanAutowiringInterceptor {

	private static final String INTEGRATION_MODULE_CONTEXT_RESOURCE_LOCATION = "classpath:META-INF/spring/mdb.integrationContext-camel.scontext";

	/**
	 * @see com.acme.orderplacement.jee.framework.common.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor#getParentApplicationContextJndiName()
	 */
	@Override
	protected String getParentApplicationContextJndiName() {

		return JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.common.jndi.AbstractJndiEnabledSpringBeanAutowiringInterceptor#getApplicationContextResourceLocation()
	 */
	@Override
	protected String getApplicationContextResourceLocation() {

		return INTEGRATION_MODULE_CONTEXT_RESOURCE_LOCATION;
	}
}
