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

		Optional<Operations> op = Operations.getSimpleOperator(node.getValue());

		Function<Resource, Object> firstArgument = arguments.get(0);

		switch (op.get()) {

		case EQUAL: {
			return ScriptPredicates.leftSide(operand).is(firstArgument);
		}
		case NOT_EQUAL: {
			return ScriptPredicates.leftSide(operand).is(firstArgument).negate();
		}
		case GREATER_THAN: {
			return resource -> {
				return ScriptPredicates.leftSide(operand).gt(firstArgument.apply(resource)).test(resource);
			};
		}
		case GREATER_THAN_OR_EQUAL: {
			return resource -> {
				return ScriptPredicates.leftSide(operand).gte(firstArgument.apply(resource)).test(resource);
			};
		}
		case LESS_THAN: {
			return ScriptPredicates.leftSide(operand).lt(firstArgument);
		}
		case LESS_THAN_OR_EQUAL: {
			return resource -> {
				return ScriptPredicates.leftSide(operand).lte(firstArgument.apply(resource)).test(resource);
			};
		}
		case IN:
			return null;// propPredicates.isIn(arguments.toArray(new
						// String[arguments.size()]));
		case NOT_IN:
			return null;// propPredicates.isIn(arguments.toArray(new
						// String[arguments.size()])).negate();
		}

		System.out.println(firstArgument + "has been found");
		return null;
	}

	// creates function predicate
	public static Predicate<Resource> toPredicate(String property, List<Object> arguments,
			final ResourceLocator locator) {
		switch (property) {
		case "path": {
			String path = (String) arguments.get(0);
			return resource -> {
				locator.startingPath(path);
				return true;
			};
		}
		case "name": {
			return resource -> {
				return resource.getName().equals(arguments.get(0));
			};
		}
		}
		return resource -> true;
	}

}