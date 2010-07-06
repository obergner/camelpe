/**
 * 
 */
package com.acme.orderplacement.framework.wslog;

import javax.persistence.NoResultException;

/**
 * <p>
 * TODO: Insert short summary for WebserviceLogger
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface WebserviceLogger {

	String SERVICE_NAME = "orderplacement.framework.webserviceLogger";

	/**
	 * @param webserviceRequestDto
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	Long logWebserviceRequest(final WebserviceRequestDto webserviceRequestDto)
			throws IllegalArgumentException, NoResultException;

	/**
	 * @param webserviceResponseDto
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	Long logWebserviceResponse(final WebserviceResponseDto webserviceResponseDto)
			throws IllegalArgumentException, NoResultException;

}