/**
 * 
 */
package net.camelpe.extension.configuration_samples;

import net.camelpe.api.CamelContextModifying;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.spi.InflightRepository;

/**
 * <p>
 * TODO: Insert short summary for SampleInflightRepository
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SampleInflightRepository implements InflightRepository {

    /**
     * @see org.apache.camel.spi.InflightRepository#add(org.apache.camel.Exchange)
     */
    @Override
    public void add(final Exchange exchange) {
    }

    /**
     * @see org.apache.camel.spi.InflightRepository#remove(org.apache.camel.Exchange)
     */
    @Override
    public void remove(final Exchange exchange) {
    }

    /**
     * @see org.apache.camel.spi.InflightRepository#size()
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * @see org.apache.camel.spi.InflightRepository#size(org.apache.camel.Endpoint)
     */
    @Override
    public int size(final Endpoint endpoint) {
        return 0;
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
