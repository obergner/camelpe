/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.internal.typeconverter;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.camel.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;
import com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent;
import com.external.schema.ns.models.item._1.Item;
import com.external.schema.ns.models.item._1.ItemSpec;

/**
 * <p>
 * TODO: Insert short summary for JaxbToItemBeanTypeConverter
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Converter
public final class JaxbToItemBeanTypeConverter {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param itemCreatedEventAsJaxbObject
	 * @return
	 */
	@Converter
	public ItemDto convertFromJaxb(
			final ItemCreatedEvent itemCreatedEventAsJaxbObject) {
		this.log
				.debug(
						"Attempting to convert event [{}] into final representation ...",
						itemCreatedEventAsJaxbObject);

		final Item createdItem = itemCreatedEventAsJaxbObject.getCreatedItem();
		final List<ItemSpec> itemSpecs = createdItem.getItemSpecs()
				.getItemSpecs();
		final SortedSet<ItemSpecificationDto> convertedItemSpecs = new TreeSet<ItemSpecificationDto>();
		for (final ItemSpec currentItemSpec : itemSpecs) {
			convertedItemSpecs.add(new ItemSpecificationDto(currentItemSpec
					.getItemSpecId(), currentItemSpec.getName()));
		}
		final ItemDto converted = new ItemDto(createdItem.getItemNumber(),
				createdItem.getName(), createdItem.getDescription(),
				convertedItemSpecs);
		this.log.debug("Successfully converted into [{}]", converted);

		return converted;
	}
}
