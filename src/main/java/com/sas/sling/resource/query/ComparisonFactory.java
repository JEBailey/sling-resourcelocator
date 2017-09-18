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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.predicates.ComparisonPredicates;

public class ComparisonFactory {

	public static Predicate<Resource> toPredicate(Node node, Function<Resource, Object> leftHandStatement,
			List<Function<Resource, Object>> arguments) {

		Optional<ComparisonOperators> op = ComparisonOperators.getSimpleOperator(node.getValue());

		Function<Resource, Object> rightHandStatement = arguments.get(0);

		switch (op.get()) {
		case EQUAL:
			return ComparisonPredicates.is(leftHandStatement, rightHandStatement);
		case NOT_EQUAL:
			return  ComparisonPredicates.is(leftHandStatement, rightHandStatement).negate();
		case GREATER_THAN:
			return ComparisonPredicates.gt(leftHandStatement, rightHandStatement);
		case GREATER_THAN_OR_EQUAL:
			return ComparisonPredicates.gte(leftHandStatement, rightHandStatement);
		case LESS_THAN:
			return ComparisonPredicates.lt(leftHandStatement, rightHandStatement);
		case LESS_THAN_OR_EQUAL:
			return ComparisonPredicates.lte(leftHandStatement, rightHandStatement);
		case LIKE:
			return ComparisonPredicates.like(leftHandStatement, rightHandStatement);
		case CONTAINS:
			return ComparisonPredicates.contains(leftHandStatement, rightHandStatement);
		case CONTAINS_NOT:
			return ComparisonPredicates.contains(leftHandStatement, rightHandStatement).negate();
		case IN:
			return ComparisonPredicates.in(leftHandStatement, rightHandStatement);
		case NOT_IN:
			return ComparisonPredicates.in(leftHandStatement, rightHandStatement).negate();
		}

		System.out.println(rightHandStatement + "is not  been found");
		return null;
	}



}