/**
 * 
 */
package com.acme.orderplacement.jee.framework.wslog.internal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;

import com.acme.orderplacement.framework.wslog.WebserviceRequestDto;
import com.acme.orderplacement.framework.wslog.WebserviceResponseDto;
import com.acme.orderplacement.jee.framework.wslog.internal.WebserviceLoggerBean;
import com.acme.orderplacement.jee.framework.wslog.internal.domain.WebserviceRequest;
import com.acme.orderplacement.jee.framework.wslog.internal.domain.WebserviceResponse;

/**
 * <p>
 * TODO: Insert short summary for WebserviceLoggerBeanIntegrationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet("WebserviceLoggerBeanIntegrationTest.xml")
@JpaEntityManagerFactory(persistenceUnit = "jee.framework.hsqldb.wslogPU")
public class WebserviceLoggerBeanIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_CONTEXT_ROOT = "/test";

	private static final String EXISTING_WS_SERVICE_NAME = "TestWS";

	private static final String EXISTING_WS_OPERATION_NAME = "registerItem";

	private static final String NON_EXISTING_WS_OPERATION_NAME = "nonExistingOperation";

	private static final Long NON_EXISTING_WS_REQUEST_ID = Long.valueOf(666L);

	private static final Long EXISTING_WS_REQUEST_ID = Long.valueOf(-1L);

	@TestedObject
	private WebserviceLoggerBean classUnderTest;

	@InjectIntoByType
	@PersistenceContext(unitName = "jee.framework.wslogPU")
	private EntityManager entityManager;

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.framework.wslog.WebserviceRequestDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceRequestRefusesToLogNullWebserviceRequest() {
		this.classUnderTest.logWebserviceRequest(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.framework.wslog.WebserviceRequestDto)}
	 * .
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatLogWebserviceRequestRefusesToLogRequestReferencingANonExistingWsOperation() {
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_CONTEXT_ROOT, EXISTING_WS_SERVICE_NAME,
				NON_EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				Collections.<String, String> emptyMap());

		this.classUnderTest.logWebserviceRequest(webserviceRequestDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.framework.wslog.WebserviceRequestDto)}
	 * .
	 */
	@Test
	public final void assertThatLogWebserviceRequestLogsRequestReferencingAnExistingWsOperation() {
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_CONTEXT_ROOT, EXISTING_WS_SERVICE_NAME,
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
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.framework.wslog.WebserviceRequestDto)}
	 * .
	 */
	@Test
	public final void assertThatLogWebserviceRequestLogsWebserviceRequestHeaders() {
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_CONTEXT_ROOT, EXISTING_WS_SERVICE_NAME,
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
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceResponseRefusesToLogNullWebserviceResponse() {
		this.classUnderTest.logWebserviceResponse(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceResponseRefusesToLogAWebserviceResponseReferencingANonExistingWebserviceRequest() {
		final WebserviceResponseDto webserviceResponseDto = new WebserviceResponseDto(
				NON_EXISTING_WS_REQUEST_ID, new Date(), "CONTENT", false);

		this.classUnderTest.logWebserviceResponse(webserviceResponseDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.framework.wslog.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 */
	@Test
	public final void assertThatLogWebserviceResponseLogsAWebserviceResponseReferencingAnExistingWebserviceRequest() {
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
