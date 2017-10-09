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
package com.sas.sling.resource.parser.predicates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.conversion.ConversionHandler;
import com.sas.sling.resource.parser.conversion.Null;

/**
 * Predicates to handle comparisons that are defined in the filter language
 *
 */
public class ComparisonPredicates {

	/**
	 * Values are converted to Strings.
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if right hand String is equal to left hand String
	 */
	public static Predicate<Resource> is(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			CharSequence lhValue = ConversionHandler.getString(lhs.apply(resource));
			CharSequence rhValue = ConversionHandler.getString(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return (lhValue instanceof Null || rhValue instanceof Null);
			}
			return lhValue.equals(rhValue);
		};

	}

	/**
	 * Values are converted to Strings. Right hand value is treated as a Regular
	 * expression.
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if right hand value pattern matches the left hand value
	 */
	public static Predicate<Resource> like(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "value may not be null");
		return resource -> {
			CharSequence lhValue = ConversionHandler.getString(lhs.apply(resource));
			CharSequence rhValue = ConversionHandler.getString(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return false;
			}
			return Pattern.matches(rhValue.toString(), lhValue);
		};

	}

	/**
	 * Values are converted to a Number, and then additionally converted to a common
	 * type as the basis of comparison
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand value is greater than right hand value
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<Resource> gt(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.getNumber(lhs.apply(resource));
			Number rhValue = ConversionHandler.getNumber(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return false;
			}
			lhValue = standardizeNumbers(lhValue, rhValue.getClass());
			rhValue = standardizeNumbers(rhValue, lhValue.getClass());
			if (lhValue instanceof Comparable) {
				return ((Comparable<Number>) lhValue).compareTo(rhValue) > 0;
			}
			return false;
		};

	}

	/**
	 * Values are converted to a Number, and then additionally converted to a common
	 * type as the basis of comparison
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand value is greater than or equal to right hand value
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<Resource> gte(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.getNumber(lhs.apply(resource));
			Number rhValue = ConversionHandler.getNumber(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return false;
			}
			lhValue = standardizeNumbers(lhValue, rhValue.getClass());
			rhValue = standardizeNumbers(rhValue, lhValue.getClass());
			if (lhValue instanceof Comparable) {
				return ((Comparable<Number>) lhValue).compareTo(rhValue) >= 0;
			}
			return false;
		};
	}

	/**
	 * Values are converted to a Number, and then additionally converted to a common
	 * type as the basis of comparison
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand value is less than right hand value
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<Resource> lt(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "type value may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.getNumber(lhs.apply(resource));
			Number rhValue = ConversionHandler.getNumber(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return false;
			}
			lhValue = standardizeNumbers(lhValue, rhValue.getClass());
			rhValue = standardizeNumbers(rhValue, lhValue.getClass());
			if (lhValue instanceof Comparable) {
				return ((Comparable<Number>) lhValue).compareTo(rhValue) < 0;
			}
			return false;
		};

	}

	/**
	 * Values are converted to a Number, and then additionally converted to a common
	 * type as the basis of comparison
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand value is less than or equal to right hand value
	 */
	@SuppressWarnings("unchecked")
	public static Predicate<Resource> lte(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.getNumber(lhs.apply(resource));
			Number rhValue = ConversionHandler.getNumber(rhs.apply(resource));
			if (lhValue == null || rhValue == null) {
				return false;
			}
			lhValue = standardizeNumbers(lhValue, rhValue.getClass());
			rhValue = standardizeNumbers(rhValue, lhValue.getClass());
			if (lhValue instanceof Comparable) {
				return ((Comparable<Number>) lhValue).compareTo(rhValue) <= 0;
			}
			return false;
		};
	}

	/**
	 * Right and Left values are converted to String arrays
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand values are a subset of right hand values
	 */
	public static Predicate<Resource> contains(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			String[] lhValues = adaptToArray(lhs.apply(resource));
			String[] rhValues = adaptToArray(rhs.apply(resource));
			if (lhValues == null || rhValues == null) {
				return false;
			}
			if (lhValues.length < rhValues.length) {
				return false;
			}
			for (String rhValue : rhValues) {
				innerLoop: {
					for (String lhValue : lhValues) {
						if (lhValue.equals(rhValue)) {
							break innerLoop;
						}
					}
					return false;
				}
			}
			// reaches here only if every rhValue was successfully found in
			// lhValues
			return true;
		};
	}

	/**
	 * Right and Left values are converted to String arrays
	 * 
	 * @param lhs
	 *            Function which provides comparison value
	 * @param rhs
	 *            Function which provides comparison value
	 * @return true if the left hand values matches any of the right hand values
	 */
	public static Predicate<Resource> containsAny(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		return resource -> {
			String[] lhValues = adaptToArray(lhs.apply(resource));
			String[] rhValues = adaptToArray(rhs.apply(resource));
			if (lhValues == null || rhValues == null) {
				return false;
			}
			for (String rhValue : rhValues) {
				for (String lhValue : lhValues) {
					if (lhValue.equals(rhValue)) {
						return true;
					}
				}
			}
			return false;
		};
	}

	/**
	 * Right and Left values are converted to String arrays
	 * 
	 * @param lhs
	 *            Function which provides value for comparison
	 * @param rhs
	 *            Function which provides value for comparison
	 * @return true if left hand values are a subset of right hand values
	 */
	public static Predicate<Resource> in(Function<Resource, Object> lhs, Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			String[] lhValues = adaptToArray(lhs.apply(resource));
			String[] rhValues = adaptToArray(rhs.apply(resource));
			if (lhValues == null || rhValues == null) {
				return false;
			}
			for (String lhValue : lhValues) {
				innerLoop: {
					for (String rhValue : rhValues) {
						if (rhValue.equals(lhValue)) {
							break innerLoop;
						}
					}
					return false;
				}
			}
			// reaches here only if every lhValue was successfully found in
			// rhValues
			return true;
		};
	}

	private static Number standardizeNumbers(Number value, Class<? extends Number> klass) {
		if (value.getClass() == klass || value instanceof BigDecimal) {
			return value;
		}
		if (value instanceof Double) {
			return BigDecimal.valueOf(value.doubleValue());
		}
		return BigDecimal.valueOf(value.longValue());
	}

	private static String[] adaptToArray(Object arr) {
		if (arr instanceof String[] || arr == null) {
			return (String[]) arr;
		}
		ArrayList<CharSequence> response = new ArrayList<>();
		if (arr.getClass().isArray()) {
			for (Object thing : (Object[]) arr) {
				response.add(ConversionHandler.getString(thing));
			}
		} else {
			response.add(ConversionHandler.getString(arr));
		}
		return response.toArray(new String[] {});
	}

}
