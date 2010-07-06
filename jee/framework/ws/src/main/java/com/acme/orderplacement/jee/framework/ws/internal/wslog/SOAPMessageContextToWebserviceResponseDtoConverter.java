/**
 * 
 */
package com.acme.orderplacement.jee.framework.ws.internal.wslog;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.Validate;

import com.acme.orderplacement.framework.wslog.WebserviceResponseDto;
import com.acme.orderplacement.jee.framework.ws.WebServiceContext;

/**
 * <p>
 * TODO: Insert short summary for
 * SOAPMessageContextToWebserviceRequestDtoConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SOAPMessageContextToWebserviceResponseDtoConverter {

	private static final String DEFAULT_SOAP_MESSAGE_ENCODING = "utf-8";

	public WebserviceResponseDto convert(
			final SOAPMessageContext soapMessageContext,
			final Date responseSentOn, final boolean failed)
			throws SOAPException, IOException, UnsupportedEncodingException,
			IllegalArgumentException {
		Validate.notNull(soapMessageContext, "soapMessageContext");
		Validate.notNull(responseSentOn, "responseSentOn");

		final String soapMessageAsString = extractSoapMessageStringRepresentationFrom(soapMessageContext);

		final Long referencedRequestId = (Long) soapMessageContext
				.get(WebServiceContext.WS_REQUEST_ID);

		return new WebserviceResponseDto(referencedRequestId, responseSentOn,
				soapMessageAsString, failed);
	}

	private String extractSoapMessageStringRepresentationFrom(
			final SOAPMessageContext soapMessageContext) throws SOAPException,
			IOException, UnsupportedEncodingException {
		BufferedOutputStream soapMessageAsBytesBuffer = null;
		try {
			final SOAPMessage soapMessage = soapMessageContext.getMessage();

			final String soapMessageEncoding = (String) soapMessage
					.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);

			final ByteArrayOutputStream soapMessageAsBytes = new ByteArrayOutputStream();
			soapMessageAsBytesBuffer = new BufferedOutputStream(
					soapMessageAsBytes);
			soapMessage.writeTo(soapMessageAsBytesBuffer);
			soapMessageAsBytesBuffer.flush();

			final String soapMessageAsString = new String(soapMessageAsBytes
					.toByteArray(),
					soapMessageEncoding != null ? soapMessageEncoding
							: DEFAULT_SOAP_MESSAGE_ENCODING);

			return soapMessageAsString;
		} finally {
			if (soapMessageAsBytesBuffer != null) {
				soapMessageAsBytesBuffer.close();
			}
		}
	}
}
