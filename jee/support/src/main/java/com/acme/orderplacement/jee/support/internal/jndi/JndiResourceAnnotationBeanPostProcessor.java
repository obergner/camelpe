/**
 * 
 */
package com.acme.orderplacement.jee.support.internal.jndi;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Context;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jndi.JndiCallback;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.jee.support.annotation.JndiResource;
import com.acme.orderplacement.jee.support.jndi.NestedJndiBinding;

/**
 * <p>
 * A {@link BeanPostProcessor <tt>Spring BeanPostProcessor</tt>} for registering
 * all <code>beans</code> annotated with {@link JndiResource
 * <code>JndiResource</code>} in <code>JNDI</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(JndiResourceAnnotationBeanPostProcessor.COMPONENT_NAME)
public class JndiResourceAnnotationBeanPostProcessor implements
		BeanPostProcessor {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "jee.support.spring.JndiResourceAnnotationBPP";

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private Properties environment = null;

	private JndiTemplate jndiTemplate;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	public JndiResourceAnnotationBeanPostProcessor() {
		// Empty
	}

	/**
	 * @param environment
	 */
	public JndiResourceAnnotationBeanPostProcessor(final Properties environment) {
		this.environment = environment;
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the environment
	 */
	public Properties getEnvironment() {
		return this.environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(final Properties environment) {
		this.environment = environment;
	}

	// -------------------------------------------------------------------------
	// Lifecycle
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		this.jndiTemplate = this.environment != null ? new JndiTemplate(
				this.environment) : new JndiTemplate();
		this.log
				.info(
						"JndiResourceAnnotationBeanPostProcessor has been registered. "
								+ "Beans annotated with [{}] will be automatically registered in JNDI.",
						JndiResource.class.getName());
	}

	/**
	 * 
	 */
	@PreDestroy
	public void destroy() {
		this.log
				.info("JndiResourceAnnotationBeanPostProcessor has been unregistered. "
						+ "Beans annotated with [{}] will NOT be automatically registered in JNDI.");
	}

	// -------------------------------------------------------------------------
	// org.springframework.beans.factory.config.BeanPostProcessor
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#
	 *      postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) throws BeansException {
		try {
			final JndiResource jndiResource = bean.getClass().getAnnotation(
					JndiResource.class);
			if (jndiResource == null) {
				this.log
						.trace(
								"No [{}] annotation found on bean [name = {} | bean = {}]. Bean will NOT be bound into JNDI",
								new Object[] { JndiResource.class.getName(),
										beanName, bean });
				return bean;
			}
			final String jndiName = jndiResource.jndiName();
			this.log
					.trace(
							"Annotation [{}] found on bean [name = {} | bean = {}]. Bean will be bound into JNDI [jndiName = {}]",
							new Object[] { jndiResource, beanName, bean,
									jndiName });
			this.jndiTemplate.execute(new JndiRegistration(jndiName, bean));
			this.log
					.debug(
							"Bean [name = {} | bean = {}] has been bound into JNDI [jndiName = {}]",
							new Object[] { beanName, bean, jndiName });

			return bean;
		} catch (final NamingException e) {
			final String error = String
					.format(
							"Failed to bind bean [name = %1$s | bean = %2$s] into JNDI: %3$s",
							beanName, bean.toString(), e.getMessage());
			this.log.error(error, e);

			throw new BeanInitializationException(error, e);
		}
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#
	 *      postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessBeforeInitialization(final Object bean,
			final String beanName) throws BeansException {
		// Do nothing
		return bean;
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private final class JndiRegistration implements JndiCallback {

		private final String jndiName;

		private final Object bean;

		/**
		 * @param jndiName
		 */
		JndiRegistration(final String jndiName, final Object bean) {
			this.bean = bean;
			this.jndiName = jndiName;
		}

		/**
		 * @see org.springframework.jndi.JndiCallback#doInContext(javax.naming.Context)
		 */
		public Object doInContext(final Context initialCtx)
				throws NamingException {
			new NestedJndiBinding(this.jndiName, this.bean, initialCtx).bind();

			return this.bean;
		}
	}
}
