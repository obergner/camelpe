/**
 * 
 */
package net.camelpe.extension.advanced_samples;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * <p>
 * TODO: Insert short summary for AdvancedProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
public class AdvancedProcessor implements Processor {

    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(final Exchange exchange) throws Exception {
        this.counter.incrementAndGet();
    }

    public AtomicInteger getCounter() {
        return this.counter;
    }
}
