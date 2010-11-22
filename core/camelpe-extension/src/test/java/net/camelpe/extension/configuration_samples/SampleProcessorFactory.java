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

package net.camelpe.extension.configuration_samples;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.RouteContext;

/**
 * <p>
 * TODO: Insert short summary for SampleProcessorFactory
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
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
