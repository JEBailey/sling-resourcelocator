package com.sas.sling.resource.query;

import java.util.List;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

public class RqlQueryHandler {
	
	//TODO - not anywhere close to done.
	public List<Resource> parseRqlQuery(Resource resource, String query){
		Node rootNode = new RSQLParser().parse(query);
		Predicate<Resource> predicate = rootNode.accept(new CustomRqlVisitor());
		return ResourceLocator.startFrom(resource).locateResources(predicate);
	}
	

}
