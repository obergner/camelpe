/**
 * 
 */
package net.camelpe.weld.camelspi.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.camelpe.weld.requestcontext.WeldRequestContext;

/**
 * <p>
 * TODO: Insert short summary for WeldRequestContextInitiatingThreadPoolExecutor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class WeldRequestContextInitiatingThreadPoolExecutor extends ThreadPoolExecutor {

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler
     */
    WeldRequestContextInitiatingThreadPoolExecutor(final int corePoolSize,
            final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BlockingQueue<Runnable> workQueue,
            final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    WeldRequestContextInitiatingThreadPoolExecutor(final int corePoolSize,
            final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BlockingQueue<Runnable> workQueue,
            final ThreadFactory threadFactory,
            final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     */
    WeldRequestContextInitiatingThreadPoolExecutor(final int corePoolSize,
            final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BlockingQueue<Runnable> workQueue,
            final ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    WeldRequestContextInitiatingThreadPoolExecutor(final int corePoolSize,
            final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
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
