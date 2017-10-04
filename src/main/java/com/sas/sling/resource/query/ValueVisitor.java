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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

import com.sas.sling.resource.parser.conversion.Null;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.Visitor;
import com.sas.sling.resource.query.function.InstantProvider;
import com.sas.sling.resource.query.function.ValueProvider;

public class ValueVisitor implements Visitor<Function<Resource, Object>, Void> {

	private Map<String, ValueProvider> functions = new HashMap<>();
	
	private ValueProvider instant = new InstantProvider();

	@Override
	public Function<Resource, Object> visit(Node node, Void param) {
		switch (node.type) {
		case FUNCTION:
			break;
		case NULL:
			return resource -> new Null();
		case NUMBER:
			Number numericValue = null;
			{
				String numberText = node.text;
				try {
					numericValue = Integer.valueOf(numberText);
				} catch (NumberFormatException nfe1) {
					try {
						numericValue = new BigDecimal(numberText);
					} catch (NumberFormatException nfe2) {
					 //swallow
					}
				}
			}
			final Number numericReply = numericValue;
			return resource -> numericReply;
		case PROPERTY:
			return resource -> {
				Object value = valueMapOf(resource).get(node.text);
				if (value instanceof Boolean) {
					return value.toString();
				}
				if (value instanceof Calendar){
					return ((Calendar)value).toInstant();
				}
				return value;
			};
		default:
			return resource -> node.text;
		}
		// will only get here in the case of the 'FUNCTION' switch case
		switch (node.text) {
		case "name":
			return resource -> resource.getName();
		case "date":
			return instant.provision(node.visitChildren(this, null));
		case "path":
			return resource -> resource.getPath();
		default:
			ValueProvider temp = functions.get(node.text);
			if (temp !=  null){
				return temp.provision(node.visitChildren(this, null));
			}
			
		}
		return null;
	}

	public ValueProvider registerFunction(String functionName, ValueProvider function) {
		return this.functions.put(functionName, function);
	}

	public ValueProvider removeFunction(String functionName) {
		return this.functions.remove(functionName);
	}
	
	private ValueMap valueMapOf(Resource resource){
		if (resource == null || ResourceUtil.isNonExistingResource(resource)){
			return ValueMap.EMPTY;
		}
		 return resource.adaptTo(ValueMap.class);
	}

}
