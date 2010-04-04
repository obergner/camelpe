/**
 * 
 */
package com.acme.orderplacement.test.support.annotation.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.acme.orderplacement.test.support.annotation.TestUser;

/**
 * <p>
 * A <tt>Spring</tt> {@link TestExecutionListener
 * <code>TestExecutionListener</code>} that is aware of {@link TestUser
 * <code>TestUser</code>} annotations present on test classes and/or test
 * methods and associates an appropriate
 * {@link org.springframework.security.Authentication
 * <code>Authentication</code>} object with the current
 * {@link org.springframework.security.context.SecurityContext
 * <code>SecurityContext</code>} prior to test execution, cleaning it up
 * afterwards.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class SpringBasedAuthenticationProvidingTestExecutionListener
		extends AbstractTestExecutionListener implements TestExecutionListener {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * Our faithful logger.
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	// ------------------------------------------------------------------------
	// org.springframework.test.context.support.AbstractTestExecutionListener
	// ------------------------------------------------------------------------

	/**
	 * @see org.springframework.test.context.support.AbstractTestExecutionListener#beforeTestMethod(org.springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestMethod(final TestContext testContext)
			throws Exception {
		TestUser testUserAnnotation = null;
		if (testContext.getTestMethod().isAnnotationPresent(TestUser.class)) {
			testUserAnnotation = testContext.getTestMethod().getAnnotation(
					TestUser.class);
			this.log.debug(
					"Found security annotation [{}] on test method [{}]",
					testUserAnnotation, testContext.getTestMethod());
		} else if (testContext.getTestClass().isAnnotationPresent(
				TestUser.class)) {
			testUserAnnotation = testContext.getTestClass().getAnnotation(
					TestUser.class);
			this.log.debug("Found security annotation [{}] on test class [{}]",
					testUserAnnotation, testContext.getTestClass().getName());
		}

		if (testUserAnnotation != null) {
			authenticateUsing(testUserAnnotation);
		}
	}

	/**
	 * @see org.springframework.test.context.support.AbstractTestExecutionListener#afterTestMethod(org.springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestMethod(final TestContext testContext) throws Exception {
		if (testContext.getTestMethod().isAnnotationPresent(TestUser.class)
				|| testContext.getTestClass().isAnnotationPresent(
						TestUser.class)) {
			clearAuthentication();
		}
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @param testUserAnnotation
	 */
	private void authenticateUsing(final TestUser testUserAnnotation) {
		final Authentication testAuthentication = new UsernamePasswordAuthenticationToken(
				testUserAnnotation.username(), testUserAnnotation.password());
		SecurityContextHolder.getContext()
				.setAuthentication(testAuthentication);
		this.log
				.debug(
						"Registered test authentication token [{}] with security context",
						testAuthentication);
	}

	/**
	 * 
	 */
	private void clearAuthentication() {
		SecurityContextHolder.clearContext();
		this.log.debug("Cleared security context after test execution");
	}
}
