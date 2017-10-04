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

import java.util.List;

import com.sas.sling.resource.parser.ParserConstants;
import com.sas.sling.resource.query.ComparisonOperator;

/**
 * Factory that creates {@link Node} instances for the parser.
 */
public class NodesFactory implements ParserConstants {

	/**
	 * Creates a specific {@link Node} instance for the specified operator and
	 * with the given children nodes.
	 *
	 * @param operator
	 *            The logical operator to create a node for.
	 * @param children
	 *            Children nodes, i.e. operands.
	 * @return A subclass of the {@link Node} according to the specified
	 *         operator.
	 */
	public Node createOrNode(List<Node> children) {
		return new Node(NodeType.OR, children);
	}

	/**
	 * Creates a specific {@link Node} instance for the 'AND'
	 * operator and with the given children nodes.
	 *
	 * @param operator
	 *            The logical operator to create a node for.
	 * @param children
	 *            Children nodes, i.e. operands.
	 * @return A subclass of the {@link Node} according to the specified
	 *         operator.
	 */
	public Node createAndNode(List<Node> children) {
		return new Node("and", NodeType.AND, children);
	}

	/**
	 * Creates a {@link Node} instance with the given parameters.
	 *
	 * @param operatorToken
	 *            A textual representation of the comparison operator to be
	 *            found in the set of supported {@linkplain ComparisonOperator
	 *            operators}.
	 * @param leftHandStatement
	 *            The selector that specifies the left side of the comparison.
	 * @param arguments
	 *            A list of arguments that specifies the right side of the
	 *            comparison.
	 *
	 * @throws UnknownOperatorException
	 *             If no operator for the specified operator token exists.
	 */
	public Node createComparisonNode(ComparisonOperator op, Node leftHandStatement, List<Node> arguments) {
		return new Node(op, leftHandStatement, arguments);
	}

	public Node createArgument(int kind, String literal) {
		String value = literal.trim();
		NodeType type = NodeType.STRING;
		switch (kind) {
		case DOUBLE_QUOTED_STR:
		case SINGLE_QUOTED_STR:
			value = literal.substring(1, literal.length() - 1);
			break;
		case NULL:
			type = NodeType.NULL;
			break;
		case NUMBER:
			type = NodeType.NUMBER;
			break;
		default:
		}
		return new Node(value, type);
	}

	public Node createFunction(Node functionName, List<Node> children) {
		return new Node(functionName.getValue(), NodeType.FUNCTION, children);
	}

	public Node createPropertySelector(String image) {
		return new Node(image, NodeType.PROPERTY);
	}
}
