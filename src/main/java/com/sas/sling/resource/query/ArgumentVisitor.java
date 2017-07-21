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
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.Visitor;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.NodeType;


public class ArgumentVisitor implements Visitor<Function<Resource,String>, ResourceLocator> {

	@Override
	public Function<Resource,String> visit(Node node, ResourceLocator param) {
		if (node.getType() != NodeType.FUNCTION){
			return resource -> node.getValue();
		}
		switch (node.getValue()){
		case "name":
			return resource -> resource.getName();
		default:
		}
		return null;
		
	}

}
