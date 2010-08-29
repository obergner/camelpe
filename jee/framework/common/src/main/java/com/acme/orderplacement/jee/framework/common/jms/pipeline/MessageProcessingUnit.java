/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jms.pipeline;

import javax.jms.Message;

/**
 * <p>
 * TODO: Insert short summary for MessageProcessingUnit
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: CURRENTLY NOT USED
 */
public interface MessageProcessingUnit<T extends Message> {

	ProcessingResult process(T message) throws Exception;
}
