package com.sas.sling.resource.query;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.sling.api.resource.Resource;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;

public class RqlResourceLocatorBuilder {

	public RqlResourceLocatorBuilder() {
		super();
	}

	public Predicate<Resource> createSpecification(Node node) {
		if (node instanceof LogicalNode) {
			return createPredicate((LogicalNode) node);
		}
		if (node instanceof ComparisonNode) {
			return createPredicate((ComparisonNode) node);
		}
		return null;
	}

	public Predicate<Resource> createPredicate(LogicalNode logicalNode) {

		List<Predicate<Resource>> predicates = logicalNode.getChildren().stream().map(node -> {
			return createSpecification(node);
		}).collect(Collectors.toList());

		Predicate<Resource> result = null;

		if (logicalNode.getOperator() == LogicalOperator.AND) {
			result = predicates.stream().reduce(null, (predicate, accumulator) -> {
				if (accumulator == null) {
					return predicate;
				}
				return accumulator.and(predicate);
			});

		} else if (logicalNode.getOperator() == LogicalOperator.OR) {
			predicates.stream().reduce(null, (predicate, accumulator) -> {
				if (accumulator == null) {
					return predicate;
				}
				return accumulator.or(predicate);
			});
		}

		return result;
	}

	public Predicate<Resource> createPredicate(ComparisonNode comparisonNode) {
		return new GenericRsqlSpecification(comparisonNode.getSelector(), comparisonNode.getOperator(),
				comparisonNode.getArguments()).toPredicate();
	}

}