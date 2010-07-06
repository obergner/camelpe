/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import junit.framework.Assert;

import org.apache.xbean.naming.context.WritableContext;
import org.apache.xbean.naming.global.GlobalContextManager;
import org.junit.Test;

/**
 * <p>
 * Test {@link NestedJndiBinding}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class NestedJndiBindingTest {

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
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#NestedJndiBinding(java.lang.String, java.lang.Object, javax.naming.Context)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void nestedJndiBindingShouldRejectNullJndiName()
			throws IllegalArgumentException, NamingException {
		new NestedJndiBinding(null, new Object(), new InitialContext(
				ENVIRONMENT));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#NestedJndiBinding(java.lang.String, java.lang.Object, javax.naming.Context)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void nestedJndiBindingShouldRejectEmptyJndiName()
			throws IllegalArgumentException, NamingException {
		new NestedJndiBinding("", new Object(), new InitialContext(ENVIRONMENT));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#NestedJndiBinding(java.lang.String, java.lang.Object, javax.naming.Context)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void nestedJndiBindingShouldRejectNullResource()
			throws IllegalArgumentException, NamingException {
		new NestedJndiBinding("java:comp/env/Test", null, new InitialContext(
				ENVIRONMENT));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#NestedJndiBinding(java.lang.String, java.lang.Object, javax.naming.Context)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void nestedJndiBindingShouldRejectNullRootContext()
			throws IllegalArgumentException {
		new NestedJndiBinding("java:comp/env/Test", new Object(), null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#bind()}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test
	public final void bindShouldCorrectlyBindResource() throws NamingException {
		final Context initialCtx = new InitialContext(ENVIRONMENT);
		final String jndiName = "java:comp/env/Test";
		final Object resource = new Object();
		final NestedJndiBinding objectUnderTest = new NestedJndiBinding(
				jndiName, resource, initialCtx);
		objectUnderTest.bind();

		final Object boundResource = initialCtx.lookup(jndiName);
		Assert.assertNotNull("bind() did NOT bind resource", boundResource);
		Assert.assertEquals("bind() bound wrong resource", resource,
				boundResource);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#bind()}
	 * .
	 * 
	 * @throws NamingException
	 */
	@Test(expected = NotContextException.class)
	public final void bindShouldRecognizeNoContextWhereContextRequired()
			throws NamingException {
		final Context initialCtx = new InitialContext(ENVIRONMENT);
		initialCtx.bind("testenv", new Object());

		final String jndiName = "testenv/Test";
		final Object resource = new Object();
		final NestedJndiBinding objectUnderTest = new NestedJndiBinding(
				jndiName, resource, initialCtx);
		objectUnderTest.bind();
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.common.jndi.NestedJndiBinding#unbind()}
	 * .
	 * 
	 * @throws NamingException
	 */
	// @Test
	// public final void unbindShouldUnbindBoundResource() throws
	// NamingException {
	// final Context initialCtx = new InitialContext(ENVIRONMENT);
	// final String jndiName = "env/Test";
	// final Object resource = new Object();
	//
	// final Context envCtx = initialCtx.createSubcontext("env");
	// envCtx.bind("Test", resource);
	// initialCtx.unbind(jndiName);
	// Assert.assertNull("??????", initialCtx.lookup(jndiName));
	// envCtx.unbind("Test");
	// Assert.assertNull("WHAT?", initialCtx.lookup(jndiName));
	//
	// final NestedJndiBinding objectUnderTest = new NestedJndiBinding(
	// jndiName, resource, initialCtx);
	// objectUnderTest.unbind();
	//
	// final Object boundResource = initialCtx.lookup(jndiName);
	// Assert.assertNull("unbind() did NOT unbind resource", boundResource);
	// }
}
