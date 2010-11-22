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
@CamelContextInjectable
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
