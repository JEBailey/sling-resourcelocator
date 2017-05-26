package com.sas.sling.resource.query;

import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class CustomRqlVisitor implements RSQLVisitor<Predicate<Resource>, Void> {
 
    private RqlResourceLocatorBuilder builder;
 
    public CustomRqlVisitor() {
        builder = new RqlResourceLocatorBuilder();
    }
 
    @Override
    public Predicate<Resource> visit(AndNode node, Void param) {
        return builder.createPredicate(node);
    }
 
    @Override
    public Predicate<Resource> visit(OrNode node, Void param) {
        return builder.createPredicate(node);
    }
 
    @Override
    public Predicate<Resource> visit(ComparisonNode node, Void param) {
        return builder.createPredicate(node);
    }
}