/**
 * 
 */
package com.acme.orderplacement.domain.item.promotion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.ForeignKey;

import com.acme.orderplacement.domain.item.offer.Offer;
import com.acme.orderplacement.framework.domain.exception.CollaborationPreconditionsNotMetException;
import com.acme.orderplacement.framework.domain.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.framework.domain.meta.AuditInfo;

/**
 * <p>
 * TODO: Insert short summary for Promotion
 * </p>
 * <p>
 * TODO: Insert comprehensive explanation for Promotion
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * @uml.stereotype uml_id="Archetype::interval"
 */
@Entity
@Table(schema = "ITEM", name = "PROMOTION")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "ITEM.ID_SEQ_PROMOTION")
@NamedQueries( {
		@NamedQuery(name = Promotion.Queries.BY_NAME, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.name = :name"),
		@NamedQuery(name = Promotion.Queries.BY_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.name like '%:name%'"),
		@NamedQuery(name = Promotion.Queries.PROMOTED_AFTER, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.endDate > :date"),
		@NamedQuery(name = Promotion.Queries.PROMOTED_BEFORE, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.startDate < :Date"),
		@NamedQuery(name = Promotion.Queries.PROMOTED_BETWEEN, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where (promotion.endDate >= :startDate and promotion.startDate <= :endDate)"),
		@NamedQuery(name = Promotion.Queries.BY_PROMOTED_OFFER_ID, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.promotedOffer.id = :offerId"),
		@NamedQuery(name = Promotion.Queries.BY_PROMOTED_OFFER_NAME, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.promotedOffer.name = :offerName"),
		@NamedQuery(name = Promotion.Queries.BY_PROMOTED_OFFER_NAME_LIKE, query = "from com.acme.orderplacement.domain.item.promotion.Promotion promotion where promotion.promotedOffer.name like '%:offerName%'") })
public class Promotion extends AbstractAuditableDomainObject<Long> implements
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

		public static final String BY_NAME = "promotion.byName";

		public static final String BY_NAME_LIKE = "promotion.byNameLike";

		public static final String PROMOTED_BEFORE = "promotion.promotedBefore";

		public static final String PROMOTED_AFTER = "promotion.promotedAfter";

		public static final String PROMOTED_BETWEEN = "promotion.promotedBetween";

		public static final String BY_PROMOTED_OFFER_ID = "promotion.byPromotedOfferId";

		public static final String BY_PROMOTED_OFFER_NAME = "promotion.byPromotedOfferName";

		public static final String BY_PROMOTED_OFFER_NAME_LIKE = "promotion.byPromotedOfferNameLike";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -9211708931692152746L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Promotion() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Promotion(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Promotion(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Promotion(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Promotion(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Promotion(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * A human-readable <i>name</i> uniquely identifying this
	 * <code>Promotion</code>.
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
	 * The <code>Date</code> on which this <code>Promotion</code> starts.
	 * </p>
	 * 
	 * @uml.property name="startDate"
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", unique = false, nullable = false)
	private Date startDate;

	/**
	 * Getter of the property <tt>startDate</tt>
	 * 
	 * @return Returns the startDate.
	 * @uml.property name="startDate"
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Setter of the property <tt>startDate</tt>
	 * 
	 * @param startDate
	 *            The startDate to set.
	 * @throws IllegalArgumentException
	 *             If <code>startDate</code> is <code>null</code>
	 * 
	 * @uml.property name="startDate"
	 */
	public void setStartDate(final Date startDate)
			throws IllegalArgumentException {
		Validate.notNull(startDate, "startDate");
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * The <code>Date</code> on which this <code>Promotion</code> ends.
	 * </p>
	 * 
	 * @uml.property name="endDate"
	 */
	@Future
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", unique = false, nullable = true)
	private Date endDate;

	/**
	 * Getter of the property <tt>endDate</tt>
	 * 
	 * @return Returns the endDate.
	 * @uml.property name="endDate"
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Setter of the property <tt>endDate</tt>
	 * 
	 * @param endDate
	 *            The endDate to set.
	 * @uml.property name="endDate"
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>
	 * A <tt>Promotional Text</tt> aimed at convincing a
	 * <tt>Potential Buyer</tt> to purchase the promoted <code>Offer</code>.
	 * </p>
	 * 
	 * @uml.property name="promotionalText"
	 */
	@NotNull
	@Size(min = 10, max = 2000)
	@Basic
	@Column(name = "PROMOTIONAL_TEXT", unique = false, nullable = false, length = 2000)
	private String promotionalText;

	/**
	 * Getter of the property <tt>promotionalText</tt>
	 * 
	 * @return Returns the promotionalText.
	 * @uml.property name="promotionalText"
	 */
	public String getPromotionalText() {
		return this.promotionalText;
	}

	/**
	 * Setter of the property <tt>promotionalText</tt>
	 * 
	 * @param promotionalText
	 *            The promotionalText to set.
	 * @throws IllegalArgumentException
	 *             If <code>promotionalText</code> is <code>null</code> or blank
	 * 
	 * @uml.property name="promotionalText"
	 */
	public void setPromotionalText(final String promotionalText)
			throws IllegalArgumentException {
		Validate.notEmpty(promotionalText, "promotionalText");
		this.promotionalText = promotionalText;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * @uml.property name="promotedOffer"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="promotion:com.acme.orderplacement.domain.item.offer.Offer"
	 * @uml.association name="Promotion - Promoted Offer"
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_OFFER", referencedColumnName = "ID", nullable = false, unique = true)
	@ForeignKey(name = "FK_PROMOTION_OFFER")
	private Offer promotedOffer;

	/**
	 * Getter of the property <tt>promotedOffer</tt>
	 * 
	 * @return Returns the promotedOffer.
	 * @uml.property name="promotedOffer"
	 */
	public Offer getPromotedOffer() {
		return this.promotedOffer;
	}

	/**
	 * Setter of the property <tt>promotedOffer</tt>
	 * 
	 * @param promotedOffer
	 *            The promotedOffer to set.
	 * @throws IllegalCollaborationPreconditionsNotMetExceptionArgumentException
	 *             If this <code>Promotion</code> already promotes an
	 *             <code>Offer</code> different from <code>promotedOffer</code>
	 * @throws IllegalArgumentException
	 *             If <code>promotedOffer</code> is <code>null</code>
	 * 
	 * @uml.property name="promotedOffer"
	 */
	public void setPromotedOffer(final Offer promotedOffer)
			throws CollaborationPreconditionsNotMetException,
			IllegalArgumentException {
		Validate.notNull(promotedOffer, "promotedOffer");
		if (promotesAnOfferDifferentFrom(promotedOffer)) {
			final String error = "Cannot promote an Offer [" + promotedOffer
					+ "] while already promoting a different Offer ["
					+ getPromotedOffer() + "]";

			throw new CollaborationPreconditionsNotMetException(error);
		}
		this.promotedOffer = promotedOffer;
	}

	/**
	 * @param offer
	 * @return
	 */
	protected boolean promotesAnOfferDifferentFrom(final Offer offer) {
		if (this.promotedOffer == null) {
			return false;
		}

		return !this.promotedOffer.equals(offer);
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
		result = PRIME
				* result
				+ ((this.promotedOffer == null) ? 0 : this.promotedOffer
						.hashCode());
		result = PRIME * result
				+ ((this.startDate == null) ? 0 : this.startDate.hashCode());
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
		final Promotion other = (Promotion) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.promotedOffer == null) {
			if (other.promotedOffer != null) {
				return false;
			}
		} else if (!this.promotedOffer.equals(other.promotedOffer)) {
			return false;
		}
		if (this.startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!this.startDate.equals(other.startDate)) {
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
				"name", this.name).append("startDate", this.startDate).append(
				"endDate", this.endDate).append("promotionalText",
				this.promotionalText).append("promotedOffer",
				this.promotedOffer).toString();
	}

}
