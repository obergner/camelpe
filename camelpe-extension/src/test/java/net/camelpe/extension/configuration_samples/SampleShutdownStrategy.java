/**
 * 
 */
package net.camelpe.extension.configuration_samples;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.camelpe.api.CamelContextModifying;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.RouteStartupOrder;
import org.apache.camel.spi.ShutdownStrategy;

/**
 * <p>
 * TODO: Insert short summary for SampleShutdownStrategy
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SampleShutdownStrategy implements ShutdownStrategy {

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#getTimeUnit()
     */
    @Override
    public TimeUnit getTimeUnit() {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#getTimeout()
     */
    @Override
    public long getTimeout() {
        return 0;
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#isShutdownNowOnTimeout()
     */
    @Override
    public boolean isShutdownNowOnTimeout() {
        return false;
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#isShutdownRoutesInReverseOrder()
     */
    @Override
    public boolean isShutdownRoutesInReverseOrder() {
        return false;
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#setShutdownNowOnTimeout(boolean)
     */
    @Override
    public void setShutdownNowOnTimeout(final boolean shutdownNowOnTimeout) {
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#setShutdownRoutesInReverseOrder(boolean)
     */
    @Override
    public void setShutdownRoutesInReverseOrder(
            final boolean shutdownRoutesInReverseOrder) {
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#setTimeUnit(java.util.concurrent.TimeUnit)
     */
    @Override
    public void setTimeUnit(final TimeUnit timeUnit) {
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#setTimeout(long)
     */
    @Override
    public void setTimeout(final long timeout) {
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#shutdown(org.apache.camel.CamelContext,
     *      java.util.List)
     */
    @Override
    public void shutdown(final CamelContext context,
            final List<RouteStartupOrder> routes) throws Exception {
    }

    /**
     * @see org.apache.camel.spi.ShutdownStrategy#shutdown(org.apache.camel.CamelContext,
     *      java.util.List, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public void shutdown(final CamelContext context,
            final List<RouteStartupOrder> routes, final long timeout,
            final TimeUnit timeUnit) throws Exception {
    }

    /**
     * @see org.apache.camel.Service#start()
     */
    @Override
    public void start() throws Exception {
    }

    /**
     * @see org.apache.camel.Service#stop()
     */
    @Override
    public void stop() throws Exception {
    }

}
