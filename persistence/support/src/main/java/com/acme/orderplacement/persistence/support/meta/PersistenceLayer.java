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
 * comprehensively describe this application's <strong>Persistence
 * Layer</strong>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Aspect
public final class PersistenceLayer {

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	@Pointcut("within(com.acme.orderplacement.persistence..*)")
	public void inPersistenceLayer() {
		// Intentionally left blank
	}

	/**
	 * All persistence operations that do <strong>not</strong> modify persistent
	 * state in our database.
	 */
	@Pointcut("@annotation(com.acme.orderplacement.persistence.support.meta.annotation.ReadOnlyPersistenceOperation)")
	public void readOnlyPersistenceOperations() {
		// Intentionally left blank
	}

	/**
	 * All persistence operations that <strong>do modify</strong> persistent
	 * state in our database.
	 */
	@Pointcut("@annotation(com.acme.orderplacement.persistence.support.meta.annotation.StateModifyingPersistenceOperation)")
	public void stateModifyingPersistenceOperations() {
		// Intentionally left blank
	}

	/**
	 * All persistence operations.
	 */
	@Pointcut("readOnlyPersistenceOperations() || stateModifyingPersistenceOperations()")
	public void persistenceOperations() {
		// Intentionally left blank
	}

	/**
	 * All <strong>read-only</strong> persistence operations that return a
	 * {@link List <code>List</code>} of <tt>Persistent Object</tt>s.
	 */
	@Pointcut("readOnlyPersistenceOperations() && execution(public java.util.List *(..))")
	public void persistenceOperationsReturningLists() {
		// Intentionally left blank
	}

	/**
	 * All {@link GenericJpaDao#findById(java.io.Serializable, boolean)
	 * <code>GenericJpaDao#findById(java.io.Serializable, boolean)</code>}
	 * method executions.
	 */
	@Pointcut("readOnlyPersistenceOperations() && execution(public Object findById(..))")
	public void findByIdOperations() {
		// Intentionally left blank
	}

	/**
	 * All {@link GenericJpaDao#makePersistent(Object)
	 * <code>GenericJpaDao#makePersistent(Object)</code>} method executions.
	 */
	@Pointcut("stateModifyingPersistenceOperations() && execution(public Object makePersistent(..))")
	public void makePersistentOperations() {
		// Intentionally left blank
	}

	/**
	 * All {@link GenericJpaDao#makePersistentOrUpdatePersistentState(Object)
	 * <code>GenericJpaDao#makePersistentOrUpdatePersistentState(Object)</code>}
	 * method executions.
	 */
	@Pointcut("stateModifyingPersistenceOperations() && execution(public Object makePersistentOrUpdatePersistentState(..))")
	public void makePersistentOrUpdatePersistentStateOperations() {
		// Intentionally left blank
	}

	/**
	 * All {@link GenericJpaDao#makeTransient(Object)
	 * <code>GenericJpaDao#makeTransient(Object)</code>} method executions.
	 */
	@Pointcut("stateModifyingPersistenceOperations() && execution(public Object makeTransient(..))")
	public void makeTransientOperations() {
		// Intentionally left blank
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
	public void persistentStateModifyingOperations(
			@SuppressWarnings("unused") final Object object) {
		// Intentionally left blank
	}

	/**
	 * All method executions that look up one or more
	 * <tt>Persistent Objects</tt> without modifying any
	 * <tt>Persistent State</tt>.
	 */
	@Pointcut("persistenceOperationsReturningLists() || findByIdOperations()")
	public void readOnlyQueryOperations() {
		// Intentionally left blank
	}

	/**
	 * All transactional persistence operations.
	 */
	@Pointcut("persistenceOperations() && (@annotation(org.springframework.transaction.annotation.Transactional) "
			+ "|| @annotation(javax.ejb.TransactionAttribute))")
	public void transactionalDataAccessOperations() {
		// Intentionally left blank
	}
}
