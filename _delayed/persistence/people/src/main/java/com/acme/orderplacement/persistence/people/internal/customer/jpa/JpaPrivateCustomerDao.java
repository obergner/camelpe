/**
 * 
 */
package com.acme.orderplacement.persistence.people.internal.customer.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao;
import com.acme.orderplacement.persistence.support.jpa.AbstractJpaDao;

/**
 * <p>
 * TODO: Insert short summary for JpaPrivateCustomerDao
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for JpaPrivateCustomerDao
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Repository("persistence.people.JpaPrivateCustomerDao")
public class JpaPrivateCustomerDao extends
		AbstractJpaDao<PrivateCustomer, Long> implements PrivateCustomerDao {

	/**
	 * @see com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao#findByCustomerNumber(java.lang.String)
	 */
	public PrivateCustomer findByCustomerNumber(final String customerNumber)
			throws IllegalArgumentException {
		Validate.notNull(customerNumber, "customerNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("customerNumber", customerNumber);

		return findUniqueByNamedQuery(
				PrivateCustomer.Queries.BY_CUSTOMER_NUMBER, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao#findByCustomerNumberLike(java.lang.String)
	 */
	public List<PrivateCustomer> findByCustomerNumberLike(
			final String customerNumber) throws IllegalArgumentException {
		Validate.notNull(customerNumber, "customerNumber");
		final Map<String, String> parameters = new HashMap<String, String>(1);
		parameters.put("customerNumber", '%' + customerNumber + '%');

		return findByNamedQuery(
				PrivateCustomer.Queries.BY_CUSTOMER_NUMBER_LIKE, parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao#findByInvoiceStreetAndCityAndPostalCodeLike(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public List<PrivateCustomer> findByInvoiceStreetAndCityAndPostalCodeLike(
			final String invoiceStreet, final String invoiceCity,
			final String invoicePostalCode) throws IllegalArgumentException {
		Validate.notNull(invoiceStreet, "invoiceStreet");
		Validate.notNull(invoiceCity, "invoiceCity");
		Validate.notNull(invoicePostalCode, "invoicePostalCode");
		final Map<String, String> parameters = new HashMap<String, String>(3);
		parameters.put("street", '%' + invoiceStreet + '%');
		parameters.put("city", '%' + invoiceCity + '%');
		parameters.put("postalCode", '%' + invoicePostalCode + '%');

		return findByNamedQuery(
				PrivateCustomer.Queries.BY_INVOICE_STREET_AND_CITY_AND_POSTAL_CODE_LIKE,
				parameters);
	}

	/**
	 * @see com.acme.orderplacement.persistence.people.customer.PrivateCustomerDao#findByShippingStreetAndCityAndPostalCodeLike(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public List<PrivateCustomer> findByShippingStreetAndCityAndPostalCodeLike(
			final String shippingStreet, final String shippingCity,
			final String shippingPostalCode) throws IllegalArgumentException {
		Validate.notNull(shippingStreet, "shippingStreet");
		Validate.notNull(shippingCity, "shippingCity");
		Validate.notNull(shippingPostalCode, "shippingPostalCode");
		final Map<String, String> parameters = new HashMap<String, String>(3);
		parameters.put("street", '%' + shippingStreet + '%');
		parameters.put("city", '%' + shippingCity + '%');
		parameters.put("postalCode", '%' + shippingPostalCode + '%');

		return findByNamedQuery(
				PrivateCustomer.Queries.BY_SHIPPING_STREET_AND_CITY_AND_POSTAL_CODE_LIKE,
				parameters);
	}

}
