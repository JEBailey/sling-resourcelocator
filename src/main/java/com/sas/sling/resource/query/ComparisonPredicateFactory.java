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
package com.sas.sling.resource.query;

import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.predicates.ComparisonPredicates;

public class ComparisonPredicateFactory {

	public static Predicate<Resource> toPredicate(ComparisonOperator op, Function<Resource, Object> leftValue,
			Function<Resource, Object> rightValue) {
		switch (op) {
		case EQUAL:
			return ComparisonPredicates.is(leftValue, rightValue);
		case NOT_EQUAL:
			return ComparisonPredicates.is(leftValue, rightValue).negate();
		case GREATER_THAN:
			return ComparisonPredicates.gt(leftValue, rightValue);
		case GREATER_THAN_OR_EQUAL:
			return ComparisonPredicates.gte(leftValue, rightValue);
		case LESS_THAN:
			return ComparisonPredicates.lt(leftValue, rightValue);
		case LESS_THAN_OR_EQUAL:
			return ComparisonPredicates.lte(leftValue, rightValue);
		case LIKE:
			return ComparisonPredicates.like(leftValue, rightValue);
		case LIKE_NOT:
			return ComparisonPredicates.like(leftValue, rightValue).negate();
		case CONTAINS:
			return ComparisonPredicates.contains(leftValue, rightValue);
		case CONTAINS_NOT:
			return ComparisonPredicates.contains(leftValue, rightValue).negate();
		case CONTAINS_ANY:
			return ComparisonPredicates.containsAny(leftValue, rightValue);
		case CONTAINS_NOT_ANY:
			return ComparisonPredicates.containsAny(leftValue, rightValue).negate();
		case IN:
			return ComparisonPredicates.in(leftValue, rightValue);
		case NOT_IN:
			return ComparisonPredicates.in(leftValue, rightValue).negate();
		}
		System.out.println(rightValue + "is not  been found");
		return null;
	}

}