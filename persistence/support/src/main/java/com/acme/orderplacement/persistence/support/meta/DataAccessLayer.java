/**
 * 
 */
package com.acme.orderplacement.persistence.support.meta;

import java.lang.annotation.Annotation;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.acme.orderplacement.persistence.support.GenericJpaDao;

/**
 * <p>
 * A class defining {@link org.aspectj.lang.reflect.Pointcut
 * <code>Pointcut</code>}s using <code>AspectJ</code>s {@link Pointcut
 * <code>@Pointcut</code>} {@link Annotation <code>Annotation</code>}s that
 * comprehensively describe this application's <strong>Data Access
 * Layer</strong>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Aspect
public final class DataAccessLayer {

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * All methods declared on an instance of type {@link GenericJpaDao
	 * <code>GenericJpaDao</code>}.
	 */
	@Pointcut("target(com.acme.orderplacement.persistence.support.GenericJpaDao)")
	public void dataAccessOperations() {

	}

	/**
	 * All method executions (read-only) that return a {@link List
	 * <code>List</code>} of <tt>Persistent Object</tt>s declared on an instance
	 * of type {@link GenericJpaDao <code>GenericJpaDao</code>}.
	 */
	@Pointcut("dataAccessOperations() && execution(public java.util.List *(..))")
	public void dataAccessOperationsReturningLists() {
	}

	/**
	 * All {@link GenericJpaDao#findById(java.io.Serializable, boolean)
	 * <code>GenericJpaDao#findById(java.io.Serializable, boolean)</code>}
	 * method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public Object findById(..))")
	public void findByIdOperations() {
	}

	/**
	 * All {@link GenericJpaDao#makePersistent(Object)
	 * <code>GenericJpaDao#makePersistent(Object)</code>} method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public Object makePersistent(..))")
	public void makePersistentOperations() {
	}

	/**
	 * All {@link GenericJpaDao#makePersistentOrUpdatePersistentState(Object)
	 * <code>GenericJpaDao#makePersistentOrUpdatePersistentState(Object)</code>}
	 * method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public Object makePersistentOrUpdatePersistentState(..))")
	public void makePersistentOrUpdatePersistentStateOperations() {
	}

	/**
	 * All {@link GenericJpaDao#makeTransient(Object)
	 * <code>GenericJpaDao#makeTransient(Object)</code>} method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public Object makeTransient(..))")
	public void makeTransientOperations() {
	}

	/**
	 * All {@link GenericJpaDao#flush() <code>GenericJpaDao#flush()</code>}
	 * method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public void flush(..))")
	public void flushOperations() {
	}

	/**
	 * All {@link GenericJpaDao#evict(Object)
	 * <code>GenericJpaDao#evict(Object)</code>} method executions.
	 */
	@Pointcut("dataAccessOperations() && execution(public void evict(..))")
	public void evictOperations() {
	}

	/**
	 * All method executions that might potentially alter a <tt>Persistent</tt>,
	 * <tt>Detached</tt> or <tt>Transient Object</tt>'s
	 * <tt>Persistent State</tt>.
	 * 
	 * @param object
	 */
	@Pointcut("(makePersistentOperations() || "
			+ "makePersistentOrUpdatePersistentStateOperations() || "
			+ "makeTransientOperations()) && " + "args(object)")
	public void persistentStateModifyingOperations(final Object object) {
	}

	/**
	 * All method executions that look up one or more
	 * <tt>Persistent Objects</tt> without modifying any
	 * <tt>Persistent State</tt>.
	 */
	@Pointcut("dataAccessOperationsReturningLists() || findByIdOperations()")
	public void readOnlyQueryOperations() {
	}

	/**
	 * All transactional data access method executions.
	 */
	@Pointcut("dataAccessOperations() && @annotation(org.springframework.transaction.annotation.Transactional)")
	public void transactionalDataAccessOperations() {
	}
}
