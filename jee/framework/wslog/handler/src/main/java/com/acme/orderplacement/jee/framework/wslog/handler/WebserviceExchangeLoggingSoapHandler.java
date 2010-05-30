/**
 * 
 */
package com.acme.orderplacement.jee.framework.wslog.handler;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.acme.orderplacement.jee.framework.wslog.handler.internal.SOAPMessageContextToWebserviceRequestDtoConverter;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceLogger;
import com.acme.orderplacement.jee.framework.wslog.service.WebserviceRequestDto;

/**
 * <p>
 * TODO: Insert short summary for WebserviceExchangeLoggingSoapHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Component(WebserviceExchangeLoggingSoapHandler.COMPONENT_NAME)
public class WebserviceExchangeLoggingSoapHandler implements
		SOAPHandler<SOAPMessageContext> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "jee.framework.wslog.handler.webserviceExchangeLoggingSoapHandler";

	// @Resource(name = WebserviceLogger.SERVICE_NAME)
	// private WebserviceLogger webserviceLogger;

	private final SOAPMessageContextToWebserviceRequestDtoConverter converter = new SOAPMessageContextToWebserviceRequestDtoConverter();

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
		// TODO Auto-generated method stub
		return false;
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
	// Internal
	// -------------------------------------------------------------------------

	private boolean handleInboundMessage(final SOAPMessageContext context) {
		try {
			final WebserviceRequestDto webserviceRequestDto = this.converter
					.convert(context, new Date());
			final Long requestId = obtainMessageLoggerFrom(context)
					.logWebserviceRequest(webserviceRequestDto);

			return true;
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return true;
		}
	}

	private boolean handleOutboundMessage(final SOAPMessageContext context) {

		return true;
	}

	private boolean isInbound(final MessageContext context) {
		final Boolean outbound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		return !outbound.booleanValue();
	}

	private WebserviceLogger obtainMessageLoggerFrom(
			final MessageContext messageContext) throws IllegalStateException {

		return obtainWebApplicationContextFrom(messageContext).getBean(
				WebserviceLogger.SERVICE_NAME, WebserviceLogger.class);
	}

	private WebApplicationContext obtainWebApplicationContextFrom(
			final MessageContext messageContext) throws IllegalStateException {
		final ServletContext servlectContext = (ServletContext) messageContext
				.get(MessageContext.SERVLET_CONTEXT);

		return WebApplicationContextUtils
				.getRequiredWebApplicationContext(servlectContext);
	}
}
