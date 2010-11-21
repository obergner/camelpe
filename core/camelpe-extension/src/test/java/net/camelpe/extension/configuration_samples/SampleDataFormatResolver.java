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

package net.camelpe.extension.configuration_samples;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;

/**
 * <p>
 * TODO: Insert short summary for SampleDataFormatResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
public class SampleDataFormatResolver implements DataFormatResolver {

    /**
     * @see org.apache.camel.spi.DataFormatResolver#resolveDataFormat(java.lang.String,
     *      org.apache.camel.CamelContext)
     */
    @Override
    public DataFormat resolveDataFormat(final String name,
            final CamelContext context) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.DataFormatResolver#resolveDataFormatDefinition(java.lang.String,
     *      org.apache.camel.CamelContext)
     */
    @Override
    public DataFormatDefinition resolveDataFormatDefinition(final String name,
            final CamelContext context) {
        return null;
    }
}
