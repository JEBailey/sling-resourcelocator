/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sas.sling.resource;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Provides property based predicates. The method follows the following
 * conventions
 * 
 * <br>
 * is = equal to argument<br>
 * isNot = not equal to argument<br>
 * isIn = property is a single value which matches one of the arguments passed in
 * for comparison<br>
 * contains = property is an array which contains all of the arguments passed in<br>
 * 
 * @author J.E. Bailey
 *
 */
public class PropertyPredicates {

	// key value to be used against the provided resource object
	private final String key;

	private PropertyPredicates(String name) {
		this.key = Objects.requireNonNull(name, "value may not be null");;
	}

	/**
	 * Used to define which value in the underlying map will we be used.
	 * 
	 * @param name key value of the property
	 * @return PropertyPredicate instance
	 */
	static public PropertyPredicates property(String name) {
		return new PropertyPredicates(name);
	}

	/**
	 * Equivalence matching against a String
	 * 
	 * @param string to match against
	 * @return true if Strings are equivalent
	 */
	public Predicate<Resource> is(String string) {
		return genericIs(string);
	}
	

	/**
	 * Equivalence matching against a Number (long)
	 * 
	 * @param number to be compared against
	 * @return true if numbers are equivalent
	 */
	public Predicate<Resource> is(Long number) {
		return genericIs(number);
	}
	
	/**
	 * Equivalence matching against a Boolean
	 * 
	 * @param number to be compared against
	 * @return true if booleans are equivalent
	 */
	public Predicate<Resource> is(Boolean bool) {
		return genericIs(bool);
	}

	/**
	 * Equivalence negation for a String
	 * 
	 * @param string to be compared against
	 * @return true if string is different
	 */
	public Predicate<Resource> isNot(String string) {
		return genericIsNot(string);
	}

	/**
	 * Equivalence negation for a number (Long)
	 * 
	 * @param number to be compared against
	 * @return true if the numbers are not equal
	 */
	public Predicate<Resource> isNot(Long number) {
		return genericIsNot(number);
	}

	/**
	 * Equivalence matching against one or more strings
	 * 
	 * @param strings to be compared with
	 * @return true if property matches one of the provided Strings
	 */
	public Predicate<Resource> isIn(String... strings) {
		return genericIsIn(strings);
	}

	/**
	 * Equivalence matching against one or more numbers (Long)
	 * 
	 * @param numbers to match against
	 * @return predicate matcher
	 */
	public Predicate<Resource> isIn(Long... numbers) {
		return genericIsIn(numbers);
	}

	/**
	 * Assumes that the referenced property value is an array of Strings and
	 * attempts to validate that all provided strings against that reference
	 * 
	 * @param strings to match against
	 * @return matcher
	 */
	public Predicate<Resource> contains(String... strings) {
		return genericContains(strings);
	}

	/**
	 * Assumes that the referenced property value is an array of numbers and
	 * attempts to validate that all provided numbers against that reference
	 * 
	 * @param numbers to match against
	 * @return predicate which will perform the matching
	 */
	public Predicate<Resource> contains(Long... numbers) {
		return genericContains(numbers);
	}

	/**
	 * Assumes that the referenced property value is a date and that this date
	 * is earlier in the epoch than the one being tested
	 * 
	 * @param when latest acceptable time
	 * @return predicate which will perform the matching
	 */
	public Predicate<Resource> isBefore(Date when) {
		Objects.requireNonNull(when, "value may not be null");
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
	 * @param when earliest acceptable value
	 * @return predicate
	 */
	public Predicate<Resource> isAfter(Date when) {
		Objects.requireNonNull(when, "value may not be null");
		return value -> {
			Date then = value.adaptTo(ValueMap.class).get(key, Date.class);
			if (then != null) {
				return then.after(when);
			}
			return false;
		};
	}

	/*
	 * Generic equalities method that is accessed via public methods that have
	 * specific types
	 */
	private <T> Predicate<Resource> genericIs(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			return type.equals(propValue);
		};

	}
	
	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	private <T> Predicate<Resource> genericGt(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			if (propValue instanceof Comparable<?>){
				return ((Comparable<T>)propValue).compareTo(type) > 0;
			}
			return type.equals(propValue);
		};

	}
	
	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	private <T> Predicate<Resource> genericGte(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			if (propValue instanceof Comparable<?>){
				return ((Comparable<T>)propValue).compareTo(type) >= 0;
			}
			return type.equals(propValue);
		};

	}
	
	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	private <T> Predicate<Resource> genericLt(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			if (propValue instanceof Comparable<?>){
				return ((Comparable<T>)propValue).compareTo(type) < 0;
			}
			return type.equals(propValue);
		};

	}
	
	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	private <T> Predicate<Resource> genericLte(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			@SuppressWarnings("unchecked")
			T propValue = (T) resource.adaptTo(ValueMap.class).get(key,
					type.getClass());
			if (propValue instanceof Comparable<?>){
				return ((Comparable<T>)propValue).compareTo(type) >= 0;
			}
			return type.equals(propValue);
		};

	}

	private <T> Predicate<Resource> genericIsNot(final T type) {
		return genericIs(type).negate();
	}

	@SuppressWarnings("unchecked")
	private <T> Predicate<Resource> genericContains(final T[] values) {
		Objects.requireNonNull(values, "value may not be null");
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

	private <T> Predicate<Resource> genericIsIn(final T[] values) {
		Objects.requireNonNull(values, "values may not be null");
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
	 * property value is not null
	 * 
	 * @return a predicate that determines existence of the value
	 */
	public Predicate<Resource> exists() {
		return resource -> resource.adaptTo(ValueMap.class).get(key) != null;
	}

	/**
	 * property value is null
	 * 
	 * @return a predicate that determines non existence of the value
	 */
	public Predicate<Resource> doesNotExist() {
		return exists().negate();
	}

	
	public Predicate<Resource> isNotIn(final Object... objects) {
		Objects.requireNonNull(objects, "objects may not be null");
		return resource -> {
			Object value = resource.adaptTo(ValueMap.class).get(key);

			for (Object object : objects) {
				if (object.equals(value)) {
					return false;
				}
			}
			return true;
		};
	}

	public Predicate<Resource> gt(String argument) {
		Objects.requireNonNull(argument, "argument may not be null");
		return genericGt(argument);
	}
	
	public Predicate<Resource> gte(String argument) {
		Objects.requireNonNull(argument, "argument may not be null");
		return genericGte(argument);
	}
	
	public Predicate<Resource> lt(String argument) {
		Objects.requireNonNull(argument, "argument may not be null");
		return genericLt(argument);
	}
	public Predicate<Resource> lte(String argument) {
		Objects.requireNonNull(argument, "argument may not be null");
		return genericLte(argument);
	}
}
