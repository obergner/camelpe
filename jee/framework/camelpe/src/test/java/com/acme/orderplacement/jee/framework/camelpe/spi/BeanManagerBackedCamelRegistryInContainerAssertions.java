/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.spi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.xml.TimeUnitAdapter;
import org.apache.camel.component.bean.CamelInvocationHandler;
import org.apache.camel.component.browse.BrowseComponent;
import org.apache.camel.component.dataset.DataSetEndpoint;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.strategy.GenericFileRenamer;
import org.apache.camel.component.log.LogEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.ref.RefComponent;
import org.apache.camel.component.seda.SedaEndpoint;
import org.apache.camel.component.timer.TimerEndpoint;
import org.apache.camel.component.vm.VmComponent;
import org.apache.camel.converter.CamelConverter;
import org.apache.camel.converter.jaxp.StaxConverter;
import org.apache.camel.converter.stream.StreamCacheConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.EnumTypeConverter;
import org.apache.camel.impl.scan.CompositePackageScanFilter;
import org.apache.camel.language.LanguageExpression;
import org.apache.camel.language.bean.BeanLanguage;
import org.apache.camel.language.constant.ConstantLanguage;
import org.apache.camel.language.header.HeaderLanguage;
import org.apache.camel.language.property.PropertyLanguage;
import org.apache.camel.language.simple.SimpleLanguage;
import org.apache.camel.language.tokenizer.TokenizeLanguage;
import org.apache.camel.language.xpath.XPathLanguage;
import org.apache.camel.management.DefaultManagementAgent;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.management.mbean.ManagedCamelContext;
import org.apache.camel.model.BeanDefinition;
import org.apache.camel.model.config.PropertyDefinition;
import org.apache.camel.model.dataformat.DataFormatsDefinition;
import org.apache.camel.model.language.ELExpression;
import org.apache.camel.model.loadbalancer.TopicLoadBalancerDefinition;
import org.apache.commons.lang.Validate;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Test {@link BeanManagerBackedCamelRegistry
 * <code>BeanManagerBackedCamelRegistry</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public class BeanManagerBackedCamelRegistryInContainerAssertions {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@Inject
	private BeanManager beanManager;

	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap.create("test.jar",
				JavaArchive.class).addPackages(true,
				Validate.class.getPackage(),
				ApplicationScopedBean.class.getPackage(),
				CamelContext.class.getPackage(), Builder.class.getPackage(),
				TimeUnitAdapter.class.getPackage(),
				CamelInvocationHandler.class.getPackage(),
				BrowseComponent.class.getPackage(),
				DataSetEndpoint.class.getPackage(),
				DirectEndpoint.class.getPackage(),
				FileEndpoint.class.getPackage(),
				GenericFileRenamer.class.getPackage(),
				LogEndpoint.class.getPackage(),
				MockEndpoint.class.getPackage(),
				RefComponent.class.getPackage(),
				SedaEndpoint.class.getPackage(),
				TimerEndpoint.class.getPackage(),
				VmComponent.class.getPackage(),
				CamelConverter.class.getPackage(),
				StaxConverter.class.getPackage(),
				StreamCacheConverter.class.getPackage(),
				DefaultCamelContext.class.getPackage(),
				EnumTypeConverter.class.getPackage(),
				CompositePackageScanFilter.class.getPackage(),
				LanguageExpression.class.getPackage(),
				BeanLanguage.class.getPackage(),
				ConstantLanguage.class.getPackage(),
				HeaderLanguage.class.getPackage(),
				PropertyLanguage.class.getPackage(),
				SimpleLanguage.class.getPackage(),
				TokenizeLanguage.class.getPackage(),
				XPathLanguage.class.getPackage(),
				DefaultManagementAgent.class.getPackage(),
				CamelContextStartedEvent.class.getPackage(),
				ManagedCamelContext.class.getPackage(),
				BeanDefinition.class.getPackage(),
				PropertyDefinition.class.getPackage(),
				DataFormatsDefinition.class.getPackage(),
				ELExpression.class.getPackage(),
				TopicLoadBalancerDefinition.class.getPackage()) // processor is
				// next
				.addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
						ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatBeanManagerBackedCamelRegistryCanLookupApplicationScopedCdiBean() {
		final Object applicationScopedCdiBean = classUnderTest().lookup(
				ApplicationScopedBean.NAME);

		assertNotNull("lookup(" + ApplicationScopedBean.NAME
				+ ") should have returned an instance of ["
				+ ApplicationScopedBean.class.getName() + "] yet it didn't",
				applicationScopedCdiBean);
	}

	@Test
	public void assertThatLookingUpAnApplicationScopedCdiBeanAlwaysReturnsTheSameInstance() {
		final Object applicationScopedCdiBean1 = classUnderTest().lookup(
				ApplicationScopedBean.NAME);
		final Object applicationScopedCdiBean2 = classUnderTest().lookup(
				ApplicationScopedBean.NAME);

		assertSame("lookup(" + ApplicationScopedBean.NAME
				+ ") should always return the same instance of ["
				+ ApplicationScopedBean.class.getName() + "] yet it didn't",
				applicationScopedCdiBean1, applicationScopedCdiBean2);
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private BeanManagerBackedCamelRegistry classUnderTest() {
		return new BeanManagerBackedCamelRegistry(this.beanManager);
	}
}
