/**
 * 
 */
package com.acme.orderplacement.service.item.internal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acme.orderplacement.domain.item.Item;
import com.acme.orderplacement.domain.item.ItemSpecification;
import com.acme.orderplacement.domain.support.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.jee.item.persistence.ItemDao;
import com.acme.orderplacement.persistence.support.exception.ObjectNotTransientException;
import com.acme.orderplacement.service.item.ItemStorageService;
import com.acme.orderplacement.service.item.dto.ItemDto;
import com.acme.orderplacement.service.item.dto.ItemSpecificationDto;
import com.acme.orderplacement.service.support.exception.entity.EntityAlreadyRegisteredException;
import com.acme.orderplacement.service.support.meta.annotation.ServiceOperation;

/**
 * <p>
 * Default implementation of the {@link ItemStorageService
 * <code>ItemStorageService</code>} that delegates to an {@link ItemDao
 * <code>ItemDao</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Service(ItemStorageService.SERVICE_NAME)
@Transactional
public class DefaultItemStorageServiceImpl implements ItemStorageService {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource(name = ItemDao.REPOSITORY_NAME)
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
	// Sanity check
	// -------------------------------------------------------------------------

	@PostConstruct
	public void ensureCorrectlyInitialized() throws IllegalStateException {
		if (this.itemDao == null) {
			throw new IllegalStateException("Unsatisfied dependency: ["
					+ ItemDao.class.getName() + "]");
		}
	}

	// -------------------------------------------------------------------------
	// com.acme.orderplacement.service.item.ItemStorageService
	// -------------------------------------------------------------------------

	/**
	 * @see com.acme.orderplacement.service.item.ItemStorageService#registerItem(com.acme.orderplacement.service.item.dto.ItemDto)
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@ServiceOperation(idempotent = true)
	public void registerItem(final ItemDto newItemToRegister)
			throws EntityAlreadyRegisteredException, IllegalArgumentException,
			RuntimeException {
		try {
			Validate.notNull(newItemToRegister, "newItemToRegister");
			this.log.info("Registering item [{}] ...", newItemToRegister);
			ensureNotExistsItemHavingItemNumber(newItemToRegister
					.getItemNumber());

			final Item registeredItem = getItemDao().makePersistent(
					convertIntoItem(newItemToRegister));

			this.log.info("Item [{}] successfully registered", registeredItem);
		} catch (final ObjectNotTransientException e) {
			/*
			 * This cannot happen since it is impossible that the Item which has
			 * been newly created from the ItemDto passed is not transient.
			 */
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
			this.log
					.error(
							"An item [{}] having the same item number [{}] as the one to register has already been registered",
							itemHavingItemNumber, itemNumber);

			throw new EntityAlreadyRegisteredException(itemHavingItemNumber);
		}
	}

	private Item convertIntoItem(final ItemDto itemDtoToConvert)
			throws RuntimeException {
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
			/*
			 * Could only happen if one of the newly created ItemSpecifications
			 * would already belong to a different Item. Therefore, it is safe
			 * to assume that this will NEVER happen.
			 */
			throw new RuntimeException(e);
		}
	}
}
