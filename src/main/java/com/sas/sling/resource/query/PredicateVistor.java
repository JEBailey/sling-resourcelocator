package com.sas.sling.resource.query;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;

import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import cz.jirutka.rsql.parser.ast.node.AbstractNode;
import cz.jirutka.rsql.parser.ast.node.FunctionNode;

public class PredicateVistor implements RSQLVisitor<Predicate<Resource>, ResourceLocator> {

	@Override
	public Predicate<Resource> visit(AbstractNode node, ResourceLocator locator) {
		List<Object> arguments = Collections.emptyList();
		switch (node.getType()){
		case AND:
			return createAndPredicate(node, locator);
		case OR:
			return createOrPredicate(node, locator);
		case COMPARISON:
			arguments = node.visitChildren(new ArgumentVisitor(), locator);
			return createComparisonPredicate(node,arguments, locator);
		case FUNCTION:
			arguments = node.visitChildren(new ArgumentVisitor(), locator);
			return createFunctionPredicate(node,arguments,locator);
		default:
			//no action
		}
		return null;
	}
	
	private Predicate<Resource> createAndPredicate(AbstractNode node, ResourceLocator locator) {
		return node.getRightOperands().stream().map(child -> {
			return visit(node,locator);
		}).reduce(null, (predicate, accumulator) -> {
			if (accumulator == null) {
				return predicate;
			}
			return accumulator.and(predicate);
		});
	}

	private Predicate<Resource> createOrPredicate(AbstractNode node, ResourceLocator locator) {
		return node.getRightOperands().stream().map(child -> {
			return visit(node,locator);
		}).reduce(null, (predicate, accumulator) -> {
			if (accumulator == null) {
				return predicate;
			}
			return accumulator.or(predicate);
		});
	}
	
	private Predicate<Resource> createFunctionPredicate(AbstractNode functionalNode, List<Object> arguments, ResourceLocator locator) {
		return GenericRsqlSpecification.toPredicate(((FunctionNode)functionalNode).getOp().toString(), arguments, locator);
	}
	
	private Predicate<Resource> createComparisonPredicate(AbstractNode comparisonNode, List<Object> arguments, ResourceLocator locator) {
		Object operand = comparisonNode.getLeftOperand().accept(new ArgumentVisitor(),locator);
		return GenericRsqlSpecification.toPredicate(comparisonNode, operand, arguments);
	}

}