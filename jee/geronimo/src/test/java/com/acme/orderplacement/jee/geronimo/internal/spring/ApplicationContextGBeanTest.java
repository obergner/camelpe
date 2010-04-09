/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.internal.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.gbean.GBeanData;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gjndi.GlobalContextGBean;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.InternalKernelException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.KernelFactory;
import org.apache.geronimo.kernel.repository.Artifact;
import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean;
import com.acme.orderplacement.jee.support.jndi.JndiNames;

/**
 * <p>
 * Test {@link ApplicationContextGBean}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ApplicationContextGBeanTest {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final String APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR = "applicationContextResourceLocation";

	private final Kernel kernel = KernelFactory.newInstance().createKernel(
			"TEST");

	private AbstractName globalContextAn;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Before
	public void setUp() throws Exception {
		this.kernel.boot();

		final GBeanData globalContextGbd = buildGBeanData("name",
				"TestGblobalContext", GlobalContextGBean.getGBeanInfo());
		this.kernel.loadGBean(globalContextGbd, getClass().getClassLoader());
		this.kernel.startGBean(this.globalContextAn = globalContextGbd
				.getAbstractName());
	}

	@After
	public void tearDown() throws GBeanNotFoundException,
			InternalKernelException, IllegalStateException {
		this.kernel.stopGBean(this.globalContextAn);
		this.kernel.unloadGBean(this.globalContextAn);
		this.globalContextAn = null;
		this.kernel.shutdown();
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#ApplicationContextGBean(org.apache.geronimo.gjndi.GlobalContextGBean, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void applicationContextGBeanCtorShouldRejectNullGlobalContext() {
		new ApplicationContextGBean(null, "TEST", "TEST", null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#ApplicationContextGBean(org.apache.geronimo.gjndi.GlobalContextGBean, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void applicationContextGBeanCtorShouldRejectNullContextResourceLocation() {
		new ApplicationContextGBean(createGlobalContextGBeanMock(), null,
				"TEST", null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#ApplicationContextGBean(org.apache.geronimo.gjndi.GlobalContextGBean, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void applicationContextGBeanCtorShouldRejectEmptyContextResourceLocation() {
		new ApplicationContextGBean(createGlobalContextGBeanMock(), "", "TEST",
				null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#ApplicationContextGBean(org.apache.geronimo.gjndi.GlobalContextGBean, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void applicationContextGBeanCtorShouldRejectNullJndiName() {
		new ApplicationContextGBean(createGlobalContextGBeanMock(), "TEST",
				null, null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#ApplicationContextGBean(org.apache.geronimo.gjndi.GlobalContextGBean, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void applicationContextGBeanCtorShouldRejectEmptyJndiName() {
		new ApplicationContextGBean(createGlobalContextGBeanMock(), "TEST", "",
				null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#doStart()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void doStartShouldStartApplicationContext() throws Exception {
		final GBeanData appContextGbd = buildGBeanData("name",
				"AppContextTestGBean", ApplicationContextGBean.getGBeanInfo());
		appContextGbd.setAttribute(APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR,
				getApplicationContextResourceLocation());
		appContextGbd.setAttribute("jndiName",
				JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);
		appContextGbd.setReferencePattern("globalJndiContext",
				this.globalContextAn);

		this.kernel.loadGBean(appContextGbd, getClass().getClassLoader());
		this.kernel.startGBean(appContextGbd.getAbstractName());

		final Object testBean = this.kernel.invoke(appContextGbd
				.getAbstractName(), "getBean",
				new Object[] { "jee.geronimo.test.TestBean" },
				new String[] { String.class.getName() });

		Assert.assertNotNull(
				"Calling getBean('jee.geronimo.test.TestBean') returned null",
				testBean);

		this.kernel.stopGBean(appContextGbd.getAbstractName());
		this.kernel.unloadGBean(appContextGbd.getAbstractName());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#doStart()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void doStartShouldUseParentApplicationContext()
			throws Exception {
		final GBeanData parentAppContextGbd = buildGBeanData("name",
				"ParentAppContextTestGBean", ApplicationContextGBean
						.getGBeanInfo());
		parentAppContextGbd.setAttribute(
				APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR,
				getParentContextResourceLocation());
		parentAppContextGbd.setAttribute("jndiName",
				JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);
		parentAppContextGbd.setReferencePattern("globalJndiContext",
				this.globalContextAn);

		this.kernel.loadGBean(parentAppContextGbd, getClass().getClassLoader());
		this.kernel.startGBean(parentAppContextGbd.getAbstractName());

		final GBeanData appContextGbd = buildGBeanData("name",
				"AppContextTestGBean", ApplicationContextGBean.getGBeanInfo());
		appContextGbd.setAttribute(APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR,
				getApplicationContextResourceLocation());
		appContextGbd.setAttribute("jndiName",
				JndiNames.ITEM_MODULE_APPLICATION_CONTEXT);
		appContextGbd.setReferencePattern("parentApplicationContext",
				parentAppContextGbd.getAbstractName());
		appContextGbd.setReferencePattern("globalJndiContext",
				this.globalContextAn);

		this.kernel.loadGBean(appContextGbd, getClass().getClassLoader());
		this.kernel.startGBean(appContextGbd.getAbstractName());

		final Object testBean = this.kernel.invoke(appContextGbd
				.getAbstractName(), "getBean",
				new Object[] { "jee.geronimo.test.ParentTestBean" },
				new String[] { String.class.getName() });

		Assert
				.assertNotNull(
						"Calling getBean('jee.geronimo.test.ParentTestBean') returned null",
						testBean);

		this.kernel.stopGBean(appContextGbd.getAbstractName());
		this.kernel.unloadGBean(appContextGbd.getAbstractName());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#doStart()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void doStartShouldRegisterApplicationContextInJndi()
			throws Exception {
		final GBeanData appContextGbd = buildGBeanData("name",
				"AppContextTestGBean", ApplicationContextGBean.getGBeanInfo());
		appContextGbd.setAttribute(APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR,
				getApplicationContextResourceLocation());
		appContextGbd.setAttribute("jndiName",
				JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);
		appContextGbd.setReferencePattern("globalJndiContext",
				this.globalContextAn);

		this.kernel.loadGBean(appContextGbd, getClass().getClassLoader());
		this.kernel.startGBean(appContextGbd.getAbstractName());

		final InitialContext ic = createInitialContext();
		final Object appCtxObj = ic
				.lookup(JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);

		Assert.assertNotNull(
				"doStart() did NOT register ApplicationContext in JNDI",
				appCtxObj);
		Assert.assertTrue("doStart() registered wrong object in JNDI",
				appCtxObj instanceof ApplicationContext);

		this.kernel.stopGBean(appContextGbd.getAbstractName());
		this.kernel.unloadGBean(appContextGbd.getAbstractName());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.geronimo.internal.spring.ApplicationContextGBean#doStop()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = javax.naming.NotContextException.class)
	public final void doStopShouldUnregisterApplicationContextFromJndi()
			throws Exception {
		GBeanData appContextGbd = null;
		try {
			appContextGbd = buildGBeanData("name", "AppContextTestGBean",
					ApplicationContextGBean.getGBeanInfo());
			appContextGbd.setAttribute(
					APPLICATION_CONTEXT_RESOURCE_LOCATION_ATTR,
					getApplicationContextResourceLocation());
			appContextGbd.setAttribute("jndiName",
					JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);
			appContextGbd.setReferencePattern("globalJndiContext",
					this.globalContextAn);

			this.kernel.loadGBean(appContextGbd, getClass().getClassLoader());
			this.kernel.startGBean(appContextGbd.getAbstractName());
			this.kernel.stopGBean(appContextGbd.getAbstractName());

			final Properties env = new Properties();
			env.setProperty("java.naming.factory.initial",
					"org.apache.xbean.naming.global.GlobalContextManager");
			new InitialContext(env)
					.lookup(JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT);
		} finally {
			if (appContextGbd != null) {
				this.kernel.unloadGBean(appContextGbd.getAbstractName());
			}
		}
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private InitialContext createInitialContext() throws NamingException {
		final Properties env = new Properties();
		env.setProperty("java.naming.factory.initial",
				"org.apache.xbean.naming.global.GlobalContextManager");

		return new InitialContext(env);
	}

	private GlobalContextGBean createGlobalContextGBeanMock() {
		final GlobalContextGBean mock = EasyMock
				.createMock(GlobalContextGBean.class);

		return mock;
	}

	private String getApplicationContextResourceLocation() {
		return getClass().getName().replace('.', '/') + ".scontext";
	}

	private String getParentContextResourceLocation() {
		return getClass().getName().replace('.', '/') + ".pcontext";
	}

	private GBeanData buildGBeanData(final String key, final String value,
			final GBeanInfo info) {
		final AbstractName abstractName = buildAbstractName(key, value);
		return new GBeanData(abstractName, info);
	}

	private AbstractName buildAbstractName(final String key, final String value) {
		final Map<String, String> names = new HashMap<String, String>();
		names.put(key, value);
		return new AbstractName(
				new Artifact("com.acme.orderplacement.jee.geronimo.TEST",
						"TEST", "1", "jar"), names);
	}

}
