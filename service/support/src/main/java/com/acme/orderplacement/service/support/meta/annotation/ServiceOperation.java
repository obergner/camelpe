/**
 * 
 */
package com.acme.orderplacement.service.support.meta.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * An annotation for marking methods that belong to the <tt>Service Layer</tt>'s
 * public API.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceOperation {

	/**
	 * <p>
	 * Boolean flag indicating if the annotated <tt>Service Operation</tt> is
	 * <i>idempotent</i>, i.e. may be called repeatedly with the <strong>same
	 * parameters</strong> without changing the system's state after the first
	 * call has completed successfully.
	 * </p>
	 */
	boolean idempotent() default false;
}
