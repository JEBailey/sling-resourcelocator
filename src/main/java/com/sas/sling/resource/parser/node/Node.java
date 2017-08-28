package com.sas.sling.resource.parser.node;

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
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common interface of the AST nodes. Implementations must be immutable.
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
	public Node(String value) {
		this.value = value;
		this.type = NodeType.STRING;
	}
	
	/**
	 * creates a node which represents a property in the resource that will be provided
	 * 
	 * @param value
	 */
	public static Node propertyNode(String value) {
		Node reply = new Node(value);
		reply.type = NodeType.PROPERTY;
		return reply;
	}

	public Node(NodeType type, String identifier, List<Node> children) {
		assert identifier != null : "operator must not be null";
		assert children != null : "children must not be null";

		this.value = identifier;
		this.children = unmodifiableList(new ArrayList<>(children));
		this.type = type;
	}

	public Node(NodeType type, List<Node> children) {
		this.children = unmodifiableList(new ArrayList<>(children));
		this.type = type;
	}

	public Node(String operatorToken, Node selector, List<Node> children) {
		this.value = operatorToken;
		this.left = selector;
		this.children = new ArrayList<>(children);
		this.type = NodeType.COMPARISON;
	}

	/**
	 * Accepts the visitor, calls its <tt>visit()</tt> method and returns a
	 * result.
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
		return new Node(this.type, this.value, children);
	}

	/**
	 * Iterate over children nodes. The underlying collection is unmodifiable!
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

	public Node getLeftOperand() {
		return left;
	}

	public String getValue() {
		return value;
	}
}
