/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.camelpe.extension;

import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import net.camelpe.extension.configuration_samples.SampleClassResolver;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.ClassResolver;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.FactoryFinderResolver;
import org.apache.camel.spi.InflightRepository;
import org.apache.camel.spi.ManagementStrategy;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.ShutdownStrategy;
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
 * TODO: Insert short summary for CdiCamelContextConfigurationInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class CdiCamelContextConfigurationInContainerTest {

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
         * deploys the class to be tested by this test,
         * CdiCamelContextConfiguration, twice: first by explicitly adding it to
         * the BeanManager as a CDI bean, and then by discovering it on the
         * classpath. Now, Weld will recognize that something is wrong with one
         * of the deployed beans and consequently fail at startup.
         * 
         * I would like to move this piece of code to some @BeforeClass
         * annotated method. However, Arquillian starts Weld even before
         * 
         * @BeforeClass methods are executed. Thus, a static initializer block.
         */
        final URL cdiServiceFileUrl = CdiCamelContextConfigurationInContainerTest.class
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
    private CdiCamelContextConfiguration classUnderTest;

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    @Deployment
    public static JavaArchive createTestArchive() {
        final JavaArchive testModule = ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addClass(CdiCamelContextConfiguration.class)
                .addPackages(false, SampleClassResolver.class.getPackage())
                .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));

        return testModule;
    }

    @AfterClass
    public static void reenableCamelPE() {
        final URL cdiServiceFileDisabledUrl = CdiCamelContextConfigurationInContainerTest.class
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
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredClassResolverOnCamelContext() {
        final Capture<ClassResolver> capturedClassResolver = new Capture<ClassResolver>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock.setClassResolver(capture(capturedClassResolver));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set ClassResolver as it should have",
                capturedClassResolver.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredPackageScanClassResolverOnCamelContext() {
        final Capture<PackageScanClassResolver> capturedPackageScanClassResolver = new Capture<PackageScanClassResolver>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setPackageScanClassResolver(capture(capturedPackageScanClassResolver));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set PackageScanClassResolver as it should have",
                capturedPackageScanClassResolver.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredDataFormatResolverOnCamelContext() {
        final Capture<DataFormatResolver> capturedDataFormatResolver = new Capture<DataFormatResolver>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setDataFormatResolver(capture(capturedDataFormatResolver));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set DataFormatResolver as it should have",
                capturedDataFormatResolver.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredExecutorServiceStrategyOnCamelContext() {
        final Capture<ExecutorServiceStrategy> capturedExecutorServiceStrategy = new Capture<ExecutorServiceStrategy>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setExecutorServiceStrategy(capture(capturedExecutorServiceStrategy));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set ExecutorServiceStrategy as it should have",
                capturedExecutorServiceStrategy.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredFactoryFinderResolverOnCamelContext() {
        final Capture<FactoryFinderResolver> capturedFactoryFinderResolver = new Capture<FactoryFinderResolver>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setFactoryFinderResolver(capture(capturedFactoryFinderResolver));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set FactoryFinderResolver as it should have",
                capturedFactoryFinderResolver.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredInflightRepositoryOnCamelContext() {
        final Capture<InflightRepository> capturedInflightRepository = new Capture<InflightRepository>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setInflightRepository(capture(capturedInflightRepository));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set InflightRepository as it should have",
                capturedInflightRepository.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredManagementStrategyOnCamelContext() {
        final Capture<ManagementStrategy> capturedManagementStrategy = new Capture<ManagementStrategy>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock
                .setManagementStrategy(capture(capturedManagementStrategy));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set ManagementStrategy as it should have",
                capturedManagementStrategy.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredProcessorFactoryOnCamelContext() {
        final Capture<ProcessorFactory> capturedProcessorFactory = new Capture<ProcessorFactory>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock.setProcessorFactory(capture(capturedProcessorFactory));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set ProcessorFactory as it should have",
                capturedProcessorFactory.hasCaptured());
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.CdiCamelContextConfiguration#configure(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatConfigureSetsDiscoveredShutdownStrategyOnCamelContext() {
        final Capture<ShutdownStrategy> capturedShutdownStrategy = new Capture<ShutdownStrategy>();
        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        camelContextMock.setShutdownStrategy(capture(capturedShutdownStrategy));
        expectLastCall();
        replay(camelContextMock);

        this.classUnderTest.configure(camelContextMock);

        assertTrue(
                "configure(camelContextMock) did not set ShutdownStrategy as it should have",
                capturedShutdownStrategy.hasCaptured());
    }
}
