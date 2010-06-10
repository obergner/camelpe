/**
 * 
 */
package com.acme.orderplacement.jee.app.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * <p>
 * A {@link Qualifier <code>Qualifier</code>} defining the
 * {@link javax.persistence.EntityManager <code>EntityManager</code>}
 * responsible for managing the application's domain objects.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Qualifier
@Target( { FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
public @interface ApplicationEntities {

}
