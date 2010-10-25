/**
 * 
 */
package com.acme.orderplacement.jee.framework.ws.auth;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.login.FailedLoginException;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.DOMException;

import com.acme.orderplacement.jee.framework.common.auth.AuthenticationService;
import com.acme.orderplacement.jee.framework.wsapi.SOAPHeaders;

/**
 * <p>
 * TODO: Insert short summary for AuthenticationHandler
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {

	/**
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		final Set<QName> headers = new HashSet<QName>();
		headers.add(SOAPHeaders.AuthenticationContext.QN_AUTHENTICATION_HEADER);

		return Collections.unmodifiableSet(headers);
	}

	/**
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(final MessageContext context) {
		// Noop
	}

	/**
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(final SOAPMessageContext context) {
		return true;
	}

	/**
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(final SOAPMessageContext context) {
		String username = null;
		try {
			final Boolean outbound = (Boolean) context
					.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (outbound.booleanValue()) {
				return true;
			}

			final SOAPElement authenticationHeader = retrieveAuthenticationHeaderFrom(context);
			username = retrieveUsernameFrom(authenticationHeader);
			final String password = retrievePasswordFrom(authenticationHeader);

			AuthenticationService.FACTORY.getAuthenticationService().login(
					username, password);

			return true;
		} catch (final FailedLoginException e) {
			handleLoginFailure(context, username, e);

			return false;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SOAPElement retrieveAuthenticationHeaderFrom(
			final SOAPMessageContext context) throws IllegalArgumentException,
			SOAPException {
		final SOAPHeader header = retrieveSOAPHeaderFrom(context);

		final Iterator<SOAPElement> authenticationHeaders = header
				.getChildElements(SOAPHeaders.AuthenticationContext.QN_AUTHENTICATION_HEADER);
		final SOAPElement authenticationHeader = authenticationHeaders.next();
		if (authenticationHeader == null) {
			throw new IllegalArgumentException(
					"No ["
							+ SOAPHeaders.AuthenticationContext.QN_AUTHENTICATION_HEADER
							+ "] header found in webservice request. Cannot perform login.");
		}

		return authenticationHeader;
	}

	private SOAPHeader retrieveSOAPHeaderFrom(final SOAPMessageContext context)
			throws SOAPException, IllegalArgumentException {
		final SOAPMessage message = context.getMessage();
		final SOAPHeader header = message.getSOAPHeader();
		if (header == null) {
			throw new IllegalArgumentException(
					"No headers found in webservice request. Cannot perform login.");
		}
		return header;
	}

	private String retrieveUsernameFrom(final SOAPElement authenticationHeader)
			throws IllegalArgumentException, DOMException {
		final Iterator<SOAPElement> usernameElements = authenticationHeader
				.getChildElements(SOAPHeaders.AuthenticationContext.QN_USERNAME);
		final SOAPElement usernameElement = usernameElements.next();
		if (usernameElement == null) {
			throw new IllegalArgumentException("No ["
					+ SOAPHeaders.AuthenticationContext.QN_USERNAME
					+ "] element found in authentication header ["
					+ authenticationHeader + "]. Cannot perform login.");
		}
		final String username = usernameElement.getValue();

		return username;
	}

	private String retrievePasswordFrom(final SOAPElement authenticationHeader)
			throws IllegalArgumentException, DOMException {
		final Iterator<SOAPElement> passwordElements = authenticationHeader
				.getChildElements(SOAPHeaders.AuthenticationContext.QN_PASSWORD);
		final SOAPElement passwordElement = passwordElements.next();
		if (passwordElement == null) {
			throw new IllegalArgumentException("No ["
					+ SOAPHeaders.AuthenticationContext.QN_PASSWORD
					+ "] element found in authentication header ["
					+ authenticationHeader + "]. Cannot perform login.");
		}
		final String password = passwordElement.getValue();

		return password;
	}

	private void handleLoginFailure(final SOAPMessageContext context,
			final String username, final FailedLoginException e)
			throws RuntimeException {
		try {
			final SOAPHeader header = retrieveSOAPHeaderFrom(context);
			header.detachNode();

			final Iterator<SOAPElement> soapBodyElements = context.getMessage()
					.getSOAPBody().getChildElements();
			while (soapBodyElements.hasNext()) {
				soapBodyElements.next().detachNode();
			}

			final SOAPFault authenticationFault = context.getMessage()
					.getSOAPBody().addFault();
			authenticationFault.setFaultCode("Client");
			authenticationFault.setFaultString("INVALID CREDENTIALS");
			final Detail faultDetail = authenticationFault.addDetail();
			final DetailEntry detailEntry = faultDetail
					.addDetailEntry(new QName("detail"));
			detailEntry.setValue(e.getMessage());

			context.getMessage().saveChanges();
		} catch (final SOAPException se) {
			throw new RuntimeException(se);
		}
	}
}
