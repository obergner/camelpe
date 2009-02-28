/**
 * 
 */
package de.obergner.soa.order.domain.people.person;

import java.util.Date;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.account.Account;
import com.acme.orderplacement.domain.people.person.Gender;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.people.person.Salutation;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;

/**
 * <p>
 * A test for {@link Person <code>Person</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class PersonTest extends TestCase {

	/**
	 * @param name
	 */
	public PersonTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setFirstName(java.lang.String)}
	 * .
	 */
	public final void testSetFirstName_RejectsNullFirstName() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setFirstName(null);
			fail("setFirstName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setFirstName(java.lang.String)}
	 * .
	 */
	public final void testSetFirstName_RejectsBlankFirstName() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setFirstName("");
			fail("setFirstName(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setLastName(java.lang.String)}
	 * .
	 */
	public final void testSetLastName_RejectsNullLastName() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setLastName(null);
			fail("setLastName(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setLastName(java.lang.String)}
	 * .
	 */
	public final void testSetLastName_RejectsBlankLastName() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setLastName("");
			fail("setLastName(\"\") did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setDateOfBirth(java.util.Date)}
	 * .
	 */
	public final void testSetDateOfBirth_RejectsNullDate() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setDateOfBirth(null);
			fail("setDateOfBirth(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setSalutation(com.acme.orderplacement.domain.people.person.Salutation)}
	 * .
	 */
	public final void testSetSalutation_RejectsNullSalutation() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setSalutation(null);
			fail("setSalutation(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setGender(com.acme.orderplacement.domain.people.person.Gender)}
	 * .
	 */
	public final void testSetGender_RejectsNullGender() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setGender(null);
			fail("setGender(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setAccount(com.acme.orderplacement.domain.people.account.Account)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetAccount_RejectsNullAccount()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setAccount(null);
			fail("setAccount(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setAccount(com.acme.orderplacement.domain.people.account.Account)}
	 * .
	 * 
	 * @throws IllegalArgumentException
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetAccount_RejectsAnotherPersonsAccount()
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		final Person anotherPerson = new Person();
		anotherPerson.setFirstName("FIRST_NAME");
		final Account anotherPersonsAccount = new Account();
		anotherPerson.setAccount(anotherPersonsAccount);
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setFirstName("FIRST_NAME2");
			classUnderTest.setAccount(anotherPersonsAccount);
			fail("setAccount(anotherPersonsAccount) did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setHomeAddress(com.acme.orderplacement.domain.people.contact.PostalAddress)}
	 * .
	 */
	public final void testSetHomeAddress_RejectsNullHomeAddress() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setHomeAddress(null);
			fail("setHomeAddress(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setPrivateResidentialPhone(com.acme.orderplacement.domain.people.contact.PhoneNumber)}
	 * .
	 */
	public final void testSetPrivateResidentialPhone_RejectsNullPhoneNumber() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setPrivateResidentialPhone(null);
			fail("setPrivateResidentialPhone(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setPrivateMobilePhone(com.acme.orderplacement.domain.people.contact.PhoneNumber)}
	 * .
	 */
	public final void testSetPrivateMobilePhone_RejectsNullPhoneNumber() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setPrivateMobilePhone(null);
			fail("setPrivateMobilePhone(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#setPrivateFax(com.acme.orderplacement.domain.people.contact.PhoneNumber)}
	 * .
	 */
	public final void testSetPrivateFax_RejectsNullPhoneNumber() {
		try {
			final Person classUnderTest = new Person();
			classUnderTest.setPrivateFax(null);
			fail("setPrivateFax(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualPersons() {
		final Date dateOfBirth = new Date();

		final Person personOne = new Person();
		personOne.setFirstName("FIRST_NAME");
		personOne.setLastName("LAST_NAME");
		personOne.setDateOfBirth(dateOfBirth);
		personOne.setGender(Gender.FEMALE);
		personOne.setSalutation(Salutation.MISS);

		final Person personTwo = new Person();
		personTwo.setFirstName("FIRST_NAME");
		personTwo.setLastName("LAST_NAME");
		personTwo.setDateOfBirth(dateOfBirth);
		personTwo.setGender(Gender.FEMALE);
		personTwo.setSalutation(Salutation.MISS);

		assertTrue("equals() not implemented correctly", personOne
				.equals(personTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalPersons() {
		final Date dateOfBirth = new Date();

		final Person personOne = new Person();
		personOne.setFirstName("FIRST_NAME");
		personOne.setLastName("LAST_NAME");
		personOne.setDateOfBirth(dateOfBirth);
		personOne.setGender(Gender.MALE);
		personOne.setSalutation(Salutation.MISTER);

		final Person personTwo = new Person();
		personTwo.setFirstName("FIRST_NAME");
		personTwo.setLastName("LAST_NAME");
		personTwo.setDateOfBirth(dateOfBirth);
		personTwo.setGender(Gender.FEMALE);
		personTwo.setSalutation(Salutation.MISS);

		assertFalse("equals() not implemented correctly", personOne
				.equals(personTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.person.Person#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualPersons() {
		final Date dateOfBirth = new Date();

		final Person personOne = new Person();
		personOne.setFirstName("FIRST_NAME");
		personOne.setLastName("LAST_NAME");
		personOne.setDateOfBirth(dateOfBirth);
		personOne.setGender(Gender.FEMALE);
		personOne.setSalutation(Salutation.MISS);

		final Person personTwo = new Person();
		personTwo.setFirstName("FIRST_NAME");
		personTwo.setLastName("LAST_NAME");
		personTwo.setDateOfBirth(dateOfBirth);
		personTwo.setGender(Gender.FEMALE);
		personTwo.setSalutation(Salutation.MISS);

		assertEquals("hashCode() not implemented correctly", personOne
				.hashCode(), personTwo.hashCode());
	}

}
