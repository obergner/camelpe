/**
 * 
 */
package net.camelpe.extension.camel.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.camel.spi.Registry;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiRegistry
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiRegistry implements Registry {

    private final BeanManager delegate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * @param delegate
     * @throws IllegalArgumentException
     */
    public CdiRegistry(final BeanManager delegate)
            throws IllegalArgumentException {
        Validate.notNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * @see org.apache.camel.spi.Registry#lookup(java.lang.String)
     */
    @Override
    public Object lookup(final String name) {
        Validate.notEmpty(name, "name");
        getLog().trace("Looking up bean using name = [{}] in CDI registry ...",
                name);

        final Set<Bean<?>> beans = getDelegate().getBeans(name);
        if (beans.isEmpty()) {
            getLog().debug(
                    "Found no bean matching name = [{}] in CDI registry.", name);
            return null;
        }
        if (beans.size() > 1) {
            throw new IllegalStateException(
                    "Expected to find exactly one bean having name [" + name
                            + "], but got [" + beans.size() + "]");
        }
        final Bean<?> bean = beans.iterator().next();
        getLog().debug("Found bean [{}] matching name = [{}] in CDI registry.",
                bean, name);

        final CreationalContext<?> creationalContext = getDelegate()
                .createCreationalContext(null);

        return getDelegate().getReference(bean, bean.getBeanClass(),
                creationalContext);
    }

    /**
     * @see org.apache.camel.spi.Registry#lookup(java.lang.String,
     *      java.lang.Class)
     */
    @Override
    public <T> T lookup(final String name, final Class<T> type) {
        Validate.notEmpty(name, "name");
        Validate.notNull(type, "type");
        getLog().trace(
                "Looking up bean using name = [{}] having expected type = [{}] in CDI registry ...",
                name, type.getName());

        return type.cast(lookup(name));
    }

    /**
     * @see org.apache.camel.spi.Registry#lookupByType(java.lang.Class)
     */
    @Override
    public <T> Map<String, T> lookupByType(final Class<T> type) {
        Validate.notNull(type, "type");
        getLog().trace(
                "Looking up all beans having expected type = [{}] in CDI registry ...",
                type.getName());

        final Set<Bean<?>> beans = getDelegate().getBeans(type);
        if (beans.isEmpty()) {
            getLog().debug(
                    "Found no beans having expected type = [{}] in CDI registry.",
                    type.getName());

            return Collections.emptyMap();
        }
        getLog().debug(
                "Found [{}] beans having expected type = [{}] in CDI registry.",
                Integer.valueOf(beans.size()), type.getName());

        final Map<String, T> beansByName = new HashMap<String, T>(beans.size());
        final CreationalContext<?> creationalContext = getDelegate()
                .createCreationalContext(null);
        for (final Bean<?> bean : beans) {
            beansByName.put(
                    bean.getName(),
                    type.cast(getDelegate().getReference(bean, type,
                            creationalContext)));
        }

        return beansByName;
    }

    private Logger getLog() {
        return this.log;
    }

    private BeanManager getDelegate() {
        return this.delegate;
    }
}
