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

import com.sas.sling.resource.PropertyPredicates;
import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.predicates.ScriptPredicates;

public class PredicateFactory {

	public static Predicate<Resource> toPredicate(Node node, Function<Resource, Object> operand,
			List<Function<Resource, Object>> arguments) {

		Optional<ComparisonOperators> op = ComparisonOperators.getSimpleOperator(node.getValue());

		Function<Resource, Object> firstArgument = arguments.get(0);

		ScriptPredicates leftSide = ScriptPredicates.leftSide(operand);

		switch (op.get()) {
		case EQUAL:
			return leftSide.is(firstArgument);
		case NOT_EQUAL:
			return leftSide.is(firstArgument).negate();
		case GREATER_THAN:
			return leftSide.gt(firstArgument);
		case GREATER_THAN_OR_EQUAL:
			return leftSide.gte(firstArgument);
		case LESS_THAN:
			return leftSide.lt(firstArgument);
		case LESS_THAN_OR_EQUAL:
			return leftSide.lte(firstArgument);
		case LIKE:
			return leftSide.like(firstArgument);
		}

		System.out.println(firstArgument + "is not  been found");
		return null;
	}



}