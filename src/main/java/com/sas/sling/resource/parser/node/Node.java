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
package com.sas.sling.resource.parser.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.sas.sling.resource.parser.predicates.ComparisonOperator;

/**
 * 
 */
public class Node implements Iterable<Node> {

	public String text;
	public NodeType type;
	public Node leftNode;
	public Node rightNode;
	public List<Node> children = Collections.emptyList();
	public ComparisonOperator comparisonOp = null;

	/**
	 * creates a node which represents a String Literal value
	 * 
	 * @param value
	 */
	public Node(String value, NodeType type) {
		this(value, type, null);
	}

	public Node(NodeType type, List<Node> children) {
		this(null, type, children);
	}

	public Node(String value, Node lhs, List<Node> children) {
		this(value, NodeType.COMPARISON, children);
		this.leftNode = lhs;
	}

	public Node(String value, NodeType type, List<Node> children) {
		this.text = value;
		this.type = type;
		if (children != null) {
			this.children = children;
		}
	}

	public Node(ComparisonOperator op, Node leftValue, Node rightValue) {
		this(op.toString(),NodeType.COMPARISON);
		this.leftNode = leftValue;
		this.rightNode = rightValue;
		this.comparisonOp = op;
	}

	public <R, A> R accept(Visitor<R, A> visitor, A param) {
		return visitor.visit(this, param);
	}

	<R, A> R accept(Visitor<R, A> visitor) {
		return accept(visitor, null);
	}

	public Node withChildren(List<Node> children) {
		return new Node(this.text, this.type, children);
	}

	/**
	 * Iterate over children nodes
	 */
	public Iterator<Node> iterator() {
		return children.iterator();
	}

	@Override
	public String toString() {
		return text + children.stream().map(item -> item.toString())
				.collect(Collectors.joining(text.toString(), "(", ")"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Node))
			return false;
		Node nodes = (Node) o;

		return children.equals(nodes.children) && text == nodes.text;
	}

	@Override
	public int hashCode() {
		int result = children.hashCode();
		result = 31 * result + text.hashCode();
		return result;
	}

	public <R, A> List<R> visitChildren(Visitor<R, A> visitor, A param) {
		return children.stream().map(child -> child.accept(visitor, param)).collect(Collectors.toList());
	}

}
