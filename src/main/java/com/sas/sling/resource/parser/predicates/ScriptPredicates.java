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

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;


import com.sas.sling.resource.parser.util.ConverterImpl;

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
public class ScriptPredicates {

	// key value to be used against the provided resource object
	private final Function<Resource,Object> lhs;

	private ScriptPredicates(Function<Resource,Object> value) {
		this.lhs = value;
	}

	/**
	 * Used to define which value in the underlying map will we be used.
	 * 
	 * @param name key value of the property
	 * @return PropertyPredicate instance
	 */
	static public ScriptPredicates leftSide(Function<Resource,Object> value) {
		return new ScriptPredicates(value);
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
			Date then = ConverterImpl.adapt(lhs, Date.class);
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
			Date then = ConverterImpl.adapt(lhs, Date.class);
			if (then != null) {
				return then.after(when);
			}
			return false;
		};
	}


	public <T> Predicate<Resource> is(Function<Resource,Object> rhs) {
		Objects.requireNonNull(rhs, "type value may not be null");
		return resource -> {
			Object lhValue = lhs.apply(resource);
			Object rhValue = rhs.apply(resource);
			if (lhValue == null || rhValue == null){
				return false;
			}
			return ConverterImpl.adapt(rhValue, lhValue.getClass()).equals(lhValue);
		};

	}

	/*
	 * Generic greater then method that is accessed via public methods that have
	 * specific types
	 */
	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> gt(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			T propValue = (T) ConverterImpl.adapt(lhs, type.getClass());
			if (propValue == null){
				return false;
			}
			if (lhs instanceof Comparable){
				if (type.getClass().isInstance(lhs)){
					return ((Comparable<T>)lhs).compareTo(type) > 0;
				}
			}
			return false;
		};

	}
	

	@SuppressWarnings("unchecked")
	public <T> Predicate<Resource> gte(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			T propValue = (T) ConverterImpl.adapt(lhs, type.getClass());
			if (propValue == null){
				return false;
			}
			if (lhs instanceof Comparable){
				if (type.getClass().isInstance(lhs)){
					return ((Comparable<T>)lhs).compareTo(type) >= 0;
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
	public <T> Predicate<Resource> lt(Function<Resource,Object> rhs) {
		Objects.requireNonNull(rhs, "type value may not be null");
		return resource -> {
			Object lhValue = lhs.apply(resource);
			Object rhValue = rhs.apply(resource);
			if (lhValue == null || rhValue == null){
				return false;
			}
			T propValue = (T) ConverterImpl.adapt(rhValue, lhValue.getClass());
			if (propValue == null){
				return false;
			}
			if (lhValue instanceof Comparable){
				if (propValue.getClass().isInstance(lhValue)){
					return ((Comparable<T>)lhValue).compareTo(propValue) < 0;
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
	public <T> Predicate<Resource> lte(T type) {
		Objects.requireNonNull(type, "type value may not be null");
		return resource -> {
			T propValue = (T) ConverterImpl.adapt(lhs, type.getClass());
			if (propValue == null){
				return false;
			}
			if (lhs instanceof Comparable){
				if (type.getClass().isInstance(lhs)){
					return ((Comparable<T>)lhs).compareTo(type) <= 0;
				}
			}
			return false;
		};

	}

	public <T> Predicate<Resource> isNot(final Function<Resource,Object> type) {
		return is(type).negate();
	}



}
