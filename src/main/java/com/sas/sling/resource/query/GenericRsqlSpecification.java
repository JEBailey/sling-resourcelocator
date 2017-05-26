package com.sas.sling.resource.query;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.PropertyPredicates;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;

public class GenericRsqlSpecification {
	private String property;
	private ComparisonOperator operator;
	private List<String> arguments;

	public GenericRsqlSpecification(String property, ComparisonOperator operator, List<String> arguments) {
		super();
		this.property = property;
		this.operator = operator;
		this.arguments = arguments;
	}

	public Predicate<Resource> toPredicate() {
		PropertyPredicates propPredicates = PropertyPredicates.property(this.property);

		Optional<RqlSearchOperation> op = RqlSearchOperation.getSimpleOperator(operator);

		String argument = arguments.get(0);

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

		return null;
	}

}