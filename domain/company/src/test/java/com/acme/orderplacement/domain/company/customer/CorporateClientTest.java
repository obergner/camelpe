/**
 * 
 */
package com.acme.orderplacement.domain.company.customer;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.company.Company;
import com.acme.orderplacement.domain.company.people.CorporateClientRepresentative;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;

/**
 * <p>
 * A test for {@link CorporateClient <code>CorporateClient</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CorporateClientTest extends TestCase {

	/**
	 * @param name
	 */
	public CorporateClientTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#setCorporateClientNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCorporateClientNumber_RejectsNullCorporateClientNumber() {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.setCorporateClientNumber(null);
			fail("setCorporateClientNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#setCorporateClientNumber(java.lang.String)}
	 * .
	 */
	public final void testSetCorporateClientNumber_RejectsBlankCorporateClientNumber() {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.setCorporateClientNumber("");
			fail("setCorporateClientNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#setDeliveryAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetDeliveryAddress_RejectsNullDeliveryAddress() {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.setDeliveryAddress(null);
			fail("setDeliveryAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#setInvoiceAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetInvoiceAddress_RejectsNullInvoiceAddress() {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.setInvoiceAddress(null);
			fail("setInvoiceAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#setParent(com.acme.orderplacement.domain.company.Company)}
	 * .
	 * 
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testSetParent_RejectsNullParent()
			throws RolePreconditionsNotMetException {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.setParent(null);
			fail("setParent(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#addRepresentative(com.acme.orderplacement.domain.company.people.CorporateClientRepresentative)}
	 * .
	 * 
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testAddRepresentative_RejectsNullRepresentative()
			throws RolePreconditionsNotMetException {
		try {
			final CorporateClient classUnderTest = new CorporateClient();
			classUnderTest.addRepresentative(null);
			fail("addRepresentative(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#addRepresentative(com.acme.orderplacement.domain.company.people.CorporateClientRepresentative)}
	 * .
	 */
	public final void testAddRepresentative_RejectsAnotherCorporateClientsRepresentative() {
		final CorporateClient corporateClientOne = new CorporateClient();
		corporateClientOne.setCorporateClientNumber("corporateClientOne");

		final CorporateClient corporateClientTwo = new CorporateClient();
		corporateClientTwo.setCorporateClientNumber("corporateClientTwo");

		final CorporateClientRepresentative representativeOfCorporateClientTwo = new CorporateClientRepresentative();
		representativeOfCorporateClientTwo
				.setRepresentedCorporateClient(corporateClientTwo);

		try {
			corporateClientOne
					.addRepresentative(representativeOfCorporateClientTwo);
			fail("addRepresentative(" + representativeOfCorporateClientTwo
					+ ") did not throw expected "
					+ RolePreconditionsNotMetException.class.getName());
		} catch (final RolePreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testEquals_RecognizesEqualCorporateClient()
			throws RolePreconditionsNotMetException, IllegalArgumentException {
		final Company companyOne = new Company(1L, 5);
		companyOne.setName("companyOne");
		companyOne.setCompanyNumber("COMP-11-111-11");

		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientOne.setParent(companyOne);

		final CorporateClient corporateClientTwo = new CorporateClient(1L, 5);
		corporateClientTwo.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientTwo.setParent(companyOne);

		assertTrue("equals() not implemented correctly", corporateClientOne
				.equals(corporateClientTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#equals(java.lang.Object)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testEquals_RecognizesUnequalCorporateClient()
			throws RolePreconditionsNotMetException, IllegalArgumentException {
		final Company companyOne = new Company(1L, 5);
		companyOne.setName("companyOne");
		companyOne.setCompanyNumber("COMP-11-111-11");

		final Company companyTwo = new Company(2L, 8);
		companyOne.setName("companyTwo");
		companyOne.setCompanyNumber("COMP-11-111-22");

		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientOne.setParent(companyOne);

		final CorporateClient corporateClientTwo = new CorporateClient(1L, 5);
		corporateClientTwo.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientTwo.setParent(companyTwo);

		assertFalse("equals() not implemented correctly", corporateClientOne
				.equals(corporateClientTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.company.customer.CorporateClient#hashCode()}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testHashCode_ProducesIdenticalHashCodeForEqualCorporateClients()
			throws RolePreconditionsNotMetException, IllegalArgumentException {
		final Company companyOne = new Company(1L, 5);
		companyOne.setName("companyOne");
		companyOne.setCompanyNumber("COMP-11-111-11");

		final CorporateClient corporateClientOne = new CorporateClient(1L, 5);
		corporateClientOne.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientOne.setParent(companyOne);

		final CorporateClient corporateClientTwo = new CorporateClient(1L, 5);
		corporateClientTwo.setCorporateClientNumber("CORPC-22-222-22");
		corporateClientTwo.setParent(companyOne);

		assertEquals("hashCode() not implemented correctly", corporateClientOne
				.hashCode(), corporateClientTwo.hashCode());
	}
}
