/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

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
	T findById(ID id, boolean lock) throws IllegalArgumentException,
			EntityNotFoundException, PersistenceException;

	/**
	 * @return
	 * 
	 * @throws PersistenceException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
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
	void flush() throws TransactionRequiredException, PersistenceException;

	/**
	 * @param persistentObject
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>persistentObject</code> is not persistent, i.e. not
	 *             associated with the current <code>Session</code>, or if is
	 *             <code>null</code>
	 */
	void evict(T persistentObject) throws IllegalArgumentException;
}
