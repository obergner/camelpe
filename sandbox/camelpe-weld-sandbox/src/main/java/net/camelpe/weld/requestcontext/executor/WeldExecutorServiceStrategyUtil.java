/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.model.ExecutorServiceAwareDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * <p>
 * TODO: Insert short summary for WeldExecutorServiceStrategyUtil
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
final class WeldExecutorServiceStrategyUtil {

    /**
     * <p>
     * TODO: Insert short summary for NamingAwareThreadFactory
     * </p>
     * 
     * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
     * 
     */
    private static final class NamingAwareThreadFactory implements
            ThreadFactory {

        private final boolean daemon;

        private final String name;

        private final String pattern;

        NamingAwareThreadFactory(final boolean daemon, final String name,
                final String pattern) {
            this.daemon = daemon;
            this.name = name;
            this.pattern = pattern;
        }

        @Override
        public Thread newThread(final Runnable r) {
            final Thread answer = new Thread(r, getThreadName(this.pattern,
                    this.name));
            answer.setDaemon(this.daemon);
            return answer;
        }
    }

    private static final String DEFAULT_PATTERN = "WELD Camel Thread ${counter} - ${name}";

    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();

    private WeldExecutorServiceStrategyUtil() {
    }

    private static int nextThreadCounter() {
        return THREAD_COUNTER.getAndIncrement();
    }

    /**
     * Creates a new thread name with the given prefix
     * 
     * @param pattern
     *            the pattern
     * @param name
     *            the name
     * @return the thread name, which is unique
     */
    static String getThreadName(final String pattern, final String name) {
        final String normalizedPattern = pattern != null ? pattern
                : DEFAULT_PATTERN;

        // we support ${longName} and ${name} as name placeholders
        final String longName = name;
        final String shortName = name.contains("?") ? ObjectHelper.before(name,
                "?") : name;

        String answer = normalizedPattern.replaceFirst("\\$\\{counter\\}", ""
                + nextThreadCounter());
        answer = answer.replaceFirst("\\$\\{longName\\}", longName);
        answer = answer.replaceFirst("\\$\\{name\\}", shortName);
        if ((answer.indexOf("$") > -1) || (answer.indexOf("${") > -1)
                || (answer.indexOf("}") > -1)) {
            throw new IllegalArgumentException("Pattern is invalid: "
                    + normalizedPattern);
        }

        return answer;
    }

    /**
     * Creates a new scheduled thread pool which can schedule threads.
     * 
     * @param poolSize
     *            the core pool size
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param daemon
     *            whether the threads is daemon or not
     * @return the created pool
     */
    static ScheduledExecutorService newScheduledThreadPool(final int poolSize,
            final String pattern, final String name, final boolean daemon) {
        return new WeldRequestContextInitiatingScheduledThreadPoolExecutor(
                poolSize, new NamingAwareThreadFactory(daemon, name, pattern));
    }

    /**
     * Creates a new fixed thread pool
     * 
     * @param poolSize
     *            the fixed pool size
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param daemon
     *            whether the threads is daemon or not
     * @return the created pool
     */
    static ExecutorService newFixedThreadPool(final int poolSize,
            final String pattern, final String name, final boolean daemon) {
        return new WeldRequestContextInitiatingThreadPoolExecutor(poolSize,
                poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamingAwareThreadFactory(daemon, name, pattern));
    }

    /**
     * Creates a new single thread pool (usually for background tasks)
     * 
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param daemon
     *            whether the threads is daemon or not
     * @return the created pool
     */
    static ExecutorService newSingleThreadExecutor(final String pattern,
            final String name, final boolean daemon) {
        return new FinalizableDelegatedExecutorService(
                new WeldRequestContextInitiatingThreadPoolExecutor(1, 1, 0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new NamingAwareThreadFactory(daemon, name, pattern)));
    }

    /**
     * Creates a new cached thread pool
     * 
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param daemon
     *            whether the threads is daemon or not
     * @return the created pool
     */
    static ExecutorService newCachedThreadPool(final String pattern,
            final String name, final boolean daemon) {
        return new WeldRequestContextInitiatingThreadPoolExecutor(0,
                Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new NamingAwareThreadFactory(
                        daemon, name, pattern));
    }

    /**
     * Creates a new custom thread pool using 60 seconds as keep alive and with
     * an unbounded queue.
     * 
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param corePoolSize
     *            the core size
     * @param maxPoolSize
     *            the maximum pool size
     * @return the created pool
     */
    static ExecutorService newThreadPool(final String pattern,
            final String name, final int corePoolSize, final int maxPoolSize) {
        return newThreadPool(pattern, name, corePoolSize, maxPoolSize, 60,
                TimeUnit.SECONDS, -1,
                new ThreadPoolExecutor.CallerRunsPolicy(), true);
    }

    /**
     * Creates a new custom thread pool
     * 
     * @param pattern
     *            pattern of the thread name
     * @param name
     *            ${name} in the pattern name
     * @param corePoolSize
     *            the core size
     * @param maxPoolSize
     *            the maximum pool size
     * @param keepAliveTime
     *            keep alive time
     * @param timeUnit
     *            keep alive time unit
     * @param maxQueueSize
     *            the maximum number of tasks in the queue, use
     *            <tt>Integer.MAX_VALUE</tt> or <tt>-1</tt> to indicate
     *            unbounded
     * @param rejectedExecutionHandler
     *            the handler for tasks which cannot be executed by the thread
     *            pool. If <tt>null</tt> is provided then
     *            {@link java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
     *            CallerRunsPolicy} is used.
     * @param daemon
     *            whether the threads is daemon or not
     * @return the created pool
     * @throws IllegalArgumentException
     *             if parameters is not valid
     */
    static ExecutorService newThreadPool(final String pattern,
            final String name, final int corePoolSize, final int maxPoolSize,
            final long keepAliveTime, final TimeUnit timeUnit,
            final int maxQueueSize,
            final RejectedExecutionHandler rejectedExecutionHandler,
            final boolean daemon) {

        // validate max >= core
        if (maxPoolSize < corePoolSize) {
            throw new IllegalArgumentException(
                    "MaxPoolSize must be >= corePoolSize, was " + maxPoolSize
                            + " >= " + corePoolSize);
        }

        final BlockingQueue<Runnable> queue;
        if ((corePoolSize == 0) && (maxQueueSize <= 0)) {
            // use a synchronous so we can act like the cached thread pool
            queue = new SynchronousQueue<Runnable>();
        } else if (maxQueueSize <= 0) {
            // unbounded task queue
            queue = new LinkedBlockingQueue<Runnable>();
        } else {
            // bounded task queue
            queue = new LinkedBlockingQueue<Runnable>(maxQueueSize);
        }
        final ThreadPoolExecutor answer = new WeldRequestContextInitiatingThreadPoolExecutor(
                corePoolSize, maxPoolSize, keepAliveTime, timeUnit, queue);
        answer.setThreadFactory(new NamingAwareThreadFactory(daemon, name,
                pattern));
        answer.setRejectedExecutionHandler(rejectedExecutionHandler != null ? rejectedExecutionHandler
                : new ThreadPoolExecutor.CallerRunsPolicy());

        return answer;
    }

    /**
     * Will lookup and get the configured
     * {@link java.util.concurrent.ExecutorService} from the given definition.
     * <p/>
     * This method will lookup for configured thread pool in the following order
     * <ul>
     * <li>from the definition if any explicit configured executor service.</li>
     * <li>from the {@link org.apache.camel.spi.Registry} if found</li>
     * <li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile
     * ThreadPoolProfile(s)}.</li>
     * <li>if none found, then <tt>null</tt> is returned.</li>
     * </ul>
     * The various {@link ExecutorServiceAwareDefinition} should use this helper
     * method to ensure they support configured executor services in the same
     * coherent way.
     * 
     * @param routeContext
     *            the rout context
     * @param name
     *            name which is appended to the thread name, when the
     *            {@link java.util.concurrent.ExecutorService} is created based
     *            on a {@link org.apache.camel.spi.ThreadPoolProfile}.
     * @param definition
     *            the node definition which may leverage executor service.
     * @return the configured executor service, or <tt>null</tt> if none was
     *         configured.
     * @throws IllegalArgumentException
     *             is thrown if lookup of executor service in
     *             {@link org.apache.camel.spi.Registry} was not found
     */
    static ExecutorService getConfiguredExecutorService(
            final RouteContext routeContext,
            final String name,
            final ExecutorServiceAwareDefinition<? extends ProcessorDefinition<?>> definition)
            throws IllegalArgumentException {
        final ExecutorServiceStrategy strategy = routeContext.getCamelContext()
                .getExecutorServiceStrategy();
        ObjectHelper.notNull(strategy, "ExecutorServiceStrategy",
                routeContext.getCamelContext());

        // prefer to use explicit configured executor on the definition
        if (definition.getExecutorService() != null) {
            return definition.getExecutorService();
        } else if (definition.getExecutorServiceRef() != null) {
            final ExecutorService answer = strategy.lookup(definition, name,
                    definition.getExecutorServiceRef());
            if (answer == null) {
                throw new IllegalArgumentException("ExecutorServiceRef "
                        + definition.getExecutorServiceRef()
                        + " not found in registry.");
            }
            return answer;
        }

        return null;
    }

    /**
     * Will lookup and get the configured
     * {@link java.util.concurrent.ScheduledExecutorService} from the given
     * definition.
     * <p/>
     * This method will lookup for configured thread pool in the following order
     * <ul>
     * <li>from the definition if any explicit configured executor service.</li>
     * <li>from the {@link org.apache.camel.spi.Registry} if found</li>
     * <li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile
     * ThreadPoolProfile(s)}.</li>
     * <li>if none found, then <tt>null</tt> is returned.</li>
     * </ul>
     * The various {@link ExecutorServiceAwareDefinition} should use this helper
     * method to ensure they support configured executor services in the same
     * coherent way.
     * 
     * @param routeContext
     *            the rout context
     * @param name
     *            name which is appended to the thread name, when the
     *            {@link java.util.concurrent.ExecutorService} is created based
     *            on a {@link org.apache.camel.spi.ThreadPoolProfile}.
     * @param definition
     *            the node definition which may leverage executor service.
     * @return the configured executor service, or <tt>null</tt> if none was
     *         configured.
     * @throws IllegalArgumentException
     *             is thrown if lookup of executor service in
     *             {@link org.apache.camel.spi.Registry} was not found or the
     *             found instance is not a ScheduledExecutorService type.
     */
    static ScheduledExecutorService getConfiguredScheduledExecutorService(
            final RouteContext routeContext,
            final String name,
            final ExecutorServiceAwareDefinition<? extends ProcessorDefinition<?>> definition)
            throws IllegalArgumentException {
        final ExecutorServiceStrategy strategy = routeContext.getCamelContext()
                .getExecutorServiceStrategy();
        ObjectHelper.notNull(strategy, "ExecutorServiceStrategy",
                routeContext.getCamelContext());

        // prefer to use explicit configured executor on the definition
        if (definition.getExecutorService() != null) {
            final ExecutorService executorService = definition
                    .getExecutorService();
            if (executorService instanceof ScheduledExecutorService) {
                return (ScheduledExecutorService) executorService;
            }
            throw new IllegalArgumentException("ExecutorServiceRef "
                    + definition.getExecutorServiceRef()
                    + " is not an ScheduledExecutorService instance");
        } else if (definition.getExecutorServiceRef() != null) {
            final ScheduledExecutorService answer = strategy.lookupScheduled(
                    definition, name, definition.getExecutorServiceRef());
            if (answer == null) {
                throw new IllegalArgumentException("ExecutorServiceRef "
                        + definition.getExecutorServiceRef()
                        + " not found in registry.");
            }
            return answer;
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Copied from java.util.concurrent.Executors since the original wrapper is
    // defined as having only package visibility
    // -------------------------------------------------------------------------

    /**
     * A wrapper class that exposes only the ExecutorService methods of an
     * ExecutorService implementation.
     */
    static class FinalizableDelegatedExecutorService extends
            AbstractExecutorService {
        private final ExecutorService delegate;

        FinalizableDelegatedExecutorService(final ExecutorService executor) {
            this.delegate = executor;
        }

        @Override
        public void execute(final Runnable command) {
            this.delegate.execute(command);
        }

        @Override
        public void shutdown() {
            this.delegate.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        @Override
        public boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        @Override
        public boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit)
                throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }

        @Override
        public Future<?> submit(final Runnable task) {
            return this.delegate.submit(task);
        }

        @Override
        public <T> Future<T> submit(final Callable<T> task) {
            return this.delegate.submit(task);
        }

        @Override
        public <T> Future<T> submit(final Runnable task, final T result) {
            return this.delegate.submit(task, result);
        }

        @Override
        public <T> List<Future<T>> invokeAll(
                final Collection<? extends Callable<T>> tasks)
                throws InterruptedException {
            return this.delegate.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(
                final Collection<? extends Callable<T>> tasks,
                final long timeout, final TimeUnit unit)
                throws InterruptedException {
            return this.delegate.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
                throws InterruptedException, ExecutionException {
            return this.delegate.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(final Collection<? extends Callable<T>> tasks,
                final long timeout, final TimeUnit unit)
                throws InterruptedException, ExecutionException,
                TimeoutException {
            return this.delegate.invokeAny(tasks, timeout, unit);
        }

        @Override
        protected void finalize() {
            shutdown();
        }
    }
}
