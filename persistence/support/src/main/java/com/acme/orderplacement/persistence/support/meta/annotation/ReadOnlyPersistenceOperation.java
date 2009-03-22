/**
 * 
 */
package com.acme.orderplacement.persistence.support.meta.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * An annotation for marking methods that <i>read</i> data from the underlying
 * data store. It is assumed - probably safely so - that such operations are
 * <i>idempotent</i>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReadOnlyPersistenceOperation {

}
