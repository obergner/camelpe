/**
 * 
 */
package com.acme.orderplacement.jee.item.ws.handler;

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
import org.w3c.dom.NodeList;

import com.acme.orderplacement.jee.framework.common.auth.AuthenticationService;
import com.acme.orderplacement.jee.item.wsapi.ItemstorageQNames;

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
		headers.add(ItemstorageQNames.AUTHENTICATION_HEADER);

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

			final SOAPHeader header = retrieveSOAPHeaderFrom(context);

			final SOAPElement authenticationHeader = retrieveAuthenticationHeaderFrom(header);

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

	private SOAPElement retrieveAuthenticationHeaderFrom(final SOAPHeader header)
			throws IllegalArgumentException {
		final Iterator<SOAPElement> authenticationHeaders = header
				.getChildElements(ItemstorageQNames.AUTHENTICATION_HEADER);
		final SOAPElement authenticationHeader = authenticationHeaders.next();
		if (authenticationHeader == null) {
			throw new IllegalArgumentException(
					"No ["
							+ ItemstorageQNames.AUTHENTICATION_HEADER
							+ "] header found in webservice request. Cannot perform login.");
		}
		return authenticationHeader;
	}

	private String retrieveUsernameFrom(final SOAPElement authenticationHeader)
			throws IllegalArgumentException, DOMException {
		final NodeList usernameElements = authenticationHeader
				.getElementsByTagName("username");
		if (usernameElements.getLength() == 0) {
			throw new IllegalArgumentException(
					"No [username] element found in authentication header ["
							+ authenticationHeader + "]. Cannot perform login.");
		}
		final String username = usernameElements.item(0).getTextContent();
		return username;
	}

	private String retrievePasswordFrom(final SOAPElement authenticationHeader)
			throws IllegalArgumentException, DOMException {
		final NodeList passwordElements = authenticationHeader
				.getElementsByTagName("password");
		if (passwordElements.getLength() == 0) {
			throw new IllegalArgumentException(
					"No [password] element found in authentication header ["
							+ authenticationHeader + "]. Cannot perform login.");
		}
		final String password = passwordElements.item(0).getTextContent();
		return password;
	}

	private void handleLoginFailure(final SOAPMessageContext context,
			final String username, final FailedLoginException e)
			throws RuntimeException {
		try {
			final SOAPFault authenticationFault = context.getMessage()
					.getSOAPBody().addFault();
			authenticationFault.setFaultCode("Client");
			authenticationFault
					.setFaultString("Failed to authenticate remote user using username = ["
							+ username + "] and password = [HIDDEN]");
			final Detail faultDetail = authenticationFault.addDetail();
			final DetailEntry detailEntry = faultDetail
					.addDetailEntry(new QName("detail"));
			detailEntry.setTextContent(e.getMessage());

			context.getMessage().saveChanges();
		} catch (final SOAPException se) {
			throw new RuntimeException(se);
		}
	}
}
