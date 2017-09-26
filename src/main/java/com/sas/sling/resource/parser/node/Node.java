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

/**
 * 
 */
public class Node implements Iterable<Node> {

	private List<Node> children = Collections.emptyList();
	private NodeType type;
	private Node left;
	private String value;

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
		this.left = lhs;
	}

	public Node(String value, NodeType type, List<Node> children) {
		this.value = value;
		this.type = type;
		if (children != null) {
			this.children = children;
		}
	}

	/**
	 * Accepts the visitor, calls its <tt>visit()</tt> method and returns a result.
	 *
	 * <p>
	 * Each implementation must implement this methods exactly as listed:
	 * 
	 * <pre>
	 * {@code
	 * public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
	 *     return visitor.visit(this, param);
	 * }
	 * }
	 * </pre>
	 *
	 * @param visitor
	 *            The visitor whose appropriate method will be called.
	 * @param param
	 *            An optional parameter to pass to the visitor.
	 * @param <R>
	 *            Return type of the visitor's method.
	 * @param <A>
	 *            Type of an optional parameter passed to the visitor's method.
	 * @return An object returned by the visitor (may be <tt>null</tt>).
	 */
	public <R, A> R accept(Visitor<R, A> visitor, A param) {
		return visitor.visit(this, param);
	}

	/**
	 * Accepts the visitor, calls its <tt>visit()</tt> method and returns the
	 * result.
	 *
	 * This method should just call {@link #accept(Visitor, Object)} with
	 * <tt>null</tt> as the second argument.
	 */
	<R, A> R accept(Visitor<R, A> visitor) {
		return accept(visitor, null);
	}

	public Node withChildren(List<Node> children) {
		return new Node(this.value, this.type, children);
	}

	/**
	 * Iterate over children nodes
	 */
	public Iterator<Node> iterator() {
		return children.iterator();
	}

	@Override
	public String toString() {
		return value + children.stream().map(item -> item.toString())
				.collect(Collectors.joining(value.toString(), "(", ")"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Node))
			return false;
		Node nodes = (Node) o;

		return children.equals(nodes.children) && value == nodes.value;
	}

	@Override
	public int hashCode() {
		int result = children.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	public <R, A> List<R> visitChildren(Visitor<R, A> visitor, A param) {
		return children.stream().map(child -> child.accept(visitor, param)).collect(Collectors.toList());
	}

	public List<Node> getRightOperands() {
		return new ArrayList<>(children);
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public Node getLeftOperand() {
		return left;
	}

	public String getValue() {
		return value;
	}
}
