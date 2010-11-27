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

package net.camelpe.weld.requestcontext;

import java.util.UUID;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.api.Lifecycle;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.api.BeanStore;
import org.jboss.weld.context.beanstore.HashMapBeanStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Encapsulates a request context <code>id</code>, its {@link BeanStore
 * <code>BeanStore</code>} and its {@link Lifecycle <code>Lifecycle</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class WeldRequestContext {

	// -------------------------------------------------------------------------
	// Static
	// -------------------------------------------------------------------------

	private static final ThreadLocal<WeldRequestContext> PER_THREAD_WELD_REQUEST_CONTEXT = new ThreadLocal<WeldRequestContext>() {
		@Override
		protected WeldRequestContext initialValue() {
			return WeldRequestContext.newRequestContext();
		}
	};

	/**
	 * <p>
	 * Associates a Weld RequestContext with the calling thread.
	 * </p>
	 * <p>
	 * <strong>IMPORTANT</strong>: MUST ONLY BE CALLED FROM THE THREAD THAT
	 * NEEDS A REQUEST CONTEXT.
	 * </p>
	 */
	public static void begin() {
		PER_THREAD_WELD_REQUEST_CONTEXT.get().beginInternal();
	}

	/**
	 * <p>
	 * Removes the Weld RequestContext from the calling thread.
	 * </p>
	 * <p>
	 * <strong>IMPORTANT</strong>: MUST ONLY BE CALLED FROM THE THREAD WHOSE
	 * REQUEST CONTEXT NEEDS TO BE DESTROYED.
	 * </p>
	 */
	public static void end() {
		if (PER_THREAD_WELD_REQUEST_CONTEXT.get().isActiveInternal()) {
			PER_THREAD_WELD_REQUEST_CONTEXT.get().endInternal();
		}
		PER_THREAD_WELD_REQUEST_CONTEXT.remove();
	}

	/**
	 * <p>
	 * Removes the current Weld RequestContext (if any) from the calling thread
	 * and associates a new Weld Request Context with the calling thread.
	 * </p>
	 * <p>
	 * <strong>IMPORTANT</strong>: MUST ONLY BE CALLED FROM THE THREAD THAT
	 * NEEDS A REQUEST CONTEXT.
	 * </p>
	 */
	public static void endThenBegin() {
		end();
		begin();
	}

	private static WeldRequestContext newRequestContext() {
		final String id = "urn:weld-request-context:tid-"
		        + Thread.currentThread().getId() + "@" + UUID.randomUUID();
		final BeanStore beanStore = new HashMapBeanStore();
		final Lifecycle lifecycle = Container.instance().services()
		        .get(ContextLifecycle.class);

		return new WeldRequestContext(id, beanStore, lifecycle);
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String id;

	private final BeanStore beanStore;

	private final Lifecycle lifecycle;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private WeldRequestContext(final String id, final BeanStore beanStore,
	        final Lifecycle lifecycle) {
		this.id = id;
		this.beanStore = beanStore;
		this.lifecycle = lifecycle;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	private void beginInternal() throws IllegalStateException {
		if (isActiveInternal()) {
			throw new IllegalStateException(
			        "There is already a Weld RequestContext active on thread [ID = "
			                + Thread.currentThread().getId() + " | Name = "
			                + Thread.currentThread().getName() + "]");
		}
		this.lifecycle.beginRequest(this.id, this.beanStore);
		this.log.trace(
		        ">>>>> Weld RequestContext [ID = {}] started on Thread [ID = {} | Name = {}]",
		        new Object[] { this.id, Thread.currentThread().getId(),
		                Thread.currentThread().getName() });
	}

	private void endInternal() throws IllegalStateException {
		if (!isActiveInternal()) {
			throw new IllegalStateException(
			        "There is no active Weld RequestContext on thread [ID = "
			                + Thread.currentThread().getId() + " | Name = "
			                + Thread.currentThread().getName() + "]");
		}
		this.lifecycle.endRequest(this.id, this.beanStore);
		this.log.trace(
		        "<<<<< Weld RequestContext [ID = {}] terminated on Thread [ID = {} | Name = {}]",
		        new Object[] { this.id, Thread.currentThread().getId(),
		                Thread.currentThread().getName() });
	}

	private boolean isActiveInternal() {
		return this.lifecycle.isRequestActive();
	}
}