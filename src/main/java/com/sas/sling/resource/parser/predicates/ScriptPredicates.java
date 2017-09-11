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

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.conversion.ConversionHandler;
import com.sas.sling.resource.parser.conversion.Null;

/**
 * Provides property based predicates. The method follows the following
 * conventions
 * 
 * <br>
 * is = equal to argument<br>
 * isNot = not equal to argument<br>
 * isIn = property is a single value which matches one of the arguments passed
 * in for comparison<br>
 * contains = property is an array which contains all of the arguments passed
 * in<br>
 * 
 * @author J.E. Bailey
 *
 */
public class ScriptPredicates {

	// key value to be used against the provided resource object
	private final Function<Resource, Object> lhs;

	private ScriptPredicates(Function<Resource, Object> lhs) {
		this.lhs = lhs;
	}

	/**
	 * 
	 * @param rhs
	 * @return
	 */
	static public ScriptPredicates leftSide(Function<Resource, Object> rhs) {
		return new ScriptPredicates(rhs);
	}

	public <T> Predicate<Resource> is(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Object lhValue = lhs.apply(resource);
			Object rhValue = rhs.apply(resource);
			if (lhValue == null || rhValue == null) {
				return (lhValue instanceof Null || rhValue instanceof Null);
			}
			return ConversionHandler.adapt(lhValue, rhValue.getClass()).equals(rhValue);
		};

	}

	/*
	
	 */
	public <T> Predicate<Resource> like(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "value may not be null");
		return resource -> {
			String lhValue = ConversionHandler.adapt(lhs.apply(resource), String.class);
			String rhValue = ConversionHandler.adapt(rhs.apply(resource), String.class);
			if (lhValue == null || rhValue == null) {
				return false;
			}
			return lhValue.matches(rhValue);
		};

	}

	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> gt(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.adapt(lhs.apply(resource), Number.class);
			Number rhValue = ConversionHandler.adapt(rhs.apply(resource), Number.class);
			if (lhValue == null || rhValue == null) {
				return false;
			}
			if (lhValue instanceof Comparable) {
				if (rhValue.getClass().isInstance(lhValue)) {
					return ((Comparable<T>) lhValue).compareTo((T) rhValue) > 0;
				}
			}
			return false;
		};

	}

	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> gte(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.adapt(lhs.apply(resource), Number.class);
			Number rhValue = ConversionHandler.adapt(rhs.apply(resource), Number.class);
			if (lhValue == null || rhValue == null) {
				return false;
			}
			if (lhValue instanceof Comparable) {
				if (rhValue.getClass().isInstance(lhValue)) {
					return ((Comparable<T>) lhValue).compareTo((T) rhValue) >= 0;
				}
			}
			return false;
		};
	}

	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> lt(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "type value may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.adapt(lhs.apply(resource), Number.class);
			Number rhValue = ConversionHandler.adapt(rhs.apply(resource), Number.class);
			if (lhValue == null || rhValue == null) {
				return false;
			}
			if (lhValue instanceof Comparable) {
				if (rhValue.getClass().isInstance(lhValue)) {
					return ((Comparable<T>) lhValue).compareTo((T) rhValue) < 0;
				}
			}
			return false;
		};

	}

	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> lte(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			Number lhValue = ConversionHandler.adapt(lhs.apply(resource), Number.class);
			Number rhValue = ConversionHandler.adapt(rhs.apply(resource), Number.class);
			if (lhValue == null || rhValue == null) {
				return false;
			}
			if (lhValue instanceof Comparable) {
				if (rhValue.getClass().isInstance(lhValue)) {
					return ((Comparable<T>) lhValue).compareTo((T) rhValue) <= 0;
				}
			}
			return false;
		};
	}

	public <T> Predicate<Resource> contains(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			String[] lhValues = adaptToArray(lhs.apply(resource));
			String[] rhValues = adaptToArray(rhs.apply(resource));
			if (lhValues == null || rhValues == null){
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
			//reaches here only if every rhValue was successfully found in lhValues
			return true;
		};
	}
	
	public <T> Predicate<Resource> in(Function<Resource, Object> rhs) {
		Objects.requireNonNull(rhs, "statement may not be null");
		return resource -> {
			String[] lhValues = adaptToArray(lhs.apply(resource));
			String[] rhValues = adaptToArray(rhs.apply(resource));
			if (lhValues == null || rhValues == null){
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
			//reaches here only if every lhValue was successfully found in rhValues
			return true;
		};
	}
	
	public <T> Predicate<Resource> notIn(Function<Resource, Object> rhs) {
		return in(rhs).negate();
	}
	
	public <T> Predicate<Resource> containsNot(Function<Resource, Object> rhs) {
		return contains(rhs).negate();
	}

	private String[] adaptToArray(Object arr) {
		if (arr instanceof String[] || arr == null) {
			return (String[])arr;
		}
		ArrayList<String> response = new ArrayList<>();
		if (arr.getClass().isArray()){
			for (Object thing:(Object[])arr){
				response.add(ConversionHandler.adapt(thing, String.class));
			}
		} else {
			response.add(ConversionHandler.adapt(arr, String.class));
		}
		return response.toArray(new String[]{});
	}

	public <T> Predicate<Resource> isNot(final Function<Resource, Object> type) {
		return is(type).negate();
	}

}
