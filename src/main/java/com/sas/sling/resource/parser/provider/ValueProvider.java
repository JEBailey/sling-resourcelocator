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
package com.sas.sling.resource.parser.provider;

import java.util.List;
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;

/**
 * A ValueProvider implementation is used to provide the result of a function
 * 
 */
public interface ValueProvider {

	/**
	 * This method returns a {@code Function} which accepts the resource being
	 * tested and returns an Object to be used as part of the comparison.
	 * 
	 * @param arguments
	 *            A list of {@code Function}'s which provides the arguments defined
	 *            in the script
	 * @return A {@code Function} which will provide a String, Instant, or Number to
	 *         be used as part of a comparison or Function
	 */
	Function<Resource, Object> provision(List<Function<Resource, Object>> arguments);

}
