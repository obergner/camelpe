/**
 * 
 */
package de.obergner.soa.order.domain.people.employee;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.employee.Employee;
import com.acme.orderplacement.domain.support.exception.RolePreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Employee <code>Employee</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class EmployeeTest extends TestCase {

	/**
	 * @param name
	 */
	public EmployeeTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#setEmployeeNumber(java.lang.String)}
	 * .
	 */
	public final void testSetEmployeeNumber_RejectsNullEmployeeNumber() {
		try {
			final Employee classUnderTest = new Employee();
			classUnderTest.setEmployeeNumber(null);
			fail("setEmployeeNumber(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#setEmployeeNumber(java.lang.String)}
	 * .
	 */
	public final void testSetEmployeeNumber_RejectsBlankEmployeeNumber() {
		try {
			final Employee classUnderTest = new Employee();
			classUnderTest.setEmployeeNumber("");
			fail("setEmployeeNumber(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#setDepartment(com.acme.orderplacement.domain.people.employee.Department)}
	 * .
	 */
	public final void testSetDepartment_RejectsNullDepartment() {
		try {
			final Employee classUnderTest = new Employee();
			classUnderTest.setDepartment(null);
			fail("setDepartment(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#setWorkEmailAddress(com.acme.orderplacement.domain.people.contact.EmailAddress)}
	 * .
	 */
	public final void testSetWorkEmailAddress_RejectsNullEmailAddress() {
		try {
			final Employee classUnderTest = new Employee();
			classUnderTest.setWorkEmailAddress(null);
			fail("setWorkEmailAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#setParent(com.acme.orderplacement.domain.people.person.Person)}
	 * .
	 * 
	 * @throws RolePreconditionsNotMetException
	 */
	public final void testSetParent_RejectsNullParent()
			throws RolePreconditionsNotMetException {
		try {
			final Employee classUnderTest = new Employee();
			classUnderTest.setParent(null);
			fail("setParent(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualEmployees() {
		final Employee employeeOne = new Employee();
		employeeOne.setEmployeeNumber("12345");

		final Employee employeeTwo = new Employee();
		employeeTwo.setEmployeeNumber("12345");

		assertTrue("equals() not implemented correctly", employeeOne
				.equals(employeeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalEmployees() {
		final Employee employeeOne = new Employee();
		employeeOne.setEmployeeNumber("12345");

		final Employee employeeTwo = new Employee();
		employeeTwo.setEmployeeNumber("123456");

		assertFalse("equals() not implemented correctly", employeeOne
				.equals(employeeTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.employee.Employee#hashCode()}
	 * .
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualEmployees() {
		final Employee employeeOne = new Employee();
		employeeOne.setEmployeeNumber("12345");

		final Employee employeeTwo = new Employee();
		employeeTwo.setEmployeeNumber("12345");

		assertEquals("hashCode() not implemented correctly", employeeOne
				.hashCode(), employeeTwo.hashCode());
	}

}
