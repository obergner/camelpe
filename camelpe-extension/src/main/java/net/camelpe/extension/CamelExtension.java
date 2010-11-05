/**
 * 
 */
package net.camelpe.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.ResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import net.camelpe.extension.cdi.spi.CamelInjectionTargetWrapper;
import net.camelpe.extension.util.BeanReference;

import org.apache.camel.Converter;
import org.apache.camel.FallbackConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CamelExtension
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class CamelExtension implements Extension {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(getClass());

    private BeanReference<CdiCamelContextLifecycleAdapter> cdiCamelContextLifecycleAdapterRef;

    // -------------------------------------------------------------------------
    // Lifecycle callbacks
    // -------------------------------------------------------------------------

    void initializeCamelExtension(@Observes final BeforeBeanDiscovery bbd,
            final BeanManager beanManager) {
        this.cdiCamelContextLifecycleAdapterRef = new BeanReference<CdiCamelContextLifecycleAdapter>(
                CdiCamelContextLifecycleAdapter.class, beanManager);
    }

    void registerCamelConverterAnnotationsAsQualifiers(
            @Observes final BeforeBeanDiscovery bbd) {
        bbd.addQualifier(Converter.class);
        bbd.addQualifier(FallbackConverter.class);

        getLog().debug(
                "Registered Camel Converter annotations [{}, {}] as CDI qualifiers",
                Converter.class.getName(), FallbackConverter.class.getName());
    }

    <T> void customizeInjectionTargetForEndpointInjection(
            @Observes final ProcessInjectionTarget<T> pit,
            final BeanManager beanManager) {
        CamelInjectionTargetWrapper.wrap(pit, beanManager);

        getLog().debug(
                "Customized InjectionTarget [{}] for AnnotatedType [{}] in "
                        + "order to enable Camel endpoint injection.", pit,
                pit.getAnnotatedType());
    }

    void registerCamelExtensionBeans(@Observes final AfterBeanDiscovery abd,
            final BeanManager beanManager) {
        abd.addBean(new CdiCamelContextConfiguration.CdiBean(beanManager));
        abd.addBean(new TypeConverterDiscovery.CdiBean(beanManager));
        abd.addBean(new RoutesDiscovery.CdiBean(beanManager));
        abd.addBean(new CdiCamelContext.CdiBean(beanManager));
        abd.addBean(new CdiCamelContextLifecycleAdapter.CdiBean(beanManager));

        getLog().debug(
                "CamelExtension Beans have been registered in the BeanManager.");
    }

    void afterDeploymentValidation(@Observes final AfterDeploymentValidation adv)
            throws ResolutionException, Exception {
        this.cdiCamelContextLifecycleAdapterRef.get()
                .afterDeploymentValidation();
    }

    void beforeShutdown(@Observes final BeforeShutdown event)
            throws ResolutionException, Exception {
        this.cdiCamelContextLifecycleAdapterRef.get().beforeShutdown();
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    /**
     * @return the log
     */
    private Logger getLog() {
        return this.log;
    }
}
