/**
 * 
 */
package com.acme.orderplacement.jee.geronimo.spring;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang.Validate;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.geronimo.gbean.GBeanLifecycle;
import org.apache.geronimo.gjndi.GlobalContextGBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.acme.orderplacement.jee.support.jndi.NestedJndiBinding;

/**
 * <p>
 * A <tt>Geronimo GBean</tt> for creating and registering
 * {@link ApplicationContext <tt>Spring Application Context</tt>} upon module
 * startup. It takes a configurable
 * {@link #getApplicationContextResourceLocation()
 * <tt>location on the classpath</tt>} and a configurable {@link #getJndiName()
 * <tt>JNDI name</tt>} and
 * <ol>
 * <li>
 * loads an {@link ApplicationContext <code>ApplicationContext</code>} from that
 * location, and</li>
 * <li>
 * registers the loaded <code>ApplicationContext</code> in <tt>Geronimo</tt>'s
 * JNDI tree, using the aforementioned configured <tt>JNDI name</tt>.</li>
 * </ol>
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ApplicationContextGBean implements ApplicationContext,
		GBeanLifecycle {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final GBeanInfo GBEAN_INFO;

	static {
		final GBeanInfoBuilder infoBuilder = new GBeanInfoBuilder(
				"com.acme.orderplacement:layer=PlatformLayer,type=GBean,name=ApplicationContextGBean",
				ApplicationContextGBean.class);
		infoBuilder.addAttribute("applicationContextResourceLocation",
				String.class, true);
		infoBuilder.addAttribute("jndiName", String.class, true);
		infoBuilder.addReference("parentApplicationContext",
				ApplicationContext.class);
		infoBuilder.addReference("globalJndiContext", GlobalContextGBean.class);
		infoBuilder.addInterface(ApplicationContext.class, new String[0]);
		infoBuilder.setConstructor(new String[] { "globalJndiContext",
				"applicationContextResourceLocation", "jndiName",
				"parentApplicationContext" });

		GBEAN_INFO = infoBuilder.getBeanInfo();
	}

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final GlobalContextGBean globalJndiContext;

	private final String applicationContextResourceLocation;

	private final String jndiName;

	private final ApplicationContext parentApplicationContext;

	private AbstractApplicationContext delegate;

	private NestedJndiBinding applicationContextBinding;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param globalJndiContext
	 * @param applicationContextResourceLocation
	 * @param jndiName
	 * @param parentApplicationContext
	 * @throws IllegalArgumentException
	 */
	public ApplicationContextGBean(final GlobalContextGBean globalJndiContext,
			final String applicationContextResourceLocation,
			final String jndiName,
			final ApplicationContext parentApplicationContext)
			throws IllegalArgumentException {
		Validate.notNull(globalJndiContext, "globalJndiContext");
		Validate.notEmpty(applicationContextResourceLocation,
				"applicationContextResourceLocation");
		Validate.notEmpty(jndiName, "jndiName");
		this.globalJndiContext = globalJndiContext;
		this.applicationContextResourceLocation = applicationContextResourceLocation;
		this.jndiName = jndiName;
		this.parentApplicationContext = parentApplicationContext;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the contextResourceLocation
	 */
	public String getApplicationContextResourceLocation() {
		return this.applicationContextResourceLocation;
	}

	/**
	 * @return the jndiName
	 */
	public String getJndiName() {
		return this.jndiName;
	}

	/**
	 * @return the parentApplicationContextJndiName
	 */
	public final ApplicationContext getParentApplicationContext() {
		return this.parentApplicationContext;
	}

	// -------------------------------------------------------------------------
	// org.apache.geronimo.gbean.GBeanLifecycle
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	public static GBeanInfo getGBeanInfo() {
		return GBEAN_INFO;
	}

	/**
	 * @see org.apache.geronimo.gbean.GBeanLifecycle#doStart()
	 */
	public void doStart() throws Exception {
		startApplicationContext();
		bindApplicationContext();
	}

	/**
	 * @see org.apache.geronimo.gbean.GBeanLifecycle#doFail()
	 */
	public void doFail() {
		getLog()
				.error(
						"Starting ApplicationContext from resource location [{}] failed",
						getApplicationContextResourceLocation());
		unbindApplicationContext();
		closeApplicationContext();
	}

	/**
	 * @see org.apache.geronimo.gbean.GBeanLifecycle#doStop()
	 */
	public void doStop() throws Exception {
		unbindApplicationContext();
		closeApplicationContext();
	}

	// -------------------------------------------------------------------------
	// org.springframework.context.ApplicationContext
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.beans.factory.ListableBeanFactory#findAnnotationOnBean
	 *      (java.lang.String, java.lang.Class)
	 */
	public <A extends Annotation> A findAnnotationOnBean(final String beanName,
			final Class<A> annotationType) {
		return getDelegate().findAnnotationOnBean(beanName, annotationType);
	}

	/**
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeansWithAnnotation
	 *      (java.lang.Class)
	 */
	public Map<String, Object> getBeansWithAnnotation(
			final Class<? extends Annotation> annotationType)
			throws BeansException {
		return getDelegate().getBeansWithAnnotation(annotationType);
	}

	/**
	 * @param name
	 * @return
	 * @see org.springframework.beans.factory.BeanFactory#containsBean(java.lang.String)
	 */
	public boolean containsBean(final String name) {
		return getDelegate().containsBean(name);
	}

	/**
	 * @param beanName
	 * @return
	 * @see org.springframework.beans.factory.ListableBeanFactory#containsBeanDefinition(java.lang.String)
	 */
	public boolean containsBeanDefinition(final String beanName) {
		return getDelegate().containsBeanDefinition(beanName);
	}

	/**
	 * @param name
	 * @return
	 * @see org.springframework.beans.factory.HierarchicalBeanFactory#containsLocalBean(java.lang.String)
	 */
	public boolean containsLocalBean(final String name) {
		return getDelegate().containsLocalBean(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.springframework.beans.factory.BeanFactory#getAliases(java.lang.String)
	 */
	public String[] getAliases(final String name) {
		return getDelegate().getAliases(name);
	}

	/**
	 * @return
	 * @throws IllegalStateException
	 * @see org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()
	 */
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
			throws IllegalStateException {
		return getDelegate().getAutowireCapableBeanFactory();
	}

	/**
	 * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.Class)
	 */
	public <T> T getBean(final Class<T> requiredType) throws BeansException {
		return getDelegate().getBean(requiredType);
	}

	/**
	 * @param name
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.String,
	 *      java.lang.Class)
	 */
	public <T> T getBean(final String name, final Class<T> requiredType)
			throws BeansException {
		return getDelegate().getBean(name, requiredType);
	}

	/**
	 * @param name
	 * @param args
	 * @return
	 * @throws BeansException
	 * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.String,
	 *      java.lang.Object[])
	 */
	public Object getBean(final String name, final Object... args)
			throws BeansException {
		return getDelegate().getBean(name, args);
	}

	/**
	 * @param name
	 * @return
	 * @throws BeansException
	 * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.String)
	 */
	public Object getBean(final String name) throws BeansException {
		return getDelegate().getBean(name);
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanDefinitionCount()
	 */
	public int getBeanDefinitionCount() {
		return getDelegate().getBeanDefinitionCount();
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanDefinitionNames()
	 */
	public String[] getBeanDefinitionNames() {
		return getDelegate().getBeanDefinitionNames();
	}

	/**
	 * @param type
	 * @param includeNonSingletons
	 * @param allowEagerInit
	 * @return
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanNamesForType(java.lang.Class,
	 *      boolean, boolean)
	 */
	public String[] getBeanNamesForType(final Class type,
			final boolean includeNonSingletons, final boolean allowEagerInit) {
		return getDelegate().getBeanNamesForType(type, includeNonSingletons,
				allowEagerInit);
	}

	/**
	 * @param type
	 * @return
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanNamesForType(java.lang.Class)
	 */
	public String[] getBeanNamesForType(final Class type) {
		return getDelegate().getBeanNamesForType(type);
	}

	/**
	 * @param type
	 * @param includeNonSingletons
	 * @param allowEagerInit
	 * @return
	 * @throws BeansException
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(java.lang.Class,
	 *      boolean, boolean)
	 */
	public <T> Map<String, T> getBeansOfType(final Class<T> type,
			final boolean includeNonSingletons, final boolean allowEagerInit)
			throws BeansException {
		return getDelegate().getBeansOfType(type, includeNonSingletons,
				allowEagerInit);
	}

	/**
	 * @param type
	 * @return
	 * @throws BeansException
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(java.lang.Class)
	 */
	public <T> Map<String, T> getBeansOfType(final Class<T> type)
			throws BeansException {
		return getDelegate().getBeansOfType(type);
	}

	/**
	 * @return
	 * @see org.springframework.core.io.ResourceLoader#getClassLoader()
	 */
	public ClassLoader getClassLoader() {
		return getDelegate().getClassLoader();
	}

	/**
	 * @return
	 * @see org.springframework.context.ApplicationContext#getDisplayName()
	 */
	public String getDisplayName() {
		return getDelegate().getDisplayName();
	}

	/**
	 * @return
	 * @see org.springframework.context.ApplicationContext#getId()
	 */
	public String getId() {
		return getDelegate().getId();
	}

	/**
	 * @param resolvable
	 * @param locale
	 * @return
	 * @throws NoSuchMessageException
	 * @see org.springframework.context.MessageSource#getMessage(org.springframework.context.MessageSourceResolvable,
	 *      java.util.Locale)
	 */
	public String getMessage(final MessageSourceResolvable resolvable,
			final Locale locale) throws NoSuchMessageException {
		return getDelegate().getMessage(resolvable, locale);
	}

	/**
	 * @param code
	 * @param args
	 * @param locale
	 * @return
	 * @throws NoSuchMessageException
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String,
	 *      java.lang.Object[], java.util.Locale)
	 */
	public String getMessage(final String code, final Object[] args,
			final Locale locale) throws NoSuchMessageException {
		return getDelegate().getMessage(code, args, locale);
	}

	/**
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param locale
	 * @return
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String,
	 *      java.lang.Object[], java.lang.String, java.util.Locale)
	 */
	public String getMessage(final String code, final Object[] args,
			final String defaultMessage, final Locale locale) {
		return getDelegate().getMessage(code, args, defaultMessage, locale);
	}

	/**
	 * @return
	 * @see org.springframework.context.ApplicationContext#getParent()
	 */
	public ApplicationContext getParent() {
		return getDelegate().getParent();
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.HierarchicalBeanFactory#getParentBeanFactory()
	 */
	public BeanFactory getParentBeanFactory() {
		return getDelegate().getParentBeanFactory();
	}

	/**
	 * @param location
	 * @return
	 * @see org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
	 */
	public Resource getResource(final String location) {
		return getDelegate().getResource(location);
	}

	/**
	 * @param locationPattern
	 * @return
	 * @throws IOException
	 * @see org.springframework.core.io.support.ResourcePatternResolver#getResources(java.lang.String)
	 */
	public Resource[] getResources(final String locationPattern)
			throws IOException {
		return getDelegate().getResources(locationPattern);
	}

	/**
	 * @return
	 * @see org.springframework.context.ApplicationContext#getStartupDate()
	 */
	public long getStartupDate() {
		return getDelegate().getStartupDate();
	}

	/**
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see org.springframework.beans.factory.BeanFactory#getType(java.lang.String)
	 */
	public Class<?> getType(final String name)
			throws NoSuchBeanDefinitionException {
		return getDelegate().getType(name);
	}

	/**
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see org.springframework.beans.factory.BeanFactory#isPrototype(java.lang.String)
	 */
	public boolean isPrototype(final String name)
			throws NoSuchBeanDefinitionException {
		return getDelegate().isPrototype(name);
	}

	/**
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see org.springframework.beans.factory.BeanFactory#isSingleton(java.lang.String)
	 */
	public boolean isSingleton(final String name)
			throws NoSuchBeanDefinitionException {
		return getDelegate().isSingleton(name);
	}

	/**
	 * @param name
	 * @param targetType
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see org.springframework.beans.factory.BeanFactory#isTypeMatch(java.lang.String,
	 *      java.lang.Class)
	 */
	public boolean isTypeMatch(final String name, final Class targetType)
			throws NoSuchBeanDefinitionException {
		return getDelegate().isTypeMatch(name, targetType);
	}

	/**
	 * @param event
	 * @see org.springframework.context.ApplicationEventPublisher#publishEvent(org.springframework.context.ApplicationEvent)
	 */
	public void publishEvent(final ApplicationEvent event) {
		getDelegate().publishEvent(event);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private AbstractApplicationContext getDelegate()
			throws IllegalStateException {
		if (this.delegate == null) {
			throw new IllegalStateException("No ApplicationContext initialized");
		}
		return this.delegate;
	}

	private GlobalContextGBean getGlobalJndiContext() {
		return this.globalJndiContext;
	}

	private void startApplicationContext() throws BeansException {
		getLog().debug(
				"Starting ApplicationContext from resource location [{}] ...",
				getApplicationContextResourceLocation());
		this.delegate = new ClassPathXmlApplicationContext(
				new String[] { getApplicationContextResourceLocation() },
				getParentApplicationContext());
		getLog().info("ApplicationContext [displayName = {}] started",
				this.delegate.getDisplayName());
	}

	private void bindApplicationContext() throws IllegalStateException,
			IllegalArgumentException, NamingException {
		getLog()
				.debug(
						"Binding ApplicationContext [displayName = {}] into JNDI [jndiName = {}] ...",
						getDelegate().getDisplayName(), getJndiName());
		this.applicationContextBinding = new NestedJndiBinding(getJndiName(),
				getDelegate(), getGlobalJndiContext());
		this.applicationContextBinding.bind();
		getLog()
				.info(
						"ApplicationContext [displayName = {}] bound into JNDI [jndiName = {}]",
						getDelegate().getDisplayName(), getJndiName());
	}

	private void unbindApplicationContext() throws IllegalStateException {
		try {
			if (this.applicationContextBinding == null) {
				/*
				 * Nothing to do
				 */
				return;
			}
			this.applicationContextBinding.unbind();
			getLog()
					.info(
							"ApplicationContext [displayName = {}] unbound from JNDI [jndiName = {}]",
							getDelegate().getDisplayName(), getJndiName());
		} catch (final NamingException e) {
			/*
			 * Log and ignore
			 */
			getLog()
					.warn(
							"Unbinding ApplicationContext failed: "
									+ e.getMessage(), e);
		} finally {
			this.applicationContextBinding = null;
		}
	}

	private void closeApplicationContext() {
		if (this.delegate == null) {
			/*
			 * Nothing to do
			 */
			return;
		}
		this.delegate.close();
		getLog().info("ApplicationContext [displayName = {}] closed",
				this.delegate.getDisplayName());
		this.delegate = null;
	}

	/**
	 * @return the log
	 */
	protected Logger getLog() {
		return this.log;
	}
}
