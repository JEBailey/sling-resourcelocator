package com.sas.aem.resourcequery;

import java.lang.reflect.Array;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Provides a set of constructors for property based predicates
 * 
 * @author J.E. Bailey
 *
 */
public class PropertyPredicate {

	// key value to be used against the provided resource object
	private final String key;

	private PropertyPredicate(String name) {
		this.key = name;
	}

	/**
	 * Method used to set the value of the property in the underlying map
	 * 
	 * @param name
	 * @return new constructor for property evaluations
	 */
	static public PropertyPredicate property(String name) {
		return new PropertyPredicate(name);
	}

	/**
	 * Equivalence matching against a String
	 * 
	 * @param string
	 * @return predicate matcher
	 */
	public Predicate is(String string) {
		return genericIs(string);
	}

	/**
	 * Equivalence matching against a Number (long)
	 * 
	 * @param number
	 * @return predicate matcher
	 */
	public Predicate is(Long number) {
		return genericIs(number);
	}

	/**
	 * Equivalence rejection for a String
	 * 
	 * @param string
	 * @return predicate matcher
	 */
	public Predicate isNot(String string) {
		return genericIsNot(string);
	}

	/**
	 * Equivalence rejection for a number (Long)
	 * 
	 * @param number
	 * @return predicate matcher
	 */
	public Predicate isNot(Long number) {
		return genericIs(number);
	}

	/**
	 * Equivalence matching against one or more strings
	 * 
	 * @param strings
	 * @return predicate matcher
	 */
	public Predicate isIn(String... strings) {
		return genericIsIn(strings);
	}

	/**
	 * Equivalence matching against one or more numbers (Long)
	 * 
	 * @param numbers
	 * @return predicate matcher
	 */
	public Predicate isIn(Long... numbers) {
		return genericIsIn(numbers);
	}

	public Predicate isNot(final Predicate predicate) {
		return predicate;
	}

	/**
	 * Assumes that the referenced property value is an array of Strings and
	 * attempts to validate that all provided strings against that reference
	 * 
	 * @param strings
	 * @return matcher
	 */
	public Predicate contains(String... strings) {
		return genericContains(strings);
	}

	/**
	 * Assumes that the referenced property value is an array of numbers and
	 * attempts to validate that all provided numbers against that reference
	 * 
	 * @param strings
	 * @return matcher
	 */
	public Predicate contains(Long... numbers) {
		return genericContains(numbers);
	}

	/**
	 * Assumes that the referenced property value is a date and that this date
	 * is earlier in the epoch than the one being tested
	 * 
	 * @param when
	 * @return matcher
	 */
	public Predicate isEarlierThan(final Date when) {
		if (when == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return value -> {
			Date then = value.adaptTo(ValueMap.class).get(key, Date.class);
			if (then != null) {
				return then.before(when);
			}
			return false;
		};
	}

	/**
	 * Assumes that the referenced property value is a date and that this date
	 * is later in the epoch than the one being tested
	 * 
	 * @param when
	 * @return predicate
	 */
	public Predicate isLaterThan(final Date when) {
		if (when == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return value -> {
			Date then = value.adaptTo(ValueMap.class).get(key, Date.class);
			if (then != null) {
				return then.after(when);
			}
			return false;
		};
	}

	private <T> Predicate genericIs(final T type) {
		if (type == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			return type.equals(propValue);
		};

	}

	private <T> Predicate genericIsNot(final T type) {
		if (type == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return new Predicate() {
			public boolean accepts(Resource resource) {
				@SuppressWarnings("unchecked")
				T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
						type.getClass());
				return !type.equals(propValue);
			}
		};

	}

	@SuppressWarnings("unchecked")
	private <T> Predicate genericContains(final T[] values) {
		if (values == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return resource -> {
			T[] propValues = (T[]) resource.adaptTo(ValueMap.class).get(key,
					values.getClass());
			if (propValues == null) {
				if (values.length > 1) {
					// no point converting if the number of values to test
					// exceeds the possible values in the repository
					return false;
				}
				Class<?> componentType = values.getClass().getComponentType();
				T tempValue = (T) resource.adaptTo(ValueMap.class).get(key,
						componentType);
				if (tempValue != null) {
					propValues = (T[]) Array.newInstance(componentType, 1);
					propValues[0] = tempValue;
				}
			}
			// property identified by resource is either not present or is
			// of a type that is not the type being requested
			if (propValues == null || propValues.length < values.length) {
				return false;
			}
			// validate that all items in values have matches in properties
			for (T item : values) {
				innerloop: {
					for (T propItem : propValues) {
						if (item.equals(propItem)) {
							break innerloop;
						}
					}
					return false;
				}
			}
			return true;
		};

	}

	private <T> Predicate genericIsIn(final T[] values) {
		if (values == null) {
			throw new IllegalArgumentException("value may not be null");
		}
		return resource -> {
			Object propValue = resource.adaptTo(ValueMap.class).get(key,
					values.getClass().getComponentType());
			if (propValue == null) {
				return false;
			}
			for (T value : values) {
				if (value.equals(propValue)) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * 
	 * 
	 * @return a predicate that determines existence of the value
	 */
	public Predicate exists() {
		return new Predicate() {

			public boolean accepts(Resource resource) {
				Object value = resource.adaptTo(ValueMap.class).get(key);
				return value != null;
			}
		};
	}

	public Predicate doesNotExist() {
		return resource -> {
			Object value = resource.adaptTo(ValueMap.class).get(key);
			return value == null;
		};
	}

	public Predicate isNotEmpty() {
		return new Predicate() {

			public boolean accepts(Resource resource) {
				Object value = resource.adaptTo(ValueMap.class).get(key);
				return value != null
						&& StringUtils.isNotEmpty(value.toString());
			}
		};
	}

	public Predicate isNotIn(final Object... objects) {
		return new Predicate() {

			public boolean accepts(Resource resource) {
				Object value = resource.adaptTo(ValueMap.class).get(key);

				for (Object object : objects) {
					if (object.equals(value)) {
						return false;
					}
				}
				return true;
			}
		};
	}
}
