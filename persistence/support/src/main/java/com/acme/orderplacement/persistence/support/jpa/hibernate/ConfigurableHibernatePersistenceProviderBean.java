/**
 * 
 */
package com.acme.orderplacement.persistence.support.jpa.hibernate;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEventListener;

/**
 * <p>
 * A custom <a href="http://www.hibernate.org"><tt>Hibernate</tt></a> based
 * {@link PersistenceProvider <code>PersistenceProvider</code>} allowing for for
 * fine grained and vendor specific configuration of the underlying
 * <tt>Hibernate</tt> JPA {@link EntityManagerFactory
 * <code>EntityManagerFactory</code>}.
 * </p>
 * <p>
 * <strong>Development Status</strong> In its current form, this class allows
 * only for setting externally created and configured - e.g. by <a
 * href="http://www.springsource.org"><tt>Spring</tt></a> - <tt>Hibernate</tt>
 * specific <tt>Event Listener</tt>s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConfigurableHibernatePersistenceProviderBean extends
		HibernatePersistence implements PersistenceProvider {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * The <tt>Hibernate</tt> {@link Ejb3Configuration
	 * <code>Ejb3Configuration</code>} used to configure the underlying
	 * <code>JPA</code> {@link EntityManagerFactory
	 * <code>EntityManagerFactory</code>}.
	 * </p>
	 */
	private final Ejb3Configuration ejb3Configuration = new Ejb3Configuration();

	// -------------------------------------------------------------------------
	// Public API
	// -------------------------------------------------------------------------

	/**
	 * @param postUpdateEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostUpdateEventListeners(org.hibernate.event.PostUpdateEventListener[])
	 */
	public void setAdditionalPostUpdateEventListeners(
			final List<PostUpdateEventListener> postUpdateEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postUpdateEventListeners, "postUpdateEventListeners");

		setAdditionalPostUpdateEventListeners(postUpdateEventListeners
				.toArray(new PostUpdateEventListener[postUpdateEventListeners
						.size()]));
	}

	/**
	 * @param postUpdateEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostUpdateEventListeners(org.hibernate.event.PostUpdateEventListener[])
	 */
	public void setAdditionalPostUpdateEventListeners(
			final PostUpdateEventListener... postUpdateEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postUpdateEventListeners, "postUpdateEventListeners");
		final PostUpdateEventListener[] extendedListenersArray = (PostUpdateEventListener[]) ArrayUtils
				.addAll(getEjb3Configuration().getEventListeners()
						.getPostUpdateEventListeners(),
						postUpdateEventListeners);
		getEjb3Configuration().getEventListeners().setPostUpdateEventListeners(
				extendedListenersArray);
	}

	/**
	 * @param postInsertEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostInsertEventListeners(org.hibernate.event.PostInsertEventListener[])
	 */
	public void setAdditionalPostInsertEventListeners(
			final List<PostInsertEventListener> postInsertEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postInsertEventListeners, "postInsertEventListeners");

		setAdditionalPostInsertEventListeners(postInsertEventListeners
				.toArray(new PostInsertEventListener[postInsertEventListeners
						.size()]));
	}

	/**
	 * @param postInsertEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostInserEventListeners(org.hibernate.event.PostInsertEventListener[])
	 */
	public void setAdditionalPostInsertEventListeners(
			final PostInsertEventListener... postInsertEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postInsertEventListeners, "postInsertEventListeners");
		final PostInsertEventListener[] extendedListenersArray = (PostInsertEventListener[]) ArrayUtils
				.addAll(getEjb3Configuration().getEventListeners()
						.getPostInsertEventListeners(),
						postInsertEventListeners);
		getEjb3Configuration().getEventListeners().setPostInsertEventListeners(
				extendedListenersArray);
	}

	/**
	 * @param postDeleteEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostDeleteEventListeners(org.hibernate.event.PostDeleteEventListener[])
	 */
	public void setAdditionalPostDeleteEventListeners(
			final List<PostDeleteEventListener> postDeleteEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postDeleteEventListeners, "postDeleteEventListeners");

		setAdditionalPostDeleteEventListeners(postDeleteEventListeners
				.toArray(new PostDeleteEventListener[postDeleteEventListeners
						.size()]));
	}

	/**
	 * @param postDeleteEventListeners
	 * @throws IllegalArgumentException
	 * @see org.hibernate.event.EventListeners#setPostInserEventListeners(org.hibernate.event.PostDeleteEventListener[])
	 */
	public void setAdditionalPostDeleteEventListeners(
			final PostDeleteEventListener... postDeleteEventListeners)
			throws IllegalArgumentException {
		Validate.notNull(postDeleteEventListeners, "postDeleteEventListeners");
		final PostDeleteEventListener[] extendedListenersArray = (PostDeleteEventListener[]) ArrayUtils
				.addAll(getEjb3Configuration().getEventListeners()
						.getPostDeleteEventListeners(),
						postDeleteEventListeners);
		getEjb3Configuration().getEventListeners().setPostDeleteEventListeners(
				extendedListenersArray);
	}

	// -------------------------------------------------------------------------
	// javax.persistence.PersistenceProvider
	// -------------------------------------------------------------------------

	/**
	 * @see org.hibernate.ejb.HibernatePersistence#createEntityManagerFactory(java.lang.String,
	 *      java.util.Map)
	 */
	@Override
	public EntityManagerFactory createEntityManagerFactory(
			final String persistenceUnitName, final Map overridenProperties) {
		final Ejb3Configuration configured = getEjb3Configuration().configure(
				persistenceUnitName, overridenProperties);
		return configured != null ? configured.buildEntityManagerFactory()
				: null;
	}

	/**
	 * @see org.hibernate.ejb.HibernatePersistence#createContainerEntityManagerFactory(javax.persistence.spi.PersistenceUnitInfo,
	 *      java.util.Map)
	 */
	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(
			final PersistenceUnitInfo info, final Map map) {
		final Ejb3Configuration configured = getEjb3Configuration().configure(
				info, map);
		return configured != null ? configured.buildEntityManagerFactory()
				: null;
	}

	/**
	 * @see org.hibernate.ejb.HibernatePersistence#createEntityManagerFactory(java.util.Map)
	 * @deprecated This is used directly by JBoss so don't remove until further
	 *             notice. bill@jboss.org
	 */
	@Override
	@Deprecated
	public EntityManagerFactory createEntityManagerFactory(final Map properties) {
		return getEjb3Configuration().createEntityManagerFactory(properties);
	}

	// -------------------------------------------------------------------------
	// Protected
	// -------------------------------------------------------------------------

	/**
	 * @return the ejb3Configuration
	 */
	protected final Ejb3Configuration getEjb3Configuration() {
		return this.ejb3Configuration;
	}
}
