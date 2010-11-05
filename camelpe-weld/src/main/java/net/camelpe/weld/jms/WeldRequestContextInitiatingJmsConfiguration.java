/**
 * 
 */
package net.camelpe.weld.jms;

import java.util.concurrent.Executor;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.commons.lang.Validate;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

/**
 * <p>
 * TODO: Insert short summary for WeldRequestContextInitiatingJmsConfiguration
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingJmsConfiguration extends
        JmsConfiguration {

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public WeldRequestContextInitiatingJmsConfiguration() {
    }

    public WeldRequestContextInitiatingJmsConfiguration(
            final ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    // -------------------------------------------------------------------------
    // Additional properties
    // -------------------------------------------------------------------------

    public void setExecutor(final Executor executor)
            throws IllegalArgumentException {
        Validate.notNull(executor, "executor");
        setTaskExecutor(new TaskExecutorAdapter(executor));
    }

    // -------------------------------------------------------------------------
    // Make Spring use our custom MessageListenerContainer
    // -------------------------------------------------------------------------

    /**
     * @see org.apache.camel.component.jms.JmsConfiguration#chooseMessageListenerContainerImplementation()
     */
    @Override
    public AbstractMessageListenerContainer chooseMessageListenerContainerImplementation() {
        return new WeldRequestContextInitiatingSpringMessageListenerContainer();
    }
}
