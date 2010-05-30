/**
 * 
 */
package com.acme.orderplacement.jee.framework.wslog.handler;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.ejb.EJB;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

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
public class WebserviceExchangeLoggingSoapHandler implements
		SOAPHandler<SOAPMessageContext> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	@EJB
	private WebserviceLogger webserviceLogger;

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
			final Long requestId = this.webserviceLogger
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

}
