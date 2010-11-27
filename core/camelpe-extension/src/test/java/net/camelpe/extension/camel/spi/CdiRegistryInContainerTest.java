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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import net.camelpe.extension.camel.spi.beans.registry.CdiBeansSharingTheSameSuperclass;
import net.camelpe.extension.camel.spi.beans.registry.ExplicitlyNamedApplicationScopedBean;
import net.camelpe.extension.camel.spi.beans.registry.ExplicitlyNamedRequestScopedBean;

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
public class CdiRegistryInContainerTest {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	@Inject
	private BeanManager beanManager;

	// Test fixture
	// -------------------------------------------------------------------------

	@Deployment
	public static JavaArchive createTestArchive() {
		final JavaArchive testModule = ShrinkWrap
		        .create(JavaArchive.class, "test.jar")
		        .addPackages(false,
		                ExplicitlyNamedApplicationScopedBean.class.getPackage())
		        .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()),
		                ArchivePaths.create("beans.xml"));

		return testModule;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void assertThatCdiRegistryCanLookupApplicationScopedCdiBean() {
		final Object applicationScopedCdiBean = classUnderTest().lookup(
		        ExplicitlyNamedApplicationScopedBean.NAME);

		assertNotNull("lookup(" + ExplicitlyNamedApplicationScopedBean.NAME
		        + ") should have returned an instance of ["
		        + ExplicitlyNamedApplicationScopedBean.class.getName()
		        + "] yet it didn't", applicationScopedCdiBean);
	}

	@Test
	public void assertThatCdiRegistryCanLookupApplicationScopedCdiBeanRestrictedByType() {
		final ExplicitlyNamedApplicationScopedBean applicationScopedCdiBean = classUnderTest()
		        .lookup(ExplicitlyNamedApplicationScopedBean.NAME,
		                ExplicitlyNamedApplicationScopedBean.class);

		assertNotNull("lookup(" + ExplicitlyNamedApplicationScopedBean.NAME
		        + ", " + ExplicitlyNamedApplicationScopedBean.class.getName()
		        + ") should have returned an instance of ["
		        + ExplicitlyNamedApplicationScopedBean.class.getName()
		        + "] yet it didn't", applicationScopedCdiBean);
	}

	@Test
	public void assertThatLookingUpAnApplicationScopedCdiBeanAlwaysReturnsTheSameInstance() {
		final Object applicationScopedCdiBean1 = classUnderTest().lookup(
		        ExplicitlyNamedApplicationScopedBean.NAME);
		final Object applicationScopedCdiBean2 = classUnderTest().lookup(
		        ExplicitlyNamedApplicationScopedBean.NAME);

		assertSame("lookup(" + ExplicitlyNamedApplicationScopedBean.NAME
		        + ") should always return the same instance of ["
		        + ExplicitlyNamedApplicationScopedBean.class.getName()
		        + "] yet it didn't", applicationScopedCdiBean1,
		        applicationScopedCdiBean2);
	}

	@Test
	public void assertThatLookingUpARequestScopedCdiBeanAlwaysReturnsTheSameInstance() {
		final Object requestScopedCdiBean1 = classUnderTest().lookup(
		        ExplicitlyNamedRequestScopedBean.NAME);
		final Object requestScopedCdiBean2 = classUnderTest().lookup(
		        ExplicitlyNamedRequestScopedBean.NAME);

		assertSame("lookup(" + ExplicitlyNamedRequestScopedBean.NAME
		        + ") should always return the same instance of ["
		        + ExplicitlyNamedRequestScopedBean.class.getName()
		        + "] yet it didn't", requestScopedCdiBean1,
		        requestScopedCdiBean2);
	}

	@Test
	public void assertThatCdiRegistryLooksUpAllMatchingCdiBeansByType() {
		final Map<String, CdiBeansSharingTheSameSuperclass> matchingBeansByName = classUnderTest()
		        .lookupByType(CdiBeansSharingTheSameSuperclass.class);

		assertNotNull(
		        "lookupByType("
		                + CdiBeansSharingTheSameSuperclass.class.getName()
		                + ") should not return null yet it did",
		        matchingBeansByName);
		assertEquals(
		        "lookupByType("
		                + CdiBeansSharingTheSameSuperclass.class.getName()
		                + ") should return all beans having the specified superclass, yet it didn't",
		        2, matchingBeansByName.size());
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private CdiRegistry classUnderTest() {
		return new CdiRegistry(this.beanManager);
	}
}
