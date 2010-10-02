/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.configuration_samples;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.RouteContext;

import com.acme.orderplacement.jee.framework.camelpe.CamelContextModifying;

/**
 * <p>
 * TODO: Insert short summary for SampleProcessorFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextModifying
public class SampleProcessorFactory implements ProcessorFactory {

	/**
	 * @see org.apache.camel.spi.ProcessorFactory#createChildProcessor(org.apache.camel.spi.RouteContext,
	 *      org.apache.camel.model.ProcessorDefinition, boolean)
	 */
	@Override
	public Processor createChildProcessor(final RouteContext routeContext,
			final ProcessorDefinition definition, final boolean mandatory)
			throws Exception {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ProcessorFactory#createProcessor(org.apache.camel.spi.RouteContext,
	 *      org.apache.camel.model.ProcessorDefinition)
	 */
	@Override
	public Processor createProcessor(final RouteContext routeContext,
			final ProcessorDefinition definition) throws Exception {
		return null;
	}
}
