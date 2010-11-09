/**
 * 
 */
package net.camelpe.extension.configuration_samples;

import java.io.InputStream;
import java.net.URL;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.spi.ClassResolver;

/**
 * <p>
 * TODO: Insert short summary for SampleClassResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
public class SampleClassResolver implements ClassResolver {

    /**
     * @see org.apache.camel.spi.ClassResolver#loadResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream loadResourceAsStream(final String uri) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#loadResourceAsURL(java.lang.String)
     */
    @Override
    public URL loadResourceAsURL(final String uri) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveClass(java.lang.String)
     */
    @Override
    public Class<?> resolveClass(final String name) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveClass(java.lang.String,
     *      java.lang.Class)
     */
    @Override
    public <T> Class<T> resolveClass(final String name, final Class<T> type) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveClass(java.lang.String,
     *      java.lang.ClassLoader)
     */
    @Override
    public Class<?> resolveClass(final String name, final ClassLoader loader) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveClass(java.lang.String,
     *      java.lang.Class, java.lang.ClassLoader)
     */
    @Override
    public <T> Class<T> resolveClass(final String name, final Class<T> type,
            final ClassLoader loader) {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveMandatoryClass(java.lang.String)
     */
    @Override
    public Class<?> resolveMandatoryClass(final String name)
            throws ClassNotFoundException {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveMandatoryClass(java.lang.String,
     *      java.lang.Class)
     */
    @Override
    public <T> Class<T> resolveMandatoryClass(final String name,
            final Class<T> type) throws ClassNotFoundException {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveMandatoryClass(java.lang.String,
     *      java.lang.ClassLoader)
     */
    @Override
    public Class<?> resolveMandatoryClass(final String name,
            final ClassLoader loader) throws ClassNotFoundException {
        return null;
    }

    /**
     * @see org.apache.camel.spi.ClassResolver#resolveMandatoryClass(java.lang.String,
     *      java.lang.Class, java.lang.ClassLoader)
     */
    @Override
    public <T> Class<T> resolveMandatoryClass(final String name,
            final Class<T> type, final ClassLoader loader)
            throws ClassNotFoundException {
        return null;
    }
}
