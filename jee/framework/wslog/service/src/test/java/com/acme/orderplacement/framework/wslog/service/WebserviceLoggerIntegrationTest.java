/**
 * 
 */
package com.acme.orderplacement.framework.wslog.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.framework.persistence.config.obsolete.PlatformIntegrationConfig;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceResponseDto;
import com.acme.orderplacement.jee.framework.wslog.service.internal.domain.WebserviceRequest;
import com.acme.orderplacement.jee.framework.wslog.service.internal.domain.WebserviceResponse;

/**
 * <p>
 * TODO: Insert short summary for WebserviceLoggerIntegrationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ContextConfiguration(locations = { "classpath:/META-INF/spring/persistence.testsupport.testEnvironment.scontext" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = PlatformIntegrationConfig.TXMANAGER_COMPONENT_NAME, defaultRollback = true)
@Transactional
public class WebserviceLoggerIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_WS_OPERATION_NAME = "TestWS#/item/registerItem";

	private static final String NON_EXISTING_WS_OPERATION_NAME = "TestWS#/item/nonExistingOperation";

	private static final Long NON_EXISTING_WS_REQUEST_ID = Long.valueOf(666L);

	private static final Long EXISTING_WS_REQUEST_ID = Long.valueOf(-1L);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@Resource(name = WebserviceLogger.SERVICE_NAME)
	private WebserviceLogger classUnderTest;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void logWebserviceRequestShouldRefuseToLogNullWebserviceRequest() {
		this.classUnderTest.logWebserviceRequest(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void logWebserviceRequestShouldRefuseToLogRequestReferencingANonExistingWsOperation() {
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				NON_EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				Collections.<String, String> emptyMap());

		this.classUnderTest.logWebserviceRequest(webserviceRequestDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 */
	@Test
	public final void logWebserviceRequestShouldLogRequestReferencingAnExistingWsOperation() {
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				Collections.<String, String> emptyMap());

		final Long requestId = this.classUnderTest
				.logWebserviceRequest(webserviceRequestDto);

		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") returned a NULL request id", requestId);

		final WebserviceRequest persistedWebserviceRequest = this.entityManager
				.find(WebserviceRequest.class, requestId);
		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") did NOT persist the passed on webservice request",
				persistedWebserviceRequest);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 */
	@Test
	public final void logWebserviceRequestShouldLogRequestContainingHeaders() {
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				headers);

		final Long requestId = this.classUnderTest
				.logWebserviceRequest(webserviceRequestDto);

		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") returned a NULL request id", requestId);

		final WebserviceRequest persistedWebserviceRequest = this.entityManager
				.find(WebserviceRequest.class, requestId);
		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") did NOT persist the webservice request passed in",
				persistedWebserviceRequest);
		assertEquals(
				"logWebserviceRequest("
						+ webserviceRequestDto
						+ ") did NOT persist the headers associated with the webservice request passed in",
				2, persistedWebserviceRequest.getHeaders().size());
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void logWebserviceResponseShouldRefuseToLogNullWebserviceResponse() {
		this.classUnderTest.logWebserviceResponse(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void logWebserviceResponseShouldRefuseToLogAWebserviceResponseReferencingANonExistingWebserviceRequest() {
		final WebserviceResponseDto webserviceResponseDto = new WebserviceResponseDto(
				NON_EXISTING_WS_REQUEST_ID, new Date(), "CONTENT", false);

		this.classUnderTest.logWebserviceResponse(webserviceResponseDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test
	public final void logWebserviceResponseShouldLogAWebserviceResponseReferencingAnExistingWebserviceRequest() {
		final WebserviceResponseDto webserviceResponseDto = new WebserviceResponseDto(
				EXISTING_WS_REQUEST_ID, new Date(), "CONTENT", false);

		final Long responseId = this.classUnderTest
				.logWebserviceResponse(webserviceResponseDto);

		assertNotNull("logWebserviceResponse(" + webserviceResponseDto
				+ ") returned a NULL response id", responseId);

		final WebserviceResponse persistedWebserviceResponse = this.entityManager
				.find(WebserviceResponse.class, responseId);
		assertNotNull("logWebserviceResponse(" + webserviceResponseDto
				+ ") did NOT persist the passed on webservice response",
				persistedWebserviceResponse);
	}
}
