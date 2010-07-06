/**
 * 
 */
package com.acme.orderplacement.jee.framework.ws.wslog;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.framework.wslog.WebserviceLogger;
import com.acme.orderplacement.framework.wslog.WebserviceRequestDto;
import com.acme.orderplacement.framework.wslog.WebserviceResponseDto;
import com.acme.orderplacement.jee.framework.ws.WebServiceContext;
import com.acme.orderplacement.jee.framework.ws.internal.wslog.SOAPMessageContextToWebserviceRequestDtoConverter;
import com.acme.orderplacement.jee.framework.ws.internal.wslog.SOAPMessageContextToWebserviceResponseDtoConverter;

/**
 * <p>
 * TODO: Insert short summary for WebserviceExchangeLoggingSoapHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WebserviceExchangeLoggingSoapHandler implements
		SOAPHandler<SOAPMessageContext> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * FIXME: Why doesn't this work? Problem posted to JBoss AS user forum:
	 * https://community.jboss.org/thread/153870?tstart=0
	 */
	// @EJB
	private WebserviceLogger webserviceLogger;

	private final SOAPMessageContextToWebserviceRequestDtoConverter requestConverter = new SOAPMessageContextToWebserviceRequestDtoConverter();

	private final SOAPMessageContextToWebserviceResponseDtoConverter responseConverter = new SOAPMessageContextToWebserviceResponseDtoConverter();

	// -------------------------------------------------------------------------
	// Implementation of javax.xml.ws.handler.soap.SOAPHandler
	// -------------------------------------------------------------------------

	/**
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	/**
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	public void close(final MessageContext context) {
		// Intentionally left blank
	}

	/**
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	public boolean handleFault(final SOAPMessageContext context) {
		WebserviceResponseDto webserviceResponseDto = null;
		try {
			webserviceResponseDto = this.responseConverter.convert(context,
					new Date(), true);
			this.webserviceLogger.logWebserviceResponse(webserviceResponseDto);

			return true;
		} catch (final Exception e) {
			this.log
					.error(
							"Failed to log web service response ["
									+ webserviceResponseDto
									+ "] to database (error will be ignored, processing continues): "
									+ e.getMessage(), e);

			return true;
		}
	}

	/**
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	public boolean handleMessage(final SOAPMessageContext context) {
		if (isInbound(context)) {
			return handleInboundMessage(context);
		}
		return handleOutboundMessage(context);
	}

	// -------------------------------------------------------------------------
	// Lifecycle callbacks
	// -------------------------------------------------------------------------

	/**
	 * HACK: To be removed as soon as @EJB above works.
	 */
	@PostConstruct
	public void lookupDependencies() throws RuntimeException {
		try {
			final InitialContext ic = new InitialContext();
			this.webserviceLogger = (WebserviceLogger) ic
					.lookup("orderplacement.jee.ear-1.0-SNAPSHOT/WebserviceLoggerBean/local");
			ic.close();
		} catch (final NamingException e) {
			throw new IllegalStateException("Failed to look up dependencies: "
					+ e.getMessage(), e);
		}
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private boolean handleInboundMessage(final SOAPMessageContext context) {
		WebserviceRequestDto webserviceRequestDto = null;
		try {
			webserviceRequestDto = this.requestConverter.convert(context,
					new Date());
			final Long requestId = this.webserviceLogger
					.logWebserviceRequest(webserviceRequestDto);

			/*
			 * Store request id in web service context for use further
			 * downstream as well as when logging the response.
			 */
			context.setScope(WebServiceContext.WS_REQUEST_ID,
					MessageContext.Scope.APPLICATION);
			context.put(WebServiceContext.WS_REQUEST_ID, requestId);

			return true;
		} catch (final Exception e) {
			this.log
					.error(
							"Failed to log web service request ["
									+ webserviceRequestDto
									+ "] to database (error will be ignored, processing continues): "
									+ e.getMessage(), e);

			return true;
		}
	}

	private boolean handleOutboundMessage(final SOAPMessageContext context) {
		WebserviceResponseDto webserviceResponseDto = null;
		try {
			webserviceResponseDto = this.responseConverter.convert(context,
					new Date(), false);
			this.webserviceLogger.logWebserviceResponse(webserviceResponseDto);

			return true;
		} catch (final Exception e) {
			this.log
					.error(
							"Failed to log web service response ["
									+ webserviceResponseDto
									+ "] to database (error will be ignored, processing continues): "
									+ e.getMessage(), e);

			return true;
		}
	}

	private boolean isInbound(final MessageContext context) {
		final Boolean outbound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		return !outbound.booleanValue();
	}

}
