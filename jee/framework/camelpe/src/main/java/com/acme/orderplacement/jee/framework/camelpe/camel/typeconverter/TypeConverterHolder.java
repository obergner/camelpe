/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.typeconverter;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.commons.lang.Validate;

/**
 * <p>
 * Holds a {@link TypeConverter <code>TypeConverter</code>} and additional meta
 * data as <code>fromType</code>, <code>toType</code> and whether it is a
 * <code>Fallback TypeConverter</code>, all of which is needed for registering
 * the encapsulated <code>TypeConverter</code> in a
 * {@link org.apache.camel.spi.TypeConverterRegistry
 * <code>TypeConverterRegistry</code>}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class TypeConverterHolder {

	// -------------------------------------------------------------------------
	// Static factory method
	// -------------------------------------------------------------------------

	public static TypeConverterHolder newNonFallbackTypeConverterHolder(
			final Class<?> fromType, final Class<?> toType,
			final TypeConverter nonFallbackTypeConverter)
			throws IllegalArgumentException {
		Validate.notNull(fromType, "fromType");
		Validate.notNull(toType, "toType");
		Validate.notNull(nonFallbackTypeConverter, "nonFallbackTypeConverter");

		return new TypeConverterHolder(false, fromType, toType,
				nonFallbackTypeConverter, false);
	}

	public static TypeConverterHolder newFallbackTypeConverterHolder(
			final TypeConverter fallbackTypeConverter, final boolean canPromote)
			throws IllegalArgumentException {
		Validate.notNull(fallbackTypeConverter, "fallbackTypeConverter");

		return new TypeConverterHolder(true, null, null, fallbackTypeConverter,
				canPromote);
	}

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final boolean isFallback;

	private final Class<?> fromType;

	private final Class<?> toType;

	private final TypeConverter typeConverter;

	private final boolean canPromote;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	private TypeConverterHolder(final boolean isFallback,
			final Class<?> fromType, final Class<?> toType,
			final TypeConverter typeConverter, final boolean canPromote)
			throws IllegalArgumentException {
		Validate.notNull(typeConverter, "typeConverter");
		this.isFallback = isFallback;
		this.fromType = fromType;
		this.toType = toType;
		this.typeConverter = typeConverter;
		this.canPromote = canPromote;
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	public void registerIn(final CamelContext camelContext)
			throws IllegalArgumentException {
		Validate.notNull(camelContext, "camelContext");
		if (this.isFallback) {
			camelContext.getTypeConverterRegistry().addFallbackTypeConverter(
					getTypeConverter(), this.canPromote);
		} else {
			camelContext.getTypeConverterRegistry().addTypeConverter(
					this.toType, this.fromType, getTypeConverter());
		}
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the typeConverter
	 */
	public final TypeConverter getTypeConverter() {
		return this.typeConverter;
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.canPromote ? 1231 : 1237);
		result = prime * result
				+ ((this.fromType == null) ? 0 : this.fromType.hashCode());
		result = prime * result + (this.isFallback ? 1231 : 1237);
		result = prime * result
				+ ((this.toType == null) ? 0 : this.toType.hashCode());
		result = prime
				* result
				+ ((this.typeConverter == null) ? 0 : this.typeConverter
						.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TypeConverterHolder other = (TypeConverterHolder) obj;
		if (this.canPromote != other.canPromote) {
			return false;
		}
		if (this.fromType == null) {
			if (other.fromType != null) {
				return false;
			}
		} else if (!this.fromType.equals(other.fromType)) {
			return false;
		}
		if (this.isFallback != other.isFallback) {
			return false;
		}
		if (this.toType == null) {
			if (other.toType != null) {
				return false;
			}
		} else if (!this.toType.equals(other.toType)) {
			return false;
		}
		if (this.typeConverter == null) {
			if (other.typeConverter != null) {
				return false;
			}
		} else if (!this.typeConverter.equals(other.typeConverter)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TypeConverterHolder [canPromote=" + this.canPromote
				+ ", fromType=" + this.fromType + ", isFallback="
				+ this.isFallback + ", toType=" + this.toType
				+ ", typeConverter=" + this.typeConverter + "]";
	}

}
