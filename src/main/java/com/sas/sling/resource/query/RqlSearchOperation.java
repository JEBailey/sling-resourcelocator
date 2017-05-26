package com.sas.sling.resource.query;

import java.util.Optional;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public enum RqlSearchOperation {
    EQUAL(RSQLOperators.EQUAL), 
    NOT_EQUAL(RSQLOperators.NOT_EQUAL), 
    GREATER_THAN(RSQLOperators.GREATER_THAN), 
    GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL), 
    LESS_THAN(RSQLOperators.LESS_THAN), 
    LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL), 
    IN(RSQLOperators.IN), 
    NOT_IN(RSQLOperators.NOT_IN);
 
    private ComparisonOperator operator;
 
    private RqlSearchOperation(ComparisonOperator operator) {
        this.operator = operator;
    }
 
    public static Optional<RqlSearchOperation> getSimpleOperator(ComparisonOperator operation) {
    	for (RqlSearchOperation value:RqlSearchOperation.values()){
    		if (operation.equals(value.operator)){
    			return Optional.of(value);
    		}
    	}
    	return Optional.empty();
    }
}