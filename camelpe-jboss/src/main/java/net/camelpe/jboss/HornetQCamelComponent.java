/**
 * 
 */
package net.camelpe.jboss;

import java.util.concurrent.Executor;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.commons.lang.Validate;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.springframework.core.task.support.TaskExecutorAdapter;

/**
 * <p>
 * TODO: Insert short summary for HornetQCamelComponent
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class HornetQCamelComponent extends JmsComponent {

    // -------------------------------------------------------------------------
    // Static
    // -------------------------------------------------------------------------

    public static final String URI_PREFIX = "hornetq";

    private static final String NETTY_CONNECTOR_FACTORY = "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory";

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public HornetQCamelComponent(final CamelContext context) {
        super(context);
        setConnectionFactory(createHornetQConnectionFactory());
    }

    public HornetQCamelComponent(final CamelContext context,
            final JmsConfiguration configuration) {
        super(context);
        final HornetQConnectionFactory connectionFactory = createHornetQConnectionFactory();
        setConnectionFactory(connectionFactory);
        configuration.setConnectionFactory(connectionFactory);
        setConfiguration(configuration);
    }

    // -------------------------------------------------------------------------
    // Additional properties
    // -------------------------------------------------------------------------

    public void setExecutor(final Executor executor)
            throws IllegalArgumentException {
        Validate.notNull(executor, "executor");
        getConfiguration().setTaskExecutor(new TaskExecutorAdapter(executor));
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private HornetQConnectionFactory createHornetQConnectionFactory() {
        return HornetQJMSClient
                .createConnectionFactory(new TransportConfiguration(
                        NETTY_CONNECTOR_FACTORY));
    }
}
