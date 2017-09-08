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
package com.sas.sling.resource;

import java.util.Map;
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
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

public class ResourcePredicates {

	/**
	 * Convenience method to wrap the resources method 'isResourceType'
	 * 
	 * @param resourceType
	 * @return predicate which evaluates
	 */
	public static Predicate<Resource> isResourceType(final String resourceType) {
		return resource -> resource.isResourceType(resourceType);
	}

	/**
	 * Convenience method to determine depth of resource via the name pattern
	 * 
	 * @param resourceType
	 * @return predicate which evaluates
	 */
	public static Predicate<Resource> depthIsLessThan(final int depth) {
		return resource -> resource.getPath().split("/").length < depth;
	}

}
