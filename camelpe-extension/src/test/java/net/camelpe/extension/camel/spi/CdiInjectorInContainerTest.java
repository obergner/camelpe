/**
 * 
 */
package net.camelpe.extension.camel.spi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import net.camelpe.extension.camel.spi.beans.injector.ApplicationScopedBean;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingNoInjectionPoints;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingOneInjectionPoint;
import net.camelpe.extension.camel.spi.beans.injector.BeanHavingPostConstructAnnotatedMethod;
import net.camelpe.extension.camel.spi.beans.injector.SingletonScopedBean;

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
                .addPackages(false,
                        BeanHavingNoInjectionPoints.class.getPackage())
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

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private CdiInjector classUnderTest() {
        return new CdiInjector(this.beanManager);
    }
}
