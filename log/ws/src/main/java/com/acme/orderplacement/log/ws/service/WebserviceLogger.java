/**
 * 
 */
package com.acme.orderplacement.log.ws.service;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * TODO: Insert short summary for WebserviceLogger
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface WebserviceLogger {

	String SERVICE_NAME = "log.ws.webserviceLogger";

	/**
	 * @param webserviceRequestDto
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	Long logWebserviceRequest(final WebserviceRequestDto webserviceRequestDto)
			throws IllegalArgumentException, NoResultException;

	/**
	 * @param webserviceResponseDto
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	Long logWebserviceResponse(final WebserviceResponseDto webserviceResponseDto)
			throws IllegalArgumentException, NoResultException;

}