/**
 * 
 */
package com.acme.orderplacement.jee.ejb.internal.spring;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * <p>
 * Custom subclass of <a href="http://www.springsource.org"><tt>Spring</tt>
 * </a>'s {@link SpringBeanAutowiringInterceptor
 * <code>SpringBeanAutowiringInterceptor</code>}, enabling this application to
 * use a custom location for its
 * {@link org.springframework.context.ApplicationContext
 * <code>org.springframework.context.ApplicationContext</code>}.
 * </p>
 * <p>
 * <strong>NOTE</strong> In its current state, this class does
 * <strong>not</strong> define the aforementioned custom
 * <code>ApplicationContext</code> location. It was created for easily being
 * able to do so in the future.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CustomSpringBeanAutowiringInterceptor extends
		SpringBeanAutowiringInterceptor {

}
