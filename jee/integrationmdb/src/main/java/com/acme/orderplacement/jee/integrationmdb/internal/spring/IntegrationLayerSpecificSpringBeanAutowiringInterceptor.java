/**
 * 
 */
package com.acme.orderplacement.jee.integrationmdb.internal.spring;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

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
public final class IntegrationLayerSpecificSpringBeanAutowiringInterceptor
		extends SpringBeanAutowiringInterceptor {

	/**
	 * Our <code>ApplicationContext</code>'s location on the classpath.
	 */
	private static final String RESOURCE_LOCATION = "classpath:META-INF/spring/mdb.beanRefContext.xml";

	/**
	 * The name of the
	 * {@link org.springframework.context.support.ClassPathXmlApplicationContext
	 * <code>ClassPathXmlApplicationContext</code>} defined in a
	 * <code>classpath*:beanRefContext.xml</code> resource we wish to load.
	 */
	private static final String BEAN_FACTORY_LOCATOR_KEY = "com.acme.orderplacement.jee.integrationComponentFactory";

	/**
	 * @see org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor#getBeanFactoryLocatorKey(java.lang.Object)
	 */
	@Override
	protected String getBeanFactoryLocatorKey(final Object target) {

		return BEAN_FACTORY_LOCATOR_KEY;
	}

	/**
	 * @see org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor#getBeanFactoryLocator(java.lang.Object)
	 */
	@Override
	protected BeanFactoryLocator getBeanFactoryLocator(final Object target) {
		return ContextSingletonBeanFactoryLocator
				.getInstance(RESOURCE_LOCATION);
	}
}
