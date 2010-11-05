/**
 * 
 */
package net.camelpe.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
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
@Target({ TYPE, FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
public @interface CamelContextModifying {

}
