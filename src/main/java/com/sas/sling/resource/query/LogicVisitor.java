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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.Visitor;


public class LogicVisitor implements Visitor<Predicate<Resource>, Void> {
	
	private ValueVisitor valueVisitor = new ValueVisitor();

	@Override
	public Predicate<Resource> visit(Node node, Void locator) {
		List<Function<Resource,Object>> arguments = Collections.emptyList();
		switch (node.getType()){
		case AND:
			return createAndPredicate(node, locator);
		case OR:
			return createOrPredicate(node, locator);
		case COMPARISON:
			arguments = node.visitChildren(new ValueVisitor(), locator);
			return createComparisonPredicate(node,arguments, locator);
		default:
			//no action
		}
		return null;
	}
	
	private Predicate<Resource> createAndPredicate(Node node, Void locator) {
		return node.getRightOperands().stream().map(child -> {
			return visit(child,locator);
		}).reduce(null, (predicate, accumulator) -> {
			if (predicate == null) {
				return accumulator;
			}
			return accumulator.and(predicate);
		});
	}

	private Predicate<Resource> createOrPredicate(Node node, Void param) {
		return node.getRightOperands().stream().map(child -> {
			return visit(child,param);
		}).reduce(null, (predicate, accumulator) -> {
			if (predicate == null) {
				return accumulator;
			}
			return accumulator.or(predicate);
		});
	}
	
	private Predicate<Resource> createComparisonPredicate(Node comparisonNode, List<Function<Resource, Object>> arguments, Void locator) {
		Function<Resource, Object> operand = comparisonNode.getLeftOperand().accept(valueVisitor,locator);
		return ComparisonFactory.toPredicate(comparisonNode, operand, arguments);
	}

}