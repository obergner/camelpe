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

import java.util.concurrent.ScheduledExecutorService;

import javax.jms.Message;

import net.camelpe.weld.requestcontext.WeldRequestContext;

import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.requestor.Requestor;

/**
 * <p>
 * A {@link org.apache.camel.component.jms.requestor.Requestor
 * <code>Requestor</code>} subclass that takes care of setting up a Weld
 * RequestContext before processing each message and tearing it down afterwards.
 * </p>
 * <p>
 * <strong>IMPORTANT</strong> This class is rarely usable. There is generally no
 * tried and true way of telling Apache Camel to use a custom
 * <code>Requestor</code> in each and every case. The <code>Requestor</code> set
 * on Camel's {@link org.apache.camel.component.jms.JmsComponent
 * <code>JmsComponent</code>} is only used by concrete
 * {@link org.apache.camel.component.jms.JmsEndpoint <code>JmsEndpoint</code>}s
 * if the {@link org.apache.camel.component.jms.JmsProducer.RequestorAffinity
 * <code>RequestorAffinity</code>} is set to <code>PER_COMPONENT</code>. This,
 * however, is <strong>not</strong> the default.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingRequestor extends Requestor {

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param delegate
	 */
	public WeldRequestContextInitiatingRequestor(
	        final JmsConfiguration configuration,
	        final ScheduledExecutorService executorService)
	        throws IllegalArgumentException {
		super(configuration, executorService);
	}

	// -------------------------------------------------------------------------
	// org.apache.camel.component.jms.requestor.Requestor
	// -------------------------------------------------------------------------

	/**
	 * @param message
	 * @see org.apache.camel.component.jms.requestor.Requestor#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(final Message message) {
		try {
			WeldRequestContext.begin();
			super.onMessage(message);
		} finally {
			WeldRequestContext.end();
		}
	}
}
