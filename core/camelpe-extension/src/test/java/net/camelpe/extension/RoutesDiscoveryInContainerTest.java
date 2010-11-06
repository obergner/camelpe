/**
 * 
 */
package net.camelpe.extension;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import net.camelpe.extension.routes_samples.FirstRouteBuilder;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.easymock.Capture;
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
 * TODO: Insert short summary for RoutesDiscoveryInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class RoutesDiscoveryInContainerTest {

    // -------------------------------------------------------------------------
    // Static
    // -------------------------------------------------------------------------

    private static final String CDIPE_SERVICE_FILE_PATH = "META-INF/services/javax.enterprise.inject.spi.Extension";

    private static final String CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX = ".DISABLED";

    static {
        /*
         * HACK: As soon as the embedded Weld container sees the file
         * "META-INF/services/javax.enterprise.inject.spi.Extension" on the
         * classpath - it need not be deployed into the container - it will look
         * for the fqn of a CDI extension in that file. In our case, it will
         * find the fqn of our Camel Portable Extension and will happily deploy
         * it. This, however, will cause this test to fail since our Camel PE
         * deploys the class to be tested by this test, RoutesDiscovery, twice:
         * first by explicitly adding it to the BeanManager as a CDI bean, and
         * then by discovering it on the classpath. Now, Weld will conclude that
         * this test's dependency on RoutesDiscovery is ambiguous and bail out.
         * 
         * I would like to move this piece of code to some @BeforeClass
         * annotated method. However, Arquillian starts Weld even before
         * 
         * @BeforeClass methods are executed. Thus, a static initializer block.
         */
        final URL cdiServiceFileUrl = RoutesDiscoveryInContainerTest.class
                .getClassLoader().getResource(CDIPE_SERVICE_FILE_PATH);
        if (cdiServiceFileUrl != null) {

            final File cdiServiceFile = new File(cdiServiceFileUrl.getFile());
            final File cdiServiceFileDisabled = new File(
                    cdiServiceFileUrl.getFile()
                            + CDIPE_SERVICE_FILE_PATH_DISABLED_SUFFIX);

            cdiServiceFile.renameTo(cdiServiceFileDisabled);
        }
    }

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    @Inject
    private RoutesDiscovery classUnderTest;

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    @Deployment
    public static JavaArchive createTestArchive() {
        final JavaArchive testModule = ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addClass(RoutesDiscovery.class)
                .addPackages(false, FirstRouteBuilder.class.getPackage())
                .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));

        return testModule;
    }

    @AfterClass
    public static void reenableCamelPE() {
        final URL cdiServiceFileDisabledUrl = RoutesDiscoveryInContainerTest.class
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

    /**
     * Test method for
     * {@link net.camelpe.extension.RoutesDiscovery#registerDiscoveredRoutesIn(org.apache.camel.CamelContext)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public final void assertThatRegisterDiscoveredRoutesInDoesRegisterAllDiscoveredRoutes()
            throws Exception {
        final Capture<RouteBuilder> capturedRouteBuilders = new Capture<RouteBuilder>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock.addRoutes(capture(capturedRouteBuilders));
        expectLastCall().times(2);
        replay(camelContextMock);

        this.classUnderTest.registerDiscoveredRoutesIn(camelContextMock);

        verify(camelContextMock);
    }

}
