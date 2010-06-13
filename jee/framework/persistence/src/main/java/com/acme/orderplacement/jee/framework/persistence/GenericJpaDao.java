/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence;

import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.acme.orderplacement.framework.common.role.ApplicationUserRole;

/**
 * <p>
 * TODO: Insert short summary for class GenericJpaDao
 * </p>
 * <p>
 * TODO: Insert comprehensive summary for class GenericJpaDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 * @param <T>
 * @param <ID>
 */
@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
		ApplicationUserRole.ROLE_EMPLOYEE, ApplicationUserRole.ROLE_ACCOUNTANT,
		ApplicationUserRole.ROLE_ADMIN })
public interface GenericJpaDao<T, ID extends Serializable> {

	/**
	 * @param id
	 * @param lock
	 * 
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>id</code> is <code>null</code>
	 * @throws EntityNotFoundException
	 *             If not entity having the specified <code>id</code> could be
	 *             found
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T findById(ID id, boolean lock) throws IllegalArgumentException,
			EntityNotFoundException, PersistenceException;

	/**
	 * @return
	 * 
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EXTERNAL_USER,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	List<T> findAll() throws PersistenceException;

	/**
	 * @param transientObject
	 * 
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>transientObject</code> is <code>null</code>
	 * @throws TransactionRequiredException
	 *             If the concrete implementation requires a transaction and
	 *             none is associated with the current thread
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T makePersistent(T transientObject) throws IllegalArgumentException,
			TransactionRequiredException, PersistenceException;

	/**
	 * @param object
	 * 
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>object</code> is <code>null</code>
	 * @throws TransactionRequiredException
	 *             If the concrete implementation requires a transaction and
	 *             none is associated with the current thread
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T makePersistentOrUpdatePersistentState(T object)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException;

	/**
	 * @param persistentOrDetachedObject
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>persistentOrDetachedObject</code> is
	 *             <code>null</code>
	 * @throws TransactionRequiredException
	 *             If the concrete implementation requires a transaction and
	 *             none is associated with the current thread
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ACCOUNTANT,
			ApplicationUserRole.ROLE_ADMIN })
	void makeTransient(T persistentOrDetachedObject)
			throws IllegalArgumentException, TransactionRequiredException,
			PersistenceException;

	/**
	 * @throws TransactionRequiredException
	 *             If the concrete implementation requires a transaction and
	 *             none is associated with the current thread
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ADMIN })
	void flush() throws TransactionRequiredException, PersistenceException;

	/**
	 * @param persistentObject
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>persistentObject</code> is not persistent, i.e. not
	 *             associated with the current <code>Session</code>, or if is
	 *             <code>null</code>
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ADMIN })
	void evict(T persistentObject) throws IllegalArgumentException;
}
