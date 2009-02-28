/**
 * 
 */
package com.acme.orderplacement.persistence.support.jta.hibernate;

import org.hibernate.transaction.JNDITransactionManagerLookup;
import org.hibernate.transaction.TransactionManagerLookup;

/**
 * <p>
 * TODO: Insert short summary for GeronimoTransactionManagerLookup
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class GeronimoTransactionManagerLookup extends
		JNDITransactionManagerLookup implements TransactionManagerLookup {

	/**
	 * @see org.hibernate.transaction.JNDITransactionManagerLookup#getName()
	 */
	@Override
	protected String getName() {

		return "java:/TransactionManager";
	}

	/**
	 * @see org.hibernate.transaction.TransactionManagerLookup#getUserTransactionName()
	 */
	public String getUserTransactionName() {

		return "java:comp/UserTransaction";
	}

}
