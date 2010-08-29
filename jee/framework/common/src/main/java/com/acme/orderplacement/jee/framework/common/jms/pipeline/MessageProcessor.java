/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import javax.jms.Message;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessor
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 * @param <T>
 * 
 *            TODO: CURRENTLY NOT USED
 */
public interface MessageProcessor<T extends Message> {

	void process(final T message) throws Exception;

}