/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.camelpe.weld.requestcontext.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import net.camelpe.weld.requestcontext.WeldRequestContext;

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
        WeldRequestContext.begin();

        super.beforeExecute(t, r);
    }

    /**
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
     *      java.lang.Throwable)
     */
    @Override
    protected void afterExecute(final Runnable r, final Throwable t) {
        super.afterExecute(r, t);

        WeldRequestContext.end();
    }
}
