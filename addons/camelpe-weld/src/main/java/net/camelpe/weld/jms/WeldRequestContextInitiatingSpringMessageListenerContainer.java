/**
 * 
 */
package net.camelpe.weld.jms;

import javax.jms.Session;

import net.camelpe.weld.requestcontext.WeldRequestContext;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * <p>
 * TODO: Insert short summary for
 * WeldRequestContextInitiatingSpringMessageListenerContainer
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class WeldRequestContextInitiatingSpringMessageListenerContainer extends
        DefaultMessageListenerContainer {

    // -------------------------------------------------------------------------
    // Initiate new Weld RequestContext
    // -------------------------------------------------------------------------

    /**
     * @see org.springframework.jms.listener.DefaultMessageListenerContainer#messageReceived(java.lang.Object,
     *      javax.jms.Session)
     */
    @Override
    protected void messageReceived(final Object invoker, final Session session) {
        super.messageReceived(invoker, session);

        WeldRequestContext.endThenBegin();
    }

    /**
     * @see org.springframework.jms.listener.DefaultMessageListenerContainer#noMessageReceived(java.lang.Object,
     *      javax.jms.Session)
     */
    @Override
    protected void noMessageReceived(final Object invoker, final Session session) {
        WeldRequestContext.end();

        super.noMessageReceived(invoker, session);
    }
}
