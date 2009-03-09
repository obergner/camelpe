/**
 * 
 */
package com.acme.orderplacement.integration.inbound.item.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.acme.orderplacement.service.item.dto.ItemDto;

/**
 * <p>
 * A customer <code>ServiceActivator</code> that receives and stores the
 * {@link ItemDto <code>ItemDto</code>}s converted and propagated by the
 * <tt>Integration Layer</tt>.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@MessageEndpoint(ItemDtoReceiver.COMPONENT_NAME)
public class ItemDtoReceiver {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final String COMPONENT_NAME = "integration.inbound.item.test.ItemDtoReceiver";

	private final List<ItemDto> receivedItemDtos = new ArrayList<ItemDto>();

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @param itemDto
	 */
	@ServiceActivator(inputChannel = "integration.inbound.item.target.ItemCreatedEventsChannel")
	public void receiveAndStore(final ItemDto itemDto) {
		this.receivedItemDtos.add(itemDto);
	}

	/**
	 * @return
	 */
	public List<ItemDto> getReceivedItemDtos() {
		return this.receivedItemDtos;
	}
}
