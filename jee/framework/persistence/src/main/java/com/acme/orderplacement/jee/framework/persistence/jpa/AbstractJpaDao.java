/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.common.role.ApplicationUserRole;
import com.acme.orderplacement.jee.framework.persistence.GenericJpaDao;
import com.acme.orderplacement.jee.framework.persistence.meta.annotation.ReadOnlyPersistenceOperation;
import com.acme.orderplacement.jee.framework.persistence.meta.annotation.StateModifyingPersistenceOperation;

/**
 * <p>
 * TODO: Insert short summary for AbstractJpaDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for AbstractJpaDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@DeclareRoles( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EXTERNAL_USER,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
public abstract class AbstractJpaDao<T, ID extends Serializable> implements
		GenericJpaDao<T, ID> {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	// @Inject
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * The {@link Class <code>Class</code>} of the entity we are handling.
	 */
	private final Class<T> persistentClass = (Class<T>) ((ParameterizedType) this
			.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	// ------------------------------------------------------------------------
	// Implementation of GenericJpaDao<T, ID>
	// ------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#evict(java.lang.Object)
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	public void evict(final T persistentObject) throws IllegalArgumentException {
		Validate.notNull(persistentObject, "persistentObject");

		entityManager().detach(persistentObject);
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findAll()
	 */
	@ReadOnlyPersistenceOperation
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<T> findAll() throws PersistenceException {
		final List<T> allEntities = entityManager().createQuery(
				"From " + getPersistentClass().getName(), getPersistentClass())
				.getResultList();
		getLog().debug("Returned all ({}) entities of type = [{}].",
				Integer.valueOf(allEntities.size()),
				getPersistentClass().getName());

		return Collections.<T> unmodifiableList(allEntities);
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#findById(java.io.Serializable,
	 *      boolean)
	 */
	@ReadOnlyPersistenceOperation
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public T findById(final ID id, final boolean lock)
			throws IllegalArgumentException, EntityNotFoundException,
			PersistenceException {
		Validate.notNull(id, "id");

		final T matchingEntity = entityManager().find(getPersistentClass(), id);
		if (matchingEntity == null) {
			getLog().error("Could not find entity of type [{}] with ID [{}]",
					getPersistentClass(), id);

			throw new EntityNotFoundException("No entity of type ["
					+ getPersistentClass().getName() + "] having ID = [" + id
					+ "] could be found");
		}
		if (lock) {
			entityManager().lock(matchingEntity, LockModeType.WRITE);
		}
		getLog().debug("Found entity = [{}] with ID = [{}].", matchingEntity,
				id);

		return matchingEntity;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#flush()
	 * 
	 *      TODO: Does this method need to be marked @Transactional?
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	public void flush() throws TransactionRequiredException,
			PersistenceException {
		entityManager().flush();
		getLog()
				.debug(
						"Flushed current Session. "
								+ "Changes have been written to the DB's transaction log, but not yet committed.");
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistent(java.lang.Object)
	 */
	@StateModifyingPersistenceOperation(idempotent = false)
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public T makePersistent(final T transientObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		Validate.notNull(transientObject, "transientObject");

		entityManager().persist(transientObject);
		getLog().debug("Saved entity = [{}].", transientObject);

		return transientObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makePersistentOrUpdatePersistentState(java.lang.Object)
	 */
	@StateModifyingPersistenceOperation(idempotent = false)
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public T makePersistentOrUpdatePersistentState(
			final T persistentOrDetachedObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		Validate.notNull(persistentOrDetachedObject,
				"persistentOrDetachedObject");

		final T persistentObject = entityManager().merge(
				persistentOrDetachedObject);
		getLog().debug("Updated entity = [{}]", persistentOrDetachedObject);

		return persistentObject;
	}

	/**
	 * @see com.acme.orderplacement.jee.framework.persistence.GenericJpaDao#makeTransient(java.lang.Object)
	 */
	@StateModifyingPersistenceOperation(idempotent = true)
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void makeTransient(final T persistentOrDetachedObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		Validate.notNull(persistentOrDetachedObject,
				"persistentOrDetachedObject");

		final T managedEntity;
		if (!entityManager().contains(persistentOrDetachedObject)) {
			managedEntity = entityManager().merge(persistentOrDetachedObject);
		} else {
			managedEntity = persistentOrDetachedObject;
		}
		entityManager().remove(managedEntity);
		getLog().debug("Removed entity = [{}] from persistent storage.",
				managedEntity);
	}

	// ------------------------------------------------------------------------
	// Protected
	// ------------------------------------------------------------------------

	/**
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	protected List<T> findByNamedQuery(final String queryName,
			final Map<String, ?> parameters) throws IllegalArgumentException,
			IllegalStateException, QueryTimeoutException,
			TransactionRequiredException, PessimisticLockException,
			LockTimeoutException, PersistenceException {
		Validate.notNull(queryName, "queryName");
		final TypedQuery<T> namedQuery = entityManager().createNamedQuery(
				queryName, getPersistentClass());
		for (final Map.Entry<String, ?> param : parameters.entrySet()) {
			namedQuery.setParameter(param.getKey(), param.getValue());
		}

		return Collections.<T> unmodifiableList(namedQuery.getResultList());
	}

	/**
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	protected List<T> findByNamedQuery(final String queryName,
			final Object... parameters) throws IllegalArgumentException,
			IllegalStateException, QueryTimeoutException,
			TransactionRequiredException, PessimisticLockException,
			LockTimeoutException, PersistenceException {
		Validate.notNull(queryName, "queryName");
		final TypedQuery<T> namedQuery = entityManager().createNamedQuery(
				queryName, getPersistentClass());
		int idx = 1;
		for (final Object param : parameters) {
			namedQuery.setParameter(idx++, param);
		}

		return Collections.<T> unmodifiableList(namedQuery.getResultList());
	}

	/**
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	protected T findUniqueByNamedQuery(final String queryName,
			final Map<String, ?> parameters) throws IllegalArgumentException,
			IllegalStateException, NonUniqueResultException,
			QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException,
			PersistenceException {
		try {
			Validate.notNull(queryName, "queryName");

			final TypedQuery<T> namedQuery = entityManager().createNamedQuery(
					queryName, getPersistentClass());
			for (final Map.Entry<String, ?> param : parameters.entrySet()) {
				namedQuery.setParameter(param.getKey(), param.getValue());
			}

			return namedQuery.getSingleResult();
		} catch (final NoResultException e) {

			return null;
		}
	}

	/**
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	protected T findUniqueByNamedQuery(final String queryName,
			final Object... parameters) throws IllegalArgumentException,
			IllegalStateException, NonUniqueResultException,
			QueryTimeoutException, TransactionRequiredException,
			PessimisticLockException, LockTimeoutException,
			PersistenceException {
		try {
			Validate.notNull(queryName, "queryName");

			final TypedQuery<T> namedQuery = entityManager().createNamedQuery(
					queryName, getPersistentClass());
			int idx = 1;
			for (final Object param : parameters) {
				namedQuery.setParameter(idx++, param);
			}

			return namedQuery.getSingleResult();
		} catch (final NoResultException e) {

			return null;
		}
	}

	protected Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	protected final Logger getLog() {

		return this.log;
	}

	protected abstract EntityManager entityManager();
}
