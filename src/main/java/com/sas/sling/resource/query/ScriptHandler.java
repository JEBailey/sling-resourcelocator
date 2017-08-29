package com.sas.sling.resource.query;
import java.io.ByteArrayInputStream;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.ParseException;
import com.sas.sling.resource.parser.Parser;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.NodesFactory;

public class ScriptHandler {

	// TODO - not anywhere close to done.
	public static Predicate<Resource> parseRqlQuery(String query) throws ParseException {
		Node rootNode = new Parser(new ByteArrayInputStream(query.getBytes()), "UTF-8", new NodesFactory()).Input();
		return rootNode.accept(new PredicateVistor(),null);
	}
}
