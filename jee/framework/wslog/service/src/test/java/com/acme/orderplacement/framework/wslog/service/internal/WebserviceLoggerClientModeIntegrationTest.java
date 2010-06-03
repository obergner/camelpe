/**
 * 
 */
package com.acme.orderplacement.framework.wslog.service.internal;

import static junit.framework.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceResponseDto;
import com.acme.orderplacement.jee.framework.wslog.service.internal.WebserviceLoggerBean;
import com.acme.orderplacement.jee.framework.wslog.service.internal.domain.WebserviceOperation;

/**
 * <p>
 * Test {@link JpaItemDao <code>JpaItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class WebserviceLoggerClientModeIntegrationTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String EXISTING_WS_OPERATION_NAME = "TestWS#/item/registerItem";

	private static final String NON_EXISTING_WS_OPERATION_NAME = "TestWS#/item/nonExistingOperation";

	private static final Long NON_EXISTING_WS_REQUEST_ID = Long.valueOf(666L);

	private static final Long EXISTING_WS_REQUEST_ID = Long.valueOf(-1L);

	private WebserviceLogger cachedWebserviceLogger;

	// -------------------------------------------------------------------------
	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive deployment = ShrinkWrap.create("test.jar",
				JavaArchive.class).addPackages(true,
				Validate.class.getPackage(),
				WebserviceLogger.class.getPackage(),
				WebserviceLoggerBean.class.getPackage(),
				WebserviceOperation.class.getPackage()).addManifestResource(
				"META-INF/glassfish/persistence.xml",
				ArchivePaths.create("persistence.xml")).addManifestResource(
				"META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml"))
				.addManifestResource("META-INF/glassfish/sun-ejb-jar.xml",
						ArchivePaths.create("sun-ejb-jar.xml"));

		return deployment;
	}

	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceRequestRefusesToLogNullWebserviceRequest()
			throws NoResultException, IllegalArgumentException, NamingException {
		lookupWebserviceLogger().logWebserviceRequest(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = NoResultException.class)
	public final void assertThatLogWebserviceRequestRefusesToLogRequestReferencingANonExistingWsOperation()
			throws Exception {
		try {
			final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
					NON_EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(),
					"TEST", Collections.<String, String> emptyMap());

			lookupWebserviceLogger().logWebserviceRequest(webserviceRequestDto);
		} catch (final EJBException e) {
			throw e.getCausedByException();
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test
	public final void assertThatLogWebserviceRequestLogsRequestReferencingAnExistingWsOperation()
			throws NoResultException, IllegalArgumentException, NamingException {
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				Collections.<String, String> emptyMap());

		final Long requestId = lookupWebserviceLogger().logWebserviceRequest(
				webserviceRequestDto);

		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") returned a NULL request id", requestId);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceRequest(com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test
	public final void assertThatLogWebserviceRequestLogsWebserviceRequestHeaders()
			throws NoResultException, IllegalArgumentException, NamingException {
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("header1", "value1");
		headers.put("header2", "value2");
		final WebserviceRequestDto webserviceRequestDto = new WebserviceRequestDto(
				EXISTING_WS_OPERATION_NAME, "1.1.1.1", new Date(), "TEST",
				headers);

		final Long requestId = lookupWebserviceLogger().logWebserviceRequest(
				webserviceRequestDto);

		assertNotNull("logWebserviceRequest(" + webserviceRequestDto
				+ ") returned a NULL request id", requestId);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceResponseRefusesToLogNullWebserviceResponse()
			throws NoResultException, IllegalArgumentException, NamingException {
		lookupWebserviceLogger().logWebserviceResponse(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void assertThatLogWebserviceResponseRefusesToLogAWebserviceResponseReferencingANonExistingWebserviceRequest()
			throws NoResultException, IllegalArgumentException, NamingException {
		final WebserviceResponseDto webserviceResponseDto = new WebserviceResponseDto(
				NON_EXISTING_WS_REQUEST_ID, new Date(), "CONTENT", false);

		lookupWebserviceLogger().logWebserviceResponse(webserviceResponseDto);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger#logWebserviceResponse(WebserviceResponseDto)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Test
	public final void assertThatLogWebserviceResponseLogsAWebserviceResponseReferencingAnExistingWebserviceRequest()
			throws NoResultException, IllegalArgumentException, NamingException {
		final WebserviceResponseDto webserviceResponseDto = new WebserviceResponseDto(
				EXISTING_WS_REQUEST_ID, new Date(), "CONTENT", false);

		final Long responseId = lookupWebserviceLogger().logWebserviceResponse(
				webserviceResponseDto);

		assertNotNull("logWebserviceResponse(" + webserviceResponseDto
				+ ") returned a NULL response id", responseId);
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	private WebserviceLogger lookupWebserviceLogger() throws NamingException {
		if (this.cachedWebserviceLogger == null) {
			this.cachedWebserviceLogger = (WebserviceLogger) new InitialContext()
					.lookup("java:global/test/WebserviceLoggerBean");
		}

		return this.cachedWebserviceLogger;
	}
}
