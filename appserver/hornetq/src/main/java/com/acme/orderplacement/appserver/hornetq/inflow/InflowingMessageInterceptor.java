/**
 * 
 */
package com.acme.orderplacement.appserver.hornetq.inflow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.Interceptor;
import org.hornetq.api.core.Message;
import org.hornetq.api.core.SimpleString;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.protocol.core.Packet;
import org.hornetq.core.protocol.core.impl.wireformat.SessionSendMessage;
import org.hornetq.spi.core.protocol.RemotingConnection;

/**
 * <p>
 * A {@link org.hornetq.api.core.Interceptor <code>HornetQ Interceptor</code>}
 * for setting an <code>InflowID</code> and <code>InflowTimestamp</code> on each
 * incoming message.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class InflowingMessageInterceptor implements Interceptor {

	private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	private static final SimpleString INFLOW_ID_HEADER = SimpleString
			.toSimpleString("InflowID");

	private static final SimpleString INFLOW_TIMESTAMP_HEADER = SimpleString
			.toSimpleString("InflowTimestamp");

	private final Logger log = Logger.getLogger(getClass());

	/**
	 * SimpleDateFormat is not thread safe. Unwilling to pull in another
	 * dependency, so e.g. FastDateFormat from Apache is no option.
	 */
	private final ThreadLocal<DateFormat> iso8601DateFormatter = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ISO8601_DATE_FORMAT);
		}
	};

	/**
	 * @see org.hornetq.api.core.Interceptor#intercept(org.hornetq.core.protocol.core.Packet,
	 *      org.hornetq.spi.core.protocol.RemotingConnection)
	 */
	@Override
	public boolean intercept(final Packet packet,
			final RemotingConnection connection) throws HornetQException {
		if (packet instanceof SessionSendMessage) {
			final SessionSendMessage realPacket = SessionSendMessage.class
					.cast(packet);
			final Message msg = realPacket.getMessage();

			final SimpleString inflowTimestamp = newInflowTimestamp();
			msg.putStringProperty(INFLOW_TIMESTAMP_HEADER, inflowTimestamp);

			final SimpleString inflowId = newInflowId();
			msg.putStringProperty(INFLOW_ID_HEADER, inflowId);

			if (this.log.isTraceEnabled()) {
				this.log.trace("Set InflowID = [" + inflowId.toString()
						+ "] and InflowTimestamp = ["
						+ inflowTimestamp.toString() + "] on message [ID = "
						+ msg.getMessageID() + "]");
			}
		}

		return true;
	}

	private SimpleString newInflowId() {
		return SimpleString.toSimpleString("urn:event-inflow:"
				+ UUID.randomUUID());
	}

	private SimpleString newInflowTimestamp() {
		return SimpleString.toSimpleString(this.iso8601DateFormatter.get()
				.format(new Date()));
	}
}
