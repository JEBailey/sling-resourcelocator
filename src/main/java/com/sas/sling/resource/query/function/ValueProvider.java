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
package com.sas.sling.resource.query.function;

import java.util.List;
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;

/**
 * This interface is used to to create a provider that will be mapped to a
 * function in the ValueVisitor.
 * 
 */
public interface ValueProvider {

	/**
	 * This method provides a Function which accepts the resource being tested and
	 * returns an Object to be used as part of the comparison. The returned Object
	 * is not necessarily tied to the resource
	 * 
	 * @param arguments
	 *            A list of Value Providers which represent the arguments.
	 * @return Function object which will provide a String, Instant, or Number based
	 *         Object to be used as part of a comparison or Function
	 */
	Function<Resource, Object> provision(List<Function<Resource, Object>> arguments);

}
