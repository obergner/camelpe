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

import javax.jms.Session;

import net.camelpe.weld.requestcontext.WeldRequestContext;

import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.component.jms.JmsMessageListenerContainer;

/**
 * <p>
 * TODO: Insert short summary for
 * WeldRequestContextInitiatingSpringMessageListenerContainer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingSpringMessageListenerContainer extends
        JmsMessageListenerContainer {

	public WeldRequestContextInitiatingSpringMessageListenerContainer(
	        final JmsEndpoint endpoint) {
		super(endpoint);
	}

	// -------------------------------------------------------------------------
	// Initiate new Weld RequestContext
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer#messageReceived(java.lang.Object,
	 *      javax.jms.Session)
	 */
	@Override
	protected void messageReceived(final Object invoker, final Session session) {
		super.messageReceived(invoker, session);

		WeldRequestContext.endThenBegin();
	}

	/**
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer#noMessageReceived(java.lang.Object,
	 *      javax.jms.Session)
	 */
	@Override
	protected void noMessageReceived(final Object invoker, final Session session) {
		WeldRequestContext.end();

		super.noMessageReceived(invoker, session);
	}
}
