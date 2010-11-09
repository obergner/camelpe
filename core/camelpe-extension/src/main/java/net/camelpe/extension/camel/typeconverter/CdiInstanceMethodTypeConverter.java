/**
 * 
 */
package net.camelpe.extension.camel.typeconverter;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.TypeConverterAware;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.util.ObjectHelper;

/**
 * <p>
 * TODO: Insert short summary for CdiInstanceMethodTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiInstanceMethodTypeConverter implements TypeConverter {

    private final Injector injector;

    private final Method method;

    private final boolean useExchange;

    private final TypeConverterRegistry registry;

    public CdiInstanceMethodTypeConverter(final Injector injector,
            final Method method, final TypeConverterRegistry registry) {
        this.injector = injector;
        this.method = method;
        this.useExchange = method.getParameterTypes().length == 2;
        this.registry = registry;
    }

    @Override
    public <T> T convertTo(final Class<T> type, final Object value) {
        return convertTo(type, null, value);
    }

    @Override
    public <T> T convertTo(final Class<T> type, final Exchange exchange,
            final Object value) {
        final Object instance = this.injector.newInstance(this.method
                .getDeclaringClass());
        if (instance == null) {
            throw new RuntimeCamelException(
                    "Could not instantiate an instance of: "
                            + type.getCanonicalName());
        }
        // inject parent type converter
        if ((instance instanceof TypeConverterAware)
                && (this.registry instanceof TypeConverter)) {
            final TypeConverter parentTypeConverter = (TypeConverter) this.registry;
            ((TypeConverterAware) instance)
                    .setTypeConverter(parentTypeConverter);
        }
        return this.useExchange ? (T) ObjectHelper.invokeMethod(this.method,
                instance, value, exchange) : (T) ObjectHelper.invokeMethod(
                this.method, instance, value);
    }

    @Override
    public <T> T mandatoryConvertTo(final Class<T> type, final Object value) {
        return convertTo(type, null, value);
    }

    @Override
    public <T> T mandatoryConvertTo(final Class<T> type,
            final Exchange exchange, final Object value) {
        return convertTo(type, null, value);
    }

    @Override
    public String toString() {
        return "CdiInstanceMethodTypeConverter[method = " + this.method
                + " | useExchange = " + this.useExchange + "]";
    }
}
