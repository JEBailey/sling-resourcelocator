package com.sas.sling.resource.query;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.PropertyPredicates;
import com.sas.sling.resource.ResourceLocator;

import cz.jirutka.rsql.parser.ast.node.AbstractNode;
import cz.jirutka.rsql.parser.ast.node.ComparisonNode;

public class GenericRsqlSpecification {

	public static Predicate<Resource> toPredicate(AbstractNode node, Object operand, List<Object> arguments) {
		
		PropertyPredicates propPredicates = PropertyPredicates.property(operand.toString());

		Optional<RqlSearchOperation> op = RqlSearchOperation.getSimpleOperator(((ComparisonNode)node).getOperator());

		String argument = (String)arguments.get(0);
		
		switch (op.get()) {

		case EQUAL: {
			return propPredicates.is(argument);
		}
		case NOT_EQUAL: {
			return propPredicates.is(argument).negate();
		}
		case GREATER_THAN: {
			return propPredicates.gt(argument);
		}
		case GREATER_THAN_OR_EQUAL: {
			return propPredicates.gte(argument);
		}
		case LESS_THAN: {
			return propPredicates.lt(argument);
		}
		case LESS_THAN_OR_EQUAL: {
			return propPredicates.lte(argument);
		}
		case IN:
			return propPredicates.isIn(arguments.toArray(new String[arguments.size()]));
		case NOT_IN:
			return propPredicates.isIn(arguments.toArray(new String[arguments.size()])).negate();
		}

		System.out.println(argument + "has been found");
		return null;
	}

	//creates function predicate
	public static Predicate<Resource> toPredicate(String property, List<Object> arguments,
			final ResourceLocator locator) {
		switch (property) {
		case "path": {
			String path = (String)arguments.get(0);
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