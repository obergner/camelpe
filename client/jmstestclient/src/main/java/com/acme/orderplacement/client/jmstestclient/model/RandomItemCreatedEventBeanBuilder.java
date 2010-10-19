/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient.model;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

/**
 * <p>
 * TODO: Insert short summary for RandomItemCreatedEventBeanBuilder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class RandomItemCreatedEventBeanBuilder {

	private final Random randomNumberGenerator = new Random();

	RandomItemCreatedEventBeanBuilder() {
		// Noop
	}

	public ItemCreatedEventBean build() {
		final String eventId = "urn:event:" + UUID.randomUUID().toString();
		final HeadersBean headers = new HeadersBean(eventId);

		final String randomItemNumber = UUID.randomUUID().toString();
		final String itemNumber = "urn:item:" + randomItemNumber;
		final String itemName = "Item - " + randomItemNumber;
		final String itemDescription = "Description - " + randomItemNumber;

		final int numberOfSpecifications = this.randomNumberGenerator
				.nextInt(10) + 1;
		final SortedSet<ItemSpecificationBean> specifications = new TreeSet<ItemSpecificationBean>();
		for (int i = 0; i < numberOfSpecifications; i++) {
			final String randomItemSpecificationNumber = UUID.randomUUID()
					.toString();
			final String itemSpecificationNumber = "urn:item-specification:"
					+ randomItemSpecificationNumber;
			final String itemSpecificationName = "Item Specification - "
					+ randomItemSpecificationNumber;

			specifications.add(new ItemSpecificationBean(
					itemSpecificationNumber, itemSpecificationName));
		}

		final ItemBean createdItem = new ItemBean(itemNumber, itemName,
				itemDescription, specifications);

		return new ItemCreatedEventBean(headers, createdItem);
	}
}
