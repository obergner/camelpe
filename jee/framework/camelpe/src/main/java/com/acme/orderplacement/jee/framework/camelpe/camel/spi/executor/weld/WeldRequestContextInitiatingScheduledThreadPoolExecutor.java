/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi.executor.weld;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * <p>
 * TODO: Insert short summary for
 * WeldRequestContextInitiatingScheduledThreadPoolExecutor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingScheduledThreadPoolExecutor extends
		ScheduledThreadPoolExecutor {

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param corePoolSize
	 */
	public WeldRequestContextInitiatingScheduledThreadPoolExecutor(
			final int corePoolSize) {
		super(corePoolSize);
	}

	/**
	 * @param corePoolSize
	 * @param threadFactory
	 */
	public WeldRequestContextInitiatingScheduledThreadPoolExecutor(
			final int corePoolSize, final ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	/**
	 * @param corePoolSize
	 * @param handler
	 */
	public WeldRequestContextInitiatingScheduledThreadPoolExecutor(
			final int corePoolSize, final RejectedExecutionHandler handler) {
		super(corePoolSize, handler);
	}

	/**
	 * @param corePoolSize
	 * @param threadFactory
	 * @param handler
	 */
	public WeldRequestContextInitiatingScheduledThreadPoolExecutor(
			final int corePoolSize, final ThreadFactory threadFactory,
			final RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	// -------------------------------------------------------------------------
	// Implement callbacks for setting up and tearing down a Weld Request
	// Context
	// -------------------------------------------------------------------------

	/**
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread,
	 *      java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(final Thread t, final Runnable r) {
		super.beforeExecute(t, r);

		WeldRequestContext.begin();
	}

	/**
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
	 *      java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(final Runnable r, final Throwable t) {
		WeldRequestContext.end();

		super.afterExecute(r, t);
	}
}
