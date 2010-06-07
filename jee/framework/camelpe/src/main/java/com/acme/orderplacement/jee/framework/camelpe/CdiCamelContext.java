/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.Registry;
import org.apache.commons.lang.Validate;

import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiInjector;
import com.acme.orderplacement.jee.framework.camelpe.camel.spi.CdiRegistry;

/**
 * <p>
 * TODO: Insert short summary for CdiCamelContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
/**
 * <p>
 * TODO: Insert short summary for CdiCamelContext
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class CdiCamelContext extends DefaultCamelContext {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final BeanManager beanManager;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	CdiCamelContext(final BeanManager beanManager)
			throws IllegalArgumentException {
		Validate.notNull(beanManager, "beanManager");
		this.beanManager = beanManager;
	}

	// -------------------------------------------------------------------------
	// Implementation methods
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#createInjector()
	 */
	@Override
	protected Injector createInjector() {
		return new CdiInjector(this.beanManager);
	}

	/**
	 * @see org.apache.camel.impl.DefaultCamelContext#createRegistry()
	 */
	@Override
	protected Registry createRegistry() {
		return new CdiRegistry(this.beanManager);
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CDI CamelContext [name = " + getName() + " | beanManager = "
				+ this.beanManager + "]";
	}
}
