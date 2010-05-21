/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jndi;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextBeanFactoryReference;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * TODO: Insert short summary for JndiStoredParentContextAwareBeanFactoryLocator
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JndiStoredParentContextAwareBeanFactoryLocator implements
		BeanFactoryLocator {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final Map<String, JndiStoredParentContextAwareBeanFactoryLocator> BEAN_FACTORY_LOCATOR_BY_PARENT_APP_CONTEXT = new HashMap<String, JndiStoredParentContextAwareBeanFactoryLocator>();

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<String, BeanFactoryReference> applicationContextRefsByResourceLocation = new HashMap<String, BeanFactoryReference>();

	private final String parentContextJndiName;

	private Properties jndiEnvironment;

	// -------------------------------------------------------------------------
	// Factory methods
	// -------------------------------------------------------------------------

	/**
	 * @return
	 */
	public static JndiStoredParentContextAwareBeanFactoryLocator getInstance() {

		return getInstance(null);
	}

	/**
	 * @param parentContextJndiName
	 * @return
	 */
	public static JndiStoredParentContextAwareBeanFactoryLocator getInstance(
			final String parentContextJndiName) {
		final String localParentContextJndiName = parentContextJndiName != null ? parentContextJndiName
				: JndiNames.PLATFORM_ADAPTER_APPLICATION_CONTEXT;
		JndiStoredParentContextAwareBeanFactoryLocator result;
		synchronized (BEAN_FACTORY_LOCATOR_BY_PARENT_APP_CONTEXT) {
			result = BEAN_FACTORY_LOCATOR_BY_PARENT_APP_CONTEXT
					.get(localParentContextJndiName);
			if (result == null) {
				result = new JndiStoredParentContextAwareBeanFactoryLocator(
						localParentContextJndiName);
				BEAN_FACTORY_LOCATOR_BY_PARENT_APP_CONTEXT.put(
						localParentContextJndiName, result);
			}
		}

		return result;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param parentContextJndiName
	 */
	protected JndiStoredParentContextAwareBeanFactoryLocator(
			final String parentContextJndiName) throws IllegalArgumentException {
		Validate.notEmpty(parentContextJndiName, "parentContextJndiName");
		this.parentContextJndiName = parentContextJndiName;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @param environment
	 */
	public void setJndiEnvironment(final Properties environment) {
		this.jndiEnvironment = environment;
	}

	// -------------------------------------------------------------------------
	// org.springframework.beans.factory.access.BeanFactoryLocator
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.beans.factory.access.BeanFactoryLocator#useBeanFactory(java.lang.String)
	 */
	public BeanFactoryReference useBeanFactory(final String resourceLocation)
			throws IllegalArgumentException, BeansException {
		Validate.notEmpty(resourceLocation, "resourceLocation");
		BeanFactoryReference result;
		getLog()
				.debug(
						"Attempting to create/look up ApplicationContext defined at [{}] using parent ApplicationContext stored in JNDI under [{}] ...",
						resourceLocation, getParentContextJndiName());
		synchronized (this.applicationContextRefsByResourceLocation) {
			result = this.applicationContextRefsByResourceLocation
					.get(resourceLocation);
			if (result == null) {
				result = createNewBeanFactoryReferenceFrom(resourceLocation);
				this.applicationContextRefsByResourceLocation.put(
						resourceLocation, result);
			}
		}
		getLog()
				.debug(
						"Successfully created/looked up ApplicationContext defined at [{}] using parent ApplicationContext stored in JNDI under [{}] ...",
						resourceLocation, getParentContextJndiName());

		return result;
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	protected String getParentContextJndiName() {
		return this.parentContextJndiName;
	}

	protected BeanFactoryReference createNewBeanFactoryReferenceFrom(
			final String resourceLocation) throws BeansException {
		getLog()
				.debug(
						"Creating new ApplicationContext using resource location [{}] and parent ApplicationContext stored in JNDI unser [{}] ...",
						resourceLocation, getParentContextJndiName());
		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { resourceLocation }, true,
				lookupParentApplicationContextInJndi());
		getLog()
				.debug(
						"Successfully created new ApplicationContext [ID = {}|displayName = {}] from resource location [{}] using parent ApplicationContext stored in JNDI under [{}]",
						new Object[] { applicationContext.getId(),
								applicationContext.getDisplayName(),
								resourceLocation, getParentContextJndiName() });

		return new ContextBeanFactoryReference(applicationContext);
	}

	protected final ApplicationContext lookupParentApplicationContextInJndi()
			throws BootstrapException {
		try {
			getLog()
					.debug(
							"Attempting to look up parent ApplicationContext using JNDI name [{}] ...",
							getParentContextJndiName());
			final InitialContext ic = new InitialContext(getJndiEnvironment());
			final Object parentApplicationContextCandidate = ic
					.lookup(getParentContextJndiName());
			if (!(parentApplicationContextCandidate instanceof ApplicationContext)) {
				final String error = "Resource stored in JNDI under ["
						+ getParentContextJndiName()
						+ "] is not an ApplicationContext but of type ["
						+ parentApplicationContextCandidate.getClass()
								.getName() + "]";
				getLog().error(error);

				throw new BootstrapException(error);
			}
			final ApplicationContext parentApplicationContext = (ApplicationContext) parentApplicationContextCandidate;
			getLog()
					.debug(
							"Successfully looked up parent ApplicationContext [ID = {}|displayName = {}] using JNDI name [{}]",
							new Object[] { parentApplicationContext.getId(),
									parentApplicationContext.getDisplayName(),
									getParentContextJndiName() });

			return parentApplicationContext;
		} catch (final NamingException e) {
			final String error = "Failed to look up parent ApplicationContext in JNDI using ["
					+ getParentContextJndiName() + "]: " + e.getMessage();
			getLog().error(error, e);

			throw new BootstrapException(error, e);
		}
	}

	protected Logger getLog() {
		return this.log;
	}

	protected Properties getJndiEnvironment() {
		return this.jndiEnvironment;
	}
}
