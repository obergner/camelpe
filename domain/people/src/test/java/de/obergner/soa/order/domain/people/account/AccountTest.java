/**
 * 
 */
package de.obergner.soa.order.domain.people.account;

import junit.framework.TestCase;

import com.acme.orderplacement.domain.people.account.Account;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;

/**
 * <p>
 * A test for {@link Account <code>Account</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AccountTest extends TestCase {

	/**
	 * @param name
	 */
	public AccountTest(final String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setUsername(java.lang.String)}
	 * .
	 */
	public final void testSetUsername_RejectsNullUsername() {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.setUsername(null);
			fail("setUsername(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setPassword(java.lang.String)}
	 * .
	 */
	public final void testSetPassword_RejectsNullPassword() {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.setPassword(null);
			fail("setPassword(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setPasswordLastChangedOn(java.util.Date)}
	 * .
	 */
	public final void testSetPasswordLastChangedOn_RejectsNullDate() {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.setPasswordLastChangedOn(null);
			fail("setPasswordLastChangedOn(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setSecretAnswer(java.lang.String)}
	 * .
	 */
	public final void testSetSecretAnswer_RecognizesNonEmptySecretQuestion() {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.setSecretQuestion("secretQuestion");
			classUnderTest.setSecretAnswer("");
			fail("setSecretAnswer('') did not throw expected "
					+ IllegalStateException.class.getName());
		} catch (final IllegalStateException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setAccountOwner(com.acme.orderplacement.domain.people.person.Person)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetAccountOwner_RejectsNullPerson()
			throws CollaborationPreconditionsNotMetException {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.setAccountOwner(null);
			fail("setAccountOwner(<null>) did not throw expected "
					+ IllegalArgumentException.class.getName());
		} catch (final IllegalArgumentException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#setAccountOwner(com.acme.orderplacement.domain.people.person.Person)}
	 * .
	 * 
	 * @throws CollaborationPreconditionsNotMetException
	 */
	public final void testSetAccountOwner_RejectsNullPersonWhoAlreadyOwnsADifferentAccount() {
		try {
			final Account differentAccount = new Account();
			differentAccount.setUsername("username");
			differentAccount.setPassword("password");
			final Person accountOwner = new Person();
			accountOwner.setFirstName("TEST");
			accountOwner.setLastName("TESTER");
			accountOwner.setAccount(differentAccount);

			final Account classUnderTest = new Account();
			classUnderTest.setAccountOwner(accountOwner);
			fail("setAccountOwner(" + accountOwner
					+ ") did not throw expected "
					+ CollaborationPreconditionsNotMetException.class.getName());
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#getAccountOwner()}
	 * .
	 */
	public final void testGetAccountOwner_RejectsNullPerson() {
		try {
			final Account classUnderTest = new Account();
			classUnderTest.getAccountOwner();
			fail("getAccountOwner(<null>) did not throw expected "
					+ IllegalDomainObjectStateException.class.getName());
		} catch (final IllegalDomainObjectStateException e) {
			// Success
		}
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesEqualAccounts() {
		final Account accountOne = new Account();
		accountOne.setUsername("IDENTICAL_USERNAME");
		accountOne.setPassword("IDENTICAL_PASSWORD");

		final Account accountTwo = new Account();
		accountTwo.setUsername("IDENTICAL_USERNAME");
		accountTwo.setPassword("IDENTICAL_PASSWORD");

		assertTrue("equals() not implemented correctly", accountOne
				.equals(accountTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#equals(java.lang.Object)}
	 * .
	 */
	public final void testEquals_RecognizesUnequalAccounts() {
		final Account accountOne = new Account();
		accountOne.setUsername("USERNAME1");
		accountOne.setPassword("PASSWORD1");

		final Account accountTwo = new Account();
		accountTwo.setUsername("USERNAME2");
		accountTwo.setPassword("PASSWORD2");

		assertFalse("equals() not implemented correctly", accountOne
				.equals(accountTwo));
	}

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.domain.people.account.Account#hashCode()}.
	 */
	public final void testHashCode_ProducesIdenticalHashCodesForEqualAccounts() {
		final Account accountOne = new Account();
		accountOne.setUsername("IDENTICAL_USERNAME");
		accountOne.setPassword("IDENTICAL_PASSWORD");

		final Account accountTwo = new Account();
		accountTwo.setUsername("IDENTICAL_USERNAME");
		accountTwo.setPassword("IDENTICAL_PASSWORD");

		assertEquals("hashCode() not implemented correctly", accountOne
				.hashCode(), accountTwo.hashCode());
	}

}
