/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.acme.orderplacement.jee.framework.camel.CamelEngine;

/**
 * <p>
 * TODO: Insert short summary for CamelEngineStartupBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Singleton
@Startup
public class CamelEngineStartupBean {

	@Inject
	private CamelEngine camelEngine;

	@PostConstruct
	public void start() throws Exception {
		this.camelEngine.initialize();
		this.camelEngine.start();
	}

	@PreDestroy
	public void stop() throws Exception {
		this.camelEngine.stop();
	}
}
