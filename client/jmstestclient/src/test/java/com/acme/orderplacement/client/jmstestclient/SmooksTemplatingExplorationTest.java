/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.milyn.Smooks;
import org.milyn.payload.JavaSource;
import org.xml.sax.SAXException;

import com.acme.orderplacement.client.jmstestclient.model.ItemCreatedEventBean;

/**
 * <p>
 * TODO: Insert short summary for SmooksTemplatingExplorationTest
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class SmooksTemplatingExplorationTest {

	/**
	 * Test method for
	 * {@link com.acme.orderplacement.client.jmstestclient.JmsTestClient#run()}.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public final void testRun() throws IOException, SAXException {
		final Smooks smooks = new Smooks("smooks-config.xml");

		final ItemCreatedEventBean sourceObject = ItemCreatedEventBean.BUILDER
				.build();

		final JavaSource sampleInput = new JavaSource(sourceObject);
		sampleInput.setEventStreamRequired(false);
		final Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("itemCreatedEvent", sourceObject);
		sampleInput.setBeans(beans);

		final StringWriter output = new StringWriter();

		smooks.filterSource(sampleInput, new StreamResult(output));

		System.out.println(output.toString());
	}
}
