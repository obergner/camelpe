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

import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;

/**
 * <p>
 * TODO: Insert short summary for AdvancedRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AdvancedRoutes extends RouteBuilder {

    public static final String ADVANCED_SOURCE_EP = "direct:fullFunctionalitySource";

    public static final String ADVANCED_TARGET_EP = "bean:"
            + AdvancedConsumer.NAME + "?method=consume";

    @Inject
    private AdvancedProcessor processor;

    /**
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        from(ADVANCED_SOURCE_EP).process(this.processor).to(ADVANCED_TARGET_EP);
    }
}
