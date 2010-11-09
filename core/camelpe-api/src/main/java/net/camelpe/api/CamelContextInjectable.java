/**
 * 
 */
package net.camelpe.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * <p>
 * A {@link javax.inject.Qualifier <code>Qualifier</code>} used to identify
 * beans needed to configure a {@link org.apache.camel.CamelContext
 * <code>CamelContext</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Qualifier
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CamelContextInjectable {
}
