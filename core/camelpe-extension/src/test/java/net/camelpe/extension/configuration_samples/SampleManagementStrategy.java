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

package net.camelpe.extension.configuration_samples;

import java.util.EventObject;
import java.util.List;

import net.camelpe.api.CamelContextInjectable;

import org.apache.camel.ManagementStatisticsLevel;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.EventFactory;
import org.apache.camel.spi.EventNotifier;
import org.apache.camel.spi.ManagementAgent;
import org.apache.camel.spi.ManagementNamingStrategy;
import org.apache.camel.spi.ManagementObjectStrategy;
import org.apache.camel.spi.ManagementStrategy;
import org.fusesource.commons.management.Statistic;
import org.fusesource.commons.management.Statistic.UpdateMode;

/**
 * <p>
 * TODO: Insert short summary for SampleManagementStrategy
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@CamelContextInjectable
public class SampleManagementStrategy implements ManagementStrategy {

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#addEventNotifier(org.apache.camel.spi.EventNotifier)
	 */
	@Override
	public void addEventNotifier(final EventNotifier eventNotifier) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#getEventFactory()
	 */
	@Override
	public EventFactory getEventFactory() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#getEventNotifiers()
	 */
	@Override
	public List<EventNotifier> getEventNotifiers() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#getManagementAgent()
	 */
	@Override
	public ManagementAgent getManagementAgent() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#getManagementNamingStrategy()
	 */
	@Override
	public ManagementNamingStrategy getManagementNamingStrategy() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#getStatisticsLevel()
	 */
	@Override
	public ManagementStatisticsLevel getStatisticsLevel() {
		return null;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#isOnlyManageProcessorWithCustomId()
	 */
	@Override
	public boolean isOnlyManageProcessorWithCustomId() {
		return false;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#manageProcessor(org.apache.camel.model.ProcessorDefinition)
	 */
	@Override
	public boolean manageProcessor(final ProcessorDefinition<?> definition) {
		return false;
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#onlyManageProcessorWithCustomId(boolean)
	 */
	@Override
	public void onlyManageProcessorWithCustomId(final boolean flag) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#setEventFactory(org.apache.camel.spi.EventFactory)
	 */
	@Override
	public void setEventFactory(final EventFactory eventFactory) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#setEventNotifiers(java.util.List)
	 */
	@Override
	public void setEventNotifiers(final List<EventNotifier> eventNotifier) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#setManagementAgent(org.apache.camel.spi.ManagementAgent)
	 */
	@Override
	public void setManagementAgent(final ManagementAgent managementAgent) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#setManagementNamingStrategy(org.apache.camel.spi.ManagementNamingStrategy)
	 */
	@Override
	public void setManagementNamingStrategy(
	        final ManagementNamingStrategy strategy) {
	}

	/**
	 * @see org.apache.camel.spi.ManagementStrategy#setStatisticsLevel(org.apache.camel.ManagementStatisticsLevel)
	 */
	@Override
	public void setStatisticsLevel(final ManagementStatisticsLevel level) {
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#createStatistic(java.lang.String,
	 *      java.lang.Object,
	 *      org.fusesource.commons.management.Statistic.UpdateMode)
	 */
	@Override
	public Statistic createStatistic(final String name, final Object owner,
	        final UpdateMode updateMode) {
		return null;
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#getManagedObjectName(java.lang.Object,
	 *      java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getManagedObjectName(final Object managableObject,
	        final String customName, final Class<T> nameType) throws Exception {
		return null;
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#isManaged(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean isManaged(final Object managableObject, final Object name) {
		return false;
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#manageNamedObject(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void manageNamedObject(final Object managedObject,
	        final Object preferedName) throws Exception {
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#manageObject(java.lang.Object)
	 */
	@Override
	public void manageObject(final Object managedObject) throws Exception {
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#notify(java.util.EventObject)
	 */
	@Override
	public void notify(final EventObject event) throws Exception {
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#unmanageNamedObject(java.lang.Object)
	 */
	@Override
	public void unmanageNamedObject(final Object name) throws Exception {
	}

	/**
	 * @see org.fusesource.commons.management.ManagementStrategy#unmanageObject(java.lang.Object)
	 */
	@Override
	public void unmanageObject(final Object managedObject) throws Exception {
	}

	/**
	 * @see org.apache.camel.Service#start()
	 */
	@Override
	public void start() throws Exception {
	}

	/**
	 * @see org.apache.camel.Service#stop()
	 */
	@Override
	public void stop() throws Exception {
	}

	@Override
	public boolean removeEventNotifier(final EventNotifier eventNotifier) {
		return false;
	}

	@Override
	public ManagementObjectStrategy getManagementObjectStrategy() {
		return null;
	}

	@Override
	public void setManagementObjectStrategy(
	        final ManagementObjectStrategy strategy) {
	}
}
