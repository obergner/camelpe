/**
 * 
 */
package com.acme.orderplacement.log.ws.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.log.ws.domain.WebserviceOperation;
import com.acme.orderplacement.log.ws.domain.WebserviceRequest;
import com.acme.orderplacement.log.ws.dto.WebserviceRequestDTO;

/**
 * <p>
 * TODO: Insert short summary for WebserviceLogger
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Service(WebserviceLogger.SERVICE_NAME)
@Transactional
public class WebserviceLogger {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String SERVICE_NAME = "log.ws.webserviceLogger";

	/**
	 * <p>
	 * Our {@link javax.persistence.EntityManager <em>JPA EntityManager</em>}.
	 * </p>
	 */
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param webserviceRequestDto
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoResultException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Long logWebserviceRequest(
			final WebserviceRequestDTO webserviceRequestDto)
			throws IllegalArgumentException, NoResultException {
		Validate.notNull(webserviceRequestDto, "webserviceRequestDto");

		final TypedQuery<WebserviceOperation> wsOperationByName = this.entityManager
				.createNamedQuery(WebserviceOperation.Queries.BY_NAME,
						WebserviceOperation.class);
		wsOperationByName.setParameter("name", webserviceRequestDto
				.getOperationName());
		final WebserviceOperation referencedWebserviceOperation = wsOperationByName
				.getSingleResult();

		final WebserviceRequest webserviceRequest = new WebserviceRequest(
				webserviceRequestDto.getSourceIp(), webserviceRequestDto
						.getReceivedOn(), webserviceRequestDto.getContent(),
				referencedWebserviceOperation, webserviceRequestDto
						.getHeaders());
		this.entityManager.persist(webserviceRequest);

		return webserviceRequest.getId();
	}
}
