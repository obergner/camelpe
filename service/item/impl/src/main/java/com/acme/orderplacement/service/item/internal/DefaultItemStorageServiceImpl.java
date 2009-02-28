/**
 * 
 */
package com.acme.orderplacement.service.item.internal;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.domain.item.ItemSpecification;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.persistence.item.ItemDao;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;
import com.acme.orderplacement.service.support.exception.entity.EntityAlreadyRegisteredException;

/**
 * <p>
 * TODO: Insert short summary for DefaultItemStorageServiceImpl
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for DefaultItemStorageServiceImpl
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Service(ItemStorageService.SERVICE_NAME)
public class DefaultItemStorageServiceImpl implements ItemStorageService {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	@Resource(name = ItemDao.SERVICE_NAME)
	private ItemDao itemDao;

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @param itemDao
	 *            the itemDao to set
	 */
	public final void setItemDao(final ItemDao itemDao)
			throws IllegalArgumentException {
		Validate.notNull(itemDao, "itemDao");
		this.itemDao = itemDao;
	}

	// -------------------------------------------------------------------------
	// com.acme.orderplacement.service.item.ItemStorageService
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.service.item.ItemStorageService#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)
	 */
	public void registerItem(final ItemDto newItemToRegister)
			throws EntityAlreadyRegisteredException, IllegalArgumentException {
		try {
			Validate.notNull(newItemToRegister, "newItemToRegister");
			ensureNotExistsItemHavingItemNumber(newItemToRegister
					.getItemNumber());

			getItemDao().makePersistent(convertIntoItem(newItemToRegister));
		} catch (final ObjectNotTransientException e) {

			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------
	// Protected
	// -------------------------------------------------------------------------

	/**
	 * @return the itemDao
	 */
	protected final ItemDao getItemDao() {
		return this.itemDao;
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private final void ensureNotExistsItemHavingItemNumber(
			final String itemNumber) throws EntityAlreadyRegisteredException {
		final Item itemHavingItemNumber = getItemDao().findByItemNumber(
				itemNumber);
		if (itemHavingItemNumber != null) {

			throw new EntityAlreadyRegisteredException(itemHavingItemNumber);
		}
	}

	private Item convertIntoItem(final ItemDto itemDtoToConvert) {
		try {
			final Item convertedItem = new Item();
			convertedItem.setItemNumber(itemDtoToConvert.getItemNumber());
			convertedItem.setDescription(itemDtoToConvert.getDescription());
			convertedItem.setName(itemDtoToConvert.getName());

			for (final ItemSpecificationDto currentSpecDto : itemDtoToConvert
					.getSpecifications()) {
				final ItemSpecification itemSpec = new ItemSpecification();
				itemSpec.setItemSpecificationNumber(currentSpecDto
						.getItemSpecificationNumber());

				convertedItem.addSpecification(itemSpec);
			}

			return convertedItem;
		} catch (final CollaborationPreconditionsNotMetException e) {
			// Could only happen if one of the newly created ItemSpecifications
			// would already belong to a different Item. Therefore, it is safe
			// to assume that this will NEVER happen.
			throw new RuntimeException(e);
		}
	}
}
