/**
 * 
 */
package com.acme.orderplacement.jee.support.jndi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.xbean.naming.context.WritableContext;
import org.apache.xbean.naming.global.GlobalContextManager;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.acme.orderplacement.jee.support.jndi.JndiStoredContextBeanFactoryLocator;

/**
 * <p>
 * Test {@link JndiStoredContextBeanFactoryLocator}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JndiStoredContextBeanFactoryLocatorTest {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final Properties ENVIRONMENT;

	static {
		try {
			GlobalContextManager.setGlobalContext(new WritableContext());
			ENVIRONMENT = new Properties();
			ENVIRONMENT.setProperty("java.naming.factory.initial",
					"org.apache.xbean.naming.global.GlobalContextManager");
		} catch (final NamingException e) {

			throw new ExceptionInInitializerError(e);
		}
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredContextBeanFactoryLocator#useBeanFactory(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void useBeanFactoryShouldRejectNullJndiName() {
		final JndiStoredContextBeanFactoryLocator objectUnderTest = new JndiStoredContextBeanFactoryLocator();
		objectUnderTest.useBeanFactory(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredContextBeanFactoryLocator#useBeanFactory(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void useBeanFactoryShouldRejectEmptyJndiName() {
		final JndiStoredContextBeanFactoryLocator objectUnderTest = new JndiStoredContextBeanFactoryLocator();
		objectUnderTest.useBeanFactory("");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredContextBeanFactoryLocator#useBeanFactory(java.lang.String)}
	 * .
	 */
	@Test(expected = BootstrapException.class)
	public final void useBeanFactoryShouldReportMissingApplicationContext() {
		final JndiStoredContextBeanFactoryLocator objectUnderTest = new JndiStoredContextBeanFactoryLocator();
		objectUnderTest.useBeanFactory("NOT_STORED_IN_JNDI");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredContextBeanFactoryLocator#useBeanFactory(java.lang.String)}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public final void useBeanFactoryShouldLocateExistingApplicationContextInJndi()
			throws NamingException {
		final ApplicationContext appCtx = new ClassPathXmlApplicationContext(
				getApplicationContextResourceLocation());
		final InitialContext ic = new InitialContext(ENVIRONMENT);
		final String jndiName = "TEST";
		ic.bind(jndiName, appCtx);

		final JndiStoredContextBeanFactoryLocator objectUnderTest = new JndiStoredContextBeanFactoryLocator();
		objectUnderTest.setJndiEnvironment(ENVIRONMENT);

		final BeanFactoryReference beanFactoryReference = objectUnderTest
				.useBeanFactory(jndiName);
		assertNotNull("useBeanFactory(" + jndiName + ") returned null",
				beanFactoryReference);
		final BeanFactory factory = beanFactoryReference.getFactory();
		assertNotNull("useBeanFacttory(" + jndiName
				+ ") returned invalid BeanFactoryReference", factory);
		assertTrue(
				"useBeanFactory("
						+ jndiName
						+ ") returned BeanFactoryReferenece that does not reference an ApplicationContext",
				factory instanceof ApplicationContext);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private String getApplicationContextResourceLocation() {
		return getClass().getName().replace('.', '/') + ".scontext";
	}
}
