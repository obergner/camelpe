/**
 * 
 */
package com.acme.orderplacement.integration.inbound.itemimport.internal.typeconverter;

import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Test;

import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;
import com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent;
import com.external.schema.ns.models.item._1.Item;
import com.external.schema.ns.models.item._1.ItemSpec;
import com.external.schema.ns.models.item._1.ItemSpecs;

/**
 * <p>
 * Test {@link JaxbToItemBeanTypeConverter}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JaxbToItemBeanTypeConverterTest {

	/**
	 * 
	 */
	private static final String EXPECTED_ITEM_SPEC_NAME = "Item Spec Name";
	/**
	 * 
	 */
	private static final String EXPECTED_ITEM_SPEC_ID = "ITMSP-23-567334";
	/**
	 * 
	 */
	private static final String EXPECTED_ITEM_NAME = "Item Name";
	/**
	 * 
	 */
	private static final String EXPECTED_ITEM_DESCRIPTION = "Description";
	/**
	 * 
	 */
	private static final String EXPECTED_ITEM_NUMBER = "ITM-02-987235";
	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final JaxbToItemBeanTypeConverter classUnderTest = new JaxbToItemBeanTypeConverter();

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.integration.inbound.item.internal.transformer.ItemCreatedEventJaxbToTargetTransformer#transformFromJaxb(com.external.schema.ns.events.itemcreated._1.ItemCreatedEvent)}
	 * .
	 */
	@Test
	public final void transformFromJaxbShouldProduceValidTargetObject() {
		final ItemDto itemDto = this.classUnderTest
				.convertFromJaxb(createValidJaxbItemCreatedEvent());

		Assert.assertNotNull("transformFromJaxb(...) returned <null>", itemDto);
		Assert
				.assertEquals(
						"transformFromJaxb(...) returned ItemDto with wrong Item Number",
						EXPECTED_ITEM_NUMBER, itemDto.getItemNumber());
		Assert.assertEquals(
				"transformFromJaxb(...) returned ItemDto with wrong Item Name",
				EXPECTED_ITEM_NAME, itemDto.getName());
		Assert
				.assertEquals(
						"transformFromJaxb(...) returned ItemDto with wrong Item Description",
						EXPECTED_ITEM_DESCRIPTION, itemDto.getDescription());

		final SortedSet<ItemSpecificationDto> specifications = itemDto
				.getSpecifications();
		Assert
				.assertNotNull(
						"transformFromJaxb(...) returned ItemDto without Item Specifications",
						specifications);
		Assert
				.assertEquals(
						"transformFromJaxb(...) returned ItemDto with wrong number of Item Specifications",
						1, specifications.size());
		final ItemSpecificationDto firstSpec = specifications.iterator().next();
		Assert
				.assertEquals(
						"transformFromJaxb(...) returned ItemDto with Item Specification having wrong Item Specification Number",
						EXPECTED_ITEM_SPEC_ID, firstSpec
								.getItemSpecificationNumber());
		Assert
				.assertEquals(
						"transformFromJaxb(...) returned ItemDto with Item Specification having wrong Item Specification Name",
						EXPECTED_ITEM_SPEC_NAME, firstSpec.getName());
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private ItemCreatedEvent createValidJaxbItemCreatedEvent() {
		final ItemCreatedEvent result = new ItemCreatedEvent();
		result.setCreatedItem(new Item());
		result.getCreatedItem().setItemNumber(EXPECTED_ITEM_NUMBER);
		result.getCreatedItem().setDescription(EXPECTED_ITEM_DESCRIPTION);
		result.getCreatedItem().setName(EXPECTED_ITEM_NAME);
		result.getCreatedItem().setItemSpecs(new ItemSpecs());
		final ItemSpec itemSpec = new ItemSpec();
		result.getCreatedItem().getItemSpecs().getItemSpecs().add(itemSpec);
		itemSpec.setItemSpecId(EXPECTED_ITEM_SPEC_ID);
		itemSpec.setName(EXPECTED_ITEM_SPEC_NAME);

		return result;
	}
}
