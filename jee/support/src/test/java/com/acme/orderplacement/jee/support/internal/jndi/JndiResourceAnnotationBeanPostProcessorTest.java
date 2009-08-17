/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.jndi;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.xbean.naming.context.WritableContext;
import org.apache.xbean.naming.global.GlobalContextManager;
import org.junit.Test;

import com.acme.orderplacement.jee.support.annotation.JndiResource;

/**
 * <p>
 * Test {@link JndiResourceAnnotationBeanPostProcessor}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JndiResourceAnnotationBeanPostProcessorTest {

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

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.internal.jndi.JndiResourceAnnotationBeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public final void postProcessAfterInitializationShouldBindBeanIntoJndi()
			throws NamingException {
		final TestJndiBean bean = new TestJndiBean();
		final JndiResourceAnnotationBeanPostProcessor objectUnderTest = new JndiResourceAnnotationBeanPostProcessor(
				ENVIRONMENT);
		objectUnderTest.init();
		objectUnderTest.postProcessAfterInitialization(bean,
				TestJndiBean.COMPONENT_NAME);

		final InitialContext initialCtx = new InitialContext(ENVIRONMENT);
		final Object boundBean = initialCtx.lookup(TestJndiBean.JNDI_NAME);

		Assert
				.assertNotNull(
						"postProcessAfterInitialization(bean,TestJndiBean.COMPONENT_NAME) did not bind bean into JNDI",
						boundBean);
		Assert
				.assertTrue(
						"postProcessAfterInitialization(bean,TestJndiBean.COMPONENT_NAME) bound wrong bean into JNDI",
						boundBean instanceof TestJndiBean);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.support.internal.jndi.JndiResourceAnnotationBeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test
	public final void postProcessBeforeInitializationShouldReturnNull() {
		final JndiResourceAnnotationBeanPostProcessor objectUnderTest = new JndiResourceAnnotationBeanPostProcessor();
		final Object bean = objectUnderTest.postProcessBeforeInitialization(
				null, null);

		Assert
				.assertNull(
						"postProcessBeforeInitialization(null, null) did not return null",
						bean);
	}

	@JndiResource(value = TestJndiBean.COMPONENT_NAME, jndiName = TestJndiBean.JNDI_NAME)
	private static class TestJndiBean {

		public static final String COMPONENT_NAME = "jee.support.test.TestJndiBean";

		public static final String JNDI_NAME = "com/acme/test/TestJndiBean";

		public TestJndiBean() {
			// Empty
		}
	}
}
