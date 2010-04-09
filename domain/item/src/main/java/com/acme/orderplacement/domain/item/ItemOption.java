/**
 * 
 */
package com.acme.orderplacement.domain.item;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.support.AbstractVersionedDomainObject;

/**
 * <p>
 * TODO: Insert short summary for ItemOption
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for ItemOption
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::description"
 */
@Entity
@Table(schema = "ITEM", name = "ITEM_OPTION")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_ITEM_OPTION")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = ItemOption.Queries.BY_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.ItemOption itemOption where itemOption.name like '%:name%'"),
		@org.hibernate.annotations.NamedQuery(name = ItemOption.Queries.BY_DESCRIPTION_LIKE, query = "from com.acme.orderplacement.domain.item.ItemOption itemOption where itemOption.description like '%:description%'"),
		@org.hibernate.annotations.NamedQuery(name = ItemOption.Queries.BY_TYPE, query = "from com.acme.orderplacement.domain.item.ItemOption itemOption where itemOption.type = :type"),
		@org.hibernate.annotations.NamedQuery(name = ItemOption.Queries.BY_TYPE_LIKE, query = "from com.acme.orderplacement.domain.item.ItemOption itemOption where itemOption.type like '%:type%'") })
public class ItemOption extends AbstractVersionedDomainObject<Long> implements
		Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>PrivateCustomer</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_NAME = "itemOption.byName";

		public static final String BY_NAME_LIKE = "itemOption.byNameLike";

		public static final String BY_DESCRIPTION_LIKE = "itemOption.byDescriptionLike";

		public static final String BY_TYPE = "itemOption.byType";

		public static final String BY_TYPE_LIKE = "itemOption.byTypeLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 1051729235189941509L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public ItemOption() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public ItemOption(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public ItemOption(final Integer version) {
		super(version);
	}

	/**
	 * @param id
	 * @param version
	 */
	public ItemOption(final Long id, final Integer version) {
		super(id, version);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>ItemOption</code>'s human-readable <i>name</i>, uniquely
	 * identifying it. Must <em>not</em> be <code>null</code>.
	 * </p>
	 * 
	 * @uml.property name="name"
	 */
	@NotNull
	@Size(min = 2, max = 60)
	@Basic
	@Column(name = "NAME", unique = true, nullable = false, length = 60)
	private String name;

	/**
	 * Getter of the property <tt>name</tt>
	 * 
	 * @return Returns the name.
	 * @uml.property name="name"
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * 
	 * @param name
	 *            The name to set.
	 * @throws IllegalArgumentException
	 *             If <code>name</code> is <code>null</code> or blank
	 * 
	 * @uml.property name="name"
	 */
	public void setName(final String name) throws IllegalArgumentException {
		Validate.notEmpty(name, "name");
		this.name = name;
	}

	/**
	 * <p>
	 * A plain-text description of this <code>ItemOption</code>, explaining its
	 * purpose to a human reader.
	 * </p>
	 * 
	 * @uml.property name="description"
	 */
	@NotNull
	@Size(min = 3, max = 200)
	@Basic
	@Column(name = "DESCRIPTION", unique = false, nullable = true, length = 200)
	private String description;

	/**
	 * Getter of the property <tt>description</tt>
	 * 
	 * @return Returns the description.
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter of the property <tt>description</tt>
	 * 
	 * @param description
	 *            The description to set.
	 * @uml.property name="description"
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * This <code>ItemOption</code> <i>type</i>, i.e. <tt>Color</tt>,
	 * <tt>Size</tt> etc.
	 * </p>
	 * 
	 * @uml.property name="type"
	 */
	@NotNull
	@Size(min = 2, max = 30)
	@Basic
	@Column(name = "TYPE", unique = false, nullable = false, length = 30)
	private String type;

	/**
	 * Getter of the property <tt>type</tt>
	 * 
	 * @return Returns the type.
	 * @uml.property name="type"
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Setter of the property <tt>type</tt>
	 * 
	 * @param type
	 *            The type to set.
	 * @throws IllegalArgumentException
	 *             If <code>type</code> is <code>null</code> or blank
	 * 
	 * @uml.property name="type"
	 */
	public void setType(final String type) throws IllegalArgumentException {
		Validate.notEmpty(type, "type");
		this.type = type;
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ItemOption other = (ItemOption) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"name", this.name).append("description", this.description)
				.append("type", this.type).toString();
	}

}
