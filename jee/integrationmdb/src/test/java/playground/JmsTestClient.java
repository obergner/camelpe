/**
 * 
 */
package playground;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>
 * TODO: Insert short summary for JmsTestClient
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JmsTestClient {

	/**
	 * @param args
	 * @throws NamingException
	 * @throws JMSException
	 */
	public static void main(final String[] args) throws NamingException,
			JMSException {
		System.out.println("JMSClient: Establishing initial context");
		System.out.println(" Establishing initial context");
		final Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		final Context ctx = new InitialContext(env);
		System.out.println("JMSClient: Looking up ConnectionFactory");
		final ConnectionFactory cf = (ConnectionFactory) ctx
				.lookup("ConnectionFactory");
		System.out.println("JMSClient: Lookup Topic");
		final Destination outDest = (Destination) ctx
				.lookup("dynamicQueues/com/acme/jms/ItemCreatedEventsTopic");
		System.out.println("destination = " + outDest.toString());
		System.out.println("JMSClient: Establishing connection");
		final Connection connection = cf.createConnection();
		System.out.println("JMSClient: Establishing session");
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		final MessageProducer destSender = session.createProducer(outDest);
		System.out.println("JMSClient: Sending message");
		// Send argument default "1 DonationFund 1"
		final TextMessage toutMessage = session.createTextMessage("TEST");
		destSender.send(toutMessage);
		System.out.println("JMSClient: Message sent!");
		destSender.close();
		session.close();
		connection.close();
	}

}
