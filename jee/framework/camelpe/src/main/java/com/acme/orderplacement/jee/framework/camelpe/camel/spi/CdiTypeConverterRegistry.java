/**
 * 
 */
package com.acme.orderplacement.jee.framework.camelpe.camel.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.converter.ArrayTypeConverter;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.impl.converter.EnumTypeConverter;
import org.apache.camel.impl.converter.FutureTypeConverter;
import org.apache.camel.impl.converter.PropertyEditorTypeConverter;
import org.apache.camel.impl.converter.ToStringTypeConverter;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.TypeConverterAware;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TODO: Insert short summary for CdiTypeConverterRegistry
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class CdiTypeConverterRegistry implements TypeConverterRegistry,
		TypeConverter {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<TypeMapping, TypeConverter> typeMappings = new ConcurrentHashMap<TypeMapping, TypeConverter>();

	private final Map<TypeMapping, TypeMapping> misses = new ConcurrentHashMap<TypeMapping, TypeMapping>();

	private final List<FallbackTypeConverter> fallbackConverters = new ArrayList<FallbackTypeConverter>();

	private Injector injector;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public CdiTypeConverterRegistry(final Injector injector) {
		this.injector = injector;

		// add to string first as it will then be last in the last as to string
		// can nearly
		// always convert something to a string so we want it only as the last
		// resort
		// ToStringTypeConverter should NOT allow to be promoted
		addFallbackTypeConverter(new ToStringTypeConverter(), false);
		// enum is okay to be promoted
		addFallbackTypeConverter(new EnumTypeConverter(), true);
		// arrays is okay to be promoted
		addFallbackTypeConverter(new ArrayTypeConverter(), true);
		// do not assume property editor as it has a String converter
		addFallbackTypeConverter(new PropertyEditorTypeConverter(), false);
		// and future should also not allowed to be promoted
		addFallbackTypeConverter(new FutureTypeConverter(this), false);
		// add sync processor to async processor converter is to be promoted
		addFallbackTypeConverter(new AsyncProcessorTypeConverter(), true);
	}

	// -------------------------------------------------------------------------
	// org.apache.camel.spi.TypeConverterRegistry
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.spi.TypeConverterRegistry#lookup(java.lang.Class,
	 *      java.lang.Class)
	 */
	public TypeConverter lookup(final Class<?> toType, final Class<?> fromType) {
		return doLookup(toType, fromType, false);
	}

	private TypeConverter doLookup(final Class<?> toType,
			final Class<?> fromType, final boolean isSuper) {

		if (fromType != null) {
			// lets try if there is a direct match
			TypeConverter converter = getTypeConverter(toType, fromType);
			if (converter != null) {
				return converter;
			}

			// try the interfaces
			for (final Class<?> type : fromType.getInterfaces()) {
				converter = getTypeConverter(toType, type);
				if (converter != null) {
					return converter;
				}
			}

			// try super then
			final Class<?> fromSuperClass = fromType.getSuperclass();
			if ((fromSuperClass != null)
					&& !fromSuperClass.equals(Object.class)) {
				converter = doLookup(toType, fromSuperClass, true);
				if (converter != null) {
					return converter;
				}
			}
		}

		// only do these tests as fallback and only on the target type (eg not
		// on its super)
		if (!isSuper) {
			if ((fromType != null) && !fromType.equals(Object.class)) {

				// lets try classes derived from this toType
				final Set<Map.Entry<TypeMapping, TypeConverter>> entries = this.typeMappings
						.entrySet();
				for (final Map.Entry<TypeMapping, TypeConverter> entry : entries) {
					final TypeMapping key = entry.getKey();
					final Class<?> aToType = key.getToType();
					if (toType.isAssignableFrom(aToType)) {
						final Class<?> aFromType = key.getFromType();
						// skip Object based we do them last
						if (!aFromType.equals(Object.class)
								&& aFromType.isAssignableFrom(fromType)) {
							return entry.getValue();
						}
					}
				}

				// lets test for Object based converters as last resort
				final TypeConverter converter = getTypeConverter(toType,
						Object.class);
				if (converter != null) {
					return converter;
				}
			}
		}

		// none found
		return null;
	}

	// -------------------------------------------------------------------------
	// org.apache.camel.TypeConverter
	// -------------------------------------------------------------------------

	/**
	 * @see org.apache.camel.TypeConverter#convertTo(java.lang.Class,
	 *      java.lang.Object)
	 */
	public <T> T convertTo(final Class<T> type, final Object value) {
		return convertTo(type, null, value);
	}

	/**
	 * @see org.apache.camel.TypeConverter#convertTo(java.lang.Class,
	 *      org.apache.camel.Exchange, java.lang.Object)
	 */
	public <T> T convertTo(final Class<T> type, final Exchange exchange,
			final Object value) {
		try {
			final Object answer = doConvertTo(type, exchange, value);
			if (answer == Void.TYPE) {
				// Could not find suitable conversion
				return null;
			}
			return (T) answer;
		} catch (final Exception e) {
			// if its a ExecutionException then we have rethrow it as its not
			// due to failed conversion
			final boolean execution = (ObjectHelper.getException(
					ExecutionException.class, e) != null)
					|| (ObjectHelper.getException(
							CamelExecutionException.class, e) != null);
			if (execution) {
				throw ObjectHelper.wrapCamelExecutionException(exchange, e);
			}

			// we cannot convert so return null
			this.log.debug(NoTypeConversionAvailableException.createMessage(
					value, type)
					+ " Caused by: [{}]. Will ignore this and continue.", e
					.getMessage());
			return null;
		}
	}

	/**
	 * @see org.apache.camel.TypeConverter#mandatoryConvertTo(java.lang.Class,
	 *      java.lang.Object)
	 */
	public <T> T mandatoryConvertTo(final Class<T> type, final Object value)
			throws NoTypeConversionAvailableException {
		return mandatoryConvertTo(type, null, value);
	}

	/**
	 * @see org.apache.camel.TypeConverter#mandatoryConvertTo(java.lang.Class,
	 *      org.apache.camel.Exchange, java.lang.Object)
	 */
	public <T> T mandatoryConvertTo(final Class<T> type,
			final Exchange exchange, final Object value)
			throws NoTypeConversionAvailableException {
		Object answer;
		try {
			answer = doConvertTo(type, exchange, value);
		} catch (final Exception e) {
			throw new NoTypeConversionAvailableException(value, type, e);
		}
		if ((answer == Void.TYPE) || (value == null)) {
			// Could not find suitable conversion
			throw new NoTypeConversionAvailableException(value, type);
		}
		return (T) answer;
	}

	private Object doConvertTo(final Class<?> type, final Exchange exchange,
			final Object value) {
		this.log.trace("Converting [{}] -> [{}] using value [{}] ...",
				new Object[] {
						(value == null ? "null" : value.getClass()
								.getCanonicalName()), type.getCanonicalName(),
						value });

		if (value == null) {
			// lets avoid NullPointerException when converting to boolean for
			// null values
			if (boolean.class.isAssignableFrom(type)) {
				return Boolean.FALSE;
			}
			return null;
		}

		// same instance type
		if (type.isInstance(value)) {
			return type.cast(value);
		}

		// check if we have tried it before and if its a miss
		final TypeMapping key = new TypeMapping(type, value.getClass());
		if (this.misses.containsKey(key)) {
			// we have tried before but we cannot convert this one
			return Void.TYPE;
		}

		// try to find a suitable type converter
		final TypeConverter converter = getOrFindTypeConverter(type, value);
		if (converter != null) {
			final Object rc = converter.convertTo(type, exchange, value);
			if (rc != null) {
				return rc;
			}
		}

		// fallback converters
		for (final FallbackTypeConverter fallback : this.fallbackConverters) {
			final Object rc = fallback.getFallbackTypeConverter().convertTo(
					type, exchange, value);

			if (Void.TYPE.equals(rc)) {
				// it cannot be converted so give up
				return Void.TYPE;
			}

			if (rc != null) {
				// if fallback can promote then let it be promoted to a first
				// class type converter
				if (fallback.isCanPromote()) {
					// add it as a known type converter since we found a
					// fallback that could do it
					this.log
							.debug(
									"Promoting fallback type converter as a known type converter to convert "
											+ "from [{}] to [{}] for the fallback converter [{}]",
									new Object[] {
											type.getCanonicalName(),
											value.getClass().getCanonicalName(),
											fallback.getFallbackTypeConverter() });
					addTypeConverter(type, value.getClass(), fallback
							.getFallbackTypeConverter());
				}
				this.log
						.trace(
								"Fallback type converter [{}] converted type from [{}] to [{}]",
								new Object[] {
										fallback.getFallbackTypeConverter(),
										type.getCanonicalName(),
										value.getClass().getCanonicalName() });

				// return converted value
				return rc;
			}
		}

		// not found with that type then if it was a primitive type then try
		// again with the wrapper type
		if (type.isPrimitive()) {
			final Class<?> primitiveType = ObjectHelper
					.convertPrimitiveTypeToWrapperType(type);
			if (primitiveType != type) {
				return convertTo(primitiveType, exchange, value);
			}
		}

		// Could not find suitable conversion, so remember it
		synchronized (this.misses) {
			this.misses.put(key, key);
		}

		// Could not find suitable conversion, so return Void to indicate not
		// found
		return Void.TYPE;
	}

	/**
	 * @see org.apache.camel.spi.TypeConverterRegistry#addTypeConverter(java.lang.Class,
	 *      java.lang.Class, org.apache.camel.TypeConverter)
	 */
	public void addTypeConverter(final Class<?> toType,
			final Class<?> fromType, final TypeConverter typeConverter) {
		this.log.trace("Adding type converter [{}]", typeConverter);
		final TypeMapping key = new TypeMapping(toType, fromType);
		synchronized (this.typeMappings) {
			final TypeConverter converter = this.typeMappings.get(key);
			// only override it if its different
			// as race conditions can lead to many threads trying to promote the
			// same fallback converter
			if (typeConverter != converter) {
				if (converter != null) {
					this.log.warn(
							"Overriding type converter from [{}] to [{}]",
							converter, typeConverter);
				}
				this.typeMappings.put(key, typeConverter);
			}
		}
	}

	/**
	 * @see org.apache.camel.spi.TypeConverterRegistry#addFallbackTypeConverter(org.apache.camel.TypeConverter,
	 *      boolean)
	 */
	public void addFallbackTypeConverter(final TypeConverter typeConverter,
			final boolean canPromote) {
		this.log.trace(
				"Adding fallback type converter [{}] which can promote [{}]",
				typeConverter, canPromote);

		// add in top of fallback as the toString() fallback will nearly always
		// be able to convert
		this.fallbackConverters.add(0, new FallbackTypeConverter(typeConverter,
				canPromote));
		if (typeConverter instanceof TypeConverterAware) {
			final TypeConverterAware typeConverterAware = (TypeConverterAware) typeConverter;
			typeConverterAware.setTypeConverter(this);
		}
	}

	private TypeConverter getTypeConverter(final Class<?> toType,
			final Class<?> fromType) {
		final TypeMapping key = new TypeMapping(toType, fromType);
		return this.typeMappings.get(key);
	}

	/**
	 * @see org.apache.camel.spi.TypeConverterRegistry#getInjector()
	 */
	public Injector getInjector() {
		return this.injector;
	}

	/**
	 * @see org.apache.camel.spi.TypeConverterRegistry#setInjector(org.apache.camel.spi.Injector)
	 */
	public void setInjector(final Injector injector) {
		this.injector = injector;
	}

	private <T> TypeConverter getOrFindTypeConverter(final Class<?> toType,
			final Object value) {
		Class<?> fromType = null;
		if (value != null) {
			fromType = value.getClass();
		}
		final TypeMapping key = new TypeMapping(toType, fromType);
		TypeConverter converter;
		synchronized (this.typeMappings) {
			converter = this.typeMappings.get(key);
			if (converter == null) {
				converter = lookup(toType, fromType);
				if (converter != null) {
					this.typeMappings.put(key, converter);
				}
			}
		}
		return converter;
	}

	/**
	 * Represents a mapping from one type (which can be null) to another
	 */
	protected static class TypeMapping {
		Class<?> toType;
		Class<?> fromType;

		public TypeMapping(final Class<?> toType, final Class<?> fromType) {
			this.toType = toType;
			this.fromType = fromType;
		}

		public Class<?> getFromType() {
			return this.fromType;
		}

		public Class<?> getToType() {
			return this.toType;
		}

		@Override
		public boolean equals(final Object object) {
			if (object instanceof TypeMapping) {
				final TypeMapping that = (TypeMapping) object;
				return ObjectHelper.equal(this.fromType, that.fromType)
						&& ObjectHelper.equal(this.toType, that.toType);
			}
			return false;
		}

		@Override
		public int hashCode() {
			int answer = this.toType.hashCode();
			if (this.fromType != null) {
				answer *= 37 + this.fromType.hashCode();
			}
			return answer;
		}

		@Override
		public String toString() {
			return "[" + this.fromType + "=>" + this.toType + "]";
		}

		public boolean isApplicable(final Class<?> fromClass) {
			return this.fromType.isAssignableFrom(fromClass);
		}
	}

	/**
	 * Represents a fallback type converter
	 */
	protected static class FallbackTypeConverter {
		private final boolean canPromote;
		private final TypeConverter fallbackTypeConverter;

		FallbackTypeConverter(final TypeConverter fallbackTypeConverter,
				final boolean canPromote) {
			this.canPromote = canPromote;
			this.fallbackTypeConverter = fallbackTypeConverter;
		}

		public boolean isCanPromote() {
			return this.canPromote;
		}

		public TypeConverter getFallbackTypeConverter() {
			return this.fallbackTypeConverter;
		}
	}
}
