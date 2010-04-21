/**
 * 
 */
package com.acme.orderplacement.persistence.support.jpa.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.sql.DataSource;

import org.hibernate.ejb.event.EJB3PostDeleteEventListener;
import org.hibernate.ejb.event.EJB3PostInsertEventListener;
import org.hibernate.ejb.event.EJB3PostUpdateEventListener;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.acme.orderplacement.persistence.testsupport.database.spring.PrePopulatingInMemoryH2DataSourceFactory;

/**
 * <p>
 * Tests for {@link ConfigurableHibernatePersistenceProviderBean
 * <code>ConfigurableHibernatePersistenceProviderBean</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConfigurableHibernatePersistenceProviderBeanTest extends
		ConfigurableHibernatePersistenceProviderBean {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private static final Long TEST_ENTITY_ID = Long.valueOf(0L);

	private static final String DATA_LOCATION = "testdata/h2/supportDB-populate.h2.dml";

	private static final String SCHEMA_LOCATION = "testdata/h2/supportDB-create.h2.ddl";

	private static final String TESTDB_NAME = "TESTDB";

	private static final String PU_NAME = "persistence.support.SupportPU";

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@BeforeClass
	public static final void initialize() throws Exception {
		initializeTestDatabase();
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostUpdateEventListeners(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setAdditionalPostUpdateEventListenersShouldRejectNullListenerList() {
		setAdditionalPostUpdateEventListeners((PostUpdateEventListener[]) null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostUpdateEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostUpdateEventListenersShouldAddListenerToListenerList() {
		final int listenerListSizeBefore = getEjb3Configuration()
				.getEventListeners().getPostUpdateEventListeners().length;
		setAdditionalPostUpdateEventListeners(new EJB3PostUpdateEventListener());
		final int listenerListSizeAfter = getEjb3Configuration()
				.getEventListeners().getPostUpdateEventListeners().length;
		assertEquals(
				"setAdditionalPostUpdateEventListeners(new EJB3PostUpdateEventListener()) "
						+ "did not increase size of listener list",
				listenerListSizeBefore + 1, listenerListSizeAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostUpdateEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostUpdateEventListenersShouldAddListenerToEndOfListenerList() {
		final EJB3PostUpdateEventListener addedListener = new EJB3PostUpdateEventListener();
		setAdditionalPostUpdateEventListeners(addedListener);
		final PostUpdateEventListener[] listenersAfter = getEjb3Configuration()
				.getEventListeners().getPostUpdateEventListeners();
		assertEquals(
				"setAdditionalPostUpdateEventListeners(new EJB3PostUpdateEventListener()) "
						+ "did not increase size of listener list",
				addedListener, listenersAfter[listenersAfter.length - 1]);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostInsertEventListeners(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setAdditionalPostInsertEventListenersShouldRejectNullListenerList() {
		setAdditionalPostInsertEventListeners((PostInsertEventListener[]) null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostInsertEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostInsertEventListenersShouldAddListenerToListenerList() {
		final int listenerListSizeBefore = getEjb3Configuration()
				.getEventListeners().getPostInsertEventListeners().length;
		setAdditionalPostInsertEventListeners(new EJB3PostInsertEventListener());
		final int listenerListSizeAfter = getEjb3Configuration()
				.getEventListeners().getPostInsertEventListeners().length;
		assertEquals(
				"setAdditionalPostInsertEventListeners(new EJB3PostInsertEventListener()) "
						+ "did not increase size of listener list",
				listenerListSizeBefore + 1, listenerListSizeAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostInsertEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostInsertEventListenersShouldAddListenerToEndOfListenerList() {
		final EJB3PostInsertEventListener addedListener = new EJB3PostInsertEventListener();
		setAdditionalPostInsertEventListeners(addedListener);
		final PostInsertEventListener[] listenersAfter = getEjb3Configuration()
				.getEventListeners().getPostInsertEventListeners();
		assertEquals(
				"setAdditionalPostInsertEventListeners(new EJB3PostInsertEventListener()) "
						+ "did not increase size of listener list",
				addedListener, listenersAfter[listenersAfter.length - 1]);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostDeleteEventListeners(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setAdditionalPostDeleteEventListenersShouldRejectNullListenerList() {
		setAdditionalPostDeleteEventListeners((PostDeleteEventListener[]) null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostDeleteEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostDeleteEventListenersShouldAddListenerToListenerList() {
		final int listenerListSizeBefore = getEjb3Configuration()
				.getEventListeners().getPostDeleteEventListeners().length;
		setAdditionalPostDeleteEventListeners(new EJB3PostDeleteEventListener());
		final int listenerListSizeAfter = getEjb3Configuration()
				.getEventListeners().getPostDeleteEventListeners().length;
		assertEquals(
				"setAdditionalPostDeleteEventListeners(new EJB3PostDeleteEventListener()) "
						+ "did not increase size of listener list",
				listenerListSizeBefore + 1, listenerListSizeAfter);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostDeleteEventListeners(java.util.List)}
	 * .
	 */
	@Test
	public final void setAdditionalPostDeleteEventListenersShouldAddListenerToEndOfListenerList() {
		final EJB3PostDeleteEventListener addedListener = new EJB3PostDeleteEventListener();
		setAdditionalPostDeleteEventListeners(addedListener);
		final PostDeleteEventListener[] listenersAfter = getEjb3Configuration()
				.getEventListeners().getPostDeleteEventListeners();
		assertEquals(
				"setAdditionalPostDeleteEventListeners(new EJB3PostDeleteEventListener()) "
						+ "did not increase size of listener list",
				addedListener, listenersAfter[listenersAfter.length - 1]);
	}

	/**
	 * 
	 */
	@Test
	public void createEntityManagerFactoryShouldSuccessfullyCreateEntityManagerFactory() {
		final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(
				PU_NAME, Collections.emptyMap());

		assertNotNull(
				"createEntityManagerFactory(\"persistence.support.SupportPU\", Collections.emptyMap()) returned <null>",
				entityManagerFactory);

		entityManagerFactory.close();
	}

	/**
	 * 
	 */
	@Test
	public void createdEntityManagerFactoryShouldPropagatePostUpdateEvent() {
		final Queue<PostUpdateEvent> eventQueue = new LinkedList<PostUpdateEvent>();
		final PostUpdateEventListener eventListener = new PostUpdateEventListener() {

			private static final long serialVersionUID = 1L;

			public void onPostUpdate(final PostUpdateEvent event) {
				eventQueue.offer(event);
			}
		};
		setAdditionalPostUpdateEventListeners(eventListener);

		final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(
				PU_NAME, Collections.emptyMap());
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		final SampleEntity te = entityManager.find(SampleEntity.class,
				TEST_ENTITY_ID);
		te.setName("NEW_NAME");

		transaction.commit();

		entityManager.close();
		entityManagerFactory.close();

		assertEquals("Created EntityManagerFactory [" + entityManagerFactory
				+ "] did not correctly propagate PostUpdateEvent", 1,
				eventQueue.size());

		entityManagerFactory.close();
	}

	/**
	 * 
	 */
	@Test
	public void createdEntityManagerFactoryShouldPropagatePostInsertEvent() {
		final Queue<PostInsertEvent> eventQueue = new LinkedList<PostInsertEvent>();
		final PostInsertEventListener eventListener = new PostInsertEventListener() {

			private static final long serialVersionUID = 1L;

			public void onPostInsert(final PostInsertEvent event) {
				eventQueue.offer(event);
			}
		};
		setAdditionalPostInsertEventListeners(eventListener);

		final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(
				PU_NAME, Collections.emptyMap());
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		final SampleEntity te = new SampleEntity();
		te.setName("NEW");
		entityManager.persist(te);

		transaction.commit();

		entityManager.close();
		entityManagerFactory.close();

		assertEquals("Created EntityManagerFactory [" + entityManagerFactory
				+ "] did not correctly propagate PostInsertEvent", 1,
				eventQueue.size());

		entityManagerFactory.close();
	}

	/**
	 * 
	 */
	@Test
	public void createdEntityManagerFactoryShouldPropagatePostDeleteEvent() {
		final Queue<PostDeleteEvent> eventQueue = new LinkedList<PostDeleteEvent>();
		final PostDeleteEventListener eventListener = new PostDeleteEventListener() {

			private static final long serialVersionUID = 1L;

			public void onPostDelete(final PostDeleteEvent event) {
				eventQueue.offer(event);
			}
		};
		setAdditionalPostDeleteEventListeners(eventListener);

		final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(
				PU_NAME, Collections.emptyMap());
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		final SampleEntity te = entityManager.find(SampleEntity.class,
				TEST_ENTITY_ID);
		entityManager.remove(te);

		transaction.commit();

		entityManager.close();
		entityManagerFactory.close();

		assertEquals("Created EntityManagerFactory [" + entityManagerFactory
				+ "] did not correctly propagate PostDeleteEvent", 1,
				eventQueue.size());

		entityManagerFactory.close();
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private static DataSource initializeTestDatabase() throws Exception {
		final PrePopulatingInMemoryH2DataSourceFactory dataSourceFactory = new PrePopulatingInMemoryH2DataSourceFactory();
		dataSourceFactory.setDatabaseName(TESTDB_NAME);
		dataSourceFactory.setSchemaLocation(new ClassPathResource(
				SCHEMA_LOCATION));
		dataSourceFactory.setDataLocation(new ClassPathResource(DATA_LOCATION));

		return (DataSource) dataSourceFactory.getObject();
	}
}
