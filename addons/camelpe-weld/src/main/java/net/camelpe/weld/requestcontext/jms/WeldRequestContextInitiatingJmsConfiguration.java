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

package net.camelpe.weld.requestcontext.jms;

import java.util.concurrent.Executor;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.commons.lang.Validate;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

/**
 * <p>
 * TODO: Insert short summary for WeldRequestContextInitiatingJmsConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingJmsConfiguration extends
        JmsConfiguration {

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public WeldRequestContextInitiatingJmsConfiguration() {
    }

    public WeldRequestContextInitiatingJmsConfiguration(
            final ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    // -------------------------------------------------------------------------
    // Additional properties
    // -------------------------------------------------------------------------

    public void setExecutor(final Executor executor)
            throws IllegalArgumentException {
        Validate.notNull(executor, "executor");
        setTaskExecutor(new TaskExecutorAdapter(executor));
    }

    // -------------------------------------------------------------------------
    // Make Spring use our custom MessageListenerContainer
    // -------------------------------------------------------------------------

    /**
     * @see org.apache.camel.component.jms.JmsConfiguration#chooseMessageListenerContainerImplementation()
     */
    @Override
    public AbstractMessageListenerContainer chooseMessageListenerContainerImplementation() {
        return new WeldRequestContextInitiatingSpringMessageListenerContainer();
    }
}
