/**
 * 
 */
package net.camelpe.extension.cdi.spi.bean_samples;

import javax.inject.Named;

import org.apache.camel.Produce;

/**
 * <p>
 * TODO: Insert short summary for BeanHavingNoEndpointInjectAnnotatedField
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Named(BeanHavingProduceAnnotatedField.NAME)
public class BeanHavingProduceAnnotatedField {

    public interface Listener {
        String sendMessage(final String message);
    }

    public static final String NAME = "beanHavingProduceAnnotatedField";

    public static final String ENDPOINT_URI = "mock://produceEndpoint";

    @Produce(uri = BeanHavingProduceAnnotatedField.ENDPOINT_URI)
    public Listener producer;
}
