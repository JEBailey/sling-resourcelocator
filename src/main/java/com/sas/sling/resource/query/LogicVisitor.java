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

import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.Visitor;

/**
 * Visitor implementation that handles the high level handling of logic between
 * statements that define the comparisons that would be performed.
 * 
 * In practical terms this handles the "and" and "or" predicates
 * 
 */
public class LogicVisitor implements Visitor<Predicate<Resource>, Void> {

	private ValueVisitor valueVisitor = new ValueVisitor();

	@Override
	public Predicate<Resource> visit(Node node, Void locator) {
		switch (node.type) {
		case AND:
			return createAndPredicate(node);
		case OR:
			return createOrPredicate(node);
		case COMPARISON:
			return createComparisonPredicate(node);
		default:
			// no action
		}
		return null;
	}

	private Predicate<Resource> createAndPredicate(Node node) {
		return node.children.stream().map(child -> {
			return visit(child, null);
		}).reduce(null, (predicate, accumulator) -> {
			if (predicate == null) {
				return accumulator;
			}
			return accumulator.and(predicate);
		});
	}

	/**
	 * Returns a predicate which consists of a series of Or statements
	 * 
	 * @param node
	 * @param param
	 * @return
	 */
	private Predicate<Resource> createOrPredicate(Node node ) {
		return node.children.stream().map(child -> {
			return visit(child, null);
		}).reduce(null, (predicate, accumulator) -> {
			if (predicate == null) {
				return accumulator;
			}
			return accumulator.or(predicate);
		});
	}

	private Predicate<Resource> createComparisonPredicate(Node comparisonNode) {
		Function<Resource, Object> leftValue = comparisonNode.leftNode.accept(valueVisitor, null);
		Function<Resource, Object> rightValue = comparisonNode.rightNode.accept(valueVisitor, null);
		return ComparisonPredicateFactory.toPredicate(comparisonNode.comparisonOp, leftValue, rightValue);
	}

	public ValueVisitor getValueVisitor() {
		return valueVisitor;
	}

}