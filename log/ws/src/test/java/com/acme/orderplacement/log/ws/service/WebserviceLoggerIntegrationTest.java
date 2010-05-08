/**
 * 
 */
package com.acme.orderplacement.log.ws.service;

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

import com.acme.orderplacement.log.ws.domain.WebserviceRequest;
import com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO;
import com.acme.orderplacement.persistence.config.PlatformIntegrationConfig;

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

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@Resource(name = WebserviceLogger.SERVICE_NAME)
	private WebserviceLogger classUnderTest;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.log.ws.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void logWebserviceRequestShouldRefuseToLogNullWebserviceRequest() {
		this.classUnderTest.logWebserviceRequest(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.log.ws.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void logWebserviceRequestShouldRefuseToLogRequestReferencingANonExistingWsOperation() {
		final WebserviceRequestDTO webserviceRequestDto = new WebserviceRequestDTO(
				NON_EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				Collections.<String, String> emptyMap());

		this.classUnderTest.logWebserviceRequest(webserviceRequestDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.log.ws.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO)}
	 * .
	 */
	@Test
	public final void logWebserviceRequestShouldLogRequestReferencingAnExistingWsOperation() {
		final WebserviceRequestDTO webserviceRequestDto = new WebserviceRequestDTO(
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
	 * {@link com.acme.orderplacement.log.ws.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO)}
	 * .
	 */
	@Test
	public final void logWebserviceRequestShouldLogRequestContainingHeaders() {
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final WebserviceRequestDTO webserviceRequestDto = new WebserviceRequestDTO(
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
}
