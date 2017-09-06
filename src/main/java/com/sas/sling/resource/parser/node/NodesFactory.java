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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.sas.sling.resource.parser.ParserConstants;
import com.sas.sling.resource.parser.TokenMgrError;
import com.sas.sling.resource.parser.predicates.UnknownOperatorException;
import com.sas.sling.resource.query.Operations;

/**
 * Factory that creates {@link Node} instances for the parser.
 */
public class NodesFactory implements ParserConstants {


    /**
     * Creates a specific {@link Node} instance for the specified operator and with the
     * given children nodes.
     *
     * @param operator The logical operator to create a node for.
     * @param children Children nodes, i.e. operands.
     * @return A subclass of the {@link Node} according to the specified operator.
     */
    public Node createOrNode(List<Node> children) {
    	return new Node(NodeType.OR,children);
    }
    
    /**
     * Creates a specific {@link LogicalNode} instance for the specified operator and with the
     * given children nodes.
     *
     * @param operator The logical operator to create a node for.
     * @param children Children nodes, i.e. operands.
     * @return A subclass of the {@link Node} according to the specified operator.
     */
    public Node createAndNode(List<Node> children) {
    	return new Node(NodeType.AND,children);
    }

    /**
     * Creates a {@link Node} instance with the given parameters.
     *
     * @param operatorToken A textual representation of the comparison operator to be found in the
     *                      set of supported {@linkplain ComparisonOperator operators}.
     * @param selector The selector that specifies the left side of the comparison.
     * @param arguments A list of arguments that specifies the right side of the comparison.
     *
     * @throws UnknownOperatorException If no operator for the specified operator token exists.
     */
    public Node createComparisonNode(
            String operatorToken, Node selector, List<Node> arguments) {
        if (operatorToken == null) {
        	String compValue = selector.getValue();
        	Optional<Operations> validOperation = Operations.getSimpleOperator(compValue);
        	if (!validOperation.isPresent()){
        		throw new TokenMgrError("unkown comparator "+compValue,0);
        	}
            return new Node(NodeType.FUNCTION, selector.getValue(), arguments);
        } else {
            return new Node(operatorToken,selector,arguments);
        }
    }
    
    public Node createArgument(int kind,String literal) {
    	if (kind == DOUBLE_QUOTED_STR || kind == SINGLE_QUOTED_STR){
    		literal = literal.substring(1, literal.length() -1);
    	}
    	if (kind == NULL) {
    		return new Node(NodeType.NULL,literal,Collections.emptyList());
    	}
    	return new Node(literal);
    }

	public Node createFunction(Node functionName, List<Node> args) {
		return new Node(NodeType.FUNCTION,functionName.getValue(), args);
	}

	public Node createPropertySelector(String image) {
		return Node.propertyNode(image);
	}
}
