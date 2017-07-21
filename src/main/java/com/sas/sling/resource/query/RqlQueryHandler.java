package com.sas.sling.resource.query;
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
import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.ParseException;
import com.sas.sling.resource.parser.Parser;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.NodesFactory;

public class RqlQueryHandler {

	// TODO - not anywhere close to done.
	public static List<Resource> parseRqlQuery(Resource resource, String query) {
		//Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
		ResourceLocator locator = ResourceLocator.startFrom(resource);
		Node rootNode = null;
		try {
			rootNode = new Parser(new ByteArrayInputStream(query.getBytes()), "UTF-8", new NodesFactory()).Input();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Predicate<Resource> predicate = rootNode.accept(new PredicateVistor(),locator);
		return locator.locateResources(predicate);
	}

	private static Set<String> functions = new HashSet<String>(
			asList("path","name"));

}
