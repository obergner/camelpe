/**
 * 
 */
package com.acme.orderplacement.client.jmstestclient;

/**
 * <p>
 * TODO: Insert short summary for Sample
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class Sample {

	private final int id;

	private final String name;

	/**
	 * @param id
	 * @param name
	 */
	public Sample(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
}
