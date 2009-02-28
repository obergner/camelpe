/**
 * 
 */
package com.acme.orderplacement.persistence.support.jpa.hibernate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p>
 * TODO: Insert short summary for SampleEntity
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for SampleEntity
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "SUPPORT", name = "TEST_ENTITY")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "SUPPORT.ID_SEQ_TEST_ENTITY")
public class SampleEntity {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_GEN")
	private Long id;

	@Basic
	@Column(name = "NAME")
	private String name;

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the id
	 */
	public final Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
}
