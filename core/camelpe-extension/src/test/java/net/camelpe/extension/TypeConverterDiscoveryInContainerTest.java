/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import net.camelpe.extension.typeconverter_samples.InstanceMethodTypeConverter;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.TypeConverterRegistry;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * TODO: Insert short summary for TypeConverterDiscoveryInContainerTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class TypeConverterDiscoveryInContainerTest {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    @Inject
    private TypeConverterDiscovery classUnderTest;

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    @Deployment
    public static JavaArchive createTestArchive() {
        final JavaArchive testModule = ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addPackages(false,
                        InstanceMethodTypeConverter.class.getPackage())
                .addServiceProvider(Extension.class, CamelExtension.class)
                .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));

        return testModule;
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    /**
     * Test method for
     * {@link net.camelpe.extension.TypeConverterDiscovery#registerIn(org.apache.camel.CamelContext)}
     * .
     */
    @Test
    public final void assertThatRegisterInDoesRegisterAllDiscoveredTypeConverters() {
        final TypeConverterRegistry typeConverterRegistryMock = createNiceMock(TypeConverterRegistry.class);
        typeConverterRegistryMock.addFallbackTypeConverter(
                (TypeConverter) anyObject(), eq(false));
        expectLastCall().once();
        typeConverterRegistryMock.addTypeConverter(eq(String.class),
                eq(Object.class), (TypeConverter) anyObject());
        expectLastCall().once();

        final CamelContext camelContextMock = createNiceMock(CamelContext.class);
        expect(camelContextMock.getTypeConverterRegistry()).andReturn(
                typeConverterRegistryMock).anyTimes();
        replay(typeConverterRegistryMock, camelContextMock);

        this.classUnderTest.registerIn(camelContextMock);

        verify(typeConverterRegistryMock, camelContextMock);
    }

}
