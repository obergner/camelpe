/**
 * 
 */
package com.acme.orderplacement.jee.support.jndi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.xbean.naming.context.WritableContext;
import org.apache.xbean.naming.global.GlobalContextManager;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * Test {@link JndiStoredParentContextAwareBeanFactoryLocator}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JndiStoredParentContextAwareBeanFactoryLocatorTest {

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
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredParentContextAwareBeanFactoryLocator#getInstance(java.lang.String)}
	 * .
	 */
	@Test
	public final void getInstanceShouldReturnSameInstanceForSameParentContextJndiLocation() {
		final String firstParentCtxJndiLocation = "location";
		final String secondParentCtxJndiLocation = "location";

		final BeanFactoryLocator firstLocator = JndiStoredParentContextAwareBeanFactoryLocator
				.getInstance(firstParentCtxJndiLocation);
		final BeanFactoryLocator secondLocator = JndiStoredParentContextAwareBeanFactoryLocator
				.getInstance(secondParentCtxJndiLocation);

		assertSame(
				"getInstance(...) returned different instances for same argument",
				firstLocator, secondLocator);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.jndi.JndiStoredParentContextAwareBeanFactoryLocator#useBeanFactory(java.lang.String)}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public final void useBeanFactoryShouldReturnValidApplicationContext()
			throws NamingException {
		final ApplicationContext parentAppCtx = new ClassPathXmlApplicationContext(
				getParentApplicationContextResourceLocation());
		final InitialContext ic = new InitialContext(ENVIRONMENT);
		final String jndiName = "TEST";
		ic.bind(jndiName, parentAppCtx);

		final JndiStoredParentContextAwareBeanFactoryLocator objectUnderTest = JndiStoredParentContextAwareBeanFactoryLocator
				.getInstance(jndiName);
		objectUnderTest.setJndiEnvironment(ENVIRONMENT);

		final BeanFactoryReference beanFactoryReference = objectUnderTest
				.useBeanFactory(getApplicationContextResourceLocation());
		assertNotNull("useBeanFactory("
				+ getApplicationContextResourceLocation() + ") returned null",
				beanFactoryReference);
		final BeanFactory factory = beanFactoryReference.getFactory();
		assertNotNull("useBeanFacttory("
				+ getApplicationContextResourceLocation()
				+ ") returned invalid BeanFactoryReference", factory);
		assertTrue(
				"useBeanFactory("
						+ getApplicationContextResourceLocation()
						+ ") returned BeanFactoryReferenece that does not reference an ApplicationContext",
				factory instanceof ApplicationContext);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private String getApplicationContextResourceLocation() {
		return getClass().getName().replace('.', '/') + ".scontext";
	}

	private String getParentApplicationContextResourceLocation() {
		return getClass().getName().replace('.', '/') + "-parent.scontext";
	}
}
