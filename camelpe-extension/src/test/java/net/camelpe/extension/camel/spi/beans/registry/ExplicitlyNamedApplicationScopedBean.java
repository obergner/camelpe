/**
 * 
 */
package net.camelpe.extension.camel.spi.beans.registry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * <p>
 * TODO: Insert short summary for ExplicitlyNamedApplicationScopedBean
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Named(ExplicitlyNamedApplicationScopedBean.NAME)
@ApplicationScoped
public class ExplicitlyNamedApplicationScopedBean {

    public static final String NAME = "explicitlyNamedApplicationScopedBean";

}
