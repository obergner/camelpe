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

package net.camelpe.extension.util;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.ResolutionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * A small utility class that serves as a <i>handle</i> for a CDI bean deployed in a 
 * {@link BeanManager <code>BeanManager</code>}. It simply encapsulates the logic for
 * looking up a <strong>unique</strong> bean in a <code>BeanManager</code>. 
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class BeanReference<T> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final BeanManager beanManager;

	private final Class<T> beanType;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public BeanReference(final Class<T> beanType, final BeanManager beanManager)
	        throws IllegalArgumentException {
		Validate.notNull(beanType, "beanType");
		Validate.notNull(beanManager, "beanManager");
		this.beanType = beanType;
		this.beanManager = beanManager;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public T get() throws ResolutionException {
		final Set<Bean<?>> matchingBeans = this.beanManager
		        .getBeans(this.beanType);
		if (matchingBeans.size() < 1) {
			throw new UnsatisfiedResolutionException("Could not find any ["
			        + this.beanType.getName() + "] Bean in BeanManager ["
			        + this.beanManager + "]");
		}
		final Bean<?> uniqueMatchingBean = this.beanManager
		        .resolve(matchingBeans);
		final CreationalContext<?> creationalContext = this.beanManager
		        .createCreationalContext(uniqueMatchingBean);

		return this.beanType.cast(this.beanManager.getReference(
		        uniqueMatchingBean, this.beanType, creationalContext));
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
		        * result
		        + ((this.beanManager == null) ? 0 : this.beanManager.hashCode());
		result = prime * result
		        + ((this.beanType == null) ? 0 : this.beanType.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BeanReference<?> other = (BeanReference<?>) obj;
		if (this.beanManager == null) {
			if (other.beanManager != null) {
				return false;
			}
		} else if (!this.beanManager.equals(other.beanManager)) {
			return false;
		}
		if (this.beanType == null) {
			if (other.beanType != null) {
				return false;
			}
		} else if (!this.beanType.equals(other.beanType)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BeanReference@" + this.hashCode() + "[beanManager = "
		        + this.beanManager + "|beanType = " + this.beanType + "]";
	}
}
