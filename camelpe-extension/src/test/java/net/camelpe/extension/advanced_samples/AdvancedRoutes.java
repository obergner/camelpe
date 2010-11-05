/**
 * 
 */
package net.camelpe.extension.advanced_samples;

import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;

/**
 * <p>
 * TODO: Insert short summary for AdvancedRoutes
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AdvancedRoutes extends RouteBuilder {

    public static final String ADVANCED_SOURCE_EP = "direct:fullFunctionalitySource";

    public static final String ADVANCED_TARGET_EP = "bean:"
            + AdvancedConsumer.NAME + "?method=consume";

    @Inject
    private AdvancedProcessor processor;

    /**
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        from(ADVANCED_SOURCE_EP).process(this.processor).to(ADVANCED_TARGET_EP);
    }
}
