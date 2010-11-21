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

package net.camelpe.extension.advanced_samples;

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
