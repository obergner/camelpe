/**
 * 
 */
package com.acme.orderplacement.framework.testsupport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * An annotation for advising the test framework to run the annotated test
 * class/test method under the specified user.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface TestUser {

	/**
	 * The test user's <tt>username</tt>.
	 */
	String username();

	/**
	 * The test user's <tt>password</tt>.
	 */
	String password() default "";
}
