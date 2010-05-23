/**
 * 
 */
package com.acme.orderplacement.service.item;

import com.acme.orderplacement.framework.service.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.service.item.dto.ItemDto;

/**
 * <p>
 * <tt>Entity Service</tt> for registering, updating and removing
 * {@link com.acme.orderplacement.domain.item.Item <code>Item</code>}s.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public interface ItemStorageService {

	String SERVICE_NAME = "service.item.ItemStorageService";

	/**
	 * @param newItemToRegister
	 * @throws EntityAlreadyRegisteredException
	 * @throws IllegalArgumentException
	 */
	void registerItem(ItemDto newItemToRegister)
			throws EntityAlreadyRegisteredException, IllegalArgumentException;
}
