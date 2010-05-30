/**
 * 
 */
package com.acme.orderplacement.jee.framework.persistence.meta.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * An annotation for marking methods that <i>write</i> data to the underlying
 * data store. Operations carrying this annotation are generally assumed to be
 * <strong>not</strong> <i>idempotent</i> yet can explicitly be marked as such
 * by setting this annotation's {@link #idempotent() <code>idempotent</code>}
 * property to <code>true</code>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StateModifyingPersistenceOperation {

	/**
	 * <p>
	 * Boolean flag indicating if the annotated
	 * <code>DataAccessWriteOperation</code> is <i>idempotent</i>, i.e. may be
	 * called repeatedly with the <strong>same parameters</strong> without
	 * changing the system's state after the first call has completed
	 * successfully.
	 * </p>
	 */
	boolean idempotent() default false;
}
