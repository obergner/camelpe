/**
 * 
 */
package com.acme.orderplacement.integration.inbound.support.interceptor;

import java.util.Date;

/**
 * <p>
 * An <code>MBean</code> interface for counting
 * {@link org.springframework.integration.core.Message
 * <code>org.springframework.integration.core.Message</code>}s received by a
 * [@link org.springframework.integration.core.MessageChannel
 * <code>org.springframework.integration.core.MessageChannel</code> .
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface MessageCounterMBean {

	/**
	 * <p>
	 * Returns the {@link Date <code>Date</code>} when this
	 * <code>MessageCounter</code> has last been reset.
	 * </p>
	 * 
	 * @return The {@link Date <code>Date</code>} when this
	 *         <code>MessageCounter</code> has last been reset
	 */
	Date getLastResetOn();

	/**
	 * <p>
	 * Reset this <code>MessageCounter</code>'s <tt>message count</tt> to
	 * <code>0</code> (zero).
	 * </p>
	 */
	void reset();

	/**
	 * <p>
	 * Return the number of {@link org.springframework.integration.core.Message
	 * <code>Message</code>}s received by the
	 * {@link org.springframework.integration.core.MessageChannel
	 * <code>MessageChannel</code>} this <code>MessageCounter</code> is
	 * responsible for since it has last been {@link #reset()
	 * <code>reset</code>}.
	 * </p>
	 * 
	 * @return The number of
	 *         {@link org.springframework.integration.core.Message
	 *         <code>Message</code>}s received by the
	 *         {@link org.springframework.integration.core.MessageChannel
	 *         <code>MessageChannel</code>} this <code>MessageCounter</code> is
	 *         responsible for since it has last been {@link #reset()
	 *         <code>reset</code>}
	 */
	int getMessageCount();
}
