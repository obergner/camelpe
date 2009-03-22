/**
 * 
 */
package com.acme.orderplacement.persistence.support.aspect;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.ObjectDeletedException;
import org.hibernate.PersistentObjectException;
import org.hibernate.TransientObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Component;

import com.acme.orderplacement.persistence.support.exception.DataAccessRuntimeException;
import com.acme.orderplacement.persistence.support.exception.NoSuchPersistentObjectException;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.persistence.support.exception.ObjectTransientException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateConcurrentlyModifiedException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateDeletedException;
import com.acme.orderplacement.persistence.support.exception.PersistentStateLockedException;

/**
 * <p>
 * An annotation-based <code>Aspect</code> for converting exceptions from <a
 * href="http://www.springframework.org">Spring</a>'s generic
 * {@link DataAccessException <code>DataAccessException</code>} based hierarchy
 * into our own.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@gmx.de">Olaf Bergner</a>
 * 
 */
@Component(SpringPersistenceExceptionConverter.ASPECT_NAME)
@Order(20)
@Aspect
public class SpringPersistenceExceptionConverter {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	public static final String ASPECT_NAME = "persistence.support.aspect.DataAccessExceptionConverter";

	/**
	 * Our faithful logger.
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	// ------------------------------------------------------------------------
	// Pointcuts
	// ------------------------------------------------------------------------

	/**
	 * All methods declared on an instance of type
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao
	 * <code>AbstractJpaDao</code>}.
	 */
	@Pointcut("target(com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao)")
	public void springBasedDaos() {
		// Intentionally left blank
	}

	/**
	 * All method executions declared on an instance of type
	 * {@link com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao
	 * <code>AbstractJpaDao</code>}.
	 */
	@Pointcut("com.acme.orderplacement.persistence.support.meta.PersistenceLayer.persistenceOperations() && springBasedDaos()")
	public void springBasedPersistenceOperations() {
		// Intentionally left blank
	}

	// ------------------------------------------------------------------------
	// Advises
	// ------------------------------------------------------------------------

	/**
	 * @param joinPoint
	 * @param dataAccessException
	 * @return
	 * @throws Exception
	 */
	@AfterThrowing(pointcut = "springBasedPersistenceOperations()", throwing = "dataAccessException")
	public void convertDataAccessException(final JoinPoint joinPoint,
			final DataAccessException dataAccessException) throws Exception {
		Exception convertedException;
		if ((dataAccessException instanceof ObjectOptimisticLockingFailureException)
				&& (ObjectOptimisticLockingFailureException.class.cast(
						dataAccessException).getIdentifier() != null)) {
			final ObjectOptimisticLockingFailureException castedException = ObjectOptimisticLockingFailureException.class
					.cast(dataAccessException);
			final Serializable id = Serializable.class.cast(castedException
					.getIdentifier());
			final String concurrentlyModifiedObjectClassName = castedException
					.getPersistentClassName();

			convertedException = new PersistentStateConcurrentlyModifiedException(
					id, concurrentlyModifiedObjectClassName,
					dataAccessException);
		} else if (dataAccessException instanceof ConcurrencyFailureException) {

			convertedException = new PersistentStateConcurrentlyModifiedException(
					null, null, dataAccessException);
		} else if (dataAccessException instanceof ObjectRetrievalFailureException) {
			final Object[] args = joinPoint.getArgs();
			final Serializable id = Serializable.class.cast(args[0]);
			final MethodSignature methodSignature = MethodSignature.class
					.cast(joinPoint.getSignature());
			final Class<?> returnType = methodSignature.getReturnType();

			convertedException = new NoSuchPersistentObjectException(
					returnType, id, dataAccessException);
		} else if ((dataAccessException instanceof InvalidDataAccessApiUsageException)
				&& (dataAccessException.getCause() != null)
				&& (dataAccessException.getCause() instanceof TransientObjectException)) {
			final Object[] args = joinPoint.getArgs();
			final Object transientObject = args[0];

			convertedException = new ObjectTransientException(transientObject,
					dataAccessException);
		} else if ((dataAccessException instanceof InvalidDataAccessApiUsageException)
				&& (dataAccessException.getCause() != null)
				&& (dataAccessException.getCause() instanceof PersistentObjectException)) {
			final Object[] args = joinPoint.getArgs();
			final Object nonTransientObject = args[0];
			convertedException = new ObjectNotTransientException(
					nonTransientObject, dataAccessException);
		} else if ((dataAccessException instanceof InvalidDataAccessApiUsageException)
				&& (dataAccessException.getCause() != null)
				&& (dataAccessException.getCause() instanceof ObjectDeletedException)) {
			final Object[] args = joinPoint.getArgs();
			final Object deletedObject = args[0];

			convertedException = new PersistentStateDeletedException(
					deletedObject, dataAccessException);
		} else if (dataAccessException instanceof CannotAcquireLockException) {
			final Object[] args = joinPoint.getArgs();
			final Object lockedObject = args[0];

			convertedException = new PersistentStateLockedException(
					lockedObject, dataAccessException);
		} else {
			convertedException = new DataAccessRuntimeException(
					dataAccessException.getMessage(), dataAccessException);
		}

		/*
		 * Check if our converted exception is compatible with the throws clause
		 * of the method we are advising. Otherwise, issue a warning and wrap
		 * the original exception in a DataAccessRuntimeException.
		 */
		if (!(exceptionIsCompatibleWithThrowsClause(convertedException,
				joinPoint))) {
			final String warn = "The DataAccessException [{}]"
					+ " as received from the Persistence Layer has been converted into [{}]. "
					+ "Yet this exception is NOT compatible with the throws clause of the method [{}]"
					+ " we are advising. "
					+ "It will therefore be converted into a plain [{}]";
			this.log.warn(warn, new Object[] { dataAccessException,
					convertedException, joinPoint.getSignature().toString(),
					DataAccessRuntimeException.class.getName() });

			convertedException = new DataAccessRuntimeException(
					dataAccessException.getMessage(), dataAccessException);
		}

		this.log.debug("Exception [{}] converted to [{}]", dataAccessException,
				convertedException);

		throw convertedException;
	}

	// ------------------------------------------------------------------------
	// Internal helper methods
	// ------------------------------------------------------------------------

	/**
	 * @param exceptionToCheck
	 * @param joinPoint
	 * @return
	 */
	private boolean exceptionIsCompatibleWithThrowsClause(
			final Exception exceptionToCheck, final JoinPoint joinPoint) {
		/*
		 * With Spring AOP, the Signature should _always_ be a MethodSignature.
		 */
		final MethodSignature methodSignature = MethodSignature.class
				.cast(joinPoint.getSignature());
		final Class<? extends Throwable>[] declaredExceptionTypes = methodSignature
				.getExceptionTypes();
		boolean answer = false;
		for (final Class<? extends Throwable> declaredExceptionType : declaredExceptionTypes) {
			if (declaredExceptionType.isInstance(exceptionToCheck)) {
				answer = true;
				break;
			}
		}

		return answer;
	}
}
