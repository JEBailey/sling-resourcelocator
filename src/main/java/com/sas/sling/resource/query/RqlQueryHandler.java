package com.sas.sling.resource.query;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.FunctionOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import cz.jirutka.rsql.parser.ast.node.Node;

public class RqlQueryHandler {

	// TODO - not anywhere close to done.
	public static List<Resource> parseRqlQuery(Resource resource, String query) {
		Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
		ResourceLocator locator = ResourceLocator.startFrom(resource);
		Node rootNode = new RSQLParser(operators, functions).parse(query);
		Predicate<Resource> predicate = rootNode.accept(new PredicateVistor(),locator);
		return locator.locateResources(predicate);
	}

	private static Set<FunctionOperator> functions = new HashSet<FunctionOperator>(
			asList(new FunctionOperator("path"), new FunctionOperator("name")));

}
