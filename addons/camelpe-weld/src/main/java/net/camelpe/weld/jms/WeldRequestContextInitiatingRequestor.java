/**
 * 
 */
package net.camelpe.weld.jms;

import java.util.concurrent.ScheduledExecutorService;

import javax.jms.Message;

import net.camelpe.weld.requestcontext.WeldRequestContext;

import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.requestor.Requestor;

/**
 * <p>
 * A {@link org.apache.camel.component.jms.requestor.Requestor
 * <code>Requestor</code>} subclass that takes care of setting up a Weld
 * RequestContext before processing each message and tearing it down afterwards.
 * </p>
 * <p>
 * <strong>IMPORTANT</strong> This class is rarely usable. There is generally no
 * tried and true way of telling Apache Camel to use a custom
 * <code>Requestor</code> in each and every case. The <code>Requestor</code> set
 * on Camel's {@link org.apache.camel.component.jms.JmsComponent
 * <code>JmsComponent</code>} is only used by concrete
 * {@link org.apache.camel.component.jms.JmsEndpoint <code>JmsEndpoint</code>}s
 * if the {@link org.apache.camel.component.jms.JmsProducer.RequestorAffinity
 * <code>RequestorAffinity</code>} is set to <code>PER_COMPONENT</code>. This,
 * however, is <strong>not</strong> the default.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingRequestor extends Requestor {

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @param delegate
     */
    public WeldRequestContextInitiatingRequestor(
            final JmsConfiguration configuration,
            final ScheduledExecutorService executorService)
            throws IllegalArgumentException {
        super(configuration, executorService);
    }

    // -------------------------------------------------------------------------
    // org.apache.camel.component.jms.requestor.Requestor
    // -------------------------------------------------------------------------

    /**
     * @param message
     * @see org.apache.camel.component.jms.requestor.Requestor#onMessage(javax.jms.Message)
     */
    @Override
    public void onMessage(final Message message) {
        try {
            WeldRequestContext.begin();
            super.onMessage(message);
        } finally {
            WeldRequestContext.end();
        }
    }
}
