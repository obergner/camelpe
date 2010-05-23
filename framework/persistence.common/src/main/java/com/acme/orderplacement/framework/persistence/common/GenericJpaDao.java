/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common;

import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.acme.orderplacement.framework.common.role.ApplicationUserRole;
import com.acme.orderplacement.framework.persistence.common.exception.DataAccessRuntimeException;
import com.acme.orderplacement.framework.persistence.common.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.framework.persistence.common.exception.ObjectNotPersistentException;
import com.acme.orderplacement.framework.persistence.common.exception.ObjectNotTransientException;
import com.acme.orderplacement.framework.persistence.common.exception.ObjectTransientException;
import com.acme.orderplacement.framework.persistence.common.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.framework.persistence.common.exception.PersistentStateDeletedException;
import com.acme.orderplacement.framework.persistence.common.exception.PersistentStateLockedException;

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
	 * @throws NoSuchPersistentObjectException
	 *             If no entity with the ID <code>id</code> could be found in
	 *             the underlying datastore
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T findById(ID id, boolean lock) throws NoSuchPersistentObjectException,
			DataAccessRuntimeException;

	/**
	 * @return
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_GUEST,
			ApplicationUserRole.ROLE_EXTERNAL_USER,
			ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	List<T> findAll() throws DataAccessRuntimeException;

	/**
	 * @param transientObject
	 * 
	 * @return
	 * 
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 * @throws ObjectNotTransientException
	 *             If <code>transientObject</code> is not transient, i.e.
	 *             already has a persistent representation in the underlying
	 *             datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T makePersistent(T transientObject) throws DataAccessRuntimeException,
			ObjectNotTransientException;

	/**
	 * @param object
	 * 
	 * @return
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_EMPLOYEE,
			ApplicationUserRole.ROLE_ACCOUNTANT, ApplicationUserRole.ROLE_ADMIN })
	T makePersistentOrUpdatePersistentState(T object)
			throws DataAccessRuntimeException;

	/**
	 * @param persistentOrDetachedObject
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 * @throws ObjectTransientException
	 *             If <code>persistentOrDetachedObject</code> is neither
	 *             <tt>persistent</tt> nor <tt>detached</tt>, i.e. has no
	 *             <tt>Persistent State</tt> in the underlying datastore
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ACCOUNTANT,
			ApplicationUserRole.ROLE_ADMIN })
	void makeTransient(T persistentOrDetachedObject)
			throws DataAccessRuntimeException, ObjectTransientException;

	/**
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 * @throws PersistentStateLockedException
	 *             If the <tt>Persistent State</tt> of some
	 *             <tt>Persistent Object</tt> associated with this <tt>DAO</tt>
	 *             is currently locked by another thread/process
	 * @throws PersistentStateConcurrentlyModifiedException
	 *             If the <tt>Persistent State</tt> of some
	 *             <tt>Persistent Object</tt> associated with this <tt>DAO</tt>
	 *             has concurrently been modified by another thread/process
	 * @throws PersistentStateDeletedException
	 *             If the <tt>Persistent State</tt> of some
	 *             <tt>Persistent Object</tt> associated with this <tt>DAO</tt>
	 *             has been deleted another thread/process
	 * 
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ADMIN })
	void flush() throws DataAccessRuntimeException,
			PersistentStateLockedException,
			PersistentStateConcurrentlyModifiedException,
			PersistentStateDeletedException;

	/**
	 * @param persistentObject
	 * 
	 * @throws DataAccessRuntimeException
	 *             If an unexpected technical error outside of the client's
	 *             control occurs while accessing the underlying datastore
	 * @throws ObjectNotPersistentException
	 *             If <code>persistentObject</code> is not peristent, i.e. not
	 *             associated with the current <code>Session</code>
	 */
	@RolesAllowed( { ApplicationUserRole.ROLE_ADMIN })
	void evict(T persistentObject) throws DataAccessRuntimeException,
			ObjectNotPersistentException;
}
