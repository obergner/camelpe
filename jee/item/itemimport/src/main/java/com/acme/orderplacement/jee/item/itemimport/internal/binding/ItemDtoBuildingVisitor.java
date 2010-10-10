/**
 * 
 */
package com.acme.orderplacement.jee.item.itemimport.internal.binding;

import java.io.IOException;

import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.orderplacement.service.item.dto.ItemDto;

/**
 * <p>
 * TODO: Insert short summary for ItemDtoBuildingVisitor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ItemDtoBuildingVisitor implements SAXVisitAfter {

	public static final String CREATED_ITEM_BEAN_NAME = "createdItem";

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see org.milyn.delivery.sax.SAXVisitAfter#visitAfter(org.milyn.delivery.sax.SAXElement,
	 *      org.milyn.container.ExecutionContext)
	 */
	@Override
	public void visitAfter(final SAXElement element,
			final ExecutionContext executionContext) throws SmooksException,
			IOException {
		final ItemDtoBuilderBean itemDtoBuilderBean = executionContext
				.getBeanContext().getBean(ItemDtoBuilderBean.class);
		final ItemDto itemDto = itemDtoBuilderBean.build();
		executionContext.getBeanContext().addBean(CREATED_ITEM_BEAN_NAME,
				itemDto);
		this.log.trace("Added Bean [{}] to BeanContext", itemDto);
	}

}
