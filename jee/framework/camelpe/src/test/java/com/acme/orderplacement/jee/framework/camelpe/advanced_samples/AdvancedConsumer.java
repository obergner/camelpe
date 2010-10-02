/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.advanced_samples;

import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * <p>
 * TODO: Insert short summary for AdvancedProducer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@ApplicationScoped
@Named(AdvancedConsumer.NAME)
public class AdvancedConsumer {

	public static final String NAME = "fullConsumer";

	private final AtomicLong timestamp = new AtomicLong(-1L);

	public void consume(final Long timestamp) {
		this.timestamp.set(timestamp.longValue());
	}

	public AtomicLong getTimestamp() {
		return this.timestamp;
	}
}
