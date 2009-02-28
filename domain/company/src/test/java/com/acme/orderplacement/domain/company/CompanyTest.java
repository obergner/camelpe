/**
 * 
 */
package com.acme.orderplacement.domain.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.acme.orderplacement.domain.company.customer.CorporateClient;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Company <code>Company</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CompanyTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setName(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setNameShouldRejectNullName() {
		final Company classUnderTest = new Company();
		classUnderTest.setName(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setName(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setNameShouldRejectBlankName() {
		final Company classUnderTest = new Company();
		classUnderTest.setName("");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setCompanyNumber(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setCompanyNumberShouldRejectNullCompanyNumber() {
		final Company classUnderTest = new Company();
		classUnderTest.setCompanyNumber(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setCompanyNumber(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setCompanyNumberShouldRejectBlankCompanyNumber() {
		final Company classUnderTest = new Company();
		classUnderTest.setCompanyNumber("");
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setMainAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setMainAddressShouldRejectNullAddress() {
		final Company classUnderTest = new Company();
		classUnderTest.setMainAddress(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setMainPhoneNumber(com.acme.orderplacement.domain.people.contact.PhoneNumber)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setMainPhoneNumberShouldRejectNullPhoneNumber() {
		final Company classUnderTest = new Company();
		classUnderTest.setMainPhoneNumber(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setMainEmailAddress(com.acme.orderplacement.domain.people.contact.EmailAddress)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setMainEmailAddressShouldRejectNullEmailAddress() {
		final Company classUnderTest = new Company();
		classUnderTest.setMainEmailAddress(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setCorporateClient(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 * 
	 * @throws RolePreconditionsNotMetException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void setCorporateClientShouldRejectNullCorporateClient()
			throws RolePreconditionsNotMetException {
		final Company classUnderTest = new Company();
		classUnderTest.setCorporateClient(null);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#setCorporateClient(com.acme.orderplacement.domain.company.customer.CorporateClient)}
	 * .
	 * 
	 * @throws RolePreconditionsNotMetException
	 */
	@Test(expected = RolePreconditionsNotMetException.class)
	public final void setCorporateClientShouldRejectAnotherCompanysCorporateClient()
			throws RolePreconditionsNotMetException {
		final Company companyOne = new Company();
		companyOne.setCompanyNumber("companyOne");

		final Company companyTwo = new Company();
		companyTwo.setCompanyNumber("companyTwo");

		final CorporateClient corporateClientOfCompanyTwo = new CorporateClient();
		corporateClientOfCompanyTwo.setParent(companyTwo);

		companyOne.setCorporateClient(corporateClientOfCompanyTwo);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public final void equalsShouldRecognizeEqualCompany() {
		final Company companyOne = new Company();
		companyOne.setCompanyNumber("IDENTICAL_COMPANY_NUMBER");
		companyOne.setName("IDENTICAL_NAME");

		final Company companyTwo = new Company();
		companyTwo.setCompanyNumber("IDENTICAL_COMPANY_NUMBER");
		companyTwo.setName("IDENTICAL_NAME");

		assertTrue("equals() not implemented correctly", companyOne
				.equals(companyTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public final void equalsShouldRecognizeUnequalCompany() {
		final Company companyOne = new Company();
		companyOne.setCompanyNumber("COMPANY_NUMBER_1");
		companyOne.setName("NAME_1");

		final Company companyTwo = new Company();
		companyTwo.setCompanyNumber("COMPANY_NUMBER_2");
		companyTwo.setName("NAME_2");

		assertFalse("equals() not implemented correctly", companyOne
				.equals(companyTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.Company#hashCode()}.
	 */
	@Test
	public final void hashCodeShouldProduceIdenticalHashCodesForEqualCompanies() {
		final Company companyOne = new Company();
		companyOne.setCompanyNumber("IDENTICAL_COMPANY_NUMBER");
		companyOne.setName("IDENTICAL_NAME");

		final Company companyTwo = new Company();
		companyTwo.setCompanyNumber("IDENTICAL_COMPANY_NUMBER");
		companyTwo.setName("IDENTICAL_NAME");

		assertEquals("hashCode() not implemented correctly", companyOne
				.hashCode(), companyTwo.hashCode());
	}
}
