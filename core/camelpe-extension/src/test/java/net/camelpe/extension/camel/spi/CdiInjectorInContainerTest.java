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

package net.camelpe.extension.camel.spi;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import net.camelpe.extension.camel.spi.beans.injector.ApplicationScopedBean;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingNoInjectionPoints;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingOneInjectionPoint;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingPostConstructAnnotatedMethod;
import net.camelpe.extension.camel.spi.beans.injector.BeanNotConstructableViaReflection;
import net.camelpe.extension.camel.spi.beans.injector.BeanNotInBeanManager;
import net.camelpe.extension.camel.spi.beans.injector.BeanTypeHavingTwoConcreteSubtypes;
import net.camelpe.extension.camel.spi.beans.injector.SingletonScopedBean;

import org.apache.camel.RuntimeCamelException;
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
 * Test {@link CdiRegistry <code>CdiRegistry</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class CdiInjectorInContainerTest {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    @Inject
    private BeanManager beanManager;

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    @Deployment
    public static JavaArchive createTestArchive() {
        final JavaArchive testModule = ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addClasses(BeanHavingNoInjectionPoints.class,
                        ApplicationScopedBean.class,
                        BeanHavingOneInjectionPoint.class,
                        BeanHavingPostConstructAnnotatedMethod.class,
                        SingletonScopedBean.class,
                        BeanTypeHavingTwoConcreteSubtypes.class)
                .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));

        return testModule;
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void assertThatCdiInjectorCreatesNewInstanceHavingNoInjectionPoints() {
        final BeanHavingNoInjectionPoints beanInstance = classUnderTest()
                .newInstance(BeanHavingNoInjectionPoints.class);

        assertNotNull(
                "newInstance(" + BeanHavingNoInjectionPoints.class.getName()
                        + ") should have returned an instance of ["
                        + BeanHavingNoInjectionPoints.class.getName()
                        + "] yet it returned null", beanInstance);
    }

    @Test
    public void assertThatCdiInjectorCreatesNewConfiguredInstanceHavingInjectionPoints() {
        final BeanHavingOneInjectionPoint beanInstance = classUnderTest()
                .newInstance(BeanHavingOneInjectionPoint.class);

        assertNotNull(
                "newInstance(" + BeanHavingOneInjectionPoint.class.getName()
                        + ") should have returned an instance of ["
                        + BeanHavingOneInjectionPoint.class.getName()
                        + "] yet it returned null", beanInstance);
        assertNotNull(
                "newInstance("
                        + BeanHavingOneInjectionPoint.class.getName()
                        + ") should have injected dependencies into the newly created instance, yet it didn't",
                beanInstance.injectionPoint);
    }

    @Test
    public void assertThatCdiInjectorInvokesPostConstructAnnotatedMethodOnNewlyCreatedInstance() {
        final BeanHavingPostConstructAnnotatedMethod beanInstance = classUnderTest()
                .newInstance(BeanHavingPostConstructAnnotatedMethod.class);

        assertNotNull("newInstance("
                + BeanHavingPostConstructAnnotatedMethod.class.getName()
                + ") should have returned an instance of ["
                + BeanHavingPostConstructAnnotatedMethod.class.getName()
                + "] yet it returned null", beanInstance);
        assertNotNull("newInstance("
                + BeanHavingPostConstructAnnotatedMethod.class.getName()
                + ") should have invoked method annotated with @PostConstruct "
                + "on newly created instance, yet it didn't",
                beanInstance.postConstructed);
    }

    @Test
    public void assertThatCdiInjectorReturnsSingletonInstanceOfApplicationScopedBean() {
        final ApplicationScopedBean singleton = new ApplicationScopedBean();

        final ApplicationScopedBean beanInstance = classUnderTest()
                .newInstance(ApplicationScopedBean.class, singleton);

        assertNotNull("newInstance(" + ApplicationScopedBean.class.getName()
                + ", " + singleton + ") should have returned an instance of ["
                + ApplicationScopedBean.class.getName()
                + "] yet it returned null", beanInstance);
        assertSame(
                "newInstance("
                        + ApplicationScopedBean.class.getName()
                        + ", "
                        + singleton
                        + ") should have returned the same instance as had been passed in, yet it didn't",
                singleton, beanInstance);
    }

    @Test
    public void assertThatCdiInjectorReturnsSingletonInstanceOfSingletonScopedBean() {
        final SingletonScopedBean singleton = new SingletonScopedBean();

        final SingletonScopedBean beanInstance = classUnderTest().newInstance(
                SingletonScopedBean.class, singleton);

        assertNotNull("newInstance(" + SingletonScopedBean.class.getName()
                + ", " + singleton + ") should have returned an instance of ["
                + SingletonScopedBean.class.getName()
                + "] yet it returned null", beanInstance);
        assertSame(
                "newInstance("
                        + SingletonScopedBean.class.getName()
                        + ", "
                        + singleton
                        + ") should have returned the same instance as had been passed in, yet it didn't",
                singleton, beanInstance);
    }

    @Test
    public void assertThatCdiInjectorCreatesNewInstanceViaReflectionIfNotFoundInBeanManager() {
        final BeanNotInBeanManager beanInstance = classUnderTest().newInstance(
                BeanNotInBeanManager.class);

        assertNotNull("newInstance(" + BeanNotInBeanManager.class.getName()
                + ") should have created an instance of ["
                + BeanNotInBeanManager.class.getName()
                + "] via reflection, yet it returned null", beanInstance);
    }

    @Test
    public void assertThatCdiInjectorReturnsNewInstanceOfNonSingletonScopedBean() {
        final BeanHavingNoInjectionPoints nonSingleton = new BeanHavingNoInjectionPoints();

        final BeanHavingNoInjectionPoints newBeanInstance = classUnderTest()
                .newInstance(BeanHavingNoInjectionPoints.class, nonSingleton);

        assertNotNull(
                "newInstance(" + BeanHavingNoInjectionPoints.class.getName()
                        + ", " + nonSingleton
                        + ") should have returned an instance of ["
                        + BeanHavingNoInjectionPoints.class.getName()
                        + "] yet it returned null", newBeanInstance);
        assertFalse(
                "newInstance("
                        + BeanHavingNoInjectionPoints.class.getName()
                        + ", "
                        + nonSingleton
                        + ") should have returned a new instance, yet it returned the instance passed in",
                nonSingleton == newBeanInstance);
    }

    @Test
    public void assertThatCdiInjectorCreatesAnArbitraryInstanceIfTwoMatchingBeansAreFound() {
        final BeanTypeHavingTwoConcreteSubtypes beanInstance = classUnderTest()
                .newInstance(BeanTypeHavingTwoConcreteSubtypes.class);

        assertNotNull(
                "newInstance("
                        + BeanTypeHavingTwoConcreteSubtypes.class.getName()
                        + ") should have created an arbitrary instance of ["
                        + BeanNotInBeanManager.class.getName()
                        + "] since two concrete subtypes are present in the bean manager, yet it returned null",
                beanInstance);
    }

    @Test(expected = RuntimeCamelException.class)
    public void assertThatCdiInjectorThrowsRuntimeCamelExceptionIfBeanCannotBeConstructedViaReflection() {
        classUnderTest().newInstance(BeanNotConstructableViaReflection.class);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private CdiInjector classUnderTest() {
        return new CdiInjector(this.beanManager);
    }
}
