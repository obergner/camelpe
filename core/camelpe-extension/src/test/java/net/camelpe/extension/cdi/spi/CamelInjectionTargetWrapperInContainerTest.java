/**
 * 
 */
package net.camelpe.extension.cdi.spi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.ResolutionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import net.camelpe.extension.camel.spi.CdiRegistry;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAndInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAndProducesAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingNoEndpointInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingProduceAnnotatedField;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Test {@link CdiRegistry <code>CdiRegistry</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class CamelInjectionTargetWrapperInContainerTest {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    private static final String CDIPE_SERVICE_FILE_PATH = "META-INF/services/javax.enterprise.inject.spi.Extension";

    private static final String CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX = ".DISABLED";

    static {
        /*
         * HACK: As soon as the embedded Weld container sees the file
         * "META-INF/services/javax.enterprise.inject.spi.Extension" on the
         * classpath - it need not be deployed into the container - it will look
         * for the fqn of a CDI extension in that file. In our case, it will
         * find the fqn of our Camel Portable Extension and will happily deploy
         * it. This, however, will cause this test to fail since (a) this test
         * consciously deploys at least one "invalid" bean into Weld and (b) our
         * Camel PE employs the class to be tested by this test,
         * CamelInjectionTargetWrapper, for wrapping its own InjectionTarget.
         * Now, Weld will recognize - via CamelInjectionTargetWrapper - that
         * something is wrong with one of the deployed beans and consequently
         * fail at startup.
         * 
         * I would like to move this piece of code to some @BeforeClass
         * annotated method. However, Arquillian starts Weld even before
         * 
         * @BeforeClass methods are executed. Thus, a static initializer block.
         */
        final URL cdiServiceFileUrl = CamelInjectionTargetWrapperInContainerTest.class
                .getClassLoader().getResource(CDIPE_SERVICE_FILE_PATH);
        if (cdiServiceFileUrl != null) {

            final File cdiServiceFile = new File(cdiServiceFileUrl.getFile());
            final File cdiServiceFileDisabled = new File(
                    cdiServiceFileUrl.getFile()
                            + CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX);

            cdiServiceFile.renameTo(cdiServiceFileDisabled);
        }
    }

    @Inject
    private BeanManager beanManager;

    @Produces
    private final CamelContext camelContext = new DefaultCamelContext();

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    @Deployment
    public static JavaArchive createTestArchive() {
        final JavaArchive testModule = ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addPackages(
                        false,
                        BeanHavingNoEndpointInjectAnnotatedField.class
                                .getPackage())
                .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));

        return testModule;
    }

    @AfterClass
    public static void reenableCamelPE() {
        final URL cdiServiceFileDisabledUrl = CamelInjectionTargetWrapperInContainerTest.class
                .getClassLoader().getResource(
                        CDIPE_SERVICE_FILE_PATH
                                + CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX);
        if (cdiServiceFileDisabledUrl == null) {
            return;
        }

        final File cdiServiceFileDisabled = new File(
                cdiServiceFileDisabledUrl.getFile());
        final File cdiServiceFile = new File(cdiServiceFileDisabledUrl
                .getFile().replace(CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX, ""));

        cdiServiceFileDisabled.renameTo(cdiServiceFile);
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void assertThatInjectionTargetForReturnsOriginalInjectionTargetForBeanHavingNoEndpointInjectAnnotatedFields() {
        final AnnotatedType<BeanHavingNoEndpointInjectAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingNoEndpointInjectAnnotatedField.class);
        final InjectionTarget<BeanHavingNoEndpointInjectAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        final InjectionTarget<BeanHavingNoEndpointInjectAnnotatedField> newInjectionTarget = CamelInjectionTargetWrapper
                .injectionTargetFor(annotatedType, originalInjectionTarget,
                        this.beanManager);

        assertSame(
                "injectionTargetFor("
                        + annotatedType
                        + ", "
                        + originalInjectionTarget
                        + ", "
                        + this.camelContext
                        + ") should have returned the InjectionTarget passed in as the supplied "
                        + "AnnotatedType does not define any field annotated with @EndpointInject, "
                        + "yet it returned a different InjectionTarget",
                originalInjectionTarget, newInjectionTarget);
    }

    @Test
    public void assertThatInjectionTargetForReturnsWrappedInjectionTargetForBeanHavingEndpointInjectAnnotatedField() {
        final AnnotatedType<BeanHavingEndpointInjectAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingEndpointInjectAnnotatedField.class);
        final InjectionTarget<BeanHavingEndpointInjectAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        final InjectionTarget<BeanHavingEndpointInjectAnnotatedField> newInjectionTarget = CamelInjectionTargetWrapper
                .injectionTargetFor(annotatedType, originalInjectionTarget,
                        this.beanManager);

        assertTrue(
                "injectionTargetFor("
                        + annotatedType
                        + ", "
                        + originalInjectionTarget
                        + ", "
                        + this.camelContext
                        + ") should have returned a wrapper for the InjectionTarget passed in as the supplied "
                        + "AnnotatedType does define a field annotated with @EndpointInject, "
                        + "yet it didn't",
                CamelInjectionTargetWrapper.class
                        .isAssignableFrom(newInjectionTarget.getClass()));
    }

    @Test
    public void assertThatInjectionTargetForReturnsWrappedInjectionTargetForBeanHavingProduceAnnotatedField() {
        final AnnotatedType<BeanHavingProduceAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingProduceAnnotatedField.class);
        final InjectionTarget<BeanHavingProduceAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        final InjectionTarget<BeanHavingProduceAnnotatedField> newInjectionTarget = CamelInjectionTargetWrapper
                .injectionTargetFor(annotatedType, originalInjectionTarget,
                        this.beanManager);

        assertTrue(
                "injectionTargetFor("
                        + annotatedType
                        + ", "
                        + originalInjectionTarget
                        + ", "
                        + this.camelContext
                        + ") should have returned a wrapper for the InjectionTarget passed in as the supplied "
                        + "AnnotatedType does define a field annotated with @Produce, "
                        + "yet it didn't",
                CamelInjectionTargetWrapper.class
                        .isAssignableFrom(newInjectionTarget.getClass()));
    }

    @Test(expected = ResolutionException.class)
    public void assertThatInjectionTargetForRejectsBeanHavingEndpointInjectAndInjectAnnotatedField() {
        final AnnotatedType<BeanHavingEndpointInjectAndInjectAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingEndpointInjectAndInjectAnnotatedField.class);
        final InjectionTarget<BeanHavingEndpointInjectAndInjectAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        CamelInjectionTargetWrapper.injectionTargetFor(annotatedType,
                originalInjectionTarget, this.beanManager);
    }

    @Test(expected = ResolutionException.class)
    public void assertThatInjectionTargetForRejectsBeanHavingEndpointInjectAndProducesAnnotatedField() {
        final AnnotatedType<BeanHavingEndpointInjectAndProducesAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingEndpointInjectAndProducesAnnotatedField.class);
        final InjectionTarget<BeanHavingEndpointInjectAndProducesAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        CamelInjectionTargetWrapper.injectionTargetFor(annotatedType,
                originalInjectionTarget, this.beanManager);
    }

    @Test
    public void assertThatCamelInjectionTargetWrapperInjectsEndpointIntoEndpointInjectAnnotatedField()
            throws Exception {
        final Endpoint endpointToInject = new MockEndpoint(
                BeanHavingEndpointInjectAnnotatedField.ENDPOINT_URI);
        this.camelContext.addEndpoint(
                BeanHavingEndpointInjectAnnotatedField.ENDPOINT_URI,
                endpointToInject);

        final AnnotatedType<BeanHavingEndpointInjectAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingEndpointInjectAnnotatedField.class);
        final InjectionTarget<BeanHavingEndpointInjectAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        final Bean<BeanHavingEndpointInjectAnnotatedField> bean = (Bean<BeanHavingEndpointInjectAnnotatedField>) this.beanManager
                .getBeans(BeanHavingEndpointInjectAnnotatedField.NAME)
                .iterator().next();
        final CreationalContext<BeanHavingEndpointInjectAnnotatedField> creationalContext = this.beanManager
                .createCreationalContext(bean);
        final BeanHavingEndpointInjectAnnotatedField instance = bean
                .create(creationalContext);

        final InjectionTarget<BeanHavingEndpointInjectAnnotatedField> wrappedInjectionTarget = CamelInjectionTargetWrapper
                .injectionTargetFor(annotatedType, originalInjectionTarget,
                        this.beanManager);
        wrappedInjectionTarget.inject(instance, creationalContext);

        assertNotNull(
                "inject("
                        + instance
                        + ", "
                        + creationalContext
                        + ") should have injected a ProducerTemplate into the @EnpointInject annotated field of the supplied instance, yet it didn't",
                instance.producerTemplate);
    }

    @Test
    public void assertThatCamelInjectionTargetWrapperInjectsEndpointIntoProduceAnnotatedField()
            throws Exception {
        final Endpoint endpointToInject = new MockEndpoint(
                BeanHavingProduceAnnotatedField.ENDPOINT_URI);
        this.camelContext.addEndpoint(
                BeanHavingProduceAnnotatedField.ENDPOINT_URI, endpointToInject);

        final AnnotatedType<BeanHavingProduceAnnotatedField> annotatedType = this.beanManager
                .createAnnotatedType(BeanHavingProduceAnnotatedField.class);
        final InjectionTarget<BeanHavingProduceAnnotatedField> originalInjectionTarget = this.beanManager
                .createInjectionTarget(annotatedType);

        final Bean<BeanHavingProduceAnnotatedField> bean = (Bean<BeanHavingProduceAnnotatedField>) this.beanManager
                .getBeans(BeanHavingProduceAnnotatedField.NAME).iterator()
                .next();
        final CreationalContext<BeanHavingProduceAnnotatedField> creationalContext = this.beanManager
                .createCreationalContext(bean);
        final BeanHavingProduceAnnotatedField instance = bean
                .create(creationalContext);

        final InjectionTarget<BeanHavingProduceAnnotatedField> wrappedInjectionTarget = CamelInjectionTargetWrapper
                .injectionTargetFor(annotatedType, originalInjectionTarget,
                        this.beanManager);
        wrappedInjectionTarget.inject(instance, creationalContext);

        assertNotNull(
                "inject("
                        + instance
                        + ", "
                        + creationalContext
                        + ") should have injected a ProducerTemplate into the @Produce annotated field of the supplied instance, yet it didn't",
                instance.producer);
    }
}
