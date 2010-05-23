/**
 * 
 */
package com.acme.orderplacement.framework.persistence.common.jta.hibernate;

import org.hibernate.transaction.JNDITransactionManagerLookup;
import org.hibernate.transaction.TransactionManagerLookup;

/**
 * <p>
 * <tt>Geronimo</tt> specific <tt>Hibernate</tt>
 * {@link TransactionManagerLookup <code>TransactionManagerLookup</code>}
 * implementation used to access the <code>JTA</code>
 * {@link javax.transaction.UserTransaction
 * <code>javax.transaction.UserTransaction</code>} and
 * {@link javax.transaction.TransactionManager
 * <code>javax.transaction.TransactionManager</code>} components when executing
 * within <tt>Geronimo</tt>.
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
