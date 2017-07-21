package com.sas.sling.resource.query;
/*
 * Copyright 2016 Jason E Bailey
 *
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
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.Visitor;
import com.sas.sling.resource.parser.node.Node;


public class PredicateVistor implements Visitor<Predicate<Resource>, ResourceLocator> {

	@Override
	public Predicate<Resource> visit(Node node, ResourceLocator locator) {
		List<Function<Resource,String>> arguments = Collections.emptyList();
		switch (node.getType()){
		case AND:
			return createAndPredicate(node, locator);
		case OR:
			return createOrPredicate(node, locator);
		case COMPARISON:
			arguments = node.visitChildren(new ArgumentVisitor(), locator);
			return createComparisonPredicate(node,arguments, locator);
		default:
			//no action
		}
		return null;
	}
	
	private Predicate<Resource> createAndPredicate(Node node, ResourceLocator locator) {
		return node.getRightOperands().stream().map(child -> {
			return visit(node,locator);
		}).reduce(null, (predicate, accumulator) -> {
			if (accumulator == null) {
				return predicate;
			}
			return accumulator.and(predicate);
		});
	}

	private Predicate<Resource> createOrPredicate(Node node, ResourceLocator locator) {
		return node.getRightOperands().stream().map(child -> {
			return visit(node,locator);
		}).reduce(null, (predicate, accumulator) -> {
			if (accumulator == null) {
				return predicate;
			}
			return accumulator.or(predicate);
		});
	}
	
	private Predicate<Resource> createComparisonPredicate(Node comparisonNode, List<Function<Resource, String>> arguments, ResourceLocator locator) {
		Function<Resource, String> operand = comparisonNode.getLeftOperand().accept(new ArgumentVisitor(),locator);
		return GenericRsqlSpecification.toPredicate(comparisonNode, operand, arguments);
	}

}