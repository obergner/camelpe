/**
 * 
 */
package com.acme.orderplacement.integration.inbound.support.internal.interceptor;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.core.Message;
import org.springframework.integration.core.MessageChannel;

import com.acme.orderplacement.integration.inbound.support.interceptor.MessageCounterMBean;

/**
 * <p>
 * TODO: Insert short summary for MessageCounterChannelInterceptor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class MessageCounterChannelInterceptor extends ChannelInterceptorAdapter
		implements MessageCounterMBean {

	private Date lastResetOn = new Date();

	private final AtomicInteger messageCount = new AtomicInteger();

	/**
	 * @see com.acme.orderplacement.integration.inbound.support.interceptor.
	 *      MessageCounterMBean#getLastResetOn()
	 */
	public Date getLastResetOn() {

		return this.lastResetOn;
	}

	/**
	 * @see com.acme.orderplacement.integration.inbound.support.interceptor.
	 *      MessageCounterMBean#getMessageCount()
	 */
	public int getMessageCount() {

		return this.messageCount.get();
	}

	/**
	 * @see com.acme.orderplacement.integration.inbound.support.interceptor.
	 *      MessageCounterMBean#reset()
	 */
	public synchronized void reset() {
		this.messageCount.set(0);
		this.lastResetOn = new Date();
	}

	/**
	 * @see org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter#postSend(org.springframework.integration.core.Message,
	 *      org.springframework.integration.core.MessageChannel, boolean)
	 */
	@Override
	public void postSend(final Message<?> message,
			final MessageChannel channel, final boolean sent) {
		this.messageCount.incrementAndGet();
	}
}
