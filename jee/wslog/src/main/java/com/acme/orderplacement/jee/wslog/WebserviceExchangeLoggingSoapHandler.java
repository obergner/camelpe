/**
 * 
 */
package com.acme.orderplacement.jee.wslog;

import java.util.Set;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.acme.orderplacement.log.ws.service.WebserviceLogger;

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

	@Resource(name = WebserviceLogger.SERVICE_NAME)
	private WebserviceLogger webserviceLogger;

	// -------------------------------------------------------------------------
	// Implementation of javax.xml.ws.handler.soap.SOAPHandler
	// -------------------------------------------------------------------------

	/**
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	public Set<QName> getHeaders() {
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private void handleIncomingMessage(final SOAPMessageContext context) {

	}

	private void handleOutgoingMessage(final SOAPMessageContext context) {

	}
}
