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
	public static Predicate<Resource> parseQuery(String query) throws ParseException {
		Node rootNode = new Parser(new ByteArrayInputStream(query.getBytes()), "UTF-8", new NodesFactory()).Input();
		return rootNode.accept(new LogicVisitor(),null);
	}
}
