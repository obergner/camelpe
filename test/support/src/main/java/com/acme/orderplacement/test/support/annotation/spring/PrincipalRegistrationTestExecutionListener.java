/**
 * 
 */
package com.acme.orderplacement.test.support.annotation.spring;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.acme.orderplacement.test.support.annotation.TestUser;
import com.acme.orderplacement.test.support.auth.PrincipalRegistration;
import com.acme.orderplacement.test.support.auth.TestPrincipal;

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
public final class PrincipalRegistrationTestExecutionListener extends
		AbstractTestExecutionListener implements TestExecutionListener {

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
			authenticateUsing(testUserAnnotation, testContext);
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
			clearAuthentication(testContext);
		}
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	/**
	 * @param testUserAnnotation
	 */
	private void authenticateUsing(final TestUser testUserAnnotation,
			final TestContext testContext) {
		final Principal testPrincipal = new TestPrincipal(testUserAnnotation
				.username());
		principalRegistration(testContext).registerCurrentPrincipal(
				testPrincipal);
		this.log
				.debug(
						"Registered test authentication token [{}] with security context",
						testPrincipal);
	}

	/**
	 * 
	 */
	private void clearAuthentication(final TestContext testContext) {
		principalRegistration(testContext).unregisterCurrentPrincipal();
		this.log.debug("Cleared security context after test execution");
	}

	private PrincipalRegistration principalRegistration(
			final TestContext testContext) {
		return testContext.getApplicationContext().getBean(
				PrincipalRegistration.COMPONENT_NAME,
				PrincipalRegistration.class);
	}
}
