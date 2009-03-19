/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item.internal.transformer;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

import com.acme.orderplacement.integration.inbound.item.Channels;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;
import com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent;
import com.external.schema.ns.models.item._1.Item;
import com.external.schema.ns.models.item._1.ItemSpec;

/**
 * <p>
 * Transforms an {@link ItemCreatedEvent <code>ItemCreatedEvent</code>} - i.e.
 * the <tt>JAXB</tt> generated representation of an XML document containing an
 * <tt>Item Created Event</tt> - into an {@link ItemDto <code>ItemDto</code>}
 * ready for consumption by the {@link ItemStorageService
 * <code>ItemStorageService</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MessageEndpoint(ItemCreatedEventJaxbToTargetTransformer.COMPONENT_NAME)
public class ItemCreatedEventJaxbToTargetTransformer {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "integration.inbound.item.ItemCreatedEventJaxbToTargetTransformer";

	private final Logger log = LoggerFactory.getLogger(getClass());

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param itemCreatedEventAsJaxbObject
	 * @return
	 */
	@Transformer(inputChannel = Channels.ITEM_CREATED_EVENTS_JAXB, outputChannel = Channels.ITEM_CREATED_EVENTS_TARGET)
	public ItemDto transformFromJaxb(
			final ItemCreatedEvent itemCreatedEventAsJaxbObject) {
		this.log
				.debug(
						"Attempting to convert event [{}] into final representation ...",
						itemCreatedEventAsJaxbObject);

		final Item createdItem = itemCreatedEventAsJaxbObject.getCreatedItem();
		final List<ItemSpec> itemSpecs = createdItem.getItemSpecs()
				.getItemSpec();
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
